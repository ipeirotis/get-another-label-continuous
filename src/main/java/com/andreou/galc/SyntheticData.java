package com.andreou.galc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class SyntheticData {

	private Set<DatumCont>										objects	= new HashSet<DatumCont>();
	private Set<Worker>											workers			= new HashSet<Worker>();
	private HashMap<CategoryWorker, Double>	labels			= new HashMap<CategoryWorker, Double>();

	private Double													mu					= new Double(0.0);
	private Double													sigma				= new Double(1.0);

	public SyntheticData(int k_objects, int l_workers) {

		buildCategories(k_objects);
		buildWorkers(l_workers);

	}

	public void initData(Double mu, Double sigma) {

		this.mu = mu;
		this.sigma = sigma;
	}

	public void build() {

		// Generate Object Real Values x_i
		Generator datumGenerator =  new Generator(Generator.Distribution.GAUSSIAN);
		datumGenerator.setGaussianParameters(this.mu, this.sigma);
		
		for (DatumCont c : this.objects) {
			c.setTrueValue(datumGenerator.nextData());
			System.out.println("(Object, value): " + c.getName() + ", " + c.getTrueValue());
		}

		// Generate Worker Characteristics
		Generator muGenerator =  new Generator(Generator.Distribution.UNIFORM);
		muGenerator.setUniformParameters(-10.0, 10.0);
		
		Generator sigmaGenerator =  new Generator(Generator.Distribution.UNIFORM);
		sigmaGenerator.setUniformParameters(0.0, 10.0);
		
		Generator rhoGenerator =  new Generator(Generator.Distribution.UNIFORM);
		rhoGenerator.setUniformParameters(-1.0, 1.0);
		
		for (Worker w : this.workers) {
			w.setMu(muGenerator.nextData());
			w.setSigma(sigmaGenerator.nextData());
			w.setRho(rhoGenerator.nextData());
			System.out.println("(Worker, mu, sigma, rho): " + w.getName() + ", " + w.getMu() + ", " + w.getSigma() + ", "
					+ w.getRho());
		}

		// Generate Observation Values y_ij

		for (DatumCont c : this.objects)
			for (Worker w : this.workers) {
				CategoryWorker label = new CategoryWorker(c, w);
				// Using Bivariate-Normal Distribution (ref: http://www.athenasc.com/Bivariate-Normal.pdf)
				
				Double label_mu = w.getMu() + w.getRho() * (w.getSigma() / this.sigma) * (c.getTrueValue() - this.mu);
				
				Double label_sigma = Math.sqrt((1 - w.getRho() * w.getRho())) * w.getSigma();
				
				Generator labelGenerator =  new Generator(Generator.Distribution.GAUSSIAN);
				labelGenerator.setGaussianParameters(label_mu, label_sigma);
				
				labels.put(label, labelGenerator.nextData());
				
				System.out.println("(" + c.getName() + "," + w.getName() + "):" + labels.get(label) + " (mu,sigma):(" + mu + ", "
						+ sigma + ")");
			}
	}

	private void buildCategories(int k_objects) {

		for (int i = 0; i < k_objects; i++)
			this.objects.add(new DatumCont("obj" + (i + 1)));
	}

	private void buildWorkers(int l_workers) {

		for (int i = 0; i < l_workers; i++)
			this.workers.add(new Worker("work" + (i + 1)));
	}

	public Set<DatumCont> getCategories() {

		return objects;
	}

	public Set<Worker> getWorkers() {

		return workers;
	}

	public HashMap<CategoryWorker, Double> getLabels() {

		return labels;
	}
}
