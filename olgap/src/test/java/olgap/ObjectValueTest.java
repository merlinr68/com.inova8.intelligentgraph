/*
 * inova8 2020
 */
package olgap;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.eclipse.rdf4j.model.Model;

import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.sail.nativerdf.NativeStore;


/**
 * The Class ObjectValueTest.
 */
public class ObjectValueTest extends OlgapTest{
	
	/** The conn. */
	static RepositoryConnection conn;

	/**
	 * Sets the up before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		File dataDir = new File("src/test/resources/datadir/");
		FileUtils.deleteDirectory(dataDir);
		org.eclipse.rdf4j.repository.Repository workingRep = new SailRepository(new NativeStore(dataDir));

		String modelFilename = "src/test/resources/calc2graph.data.ttl";
		InputStream input = new FileInputStream(modelFilename);
		Model model = Rio.parse(input, "", RDFFormat.TURTLE);
		conn = workingRep.getConnection();
		conn.add(model.getStatements(null, null, null));
	}

	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@BeforeEach
	void setUp() throws Exception {

	}
	
	/**
	 * Test 0.
	 */
	@Test
	void test0() {
		String queryString8 = "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString8 +=	 "  SELECT *\n" 
		+ "WHERE {  VALUES(?Script_p ?Script_s){(<http://inova8.com/olgap/scriptCode> <http://inova8.com/calc2graph/id/calculateMassYield>)}.    \n"
		+ "		?Script_s ?Script_p ?Script_or .   BIND(olgap:objectValue(?Script_s , ?Script_p, ?Script_or) as ?Script_o ) \n"
		+ "}";
		String result = runQuery(conn, queryString8);
		assertEquals("p=http://www.w3.org/1999/02/22-rdf-syntax-ns#type;s=http://inova8.com/calc2graph/id/BatteryLimit1;o=http://inova8.com/calc2graph/def/BatteryLimit;result1=http://inova8.com/calc2graph/def/BatteryLimit;\r\n" + 
				"" ,result);
	}
	
	/**
	 * Test 1.
	 */
	@Test
	void test1() {
		String queryString8 = "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString8 +=	 "  SELECT  * \n" 
		+ "WHERE {  VALUES(?s){(<http://inova8.com/calc2graph/id/BatteryLimit2>)}.    \n"
		+ "		?s ?p ?o . \n"
		+ "	 BIND( olgap:objectValue(?s , ?p, ?o, 'service','http://localhost:8080/rdf4j-server/repositories/calc2graph/') as ?result1 ). \n"
		+ "}";
		String result = runQuery(conn, queryString8);
		assertEquals("p=http://www.w3.org/1999/02/22-rdf-syntax-ns#type;s=http://inova8.com/calc2graph/id/BatteryLimit1;o=http://inova8.com/calc2graph/def/BatteryLimit;result1=http://inova8.com/calc2graph/def/BatteryLimit;\r\n" + 
				"p=http://inova8.com/calc2graph/def/density;s=http://inova8.com/calc2graph/id/BatteryLimit1;o=.42;result1=.42;\r\n" + 
				"p=http://inova8.com/calc2graph/def/massFlow;s=http://inova8.com/calc2graph/id/BatteryLimit1;o=$$http://inova8.com/calc2graph/id/calculateMassFlow;result1=24.77999922633171;\r\n" + 
				"p=http://inova8.com/calc2graph/def/volumeFlow;s=http://inova8.com/calc2graph/id/BatteryLimit1;o=59;;result1=59;\r\n" + 
				"p=http://www.w3.org/2000/01/rdf-schema#label;s=http://inova8.com/calc2graph/id/BatteryLimit1;o=BatteryLimit1;result1=BatteryLimit1;\r\n" + 
				"" ,result);
	}
	
	/**
	 * Test 2.
	 */
	@Test
	void test2() {
		String queryString8 = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>  \n";
		queryString8 += "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString8 +=	 "  SELECT  ?s  ?p  ?result1\n" 
		+ "WHERE { VALUES(?s){(<http://inova8.com/calc2graph/id/Unit1>)}.    \n"
		+ "		?s ?p ?o . \n"
		+ "	 BIND( olgap:objectValue(?s , ?p, ?o, 'service','http://localhost:8080/rdf4j-server/repositories/calc2graph/') as ?result1 ). \n"
		+ "}";
		String result = runQuery(conn, queryString8);
		assertEquals( "p=http://www.w3.org/1999/02/22-rdf-syntax-ns#type;s=http://inova8.com/calc2graph/id/Unit1;result1=http://inova8.com/calc2graph/def/Unit;\r\n" + 
				"p=http://inova8.com/calc2graph/def/batteryLimits;s=http://inova8.com/calc2graph/id/Unit1;result1=http://inova8.com/cat2graph/data/501A35D75E3A97E9A1FB2D7A7917A96D;\r\n" + 
				"p=http://inova8.com/calc2graph/def/hasFeedBatteryLimit;s=http://inova8.com/calc2graph/id/Unit1;result1=http://inova8.com/calc2graph/id/BatteryLimit1;\r\n" + 
				"p=http://inova8.com/calc2graph/def/hasProductBatteryLimit;s=http://inova8.com/calc2graph/id/Unit1;result1=http://inova8.com/calc2graph/id/BatteryLimit2;\r\n" + 
				"p=http://inova8.com/calc2graph/def/hasProductBatteryLimit;s=http://inova8.com/calc2graph/id/Unit1;result1=http://inova8.com/calc2graph/id/BatteryLimit3;\r\n" + 
				"p=http://inova8.com/calc2graph/def/massFlowBalance;s=http://inova8.com/calc2graph/id/Unit1;result1=-0.8200013935565948;\r\n" + 
				"p=http://inova8.com/calc2graph/def/massThroughput;s=http://inova8.com/calc2graph/id/Unit1;result1=25.600000619888306;\r\n" + 
				"p=http://inova8.com/calc2graph/def/maximumMassFlow;s=http://inova8.com/calc2graph/id/Unit1;result1=http://inova8.com/calc2graph/id/BatteryLimit2;\r\n" + 
				"" ,
				result);
	}
	
	/**
	 * Test 3.
	 */
	@Test
	void test3() {
		String queryString8 = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>  \n";
		queryString8 += "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString8 +=	 "  SELECT  ?s  ?p  ?result1\n" 
		+ "WHERE { VALUES(?s ?p){(<http://inova8.com/calc2graph/id/Unit1> <http://inova8.com/calc2graph/def/batteryLimits>)}.    \n"
		+ "		?s ?p ?o . \n"
		+ "	 BIND( olgap:objectValue(?s , ?p, ?o, 'service','http://localhost:8080/rdf4j-server/repositories/calc2graph/') as ?result1 ). \n"
		+ "}";
		String result = runQuery(conn, queryString8);
		assertEquals( "p=http://inova8.com/calc2graph/def/batteryLimits;s=http://inova8.com/calc2graph/id/Unit1;result1=http://inova8.com/script/data/B884D1E807B7E3F4B6F2F36C58E17AC1;" ,
				result.trim());
	}

}
