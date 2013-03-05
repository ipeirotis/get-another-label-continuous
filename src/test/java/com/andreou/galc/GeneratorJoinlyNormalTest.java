package com.andreou.galc;


import org.junit.Before;
import org.junit.Test;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import com.andreou.galc.engine.Engine;
import com.andreou.galc.engine.EngineContext;

public class GeneratorJoinlyNormalTest {
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
	public void testCreateNewJoinlyNormalData() throws Exception {
		//TODO: fix joinlynormalGenerator
//			executeOn("--input data/synthetic/joinlynormal/assignedlabels.txt --evalObjects data/synthetic/joinlynormal/evaluationObjects.txt --evalWorkers data/synthetic/joinlynormal/evaluationWorkers.txt --correct data/synthetic/joinlynormal/goldObjects.txt --synthetic data/synthetic/joinlynormal/options.txt --output results/synthetic/joinlynormal");
	}
	@Test
	public void testNewJoinlyNormalData() throws Exception {
		//executeOn("--input data/synthetic/joinlynormal/assignedlabels.txt --evalObjects data/synthetic/joinlynormal/evaluationObjects.txt --evalWorkers data/synthetic/joinlynormal/evaluationWorkers.txt --output results/synthetic/joinlynormal");
	}
}

