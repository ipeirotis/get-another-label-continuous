package com.andreou.galc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class SyntheticData {
	

	private Set<Category> 					categories			= new HashSet<Category>();
	private Set<Worker> 					workers				= new HashSet<Worker>();
	private HashMap<CategoryWorker,Double> 	labels 				= new HashMap<CategoryWorker, Double>();

	private Generator 						gen 				= new Generator();
	private Double 							mu 					= new Double(0.0);
	private Double 							sigma 				= new Double(1.0);

	
	public SyntheticData(int k_objects, int l_workers){
		buildCategories(k_objects);
		buildWorkers(l_workers);
		
	}
	public void initData(Double mu, Double sigma){
		this.mu = mu;
		this.sigma = sigma;
	}
	public void build(){

		//Generate Object Real Values x_i
		for(Category c:this.categories){
			c.setTrueValue(gen.nextData(mu, sigma, gen.GAUSSIAN));
			System.out.println("(name,trueValue):("+c.getName()+", "+ c.getTrueValue()+")");
		}
		
		//Generate Observer Characteristics
		for(Worker w:this.workers){
			w.setMu(gen.nextData(null, null, gen.JAVARANDOM));
			w.setSigma(gen.nextData(null, null, gen.JAVARANDOM));
			w.setRho(gen.nextData(null, null, gen.JAVARANDOM_minus));
			System.out.println("(name,mu,sigma,Rho):"+w.getName()+", "+ w.getMu()+", " + w.getSigma()+", " + w.getRho()+")");
		}
		
		//Generate Observation Values y_ij
		Double mu,sigma;
		for( Category c: this.categories)
			for(Worker w: this.workers){
				CategoryWorker aux = new CategoryWorker(c, w);
				//Using Bivariate-Normal Distribution (ref: http://www.athenasc.com/Bivariate-Normal.pdf)
				mu = w.getMu() + w.getRho() * ( w.getSigma() / this.sigma ) * (c.getTrueValue() - this.mu);
				sigma = Math.sqrt( (1-w.getRho()*w.getRho()) ) *  w.getSigma();
				labels.put(aux, gen.nextData(mu, sigma, gen.GAUSSIAN) );
				System.out.println("("+c.getName() +","+ w.getName() +"):"+ labels.get(aux)+" (mu,sigma):("+ mu+ ", "+ sigma +")");
			}		
	}

	private void buildCategories(int k_objects) {
		for(int i=0; i<k_objects; i++)
			this.categories.add(new Category("obj" + (i+1) ));
	}
	private void buildWorkers(int l_workers) {
		for(int i=0; i<l_workers; i++)
			this.workers.add(new Worker("work" + (i+1) ));
	}

	public Set<Category> getCategories() { return categories; }
	public Set<Worker> getWorkers() { return workers; }
	public HashMap<CategoryWorker, Double> getLabels() { return labels; }	
}
