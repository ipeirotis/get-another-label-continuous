package com.ipeirotis.galc;

import java.util.Set;


public class Category {
	
	private String name;
	private Double value;
	private Double label;

	private Double mean	= 0.0;
	private Double std	= 0.0;
	private Double zeta	= 0.0;
	private Set<Worker> workers;

	public Category(String name){
		this.name = name;
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}
	
	public Double getLabel() {
		return label;
	}
	public void setLabel(Double label) {
		this.label = label;
	}

	public Double getMean() {
		return mean;
	}

	public void setMean(Double mean) {
		this.mean = mean;
	}

	public Double getStd() {
		return std;
	}

	public void setStd(Double std) {
		this.std = std;
	}

	public Double getZeta() {
		return zeta;
	}

	public void setZeta(Double zeta) {
		this.zeta = zeta;
	}
	
	public Set<Worker> getWorkers() {
		return workers;
	}

	public void setWorkers(Set<Worker> workers) {
		this.workers = workers;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Category))
			return false;
		Category other = (Category) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
		
}
