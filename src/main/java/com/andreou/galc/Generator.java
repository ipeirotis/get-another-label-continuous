package com.andreou.galc;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.apache.commons.math.random.RandomData;
import org.apache.commons.math.random.RandomDataImpl;

public class Generator {
	
	public static int GAUSSIAN = 0;
	public static int JAVARANDOM = 1;
	public static int JAVARANDOM_minus = -1;

	private Double mu;
	private Double sigma;
	private Double x;

	private RandomData randomData = new RandomDataImpl(); 

	public Generator() {


		
	}

	public Double nextData(Double mu, Double sigma, int dist) {
		Double out=0.0; 
		switch(dist){
			case 0: 	out = GaussianDistr(mu,sigma);
				break;
			case -1: 	out = RandDistr();
				break;
			case 1: 	out = Random();
				break;
			default: 	out = GaussianDistr(sigma, mu );
				break;
		}

		return out;
	}
	
	private Double GaussianDistr(Double mu, Double sigma ){
		Double out= 0.0;
		out = this.randomData.nextGaussian(mu, sigma);
		return out;
	}
	private Double RandDistr() {
		Random rn = new Random();
		return  2 * rn.nextDouble() - 1;
		
	}
	private Double Random() {
		Random rn = new Random();
		return  rn.nextDouble();
		
	}

}
