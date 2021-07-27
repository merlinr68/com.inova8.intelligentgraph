/*
 * inova8 2020
 */
package tutorialExample;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import pathCalc.Thing;
import pathQL.PathQL;
import pathQLModel.Resource;
import pathQLRepository.Graph;
import pathQLRepository.PathQLRepository;
import pathQLResults.FactResults;
import pathQLResults.PathQLResults;
import pathQLResults.ResourceResults;
import utilities.Query;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Example2_Tests {

	private static PathQLRepository source;
	static org.eclipse.rdf4j.repository.Repository workingRep ;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {

		workingRep = Query.createNativeLuceneIntelligentGraphRepository("src/test/resources/datadir/Example2_Tests/");
		Query.addFile(workingRep, "src/test/resources/example2.ttl");
		
		RepositoryConnection conn = workingRep.getConnection();
		conn.setNamespace("", "http://inova8.com/intelligentgraph/example2/");
		conn.setNamespace("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		conn.setNamespace("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
		source = PathQLRepository.create(workingRep);

	}
	@AfterAll
	static void closeClass() throws Exception {
		//conn.close();
	}
	@Test
	@Order(1)
	void example2_1() {

		try {
			Thing peter = source.getThing(":Peter");
			Resource bmi = peter.getFact(":hasBMI");
			assertEquals("21.453287197231838", bmi.stringValue());
			Thing another1 = source.getThing(":Another1");
			Resource another1bmi = another1.getFact(":hasBMI");
			assertEquals("19.94459833795014", another1bmi.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	@Test
	@Order(2)
	void example2_2() {

		try {
			Thing personType = source.getThing(":Person");
			 ResourceResults persons = personType.getFacts("^rdf:type");
			 ArrayList<String> personValues = new ArrayList<String>();;
			 for(Resource person:persons) {
				 personValues.add(person.getValue().stringValue());
			 }
			assertEquals("[http://inova8.com/intelligentgraph/example2/Another1, http://inova8.com/intelligentgraph/example2/Another10, http://inova8.com/intelligentgraph/example2/Another11, http://inova8.com/intelligentgraph/example2/Another12, http://inova8.com/intelligentgraph/example2/Another2, http://inova8.com/intelligentgraph/example2/Another3, http://inova8.com/intelligentgraph/example2/Another4, http://inova8.com/intelligentgraph/example2/Another5, http://inova8.com/intelligentgraph/example2/Another6, http://inova8.com/intelligentgraph/example2/Another7, http://inova8.com/intelligentgraph/example2/Another8, http://inova8.com/intelligentgraph/example2/Another9, http://inova8.com/intelligentgraph/example2/Peter]", personValues.toString());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	@Test
	@Order(3)
	void example2_3() {

		try {
			Thing personType = source.getThing(":Person");
			 ResourceResults persons = personType.getFacts("^rdf:type/:hasBMI");
			 ArrayList<String> personValues = new ArrayList<String>();;
			 for(Resource person:persons) {
				 personValues.add(person.getValue().stringValue());
			 }
			assertEquals("[19.94459833795014, 16.897506925207757, 27.11111111111111, 23.148148148148145, 22.49134948096886, 14.75, 14.506172839506172, 31.11111111111111, 24.88888888888889, 21.79930795847751, 25.781249999999996, 15.91695501730104, 21.453287197231838]", personValues.toString());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	@Test
	@Order(4)
	void example2_4() {

		try {
			Thing person = source.getThing(":Person");
			Resource averagebmi = person.getFact(":averageBMI");
			assertEquals("21.523052847377123", averagebmi.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	@Test
	@Order(5)
	void example2_5() {

		try {
			Thing person = source.getThing(":Peter");
			Resource relativeBMI = person.getFact(":hasRelativeBMI");
			assertEquals("0.9967585615925397", relativeBMI.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	@Test
	@Order(6)
	void example2_6() {

		try {
			Thing peter = source.getThing(":Person"); 
			Double bmi = peter.getFacts("^rdf:type[:hasHeight [ ge '1.9' ]]/:hasBMI").average();
			assertEquals("21.453287197231838", bmi);
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	@Test
	@Order(7)
	void example2_7() {

		try {
			Thing peter = source.getThing(":Person"); 
			Resource bmi = peter.getFact(":averageTallBMI");
			assertEquals("21.453287197231838", bmi.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
}
