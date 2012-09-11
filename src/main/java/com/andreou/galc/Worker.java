package com.andreou.galc;

import java.util.Set;

public class Worker {

	private String				name;
	private Double				quality;
	private Double				beta;
	private Set<DatumCont>	categories;

	// Data generation characteristics
	private Double				mu;
	private Double				sigma;
	private Double				rho;

	public Worker(String name) {

		this.name = name;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public Double getQuality() {

		return quality;
	}

	public void setQuality(Double quality) {

		this.quality = quality;
	}

	public Double getBeta() {

		return beta;
	}

	public void setBeta(Double beta) {

		this.beta = beta;
	}

	public Set<DatumCont> getCategories() {

		return categories;
	}

	public void setCategories(Set<DatumCont> categories) {

		this.categories = categories;
	}

	public Double getMu() {

		return mu;
	}

	public void setMu(Double mu) {

		this.mu = mu;
	}

	public Double getSigma() {

		return sigma;
	}

	public void setSigma(Double sigma) {

		this.sigma = sigma;
	}

	public Double getRho() {

		return rho;
	}

	public void setRho(Double rho) {

		this.rho = rho;
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Worker))
			return false;
		Worker other = (Worker) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
