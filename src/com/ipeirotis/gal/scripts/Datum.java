package com.ipeirotis.gal.scripts;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.ipeirotis.utils.Utils;

public class Datum {

	String									name;

	// Defines if we have the correct category for this object
	// and if it is gold, the correctCategory holds the name of the correct category
	Boolean									isGold;
	String									correctCategory;

	// The probability estimates for the object belonging to different categories
	HashMap<String, Double>	categoryProbability;

	// The labels that have been assigned to this object, together with the workers who
	// assigned these labels. Serves mainly as a speedup, and intended to be used in
	// environments with persistence and caching (especially memcache)
	Set<AssignedLabel>			labels;

	/**
	 * @return the isGold
	 */
	public Boolean isGold() {

		return isGold;
	}

	/**
	 * @param isGold
	 *          the isGold to set
	 */
	public void setGold(Boolean isGold) {

		this.isGold = isGold;
	}

	/**
	 * @return the goldCategory
	 */
	public String getCorrectCategory() {

		return correctCategory;
	}

	/**
	 * @param goldCategory
	 *          the goldCategory to set
	 */
	public void setCorrectCategory(String correctCategory) {

		this.correctCategory = correctCategory;

	}

	public Double getCategoryProbability(String c) {

		if (this.isGold) {
			if (c.equals(this.correctCategory)) {
				return 1.0;
			} else {
				return 0.0;
			}
		}
		return categoryProbability.get(c);
	}

	public void setCategoryProbability(String c, Double prob) {

		categoryProbability.put(c, prob);
	}

	public Double getEntropy() {

		double[] p = new double[this.categoryProbability.size()];

		int i = 0;
		for (String c : this.categoryProbability.keySet()) {
			p[i] = getCategoryProbability(c);
			i++;
		}

		return Utils.entropy(p);
	}

	public Datum(String name, Set<Category> categories) {

		this.name = name;
		this.isGold = false;
		this.correctCategory = null;
		this.labels = new HashSet<AssignedLabel>();

		// We initialize the probabilities vector to be uniform across categories
		this.categoryProbability = new HashMap<String, Double>();
		for (Category c : categories) {
			this.categoryProbability.put(c.getName(), 1.0 / categories.size());
		}
	}

	public void addAssignedLabel(AssignedLabel al) {

		if (al.getObjectName().equals(name)) {
			this.labels.add(al);
		}
	}

	public Set<AssignedLabel> getAssignedLabels() {

		return this.labels;
	}

	public String getMajorityCategory() {

		double maxProbability = -1;
		String majorityCategory = null;

		for (String category : this.categoryProbability.keySet()) {
			Double probability = this.categoryProbability.get(category);
			if (probability > maxProbability) {
				maxProbability = probability;
				majorityCategory = category;
			} else if (probability == maxProbability) {
				// In case of a tie, break ties randomly
				// TODO: This is a corner case. We can also break ties
				// using the priors. But then we also need to group together
				// all the ties, and break ties probabilistically across the
				// group. Otherwise, we slightly favor the later comparisons.
				if (Math.random() > 0.5) {
					maxProbability = probability;
					majorityCategory = category;
				}
			}
		}

		return majorityCategory;
	}

	/**
	 * @return the categoryProbability
	 */
	public HashMap<String, Double> getCategoryProbability() {

		return categoryProbability;
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
		if (!(obj instanceof Datum))
			return false;
		Datum other = (Datum) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	/**
	 * @return the name
	 */
	public String getName() {

		return name;
	}

	/**
	 * @param name
	 *          the name to set
	 */
	public void setName(String name) {

		this.name = name;
	}

}