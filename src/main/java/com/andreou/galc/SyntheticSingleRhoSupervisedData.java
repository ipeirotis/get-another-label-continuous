package com.andreou.galc;

public class SyntheticSingleRhoSupervisedData extends Data {

	private int				data_points;
	private Double		data_mu;
	private Double		data_sigma;
	private int				data_gold;
	private int				workers_points;
	private Double		worker_mu_down;
	private Double		worker_mu_up;
	private Double		worker_sigma_down;
	private Double		worker_sigma_up;
	private Double		worker_rho;

	private Double		supervisor_rho = 0.99;

	private String		dist;

	private Generator	muGenerator;
	private Generator	sigmaGenerator;

	private Generator	datumGenerator;

	public SyntheticSingleRhoSupervisedData(Boolean verbose, String file) {

		loadSyntheticOptions(file);
		if(!verbose) {
			System.out.println("-----------------------------------");
			System.out.println("Data points: " + this.data_points);
			System.out.println("Data gold: " + this.data_gold);
			System.out.println("Workers: " + this.workers_points);
			System.out.println("Workers rho: " + this.worker_rho);
			System.out.println("Distribution: " + this.dist);
		}

	}

	public void initDataParameters() {
		datumGenerator = new Generator(Generator.Distribution.GAUSSIAN);
		datumGenerator.setGaussianParameters(this.data_mu, this.data_sigma);
	}

	public void initWorkerParameters() {

		muGenerator = new Generator(Generator.Distribution.UNIFORM);
		muGenerator.setUniformParameters(this.worker_mu_down, this.worker_mu_up);

		sigmaGenerator = new Generator(Generator.Distribution.UNIFORM);
		sigmaGenerator.setUniformParameters(this.worker_sigma_down, this.worker_sigma_up);

	}

	public void build() {

		createObjects(this.data_points);
		createGold(this.data_gold);
		createWorkers(this.workers_points);
		createLabels();

	}

	private void createGold(int g_gold_objects) {

		//first g_gold_objects will be gold
		int i = 0;

		for (DatumCont d : this.objects) {
			if(i++ < g_gold_objects) {
				d.setGold(true);
				d.setGoldValue(d.getTrueValue());
				d.setGoldZeta(d.getTrueZeta());
			}
		}
	}

	private void createLabels() {

		// Generate Observation Values y_ij

		for (DatumCont d : this.objects) {
			for (Worker w : this.workers) {

				Double datum_z = (d.getTrueValue() - this.data_mu) / this.data_sigma;
				Double label_mu = w.getTrueMu() + w.getTrueRho() * w.getTrueSigma() * datum_z;
				Double label_sigma = Math.sqrt(1 - Math.pow(w.getTrueRho(), 2)) * w.getTrueSigma();

				Generator labelGenerator = new Generator(Generator.Distribution.GAUSSIAN);
				labelGenerator.setGaussianParameters(label_mu, label_sigma);
				Double label = labelGenerator.nextData();

				AssignedLabel al = new AssignedLabel(w.getName(), d.getName(), label);
				labels.add(al);
				w.addAssignedLabel(al);
				d.addAssignedLabel(al);
			}
		}
	}

	private void createObjects(int k_objects) {

		// Generate Object Real Values x_i
		for (int i = 0; i < k_objects; i++) {
			DatumCont d = new DatumCont("Object" + (i + 1));
			Double v = datumGenerator.nextData();
			Double z = (v-this.data_mu)/this.data_sigma;
			d.setTrueValue(v);
			d.setTrueZeta(z);
			this.objects.add(d);
		}
	}

	private void createWorkers(int l_workers) {

		// Generate Worker Characteristics
		for (int i = 0; i < l_workers; i++) {
			Worker w = new Worker("Worker" + (i + 1));
			if(i==0){
				w.setTrueMu(muGenerator.nextData());
				w.setTrueSigma(sigmaGenerator.nextData());
				w.setTrueRho(supervisor_rho);
				this.workers.add(w);
				
			} else {
				w.setTrueMu(muGenerator.nextData());
				w.setTrueSigma(sigmaGenerator.nextData());
				w.setTrueRho(worker_rho);
				this.workers.add(w);
			}
		}

	}

	public void loadSyntheticOptions(String filename) {
		String[] lines = Utils.getFile(filename).split("\n");
		for(String line : lines) {
			String[] entries = line.split("=");
			if (entries.length != 2) {
				throw new IllegalArgumentException("Error while loading from synthetic sptions file");
			} else if(entries[0].equals("dist")) {
				this.dist = entries[1];
			} else if(entries[0].equals("data_points")) {
				this.data_points = (int)Integer.parseInt(entries[1]);
			} else if (entries[0].equals("data_mu")) {
				this.data_mu = Double.parseDouble(entries[1]);
			} else if (entries[0].equals("data_sigma")) {
				this.data_sigma = Double.parseDouble(entries[1]);
			} else if (entries[0].equals("data_gold")) {
				this.data_gold = (int)Integer.parseInt(entries[1]);
			} else if (entries[0].equals("workers")) {
				this.workers_points = (int)Integer.parseInt(entries[1]);
			} else if (entries[0].equals("worker_mu_down")) {
				this.worker_mu_down = Double.parseDouble(entries[1]);
			} else if (entries[0].equals("worker_mu_up")) {
				this.worker_mu_up = Double.parseDouble(entries[1]);
			} else if (entries[0].equals("worker_sigma_down")) {
				this.worker_sigma_down = Double.parseDouble(entries[1]);
			} else if (entries[0].equals("worker_sigma_up")) {
				this.worker_sigma_up = Double.parseDouble(entries[1]);
			} else if (entries[0].equals("worker_rho")) {
				this.worker_rho = Double.parseDouble(entries[1]);
			} else {
				System.err.println("Error in SingleRho synthetic options file variables");
			}

		}

	}

}

