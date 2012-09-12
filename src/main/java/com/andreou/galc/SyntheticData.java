package com.andreou.galc;


public class SyntheticData extends Data {


	private Double							data_mu;
	private Double							data_sigma;

	private Generator										muGenerator;
	private Generator										sigmaGenerator;
	private Generator										rhoGenerator;

	private Generator										datumGenerator;

	public SyntheticData() {

	}

	public void setDataParameters(Double mu, Double sigma) {

		this.data_mu = mu;
		this.data_sigma = sigma;
		datumGenerator = new Generator(Generator.Distribution.GAUSSIAN);
		datumGenerator.setGaussianParameters(mu, sigma);
	}

	public void setWorkerParameters(Double mu_down, Double mu_up, Double sigma_down, Double sigma_up, Double rho_down,
			Double rho_up) {

		muGenerator = new Generator(Generator.Distribution.UNIFORM);
		muGenerator.setUniformParameters(mu_down, mu_up);

		sigmaGenerator = new Generator(Generator.Distribution.UNIFORM);
		sigmaGenerator.setUniformParameters(sigma_down, sigma_up);

		rhoGenerator = new Generator(Generator.Distribution.UNIFORM);
		rhoGenerator.setUniformParameters(rho_down, rho_up);
	}

	public void build(int k_objects, int l_workers) {

		createObjects(k_objects);
		createWorkers(l_workers);
		createLabels();

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
			w.setTrueMu(muGenerator.nextData());
			w.setTrueSigma(sigmaGenerator.nextData());
			w.setTrueRho(rhoGenerator.nextData());
			this.workers.add(w);
		}

	}

}
