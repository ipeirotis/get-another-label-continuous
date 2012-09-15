package com.andreou.galc.engine;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;


public class EngineContext {

	@Argument(index=0, metaVar="<labelsfile>", required=true, usage="A tab-separated text file. Each line has the form <workerid><tab><objectid><tab><assigned_label> and records the label that the given worker gave to that object")
	private String labelsFile = "";

	@Argument(index=1, metaVar="<objectsfile>", usage="")
	private String objectsFile = "";

	@Argument(index=2, metaVar="<workersfile>", usage="")
	private String workersFile = "";

	@Argument(index=3, metaVar="<evaluationfile>", usage="An Evaluation Report File")
	private String evaluationFile = "";

	@Option(name="--iterations", metaVar="<num-iterations>", usage="is the maximum number of times to run the algorithm until convergence. Usually convergence achieved before the value of 20 (the default).")
	private int numIterations = 20;

	@Option(name="--v", metaVar="<verbose>", usage="Verbose Mode?")
	private boolean verbose;

	@Option(name="--syntheticDataSet", usage="Create new synthetic DataSet or use empirical DataSet?")
	private boolean syntheticDataSet;

	public String getLabelsFile() {
		return labelsFile;
	}

	public void setLabelsFile(String labelsFile) {
		this.labelsFile = labelsFile;
	}

	public String getObjectsFile() {
		return objectsFile;
	}

	public void setObjectsFile(String objectsFile) {
		this.objectsFile = objectsFile;
	}

	public String getWorkersFile() {
		return workersFile;
	}

	public void setWorkersFile(String workersFile) {
		this.workersFile = workersFile;
	}

	public String getEvaluationFile() {
		return evaluationFile;
	}

	public void setEvaluationFile(String evaluationFile) {
		this.evaluationFile = evaluationFile;
	}

	public int getNumIterations() {
		return numIterations;
	}

	public void setNumIterations(int numIterations) {
		this.numIterations = numIterations;
	}

	public boolean isVerbose() {
		return verbose;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	public boolean isSyntheticDataSet() {
		return syntheticDataSet;
	}

	public void setSyntheticDataSet(boolean syntheticDataSet) {
		this.syntheticDataSet = syntheticDataSet;
	}

	


}
