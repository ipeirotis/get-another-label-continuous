package com.andreou.galc;

import java.util.Set;
import java.util.TreeSet;

public class DatumCont implements Comparable<DatumCont> {

	private String							name;
	private Set<AssignedLabel>	labels;

	private Double							est_value;
	private Double							est_zeta;

	// Data generation characteristics
	private Double							trueValue;

	public DatumCont(String name) {

		this.name = name;
		this.labels = new TreeSet<AssignedLabel>();

	}

	public void addAssignedLabel(AssignedLabel al) {

		if (al.getDatum().equals(name)) {
			this.labels.add(al);
		}
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public Double getValue() {

		return est_value;
	}

	public void setValue(Double value) {

		this.est_value = value;
	}

	public Double getZeta() {

		return est_zeta;
	}

	public void setZeta(Double zeta) {

		this.est_zeta = zeta;
	}

	public Double getTrueValue() {

		return trueValue;
	}

	public void setTrueValue(Double trueValue) {

		this.trueValue = trueValue;
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
		if (!(obj instanceof DatumCont))
			return false;
		DatumCont other = (DatumCont) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	/**
	 * @return the labels
	 */
	public Set<AssignedLabel> getAssignedLabels() {

		return labels;
	}

	/**
	 * @param labels
	 *          the labels to set
	 */
	public void setLabels(Set<AssignedLabel> labels) {

		this.labels = labels;
	}

	@Override
	public int compareTo(DatumCont o) {

		return this.getName().compareTo(o.getName());
	}

}
