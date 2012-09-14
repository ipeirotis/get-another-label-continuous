package com.andreou.galc.engine;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;


public class EngineContext {

	@Argument(index=0, metaVar="<labelsfile>", required=true, usage="A tab-separated text file. Each line has the form <workerid><tab><objectid><tab><assigned_label> and records the label that the given worker gave to that object")
	String labelsFile = "";

	@Argument(index=1, metaVar="<objectsfile>", usage="")
	String objectsFile = "";

	@Argument(index=2, metaVar="<workersfile>", usage="")
	String workersFile = "";

	@Argument(index=3, metaVar="<evaluationfile>", usage="Evaluation File (TBD)")
	String evaluationFile = "";

	@Option(name="--iterations", metaVar="<num-iterations>", usage="is the number of times to run the algorithm. Even a value of 10 (the default) less often works well.")
	int numIterations = 10;

	@Option(name="--verbose", usage="Verbose Mode?")
	boolean verbose = false;

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

	public boolean isVerbose() {
		return verbose;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	


}
