/*
 * inova8 2020
 */
package localPathCalc;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import pathCalc.Evaluator;
import pathCalc.Thing;
import pathQL.Match;
import pathQLModel.Resource;
import pathQLRepository.PathQLRepository;
import utilities.Query;

/**
 * The Class ThingTests.
 */
@SuppressWarnings("deprecation")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Local_PathQL_CacheTests {
		
	/** The source. */
	private static PathQLRepository source;
	
	/** The evaluator. */
	private static Evaluator evaluator;

	/** The match. */
	private static Match match;
	static org.eclipse.rdf4j.repository.Repository workingRep ;
	/**
	 * Sets the up before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		workingRep = Query.createNativeLuceneIntelligentGraphRepository("src/test/resources/datadir/Local_PathQL_CacheTests/");
		Query.addFile(workingRep, "src/test/resources/calc2graph.data.ttl");
		Query.addFile(workingRep, "src/test/resources/calc2graph.def.ttl");	
		source = PathQLRepository.create(workingRep);
		source.prefix("<http://inova8.com/calc2graph/def/>");
		source.prefix("rdfs", "<http://www.w3.org/2000/01/rdf-schema#>");
	}
	
	/**
	 * Test 1.
	 */
	@Test
	@Order(1)
	void test_1() {
		try {
			Thing $this =source.getThing(iri("http://inova8.com/calc2graph/id/BatteryLimit2"));
			Resource result1 = $this.getFact(":volumeFlow");
			String trace = $this.traceFact(":massThroughput");
			$this =source.getThing(iri("http://inova8.com/calc2graph/id/Unit1"));
			Resource result2 = $this.getFact(":massThroughput");
			
			
			assertEquals("40", result1.stringValue());
			assertEquals("37.99999952316284", result2.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}

	}

}