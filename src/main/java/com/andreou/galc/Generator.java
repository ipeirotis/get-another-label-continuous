package com.andreou.galc;

import org.apache.commons.math3.random.RandomData;
import org.apache.commons.math3.random.RandomDataImpl;

public class Generator {

	public enum Distribution {
		GAUSSIAN, UNIFORM;
	}

	private Distribution	dist;
	private RandomData		randomData;

	public Generator(Distribution d) {

		this.dist = d;
		this.randomData = new RandomDataImpl();
	}

	double	mu		= 0;
	double	sigma	= 1;

	public void setGaussianParameters(Double mu, Double sigma) {

		this.mu = mu;
		this.sigma = sigma;
	}

	double	up		= 1;
	double	down	= 0;

	public void setUniformParameters(Double d, Double u) {

		this.up = u;
		this.down = d;
	}

	public Double nextData() {

		switch (dist) {
			case GAUSSIAN:
				return this.randomData.nextGaussian(mu, sigma);
			case UNIFORM:
				return this.randomData.nextUniform(down, up);
			default:
				return null;
		}

	}

}
