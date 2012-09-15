package com.andreou.galc.engine;

import org.kohsuke.args4j.Option;


public class EngineContext {

	@Option(name="--input", metaVar="<labelsfile>", required=true, usage="A tab-separated text file. Each line has the form <workerid><tab><objectid><tab><assigned_label> and records the label that the given worker gave to that object")
	private String labelsfile = "";

	@Option(name="--objects", metaVar="<objectsfile>", usage="")
	private String objectsFile = "";

	@Option(name="--workers", metaVar="<workersfile>", usage="")
	private String workersFile = "";

	@Option(name="--eval", metaVar="<evaluationfile>", usage="An Evaluation Report File")
	private String evaluationFile = "";

	@Option(name="--verbose", metaVar="<verbose>", usage="Verbose Mode?")
	private boolean verbose;

	@Option(name="--syntheticDataSet", usage="Create new synthetic DataSet or use empirical DataSet?")
	private boolean syntheticDataSet;

	public String getLabelsFile() {
		return labelsfile;
	}

	public void setLabelsFile(String labelsFile) {
		this.labelsfile = labelsFile;
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
