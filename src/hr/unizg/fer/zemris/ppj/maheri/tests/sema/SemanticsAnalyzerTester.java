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
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class SemanticsAnalyzerTester {
	private String fileName;
	
	public SemanticsAnalyzerTester(String fileName) {
		this.fileName=fileName;
	}
	
	@Parameters
	public static List<String[]> getFileNames() {
		File files= new File("res/examples/seman-in");
		File[] listFiles= files.listFiles();
		List<String[]> fileNames = new ArrayList<String[]>();
		for (File f: listFiles) {
			String fName=f.getName();
			String[] arej = new String[1];
			arej[0]= fName.substring(0, fName.length()-3);
			fileNames.add(arej);
		}
		return fileNames;
	}
	
	
	@Test
	public void superDuperTest() throws IOException {
		System.out.println("\nTESTISUJEMO TEST PRIMJER: "+fileName);
		String output=getAnalyzerOutputFromFile("res/examples/seman-in/"+fileName+".in");
		String correctOutput=new Scanner(new File("res/examples/seman-out/"+fileName+".out")).useDelimiter("\\Z").next();
		System.out.println("------------");
		System.out.println(output);
		System.out.println(correctOutput);
		System.out.println("------------");
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
		reader.close();
		InputProcessor ip = new InputProcessor(inputLines);
		Node tree = ip.getTree();
		
		SemanticsAnalyzer analyzer = new SemanticsAnalyzer(tree);
		
		analyzer.checkAttributes();
		
		analyzer.checkFunctions();
		
		return analyzer.getOutput();
	}
	
//	@Test
//	public void simpleTest() throws IOException {
//		String output=getAnalyzerOutputFromFile("res/examples/seman-in/01_idn.in");
//		String correctOutput=new Scanner(new File("res/examples/seman-out/01_idn.out")).useDelimiter("\\Z").next();
//		
//		assertEquals(correctOutput, output);
//		
//
//	}
}