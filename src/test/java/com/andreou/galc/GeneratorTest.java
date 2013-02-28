package com.andreou.galc;

import org.junit.Before;
import org.junit.Test;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import com.andreou.galc.engine.Engine;
import com.andreou.galc.engine.EngineContext;

public class GeneratorTest {
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
		//executeOn("--input data/joinlynormal/assignedlabels.txt --evalObjects data/joinlynormal/evaluationObjects.txt --evalWorkers data/joinlynormal/evaluationWorkers.txt --correct data/joinlynormal/goldObjects.txt --synthetic data/joinlynormal/options.txt --output results/joinlynormal");
	}
	@Test
	public void testCreateCurrentSyntheticData_verbose() throws Exception {
		//executeOn("--input data/synthetic/assignedlabels.txt --evalObjects data/synthetic/evaluationObjects.txt --evalWorkers data/synthetic/evaluationWorkers.txt --correct data/synthetic/goldObjects.txt  --synthetic data/synthetic/synthetic-options.txt --output results/synthetic");
	}
	@Test
	public void testNewJoinlyNormalData() throws Exception {
		executeOn("--input data/joinlynormal/assignedlabels.txt --evalObjects data/joinlynormal/evaluationObjects.txt --evalWorkers data/joinlynormal/evaluationWorkers.txt --correct data/joinlynormal/goldObjects.txt --output results/joinlynormal");
	}
	@Test
	public void testCurrentSyntheticData_verbose() throws Exception {
		executeOn("--input data/synthetic/assignedlabels.txt --evalObjects data/synthetic/evaluationObjects.txt --evalWorkers data/synthetic/evaluationWorkers.txt --correct data/synthetic/goldObjects.txt --output results/synthetic");
	}
	@Test
	public void testCurrentSyntheticData_labels() throws Exception {
//		executeOn("--input data/synthetic/assignedlabels.txt --output results/synthetic");
	}
}
