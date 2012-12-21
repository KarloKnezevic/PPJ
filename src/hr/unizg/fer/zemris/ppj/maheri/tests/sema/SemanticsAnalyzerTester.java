package hr.unizg.fer.zemris.ppj.maheri.tests.sema;

import hr.unizg.fer.zemris.ppj.maheri.semantics.InputProcessor;
import hr.unizg.fer.zemris.ppj.maheri.semantics.Node;
import hr.unizg.fer.zemris.ppj.maheri.semantics.SemanticsAnalyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.junit.Test;
import static org.junit.Assert.*;

public class SemanticsAnalyzerTester {
	@Test
	public void simpleTest() throws IOException {
		String output=getAnalyzerOutputFromFile("res/examples/seman-in/01_idn.in");
		String correctOutput=new Scanner(new File("res/examples/seman-out/01_idn.out")).useDelimiter("\\Z").next();
		
		assertEquals(correctOutput, output);

	}
	
	public String getAnalyzerOutputFromFile(String filename) throws IOException {
		BufferedReader reader=null;
		try {
			reader = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		List<String> inputLines = new ArrayList<String>();

		String currentLine;
		while ((currentLine = reader.readLine()) != null) {
			inputLines.add(currentLine);
		}
		
		InputProcessor ip = new InputProcessor(inputLines);
		Node tree = ip.getTree();
		
		SemanticsAnalyzer analyzer = new SemanticsAnalyzer(tree);
		
		analyzer.checkAttributes();
		
		analyzer.checkFunctions();
		
		return analyzer.getOutput();
	}
}