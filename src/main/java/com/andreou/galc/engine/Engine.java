package com.andreou.galc.engine;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Set;

import javax.swing.plaf.metal.MetalIconFactory.FolderIcon16;

import com.andreou.galc.AssignedLabel;
import com.andreou.galc.Data;
import com.andreou.galc.DatumCont;
import com.andreou.galc.EmpiricalData;
import com.andreou.galc.Ipeirotis;
import com.andreou.galc.SyntheticData;
import com.andreou.galc.Worker;

class ReportGenerator {
	
	private Ipeirotis ip;
	private boolean verbose; 
	
	public ReportGenerator(Ipeirotis ip, EngineContext ctx) {
		this.ip = ip;
		this.verbose = ctx.isVerbose();
	}
	
	/**
	 * 
	 */
	  double getCorrelationRelativeError() {

		Double relRhoError = 0.0;
		int n = ip.getWorkers().size();
		for (Worker w : ip.getWorkers()) {
			double estRho = w.getEst_rho();
			double realRho = w.getTrueRho();
			double absDiff = Math.abs(realRho - estRho);
			double relDiff = Math.abs(absDiff / realRho);
			relRhoError += relDiff / n;
		}
		return relRhoError;
	}
	
	/**
	 * 
	 */
	  double getCorrelationAbsoluteError() {

		Double avgRhoError = 0.0;
		int n = ip.getWorkers().size();
		for (Worker w : ip.getWorkers()) {
			double estRho = w.getEst_rho();
			double realRho = w.getTrueRho();
			double absDiff = Math.abs(realRho - estRho);
		
			avgRhoError += absDiff / n;
		}
		return avgRhoError;
	}

	
	/**
	 * 
	 */
	  public String generateWorkerReport() {

		String out = "Average absolute estimation error for correlation values: " + getCorrelationAbsoluteError() + "\n" +
		"Average relative estimation error for correlation values: " + getCorrelationRelativeError();
		if(!this.verbose) System.out.println(out);
		return out;
	}

	/**
	 * @return
	 */
	  Double estimateDistributionSigma() {

		Double nominator_sigma = 0.0;
		Double denominator_sigma = 0.0;
		for (Worker w : ip.getWorkers()) {
			Double b = w.getBeta();
			Double coef = Math.sqrt(b * b - b);
			Double s = w.getEst_sigma();
			nominator_sigma += coef * s;
			denominator_sigma += b;
		}
		Double est_sigma = nominator_sigma / denominator_sigma;
		return est_sigma;
	}

	/**
	 * @return
	 */
	  Double estimateDistributionMu() {

		// Estimate mu and sigma of distribution
		Double nominator_mu = 0.0;
		Double denominator_mu = 0.0;
		for (Worker w : ip.getWorkers()) {
			Double b = w.getBeta();
			Double coef = Math.sqrt(b * b - b);
			Double m = w.getEst_mu();
			nominator_mu += coef * m;
			denominator_mu += b;
		}
		Double est_mu = nominator_mu / denominator_mu;
		return est_mu;
	}

	/**
	 * @param data_mu
	 * @param data_sigma
	 */
	  public String generateDistributionReport() {

		String out = 	"Estimated mu = " + estimateDistributionMu() + "\n" + 
						"Estimated sigma = " + estimateDistributionSigma();
		if(!this.verbose)System.out.println(out);
		return out;
	}

	/**
	 * @param data_mu
	 * @param data_sigma
	 */
	  double getZetaAbsoluteErrorObject() {

		Double avgAbsError = 0.0;
		int n = ip.getObjects().size();
		for (DatumCont d : ip.getObjects()) {
			double estZ = d.getEst_zeta();
			double realZ = d.getTrueZeta();
			double absDiff = Math.abs(realZ - estZ);
			avgAbsError += absDiff / n;
		}
		return avgAbsError;
	}
	
	/**
	 * @param data_mu
	 * @param data_sigma
	 */
	  double getZetaRelativeErrorObject() {

		Double avgRelError = 0.0;
		int n = ip.getObjects().size();
		for (DatumCont d : ip.getObjects()) {
			double estZ = d.getEst_zeta();
			double realZ = d.getTrueZeta();
			double absDiff = Math.abs(realZ - estZ);
			double relDiff = Math.abs(absDiff / realZ);

			avgRelError += relDiff / n;
		}
		return avgRelError;
	}
	
	public String generateObjectReport() {

		String out = 	"Average absolute estimation error for z-values: " + getZetaAbsoluteErrorObject() + "\n" + 
						"Average relative estimation error for z-values: " + getZetaRelativeErrorObject();
		if(!this.verbose) System.out.println(out);
		return out;
	}
	
	public void writeReportToFile(String foldername, String filename, String reportcontent) {

		try {
			File outfile = new File(foldername+"/"+filename);
			
			if (outfile.getParent() != null) {
				File parentDir = new File(outfile.getParent());
				if (!parentDir.exists()) {
					parentDir.mkdirs();
				}
			}
			BufferedWriter bw = new BufferedWriter(new FileWriter(outfile));
			bw.write(reportcontent);
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}


public class Engine {


	private EngineContext  ctx;

	public Engine(EngineContext ctx){
		this.ctx = ctx;
	}
	
	public void execute(){
		Data data;
		if(ctx.isSyntheticDataSet()) {
			SyntheticData sdata = createSyntheticDataSet(ctx.isVerbose());
			sdata.writeLabelsToFile(ctx.getOutputFolder()+"/"+ctx.getInputFile());
			sdata.writeTrueWorkerDataToFile(ctx.getOutputFolder()+"/"+ctx.getTrueWorkersFile());
			sdata.writeTrueObjectDataToFile(ctx.getOutputFolder()+"/"+ctx.getTrueObjectsFile());
			data = sdata;
		} else {
			// PANOS: not verified that it works...
			EmpiricalData edata = new EmpiricalData();
			edata.loadLabelFile(ctx.getOutputFolder()+"/"+ctx.getInputFile());
			edata.loadTrueWorkerData(ctx.getOutputFolder()+"/"+ctx.getTrueWorkersFile());
			edata.loadTrueObjectData(ctx.getOutputFolder()+"/"+ctx.getTrueObjectsFile());
		data = edata;
		}		
		Ipeirotis ip = new Ipeirotis(data, ctx);
		
		ReportGenerator rpt = new ReportGenerator(ip, ctx);

		
		// Report about distributional estimates
		rpt.writeReportToFile(ctx.getOutputFolder(), "results-distribution.txt", rpt.generateDistributionReport()); 
		// Give report for objects
		rpt.writeReportToFile(ctx.getOutputFolder(), "results-objects.txt", rpt.generateObjectReport());
		// Give report for workers
		rpt.writeReportToFile(ctx.getOutputFolder(), "results-workers.txt", rpt.generateWorkerReport());

		if(ctx.isVerbose()) System.out.println("Results in folder: " + ctx.getOutputFolder());
	
	}

	/**
	 * @return
	 */
	private static SyntheticData createSyntheticDataSet(boolean verbose) {

		int data_points = 1000;
		Double data_mu = 7.0;
		Double data_sigma = 11.0;

		int workers = 1;
		Double worker_mu_down = -5.0;
		Double worker_mu_up = 5.0;
		Double worker_sigma_down = 0.5;
		Double worker_sigma_up = 1.5;
		Double worker_rho_down = 0.5;
		Double worker_rho_up = 1.0;

		if(!verbose) {
			System.out.println("Data points: " + data_points);
			System.out.println("Workers: " + workers);
	
			System.out.println("Low rho: " + worker_rho_down);
			System.out.println("High rho: " + worker_rho_up);
		}
		SyntheticData data = createDataSet(data_points, data_mu, data_sigma, workers, worker_mu_down, worker_mu_up,
				worker_sigma_down, worker_sigma_up, worker_rho_down, worker_rho_up);
		return data;
	}


	 static SyntheticData createDataSet(int data_points, Double data_mu, Double data_sigma, int workers,
			Double worker_mu_down, Double worker_mu_up, Double worker_sigma_down, Double worker_sigma_up,
			Double worker_rho_down, Double worker_rho_up) {

		SyntheticData data = new SyntheticData();

		data.setDataParameters(data_mu, data_sigma);

		data.setWorkerParameters(worker_mu_down, worker_mu_up, worker_sigma_down, worker_sigma_up, worker_rho_down,
				worker_rho_up);

		data.build(data_points, workers);
		return data;
	}

	public void println(String mask, Object... args) {
		print(mask + "\n", args);
	}

	public void print(String mask, Object... args) {
		if (! ctx.isVerbose())
			return;

		String message;

		if (args.length > 0) {
			message = String.format(mask, args);
		} else {
			// without format arguments, print the mask/string as-is
			message = mask;
		}

		System.out.println(message);
	}
}
