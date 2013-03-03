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
		executeOn("--input data/synthetic/joinlynormal/assignedlabels.txt --evalObjects data/synthetic/joinlynormal/evaluationObjects.txt --evalWorkers data/synthetic/joinlynormal/evaluationWorkers.txt --correct data/synthetic/joinlynormal/goldObjects.txt --synthetic data/synthetic/joinlynormal/options.txt --output results/synthetic/joinlynormal");
	}
	@Test
	public void testCreateNewSingleRhoData() throws Exception {
		executeOn("--input data/synthetic/singlerho/assignedlabels.txt --evalObjects data/synthetic/singlerho/evaluationObjects.txt --evalWorkers data/synthetic/singlerho/evaluationWorkers.txt --correct data/synthetic/singlerho/goldObjects.txt --synthetic data/synthetic/singlerho/options.txt --output results/synthetic/singlerho");
	}
	@Test
	public void testCreateCurrentSyntheticData_verbose() throws Exception {
		executeOn("--input data/synthetic/uniform/assignedlabels.txt --evalObjects data/synthetic/uniform/evaluationObjects.txt --evalWorkers data/synthetic/uniform/evaluationWorkers.txt --correct data/synthetic/uniform/goldObjects.txt  --synthetic data/synthetic/uniform/options.txt --output results/synthetic/uniform");
	}
	@Test
	public void testNewJoinlyNormalData() throws Exception {
		//executeOn("--input data/synthetic/joinlynormal/assignedlabels.txt --evalObjects data/synthetic/joinlynormal/evaluationObjects.txt --evalWorkers data/synthetic/joinlynormal/evaluationWorkers.txt --output results/synthetic/joinlynormal");
	}
	@Test
	public void testNewSingleRhoData() throws Exception {
		//executeOn("--input data/synthetic/singlerho/assignedlabels.txt --evalObjects data/synthetic/singlerho/evaluationObjects.txt --evalWorkers data/synthetic/singlerho/evaluationWorkers.txt --output results/synthetic/singlerho");
	}
	@Test
	public void testCurrentSyntheticData_verbose() throws Exception {
		//executeOn("--input data/synthetic/uniform/assignedlabels.txt --evalObjects data/synthetic/uniform/evaluationObjects.txt --evalWorkers data/synthetic/uniform/evaluationWorkers.txt --output results/synthetic/uniform");
	}
	@Test
	public void testCurrentSyntheticData_labels() throws Exception {
//		executeOn("--input data/synthetic/uniform/assignedlabels.txt --output results/synthetic/uniform");
	}
}
