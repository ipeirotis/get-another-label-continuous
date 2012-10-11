package com.andreou.galc;

import org.junit.Before;
import org.junit.Test;
import org.kohsuke.args4j.CmdLineParser;

import com.andreou.galc.engine.Engine;
import com.andreou.galc.engine.EngineContext;

public class EngineTest {
	private EngineContext ctx;
	private CmdLineParser parser;

	@Before
	public void setUp() {
		ctx = new EngineContext();

		parser = new CmdLineParser(ctx);
	}

	@Test
	public void testBasicExecution() throws Exception {
		executeOn("--input assignedlabels.txt --evalObjects evaluationObjects.txt --evalWorkers evaluationWorkers.txt --correct goldObjects.txt --synthetic synthetic-options.txt --output results --verbose");
	}

	private void executeOn(String unparsedArgs) throws Exception {
		String[] args = unparsedArgs.split("\\s+");
		
		parser.parseArgument(args);

		Engine engine = new Engine(ctx);

		engine.execute();
	}
}
