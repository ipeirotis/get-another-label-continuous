package com.andreou.galc.engine;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Set;

import org.omg.CORBA.CTX_RESTRICT_SCOPE;

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
	  String generateWorkerReport() {

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
	  String generateDistributionReport() {

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
	
	String generateObjectReport() {

		String out = 	"Average absolute estimation error for z-values: " + getZetaAbsoluteErrorObject() + "\n" + 
						"Average relative estimation error for z-values: " + getZetaRelativeErrorObject();
		if(!this.verbose) System.out.println(out);
		return out;
	}
	
	public void writeReportsToFile(String filename) {

		try {
			File outfile = new File(filename);
			
			if (outfile.getParent() != null) {
				File parentDir = new File(outfile.getParent());
				if (!parentDir.exists()) {
					parentDir.mkdirs();
				}
			}

			BufferedWriter bw = new BufferedWriter(new FileWriter(outfile));

			String lines = ""; 
			// Report about distributional estimates
			lines+=  this.generateDistributionReport() + "\n"; 
			// Give report for objects
			lines+=  this.generateObjectReport() + "\n";
			// Give report for workers
			lines+=  this.generateWorkerReport() + "\n";

			bw.write(lines);
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(this.verbose) System.out.println("Evaluation Reports: " + filename);
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
			sdata.writeLabelsToFile(ctx.getLabelsFile());
			sdata.writeTrueWorkerDataToFile(ctx.getWorkersFile());
			sdata.writeTrueObjectDataToFile(ctx.getObjectsFile());
			data = sdata;
		} else {
			// PANOS: not verified that it works...
			EmpiricalData edata = new EmpiricalData();
			edata.loadLabelFile(ctx.getLabelsFile());
			edata.loadTrueWorkerData(ctx.getWorkersFile());
			edata.loadTrueObjectData(ctx.getObjectsFile());
		data = edata;
		}		
		Ipeirotis ip = new Ipeirotis(data, ctx);
		
		ReportGenerator rpt = new ReportGenerator(ip, ctx);
		rpt.writeReportsToFile(ctx.getEvaluationFile());
	
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
