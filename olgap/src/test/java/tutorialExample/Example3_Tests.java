/*
 * inova8 2020
 */
package tutorialExample;

import static org.eclipse.rdf4j.model.util.Values.literal;
import static org.junit.jupiter.api.Assertions.*;

import org.eclipse.rdf4j.model.Literal;
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
import com.inova8.intelligentgraph.vocabulary.OWL;
import com.inova8.intelligentgraph.vocabulary.RDF;
import com.inova8.intelligentgraph.vocabulary.RDFS;
import com.inova8.intelligentgraph.vocabulary.XSD;

import utilities.Query;

/**
 * The Class Example3_Tests.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Example3_Tests {

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

		workingRep = Query.createNativeLuceneIntelligentGraphRepository("src/test/resources/datadir/Example3_Tests/");
		Query.addFile(workingRep, "src/test/resources/example3.ttl");
		
		RepositoryConnection conn = workingRep.getConnection();
		conn.setNamespace("", "http://inova8.com/intelligentgraph/example3/");
		conn.setNamespace(XSD.PREFIX, XSD.NAMESPACE);
		conn.setNamespace( RDF.PREFIX, RDF.NAMESPACE);
		conn.setNamespace(RDFS.PREFIX , RDFS.NAMESPACE);
		conn.setNamespace(OWL.PREFIX, OWL.NAMESPACE);
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
	 * Example 3 1.
	 */
	@Test
	@Order(1)
	void example3_1() {

		try {
			Thing tideswell = source.getThing(":Tideswell"); 
			Resource bmi = tideswell.getFact(":averageBMI");
			assertEquals("21.7109303439298", bmi.stringValue());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Example 3 2.
	 */
	@Test
	@Order(2)
	void example3_2() {

		try {
			try {
				Thing tideswell = source.getThing(":Tideswell"); 
				Double bmi = tideswell.getFacts("^:hasLocation[:hasHeight [ lt '1.9'^^xsd:double ]]/:hasBMI").average();
				assertEquals("23.904182152163695", bmi.toString());
			} catch (Exception e) {
				assertEquals("", e.getMessage());
				e.printStackTrace();
			}
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Example 3 3.
	 */
	@Test
	@Order(3)
	void example3_3() {

		try {
			try {
				Thing tideswell = source.getThing(":Tideswell"); 
				Literal maxHeight = literal(1.9);
				Double bmi = tideswell.getFacts("^:hasLocation[:hasHeight [ lt  %1 ]]/:hasBMI",maxHeight).average();
				assertEquals("23.904182152163695", bmi.toString());
			} catch (Exception e) {
				assertEquals("", e.getMessage());
				e.printStackTrace();
			}
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Example 3 4.
	 */
	@Test
	@Order(4)
	void example3_4() {

		try {
			Resource bmi = source.getThing(":Maidstone").getFact(":averageBMI");
			assertEquals("21.405629412031697", bmi.stringValue());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
}
