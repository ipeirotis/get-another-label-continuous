package com.andreou.galc;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.random.CorrelatedRandomVectorGenerator;
import org.apache.commons.math3.random.GaussianRandomGenerator;
import org.apache.commons.math3.random.JDKRandomGenerator;
import org.apache.commons.math3.random.RandomDataGenerator;

import org.apache.commons.math3.random.RandomGenerator;

public class Generator {

	public enum Distribution {
		GAUSSIAN, UNIFORM, BIVARIATE;
	}
	private RandomGenerator gen;
	
	private Distribution	dist;
	private RandomDataGenerator		randomData;
	private CorrelatedRandomVectorGenerator bivariateData;

	private double[]	bivar_mu = {0.0,0.0};
	private RealMatrix covariance; 
	private GaussianRandomGenerator gaussData;

	public Generator(Distribution d) {

		dist = d;
		gen = new JDKRandomGenerator();
		//for GAUSSIAN & UNIFORM
		randomData = new RandomDataGenerator(gen);
		//for GAUSSIAN BIVARIATE
		gaussData = new GaussianRandomGenerator(gen);
	}

	private Double	mu;
	private Double	sigma;

	public void setGaussianParameters(Double mu, Double sigma) {

		this.mu = mu;
		this.sigma = sigma;
	}

	Double	up;
	Double	down;

	public void setUniformParameters(Double d, Double u) {

		this.up = u;
		this.down = d;
	}

	public void setBivariateParameters(Double mu, Double sigma, Double c) {

		bivar_mu[0] = this.mu;
		bivar_mu[1] = mu;
		Double auxc = c/Math.sqrt(this.mu)/Math.sqrt(mu);
		double[][] cov = {{this.mu, auxc}, {auxc, mu}};
		covariance = MatrixUtils.createRealMatrix(cov);
		bivariateData = new CorrelatedRandomVectorGenerator(bivar_mu, covariance, 
				1.0e-12 * covariance.getNorm(), gaussData);
	}

	public Double nextData() {

		switch (dist) {
			case GAUSSIAN:
				return randomData.nextGaussian(mu, sigma);
			case UNIFORM:
				return randomData.nextUniform(down, up);
			//TODO: it seems that we won't need this case
			case BIVARIATE:
				return (gaussData.nextNormalizedDouble()+mu)*sigma;
			default:
				return null;
		}
	}

	public double[] nextVector() {

		switch (dist) {
			case BIVARIATE:
				return bivariateData.nextVector();	
			default:
				return null;
		}
	}	

}
