package olgap;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.sail.nativerdf.NativeStore;

public class FactValueTest extends OlgapTest{

	private static RepositoryConnection conn;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		File dataDir = new File("src/test/resources/datadir/");
		FileUtils.deleteDirectory(dataDir);
		Repository workingRep = new SailRepository(new NativeStore(dataDir));

		String modelFilename = "src/test/resources/calc2graph.data.ttl";
		InputStream input = new FileInputStream(modelFilename);
		Model model = Rio.parse(input, "", RDFFormat.TURTLE);
		conn = workingRep.getConnection();
		conn.add(model.getStatements(null, null, null));
	}


	@Test
	void test1() {
		String queryString = "";
		queryString += "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString += "  SELECT  * \n" + "WHERE {\n"
				+ "	 BIND( olgap:factValue(<http://inova8.com/calc2graph/id/Unit1> , <http://inova8.com/calc2graph/def/massFlowBalance>,'abc','def') as ?result ).\n"
				+ "}";
		String result = runQuery(conn, queryString);
		assertEquals( "result=-0.8200013935565948;\r\n",result);
	}
	@Test
	void test2_1() {
		String queryString = "";
		queryString += "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString += "  SELECT  * \n" + "WHERE {\n"
				+ "	 BIND( olgap:factValue(<http://inova8.com/calc2graph/id/BatteryLimit1> , <http://inova8.com/calc2graph/def/massFlow>,'abc','def') as ?result ).\n"
				+ "}";
		String result = runQuery(conn, queryString);
		assertEquals( "result=24.77999922633171;\r\n" + 
				"",result);
	}
	@Test
	void test2_2() {
		String queryString = "";
		queryString += "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString += "  SELECT  * \n" + "WHERE {\n"
				+ "	 BIND( olgap:factValue(<http://inova8.com/calc2graph/id/BatteryLimit2> , <http://inova8.com/calc2graph/def/massFlow>,'abc','def') as ?result ).\n"
				+ "}";
		String result = runQuery(conn, queryString);
		assertEquals( "result=14.40000057220459;\r\n" + 
				"",result);
	}
	@Test
	void test2_3() {
		String queryString = "";
		queryString += "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString += "  SELECT  * \n" + "WHERE {\n"
				+ "	 BIND( olgap:factValue(<http://inova8.com/calc2graph/id/BatteryLimit3> , <http://inova8.com/calc2graph/def/massFlow>,'abc','def') as ?result ).\n"
				+ "}";
		String result = runQuery(conn, queryString);
		assertEquals( "result=11.200000047683716;\r\n" +
				"",result);
	}
	@Test
	void test3() {
		String queryString = "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString +=	 "  SELECT  * \n" 
		+ "WHERE {\n"
		+ "	 BIND( olgap:factValue(<http://inova8.com/calc2graph/id/Unit1> , <http://inova8.com/calc2graph/def/maximumMassFlow>,'abc','def') as ?result ).\n"
		+ "}";
		String result = runQuery(conn, queryString);
		assertEquals( "result=http://inova8.com/calc2graph/id/BatteryLimit2;\r\n",result);
	}
//	@Test
//	void test4() {
//		String queryString = "PREFIX olgap: <http://inova8.com/olgap/> \n";
//		queryString +=	 "  SELECT  *\n" 
//		+ "WHERE {\n"
//		+ "	 BIND( olgap:factValue(<http://inova8.com/calc2graph/id/Unit1> , <http://inova8.com/calc2graph/def/batteryLimits>,'abc','def') as ?result ).\n"
//		+ "  SERVICE <http://localhost:8080/rdf4j-server/repositories/olgap>{ GRAPH ?result4 { ?s ?p ?o }}"
//		+ "}";
//		String result = runQuery(conn, queryString);
//		assertEquals( "result=24.77999922633171;traceStart=true;\r\n",result);
//	}
	@Test
	void test5() {
		String queryString5 = "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString5 +=	 "  SELECT  *\n" 
		+ "WHERE {\n"
		+ "	 BIND( olgap:factValue(<http://inova8.com/calc2graph/id/Unit1> , <http://inova8.com/calc2graph/def/massThroughput>,'abc','def') as ?result ).\n"
		+ "}";
		String result = runQuery(conn, queryString5);
		assertEquals("result=25.600000619888306;\r\n",result);
	}
	@Test
	void test6() {
		String queryString6 = "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString6 +=	 "  SELECT  *\n" 
		+ "WHERE {\n"
		+ "	 BIND( olgap:factValue(<http://inova8.com/calc2graph/id/BatteryLimit3> , <http://inova8.com/calc2graph/def/massYield>,'abc','def') as ?result ).\n"
		+ "}";
		String result = runQuery(conn, queryString6);
		assertEquals( "result=0.43749998195562556;\r\n",result);
	}
	@Test
	void test7() {
		String queryString = "";
		queryString += "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString += "  SELECT  * \n" + "WHERE {\n"
				+ "	 BIND( olgap:factValue(<http://inova8.com/calc2graph/id/BatteryLimit1> , <http://inova8.com/calc2graph/def/volumeFlow>,'abc','def') as ?result ).\n"
				+ "}";
		String result = runQuery(conn, queryString);
		assertEquals( "result=59;\r\n",result);
	}

}
