package com.andreou.galc;

import org.junit.Before;
import org.junit.Test;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import com.andreou.galc.engine.Engine;
import com.andreou.galc.engine.EngineContext;

public class GeneratorSingleRhoSupervisedDataTest {
	private EngineContext ctx;
	private CmdLineParser parser;

	@Before
	public void setUp() {
		ctx = new EngineContext();
		parser = new CmdLineParser(ctx);
		parser.setUsageWidth(300);
	}

	private void executeOn(String unparsedArgs) throws Exception {
		parseArgs(unparsedArgs);

		Engine engine = new Engine(ctx);

		engine.execute();
	}

	private void parseArgs(String unparsedArgs) throws CmdLineException {
		String[] args = unparsedArgs.split("\\s+");

		parser.parseArgument(args);
	}
	
	@Test
	public void testCreateNewSingleRhoSupervisedData() throws Exception {
		executeOn("--input data/synthetic/singlerhosupervised/assignedlabels.txt --evalObjects data/synthetic/singlerhosupervised/evaluationObjects.txt --evalWorkers data/synthetic/singlerhosupervised/evaluationWorkers.txt --correct data/synthetic/singlerhosupervised/goldObjects.txt --synthetic data/synthetic/singlerhosupervised/options.txt --output results/synthetic/singlerhosupervised");
	}
	@Test
	public void testNewSingleRhoData() throws Exception {
		//executeOn("--input data/synthetic/singlerhosupervised/assignedlabels.txt --evalObjects data/synthetic/singlerhosupervised/evaluationObjects.txt --evalWorkers data/synthetic/singlerhosupervised/evaluationWorkers.txt --output results/synthetic/singlerhosupervised");
	}
}

