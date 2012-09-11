package com.andreou.galc;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Engine {

	private Set<DatumCont>					objects;
	private Map<String, DatumCont>	objects_index;
	private Set<Worker>							workers;
	private Map<String, Worker>			workers_index;
	private Set<AssignedLabel>			labels;

	// private Set<Category> current_categories = new HashSet<Category>();
	// private Set<Worker> current_workers = new HashSet<Worker>();

	// private Ipeirotis ip;

	public Engine() {

		SyntheticData data = new SyntheticData();
		Double data_mu = 0.0;
		Double data_sigma = 1.0;
		data.setDataParameters(data_mu, data_sigma);

		Double worker_mu_down = -10.0;
		Double worker_mu_up = 10.0;
		Double worker_sigma_down = 0.0;
		Double worker_sigma_up = 1.0;
		Double worker_rho_down = 0.0;
		Double worker_rho_up = 0.9;
		data.setWorkerParameters(worker_mu_down, worker_mu_up, worker_sigma_down, worker_sigma_up, worker_rho_down,
				worker_rho_up);

		int data_points = 500;
		int workers = 10;
		data.build(data_points, workers);

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

		// Run until convergence.
		int round = 1;
		double epsilon = 0.0001;
		while (true) {
			System.out.println("Round:" + round);
			double d1 = estimateObjectZetas();
			System.out.println("DiffObjects:" + d1);
			double d2 = estimateWorkerRho();
			System.out.println("DiffWorkers:" + d2);
			round++;
			if (d1+d2<epsilon) break;
			if (Double.isNaN(d1+d2)) System.err.println("ERROR: Check for division by 0");
		}

	}

	private void initWorkers() {

		Generator rhoGenerator = new Generator(Generator.Distribution.UNIFORM);

		// We do not assign the full range from -1 to 1, because
		// in that case we will not converge into a reasonable equilibrium
		rhoGenerator.setUniformParameters(0.5, 0.9);

		for (Worker w : this.workers) {
			Double rho_init = rhoGenerator.nextData();
			w.setRho(rho_init);

			w.computeZetaValues();
		}

	}

	private double estimateObjectZetas() {

		// See equation 9

		double diff = 0.0;
		for (DatumCont d : this.objects) {
			Double oldzeta = d.getZeta();
			if (oldzeta == null)
				oldzeta = 0.0;

			Double zeta = 0.0;
			Double betasum = 0.0;
			for (AssignedLabel al : d.getAssignedLabels()) {
				String wid = al.getWorker();
				Worker w = this.workers_index.get(wid);
				zeta +=  Math.sqrt( Math.pow(w.getBeta(), 2) - w.getBeta()) * w.getZeta(al.getLabel());
				betasum += w.getBeta();
			}

			d.setZeta(zeta / betasum);

			diff += Math.abs(d.getZeta() - oldzeta);
		}
		return diff;

	}

	private double estimateWorkerRho() {

		// See equation 10
		Double sum_prod = 0.0;
		Double sum_zi = 0.0;
		Double sum_zij = 0.0;

		double diff = 0.0;
		for (Worker w : this.workers) {

			double oldrho = w.getRho();
			for (AssignedLabel zl : w.getZetaValues()) {
				String oid = zl.getDatum();
				DatumCont d = objects_index.get(oid);
				double z_i = d.getZeta();
				double z_ij = zl.getLabel();

				sum_prod += z_i * z_ij;
				sum_zi += z_i * z_i;
				sum_zij += z_ij * z_ij;
			}
			double rho = sum_prod / Math.sqrt(sum_zi * sum_zij);

			w.setRho(rho);
			System.out.println(w.toString());

			diff += Math.abs(w.getRho() - oldrho);
		}
		return diff;
	}

	public Set<DatumCont> getObjects() {

		return objects;
	}

	public void setObjects(Set<DatumCont> objects) {

		this.objects = objects;
	}

}
