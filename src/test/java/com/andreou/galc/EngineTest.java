package com.andreou.galc;

import org.junit.Before;
import org.junit.Test;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import com.andreou.galc.engine.Engine;
import com.andreou.galc.engine.EngineContext;

/**
 * Just in case: When developing, and you <b>**REALLY**</b> need to run from
 * CLI, use mvn exec:java as shown below:
 * 
 * <p>
 * <code>mvn exec:java -Dexec.mainClass=com.andreou.galc.Main -Dexec.args="--input assignedlabels.txt --evalObjects evaluationObjects.txt --evalWorkers evaluationWorkers.txt --correct goldObjects.txt
--synthetic options.txt --output results --verbose"</code>
 * </p>
 * 
 */
public class EngineTest {
	private EngineContext ctx;
	private CmdLineParser parser;

	@Before
	public void setUp() {
		ctx = new EngineContext();

		parser = new CmdLineParser(ctx);
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
	
	@Test(expected=CmdLineException.class)
	public void testInvalidOption() throws Exception {
		parseArgs("--foo");
	}

	@Test
	public void testBasicExecution() throws Exception {
		//executeOn("--input data/synthetic/uniform/assignedlabels.txt --evalObjects data/synthetic/uniform/evaluationObjects.txt --evalWorkers data/synthetic/uniform/evaluationWorkers.txt --correct data/synthetic/uniform/goldObjects.txt --synthetic data/synthetic/uniform/options.txt --output results/synthetic/uniform");
	}

	@Test
	public void testNewSyntheticData_verbose() throws Exception {
		//executeOn("--input data/synthetic/assignedlabels.txt --evalObjects data/synthetic/uniform/evaluationObjects.txt --evalWorkers data/synthetic/uniform/evaluationWorkers.txt --correct data/synthetic/uniform/goldObjects.txt --synthetic data/synthetic/uniform/options.txt --output results/synthetic/uniform --verbose");
	}
	@Test
	public void testCurrentSyntheticData_verbose() throws Exception {
		//executeOn("--input data/synthetic/uniform/assignedlabels.txt --evalObjects data/synthetic/uniform/evaluationObjects.txt --evalWorkers data/synthetic/uniform/evaluationWorkers.txt --correct data/synthetic/uniform/goldObjects.txt --output results/synthetic/uniform --verbose");
	}
	@Test
	public void testCurrentSyntheticData_labels_objects_workers() throws Exception {
		//executeOn("--input data/synthetic/uniform/assignedlabels.txt --evalObjects data/synthetic/uniform/evaluationObjects.txt --evalWorkers data/synthetic/uniform/evaluationWorkers.txt --output results/synthetic/uniform");
	}

	@Test
	public void testCurrentSyntheticData_labels_objects() throws Exception {
//		executeOn("--input data/synthetic/uniform/assignedlabels.txt --evalObjects data/synthetic/uniform/evaluationObjects.txt --output results/synthetic/uniform");
	}

	@Test
	public void testCurrentSyntheticData_labels() throws Exception {
//		executeOn("--input data/synthetic/uniform/assignedlabels.txt --output results/synthetic/uniform");
	}

	@Test
	public void testAdcountingData() throws Exception {
//		executeOn("--input data/adcounting/assignedlabels.txt --evalObjects data/adcounting/evaluationObjects.txt --evalWorkers data/adcounting/evaluationWorkers.txt --correct data/adcounting/goldObjects.txt --output results/adcounting");
	}
	@Test
	public void testAdcountingData_labels_objects_workers() throws Exception {
//		executeOn("--input data/adcounting/assignedlabels.txt --evalObjects data/adcounting/evaluationObjects.txt --evalWorkers data/adcounting/evaluationWorkers.txt --output results/adcounting");
	}

	@Test
	public void testAdcountingData_labels_objects() throws Exception {
//		executeOn("--input data/adcounting/assignedlabels.txt --evalObjects data/adcounting/evaluationObjects.txt --output results/adcounting");
	}

	@Test
	public void testAdcountingData_labels1() throws Exception {
		executeOn("--input data/adcounting/assignedlabels1.txt --output results/adcounting");
	}
	
	@Test
	public void testAdcountingData_labels() throws Exception {
		executeOn("--input data/adcounting/assignedlabels.txt --output results/adcounting");
	}
	@Test
	public void testAdcountingData_labelsOld() throws Exception {
		executeOn("--input data/adcounting/assignedlabels.old.txt --output results/adcounting");
	}
	
	@Test
	public void testBasicExecution_AdultContentWithEvaluation() throws Exception {
		executeOn("--input data/adultcontent/input.txt --evalObjects data/adultcontent/evaluation.txt --correct data/adultcontent/correct.txt --output results/adultcontent");
	}

}
