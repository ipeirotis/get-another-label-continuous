package com.andreou.galc;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.andreou.galc.engine.EngineContext;

public class Ipeirotis {

	private Set<DatumCont>					objects;
	private Map<String, DatumCont>	objects_index;
	private Set<Worker>							workers;
	private Map<String, Worker>			workers_index;
	


	public Ipeirotis(Data data, EngineContext ctx) {


		
		this.objects = data.getObjects();
		this.objects_index = new TreeMap<String, DatumCont>();
		for (DatumCont d : this.objects) {
			objects_index.put(d.getName(), d);
		}

		this.workers = data.getWorkers();
		this.workers_index = new TreeMap<String, Worker>();
		for (Worker w : this.workers) {
			workers_index.put(w.getName(), w);
		}


		initWorkers();
		//System.out.println("=======");
		estimateObjectZetas();
		//generateObjectReport(data_mu, data_sigma);
		//generateWorkerReport();
		//System.out.println("=======");

		// Run until convergence.
		double epsilon = 0.00001;
		double logLikelihood = estimate(epsilon, ctx);
		if (!ctx.isVerbose()) System.out.println("Done! (logLikelihood= " + logLikelihood + ")\n----");
		
		
	}


	private double estimate(double epsilon, EngineContext ctx) {
		double pastLogLikelihood = Double.POSITIVE_INFINITY;
		double logLikelihood = 0d;

		int round = 1;
		while (Math.abs(logLikelihood - pastLogLikelihood) > epsilon) {
			pastLogLikelihood = logLikelihood;

			if (!ctx.isVerbose()) System.out.print(round+"... ");
			Double diffZetas = estimateObjectZetas();
			Double diffWorkers = estimateWorkerRho();
			round++;
			if (!ctx.isVerbose()) System.out.println("");
			if (Double.isNaN(diffZetas+diffWorkers)) {
				System.err.println("ERROR: Check for division by 0");
				break;
			}
			logLikelihood = getLogLikelihood();
		}
		

		return logLikelihood;
	}
	private double getLogLikelihood() {
		double result = 0d;
		for (Worker w : this.workers) {
			String workerToIgnore = w.getName();
			for (AssignedLabel zl : w.getZetaValues()) {
				HashMap<String, Double> zetas = estimateObjectZetas(workerToIgnore);
				String oid = zl.getDatum();
				Double zeta = zetas.get(oid);
				double rho = w.getEst_rho();
				result += 0.5*Math.pow(zeta, 2) / (1-Math.pow(rho, 2)) - Math.log(Math.sqrt(1-Math.pow(rho, 2)));
			}			
		}
		return result;
		
	}
	
	
	private void initWorkers() {

		double initial_rho = 0.9;
		for (Worker w : this.workers) {
			w.setEst_rho(initial_rho);
			w.computeZetaValues();
		}

	}

	private Double estimateObjectZetas() {

		// See equation 9
		double diff = 0.0;
		for (DatumCont d : this.objects) {
			Double oldZeta = 0.0;
			Double newZeta = 0.0;
			Double zeta = 0.0;
			Double betasum = 0.0;
			if(!d.isGold()) {
				oldZeta = d.getEst_zeta();
				for (AssignedLabel al : d.getAssignedLabels()) {
					String wid = al.getWorker();
					Worker w = this.workers_index.get(wid);
					Double b = w.getBeta();
					Double r = w.getEst_rho();
					Double z = w.getZeta(al.getLabel());
					zeta +=  b* r * z;
					betasum += b;
				}
				
				//d.setEst_zeta(zeta / betasum);
				newZeta = zeta/ betasum;
			} else {
				oldZeta = d.getGoldZeta();
				newZeta = d.getGoldZeta();
			}
				
			d.setEst_zeta(newZeta);
			this.objects_index.put(d.getName(), d);
			
			if (d.isGold())
				continue;
			else if (oldZeta == null) {
				diff += 1;
				continue;
			}
			
			diff += Math.abs(d.getEst_zeta() - oldZeta);
		}
		
		return diff;

	}

	private HashMap<String, Double> estimateObjectZetas(String workerToIgnore) {
			
		HashMap<String, Double> result = new HashMap<String, Double>();

		// See equation 9 without the influence of worker w
		for (DatumCont d : this.objects) {
			Double newZeta = 0.0;
			Double zeta = 0.0;
			Double betasum = 0.0;

			for (AssignedLabel al : d.getAssignedLabels()) {
				String wid = al.getWorker();
				if(wid.equals(workerToIgnore))
					continue;
				Worker w = this.workers_index.get(wid);
				Double b = w.getBeta();
				Double r = w.getEst_rho();
				Double z = w.getZeta(al.getLabel());
				zeta +=  b* r * z;
				betasum += b;
			}
			
			//d.setEst_zeta(zeta / betasum);
			newZeta = zeta/ betasum;				
			result.put(d.getName(), newZeta);
		}
		
		return result;

	}

	private double estimateWorkerRho() {

		// See equation 10


		double diff = 0.0;
		for (Worker w : this.workers) {

			String workerToIgnore = w.getName();
			
			Double sum_prod = 0.0;
			Double sum_zi = 0.0;
			Double sum_zij = 0.0;
			
			double oldrho = w.getEst_rho();
			for (AssignedLabel zl : w.getZetaValues()) {
				
				HashMap<String, Double> zeta = estimateObjectZetas(workerToIgnore);
				
				String oid = zl.getDatum();
				Double z_i = zeta.get(oid);
				double z_ij = zl.getLabel();

				sum_prod += z_i * z_ij;
				sum_zi += z_i * z_i;
				sum_zij += z_ij * z_ij;
			}
			double rho = sum_prod / Math.sqrt(sum_zi * sum_zij);

			w.setEst_rho(rho);
			this.workers_index.put(w.getName(), w);
	
			//System.out.println(w.toString());

			diff += Math.abs(w.getEst_rho() - oldrho);
		}
		return diff;
	}

	public Set<DatumCont> getObjects() {

		return objects;
	}

	public void setObjects(Set<DatumCont> objects) {

		this.objects = objects;
	}


	
	/**
	 * @return the workers
	 */
	public Set<Worker> getWorkers() {
	
		return workers;
	}


	
	/**
	 * @param workers the workers to set
	 */
	public void setWorkers(Set<Worker> workers) {
	
		this.workers = workers;
	}

}
