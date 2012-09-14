package com.andreou.galc;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Ipeirotis {

	private Set<DatumCont>					objects;
	private Map<String, DatumCont>	objects_index;
	private Set<Worker>							workers;
	private Map<String, Worker>			workers_index;
	private Set<AssignedLabel>			labels;
	


	public Ipeirotis(Data data) {


		
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

		this.labels = data.getLabels();

		initWorkers();
		//System.out.println("=======");
		estimateObjectZetas();
		//generateObjectReport(data_mu, data_sigma);
		//generateWorkerReport();
		//System.out.println("=======");

		// Run until convergence.
		int round = 1;
		double epsilon = 0.00001;
		//System.out.print("----\nRound: ");
		while (true) {
			System.out.print(round+"... ");
			double d1 = estimateObjectZetas();
			//System.out.println("DiffObjects:" + d1);
			double d2 = estimateWorkerRho();
			//System.out.println("DiffWorkers:" + d2);
			round++;
			System.out.println("");
			if (d1+d2<epsilon) break;
			if (Double.isNaN(d1+d2)) {
				System.err.println("ERROR: Check for division by 0");
				break;
			}
		}
		System.out.println("Done!\n----");
		
		
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
			Double oldzeta = d.getEst_zeta();
			
			Double zeta = 0.0;
			Double betasum = 0.0;
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
			Double newZeta = zeta/ betasum;
			d.setEst_zeta(newZeta);
			this.objects_index.put(d.getName(), d);
			
			if (oldzeta == null) {
				diff += 1;
				continue;
			}
			
			diff += Math.abs(d.getEst_zeta() - oldzeta);
		}
		
		return diff;

	}

	private double estimateWorkerRho() {

		// See equation 10


		double diff = 0.0;
		for (Worker w : this.workers) {

			Double sum_prod = 0.0;
			Double sum_zi = 0.0;
			Double sum_zij = 0.0;
			
			double oldrho = w.getEst_rho();
			for (AssignedLabel zl : w.getZetaValues()) {
				String oid = zl.getDatum();
				DatumCont d = objects_index.get(oid);
				double z_i = d.getEst_zeta();
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
