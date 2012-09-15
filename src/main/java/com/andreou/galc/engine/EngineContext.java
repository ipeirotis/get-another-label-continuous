package com.andreou.galc.engine;

import org.kohsuke.args4j.Option;


public class EngineContext {

	@Option(name="--input", metaVar="<inputfile>", required=true, usage="A tab-separated text file. Each line has the form <workerid><tab><objectid><tab><assigned_label> and records the label that the given worker gave to that object")
	private String inputfile = "";

	@Option(name="--evalObjects", metaVar="<trueobjectsfile>", usage="")
	private String trueobjectsfile = "";

	@Option(name="--evalWorkers", metaVar="<trueworkersfile>", usage="")
	private String trueworkersfile = "";

	@Option(name="--output", metaVar="<outputfolder>", usage="An Evaluation Report File")
	private String outputfolder = "results";

	@Option(name="--verbose", metaVar="<verbose>", usage="Verbose Mode?")
	private boolean verbose;

	@Option(name="--syntheticDataSet", usage="Create new synthetic DataSet or use empirical DataSet?")
	private boolean syntheticDataSet;

	public String getInputFile() {
		return inputfile;
	}

	public void setInputFile(String inputfile) {
		this.inputfile = inputfile;
	}

	public String getTrueObjectsFile() {
		return trueobjectsfile;
	}

	public void setTrueObjectsFile(String trueobjectsfile) {
		this.trueobjectsfile = trueobjectsfile;
	}

	public String getTrueWorkersFile() {
		return trueworkersfile;
	}

	public void setTrueWorkersFile(String trueworkersfile) {
		this.trueworkersfile = trueworkersfile;
	}

	public String getOutputFolder() {
		return outputfolder;
	}

	public void setOutputFolder(String outputfolder) {
		this.outputfolder = outputfolder;
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
