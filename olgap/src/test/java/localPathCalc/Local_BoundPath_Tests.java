/*
 * inova8 2020
 */
package localPathCalc;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.inova8.intelligentgraph.intelligentGraphRepository.IntelligentGraphRepository;
import com.inova8.intelligentgraph.model.Resource;
import com.inova8.intelligentgraph.model.Thing;
import com.inova8.intelligentgraph.path.Path;
import com.inova8.intelligentgraph.results.PathResults;
import com.inova8.intelligentgraph.results.ResourceResults;

import utilities.Query;

/**
 * The Class Local_GetPath_Tests.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Local_BoundPath_Tests {
	

	
	/** The source. */
	private static IntelligentGraphRepository source;
	

	/** The working rep. */
	static org.eclipse.rdf4j.repository.Repository workingRep ;
	
	/**
	 * Sets the up before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		workingRep = Query.createNativeLuceneIntelligentGraphRepository("src/test/resources/datadir/Local_BoundPath_Tests/");
		Query.addFile(workingRep, "src/test/resources/calc2graph.data.ttl");
		Query.addFile(workingRep, "src/test/resources/calc2graph.def.ttl");
		RepositoryConnection conn = workingRep.getConnection();
		conn.setNamespace("", "http://inova8.com/calc2graph/def/");
		conn.setNamespace("def", "http://inova8.com/calc2graph/def/");
		conn.setNamespace("id", "http://inova8.com/calc2graph/id/");
		conn.setNamespace("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
		source = IntelligentGraphRepository.create(workingRep);


	}
	
	/**
	 * Close class.
	 *
	 * @throws Exception the exception
	 */
	@AfterAll
	static void closeClass() throws Exception {
		//conn.close();
	}	
	
	/**
	 * Removes the white spaces.
	 *
	 * @param input the input
	 * @return the string
	 */
	String removeWhiteSpaces(String input) {
	    return input.replaceAll("\\s+", "");
	    //return input;
	}
	
	/**
	 * Assert equals WO spaces.
	 *
	 * @param actual the actual
	 * @param expected the expected
	 */
	void assertEqualsWOSpaces(String actual, String expected){
		assertEquals(removeWhiteSpaces(actual), removeWhiteSpaces(expected));
}	

	/**
	 * Test 1.
	 */
	@Test
	@Order(1)
	void test_1() {
		try {
			PathResults paths =  source.getPaths("[ eq <http://inova8.com/calc2graph/id/Equipment_1>]/:connectedTo/:connectedTo/:connectedTo");
			assertEquals("Path=[[http://inova8.com/calc2graph/id/Equipment_1,http://inova8.com/calc2graph/def/connectedTo,http://inova8.com/calc2graph/id/Equipment_2,DIRECT]\r\n"
					+ "[http://inova8.com/calc2graph/id/Equipment_2,http://inova8.com/calc2graph/def/connectedTo,http://inova8.com/calc2graph/id/Equipment_3,DIRECT]\r\n"
					+ "[http://inova8.com/calc2graph/id/Equipment_3,http://inova8.com/calc2graph/def/connectedTo,http://inova8.com/calc2graph/id/Equipment_4,DIRECT]\r\n"
					+ "]\r\n"
					+ "Path=[[http://inova8.com/calc2graph/id/Equipment_1,http://inova8.com/calc2graph/def/connectedTo,http://inova8.com/calc2graph/id/Equipment_2_1,DIRECT]\r\n"
					+ "[http://inova8.com/calc2graph/id/Equipment_2_1,http://inova8.com/calc2graph/def/connectedTo,http://inova8.com/calc2graph/id/Equipment_2_2,DIRECT]\r\n"
					+ "[http://inova8.com/calc2graph/id/Equipment_2_2,http://inova8.com/calc2graph/def/connectedTo,http://inova8.com/calc2graph/id/Equipment_2_3,DIRECT]\r\n"
					+ "]\r\n"
					+ "", paths.toString());
		} catch (Exception e) {
			assertEquals("", e.getCause().getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Test 2.
	 */
	@Test
	@Order(2)
	void test_2() {
		try {
			PathResults paths =  source.getPaths("[ eq <http://inova8.com/calc2graph/id/Continuant_1>]/:Connection@:connectedTo/:Connection@:connectedTo/:Connection@:connectedTo");
			assertEquals("Path=[[http://inova8.com/calc2graph/id/Continuant_1,<http://inova8.com/calc2graph/id/Connection_1_2>:<http://inova8.com/calc2graph/def/Connection>@http://inova8.com/calc2graph/def/connectedTo,http://inova8.com/calc2graph/id/Continuant_2,DIRECT,false]\r\n"
					+ "[http://inova8.com/calc2graph/id/Continuant_2,<http://inova8.com/calc2graph/id/Connection_2_3>:<http://inova8.com/calc2graph/def/Connection>@http://inova8.com/calc2graph/def/connectedTo,http://inova8.com/calc2graph/id/Continuant_3,DIRECT,false]\r\n"
					+ "[http://inova8.com/calc2graph/id/Continuant_3,<http://inova8.com/calc2graph/id/Connection_3_4>:<http://inova8.com/calc2graph/def/Connection>@http://inova8.com/calc2graph/def/connectedTo,http://inova8.com/calc2graph/id/Continuant_4,DIRECT,false]\r\n"
					+ "]\r\n"
					+ "", paths.toString());
		} catch (Exception e) {
			assertEquals("", e.getCause().getMessage());
			e.printStackTrace();
		}

	}


	/**
	 * Test 3.
	 */
	@Test
	@Order(3)
	void test_3() {
		try {
			PathResults paths =  source.getPaths("[ eq <http://inova8.com/calc2graph/id/Continuant_1>]/:Connection@:connectedTo/:Connection@:connectedTo/:Connection@:connectedTo#");
			assertEquals("Path=[[http://inova8.com/calc2graph/id/Continuant_1,<http://inova8.com/calc2graph/id/Connection_1_2>:<http://inova8.com/calc2graph/def/Connection>@http://inova8.com/calc2graph/def/connectedTo,http://inova8.com/calc2graph/id/Continuant_2,DIRECT,false]\r\n"
					+ "[http://inova8.com/calc2graph/id/Continuant_2,<http://inova8.com/calc2graph/id/Connection_2_3>:<http://inova8.com/calc2graph/def/Connection>@http://inova8.com/calc2graph/def/connectedTo,http://inova8.com/calc2graph/id/Continuant_3,DIRECT,false]\r\n"
					+ "[http://inova8.com/calc2graph/id/Continuant_3,<http://inova8.com/calc2graph/id/Connection_3_4>:<http://inova8.com/calc2graph/def/Connection>@http://inova8.com/calc2graph/def/connectedTo,http://inova8.com/calc2graph/id/Connection_3_4,DIRECT,true]\r\n"
					+ "]\r\n"
					+ "", paths.toString());
		} catch (Exception e) {
			assertEquals("", e.getCause().getMessage());
			e.printStackTrace();
		}

	}


	/**
	 * Test 4.
	 */
	@Test
	@Order(4)
	void test_4() {
		try {
			PathResults paths =  source.getPaths("[ eq <http://inova8.com/calc2graph/id/Continuant_1>]/:Connection@:connectedTo/:Connection@:connectedTo#/:connection.to.Continuant/:Connection@:connectedTo");
			assertEquals("Path=[[http://inova8.com/calc2graph/id/Continuant_1,<http://inova8.com/calc2graph/id/Connection_1_2>:<http://inova8.com/calc2graph/def/Connection>@http://inova8.com/calc2graph/def/connectedTo,http://inova8.com/calc2graph/id/Continuant_2,DIRECT,false]\r\n"
					+ "[http://inova8.com/calc2graph/id/Continuant_2,<http://inova8.com/calc2graph/id/Connection_2_3>:<http://inova8.com/calc2graph/def/Connection>@http://inova8.com/calc2graph/def/connectedTo,http://inova8.com/calc2graph/id/Connection_2_3,DIRECT,true]\r\n"
					+ "[http://inova8.com/calc2graph/id/Connection_2_3,http://inova8.com/calc2graph/def/connection.to.Continuant,http://inova8.com/calc2graph/id/Continuant_3,DIRECT]\r\n"
					+ "[http://inova8.com/calc2graph/id/Continuant_3,<http://inova8.com/calc2graph/id/Connection_3_4>:<http://inova8.com/calc2graph/def/Connection>@http://inova8.com/calc2graph/def/connectedTo,http://inova8.com/calc2graph/id/Continuant_4,DIRECT,false]\r\n"
					+ "]\r\n"
					+ "", paths.toString());
		} catch (Exception e) {
			assertEquals("", e.getCause().getMessage());
			e.printStackTrace();
		}
	}


	/**
	 * Test 5.
	 */
	@Test
	@Order(5)
	void test_5() {

		try {
			Path path = source.getPath("[ eq <http://inova8.com/calc2graph/id/BatteryLimit1>]/:Location@:appearsOn[eq id:Calc2Graph1]#/:lat");

			assertEquals("Path=[[http://inova8.com/calc2graph/id/BatteryLimit1,<http://inova8.com/calc2graph/id/Location_BL1>:<http://inova8.com/calc2graph/def/Location>@http://inova8.com/calc2graph/def/appearsOn,http://inova8.com/calc2graph/id/Location_BL1,DIRECT,true]\r\n"
					+ "[http://inova8.com/calc2graph/id/Location_BL1,http://inova8.com/calc2graph/def/lat,\"400\"^^<http://www.w3.org/2001/XMLSchema#integer>,DIRECT]\r\n"
					+ "]\r\n"
					+ "", path.toString());
		} catch (Exception e) {
			assertEquals("", e.getCause().getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Test 6.
	 */
	@Test
	@Order(6)
	void test_6() {

		try {
			Path path  = source.getPath("[ eq <http://inova8.com/calc2graph/id/BatteryLimit1>]/:Location@:appearsOn[eq id:Calc2Graph2]#"); //""

			if (path != null)
				assertEquals("Path=[[http://inova8.com/calc2graph/id/BatteryLimit1,<http://inova8.com/calc2graph/id/Location_BL1_2>:<http://inova8.com/calc2graph/def/Location>@http://inova8.com/calc2graph/def/appearsOn,http://inova8.com/calc2graph/id/Location_BL1_2,DIRECT,true]\r\n"
						+ "]\r\n"
						+ "", path.toString());

		} catch (Exception e) {
			assertEquals("", e.getCause().getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Test 7.
	 */
	@Test
	@Order(7)
	void test_7() {
		try {
			Thing _this =source.getThing(iri("http://inova8.com/calc2graph/id/Equipment_1"), null);
			
			ResourceResults results = _this.getFacts(":connectedTo{1,3}");
			ArrayList<String> resultsArrayList = new ArrayList<String>();
			for (Resource result : results) {
				resultsArrayList.add(result.stringValue());
			}
			assertEquals("[http://inova8.com/calc2graph/id/Equipment_2, http://inova8.com/calc2graph/id/Equipment_2_1, http://inova8.com/calc2graph/id/Equipment_3, http://inova8.com/calc2graph/id/Equipment_2_2, http://inova8.com/calc2graph/id/Equipment_4, http://inova8.com/calc2graph/id/Equipment_2_3]", resultsArrayList.toString());
			PathResults paths =  _this.getPaths(":connectedTo{1,3}");
			ArrayList<String> pathArrayList = new ArrayList<String>();
			for (Path path : paths) {
				pathArrayList.add(path.toString());
			}
			assertEquals("[Path=[[http://inova8.com/calc2graph/id/Equipment_1,http://inova8.com/calc2graph/def/connectedTo,http://inova8.com/calc2graph/id/Equipment_2,DIRECT]\r\n"
					+ "]\r\n"
					+ ", Path=[[http://inova8.com/calc2graph/id/Equipment_1,http://inova8.com/calc2graph/def/connectedTo,http://inova8.com/calc2graph/id/Equipment_2_1,DIRECT]\r\n"
					+ "]\r\n"
					+ ", Path=[[http://inova8.com/calc2graph/id/Equipment_1,http://inova8.com/calc2graph/def/connectedTo,http://inova8.com/calc2graph/id/Equipment_2,DIRECT]\r\n"
					+ "[http://inova8.com/calc2graph/id/Equipment_2,http://inova8.com/calc2graph/def/connectedTo,http://inova8.com/calc2graph/id/Equipment_3,DIRECT]\r\n"
					+ "]\r\n"
					+ ", Path=[[http://inova8.com/calc2graph/id/Equipment_1,http://inova8.com/calc2graph/def/connectedTo,http://inova8.com/calc2graph/id/Equipment_2_1,DIRECT]\r\n"
					+ "[http://inova8.com/calc2graph/id/Equipment_2_1,http://inova8.com/calc2graph/def/connectedTo,http://inova8.com/calc2graph/id/Equipment_2_2,DIRECT]\r\n"
					+ "]\r\n"
					+ ", Path=[[http://inova8.com/calc2graph/id/Equipment_1,http://inova8.com/calc2graph/def/connectedTo,http://inova8.com/calc2graph/id/Equipment_2,DIRECT]\r\n"
					+ "[http://inova8.com/calc2graph/id/Equipment_2,http://inova8.com/calc2graph/def/connectedTo,http://inova8.com/calc2graph/id/Equipment_3,DIRECT]\r\n"
					+ "[http://inova8.com/calc2graph/id/Equipment_3,http://inova8.com/calc2graph/def/connectedTo,http://inova8.com/calc2graph/id/Equipment_4,DIRECT]\r\n"
					+ "]\r\n"
					+ ", Path=[[http://inova8.com/calc2graph/id/Equipment_1,http://inova8.com/calc2graph/def/connectedTo,http://inova8.com/calc2graph/id/Equipment_2_1,DIRECT]\r\n"
					+ "[http://inova8.com/calc2graph/id/Equipment_2_1,http://inova8.com/calc2graph/def/connectedTo,http://inova8.com/calc2graph/id/Equipment_2_2,DIRECT]\r\n"
					+ "[http://inova8.com/calc2graph/id/Equipment_2_2,http://inova8.com/calc2graph/def/connectedTo,http://inova8.com/calc2graph/id/Equipment_2_3,DIRECT]\r\n"
					+ "]\r\n"
					+ "]", pathArrayList.toString());
		} catch (Exception e) {
			assertEquals("", e.getCause().getMessage());
			e.printStackTrace();
		}

	}
}
