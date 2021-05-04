/*
 * inova8 2020
 */
package pathCalc;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.eclipse.rdf4j.model.util.Values.literal;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import org.apache.commons.io.FileUtils;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.evaluation.RepositoryTripleSource;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.sail.nativerdf.NativeStore;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import olgap.ClearCache;
import olgap.FactValue;
import olgap.ObjectValue;
import pathQLRepository.PathQLRepository;

/**
 * The Class PathCalcTests.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PathCalcTests {
	
	/** The conn. */
	private static RepositoryConnection conn;
	
	/** The repository triple source. */
	static RepositoryTripleSource repositoryTripleSource;
	
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

		String dataFilename = "src/test/resources/calc2graph.data.ttl";
		InputStream dataInput = new FileInputStream(dataFilename);
		Model dataModel = Rio.parse(dataInput, "", RDFFormat.TURTLE);
		conn = workingRep.getConnection();
		conn.add(dataModel.getStatements(null, null, null));
		
		String modelFilename = "src/test/resources/calc2graph.def.ttl";
		InputStream modelInput = new FileInputStream(modelFilename);
		Model modelModel = Rio.parse(modelInput, "", RDFFormat.TURTLE);
		conn = workingRep.getConnection();
		conn.add(modelModel.getStatements(null, null, null));
		repositoryTripleSource = new RepositoryTripleSource(conn);
		//new PathQLRepository(repositoryTripleSource);
		new Evaluator();
	}
	
	/**
	 * Test 0.
	 */
	@Test
	@Order(0)
	//literal("$this.prefix(\"<http://inova8.com/calc2graph/def/>\");var result= $this.getFact(\":volumeFlow\").floatValue()* $this.getFact(\":Attribute@:density\").floatValue();  result;",
	void test_0() {
		try {
			ClearCache clearCache = new ClearCache();
			org.eclipse.rdf4j.model.Value result = clearCache.evaluate(repositoryTripleSource,
					iri("http://inova8.com/calc2graph/id/BatteryLimit1"),
					iri("http://inova8.com/calc2graph/def/testProperty4"),
					literal("$this.prefix(\"<http://inova8.com/calc2graph/def/>\");var result= $this.getFact(\":volumeFlow\").floatValue()* $this.getFact(\":Attribute@:density\").floatValue();  result;",
							iri("http://inova8.com/script/groovy")));
			assertEquals("true", result.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	
	/**
	 * Test 1.
	 */
	@Test
	@Order(3)
	//literal("$this.prefix(\"<http://inova8.com/calc2graph/def/>\");var result= $this.getFact(\":volumeFlow\").floatValue()* $this.getFact(\":Attribute@:density\").floatValue();  result;",
	void test_1() {
		try {
			ObjectValue objectValue = new ObjectValue();
			org.eclipse.rdf4j.model.Value result = objectValue.evaluate(repositoryTripleSource,
					iri("http://inova8.com/calc2graph/id/BatteryLimit1"),
					iri("http://inova8.com/calc2graph/def/testProperty4"),
					literal("$this.prefix(\"<http://inova8.com/calc2graph/def/>\");var result= $this.getFact(\":volumeFlow\").floatValue()* $this.getFact(\":Attribute@:density\").floatValue();  result;",
							iri("http://inova8.com/script/groovy")));
			assertEquals("24.77999922633171", result.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	
	/**
	 * Test 2.
	 */
	@Test
	@Order(4)
	void test_2() {
		try {
			ObjectValue objectValue = new ObjectValue();
			org.eclipse.rdf4j.model.Value result = objectValue.evaluate(repositoryTripleSource,
					iri("http://inova8.com/calc2graph/id/BatteryLimit1"),
					iri("http://inova8.com/calc2graph/def/testProperty4"), 
					literal("$this.prefix(\"<http://inova8.com/calc2graph/def/>\");var result= $this.getFact(\":massFlow\").floatValue();  result;",
							iri("http://inova8.com/script/groovy")));
			assertEquals("24.779999", result.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
		
	}
	@Test
	@Order(3)
	void test_stack() {
		try {
			FactValue factValue = new FactValue();
			org.eclipse.rdf4j.model.Value result = factValue.evaluate(repositoryTripleSource,
					iri("http://inova8.com/calc2graph/id/BatteryLimit1"),
					iri("http://inova8.com/calc2graph/def/testProperty3"));
			assertEquals("Script failed with <br/><code ><span style=\"white-space: pre-wrap\">java.lang.NumberFormatException: For input string: &quot;Script failed with &lt;br/&gt;&lt;code &gt;&lt;span style=&quot;white-space: pre-wrap&quot;&gt;java.lang.NumberFormatException: For input string: &amp;quot;**Circular Reference**&amp;quot;&lt;/span&gt;&lt;/code &gt;&quot;</span></code >", result.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
		
	}
	/**
	 * Test 4.
	 */
	@Test
	@Order(5)
	void test_4() {
		
		try {
			ObjectValue objectValue = new ObjectValue();
			org.eclipse.rdf4j.model.Value result = objectValue.evaluate(repositoryTripleSource,
					iri("http://inova8.com/calc2graph/id/Unit1"),
					iri("http://inova8.com/calc2graph/def/test"),
					literal("$this.prefix(\"<http://inova8.com/calc2graph/def/>\");var result= $this.getFact(\":massThroughput\").floatValue();  result;",
							iri("http://inova8.com/script/groovy")));
			assertEquals("23.44", result.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		} 
	}
	
	/**
	 * Test 4 3.
	 */
	@Test
	@Order(5)
	void test_4_3() {
		
		try {
			ObjectValue objectValue = new ObjectValue();
			org.eclipse.rdf4j.model.Value result = objectValue.evaluate(repositoryTripleSource,
					iri("http://inova8.com/calc2graph/id/BatteryLimit1"),
					iri("http://inova8.com/calc2graph/def/density"),
					literal("$this.prefix(\"def\",\"<http://inova8.com/calc2graph/def/>\");var result= $this.getFact(\"def:Attribute@def:density\").floatValue();  result;",
							iri("http://inova8.com/script/groovy")));
			if(result!=null) 
					assertEquals("0.42", result.stringValue());
			else
				fail();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * Test 4 2.
	 */
	@Test
	@Order(5)
	void test_4_2() {
		
		try {
			FactValue factValue = new FactValue();
			org.eclipse.rdf4j.model.Value result = factValue.evaluate(repositoryTripleSource,
					iri("http://inova8.com/calc2graph/id/BatteryLimit2"),
					iri("http://inova8.com/calc2graph/def/massFlow"));
			assertEquals("12.239999771118164", result.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		} 
	}
	@Test
	@Order(5)
	void test_4_4() {
		
		try {
			FactValue factValue = new FactValue();
			org.eclipse.rdf4j.model.Value result = factValue.evaluate(repositoryTripleSource,
					iri("http://inova8.com/calc2graph/id/BatteryLimit3"),
					iri("http://inova8.com/calc2graph/def/massFlow"));
			assertEquals("11.200000047683716", result.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		} 
	}	
	/**
	 * Test 4 1.
	 */
	@Test
	@Order(5)
	void test_4_1() {
		
		try {
			FactValue factValue = new FactValue();
			org.eclipse.rdf4j.model.Value result = factValue.evaluate(repositoryTripleSource,
					iri("http://inova8.com/calc2graph/id/Unit1"),
					iri("http://inova8.com/calc2graph/def/massThroughput"));
			assertEquals("23.43999981880188", result.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		} 
	}
	
	/**
	 * Test 5.
	 */
	@Test
	@Order(6)
	void test_5() {
		
		try{
			
			ObjectValue objectValue = new ObjectValue();
			org.eclipse.rdf4j.model.Value result = objectValue.evaluate(repositoryTripleSource,
					iri("http://inova8.com/calc2graph/id/BatteryLimit2"),
					iri("http://inova8.com/calc2graph/def/massYield"),
					literal("$this.prefix(\"<http://inova8.com/calc2graph/def/>\");var result= $this.getFact(\"^:hasProductBatteryLimit\").getFact(\":massThroughput\").floatValue();  result;",
							iri("http://inova8.com/script/groovy")));
			assertEquals("23.44", result.stringValue());
			}catch(Exception e){
				fail();
				e.printStackTrace();
			}
	}
	
	/**
	 * Test 6.
	 */
	@Test
	@Order(7)
	void test_6() {
		
		try{
			
			ObjectValue objectValue = new ObjectValue();
			org.eclipse.rdf4j.model.Value result = objectValue.evaluate(repositoryTripleSource,
					iri("http://inova8.com/calc2graph/id/BatteryLimit2"),
					iri("http://inova8.com/calc2graph/def/massYield"),
					literal("$this.prefix(\"<http://inova8.com/calc2graph/def/>\");var result= $this.getFact(\":massFlow\").floatValue()/ $this.getFact(\"^:hasProductBatteryLimit\").getFact(\":massThroughput\").floatValue();  result;",
							iri("http://inova8.com/script/groovy")));
			assertEquals("0.5221842786792522", result.stringValue());
			}catch(Exception e){
				fail();
				e.printStackTrace();
			}
	}
	
	/**
	 * Test 7.
	 */
	@Test
	@Order(8)
	void test_7() {		
		try{
			
			ObjectValue objectValue = new ObjectValue();
			org.eclipse.rdf4j.model.Value result = objectValue.evaluate(repositoryTripleSource,
					iri("http://inova8.com/calc2graph/id/BatteryLimit2"),
					iri("http://inova8.com/calc2graph/def/massThroughput"),
					literal("$this.prefix(\"<http://inova8.com/calc2graph/def/>\");var result=  $this.getFact(\"^:hasProductBatteryLimit/:massThroughput\").floatValue();  result;",
							iri("http://inova8.com/script/groovy")));
			 assertEquals("23.44",result.stringValue());
			}catch(Exception e){
				fail();
				e.printStackTrace();
			}
	}
	
	/**
	 * Test 8.
	 */
	@Test
	@Order(9)
	void test_8() {		
		try{
			
			FactValue factValue = new FactValue();
			org.eclipse.rdf4j.model.Value result = factValue.evaluate(repositoryTripleSource,
					iri("http://inova8.com/calc2graph/id/BatteryLimit1"),
					iri("http://inova8.com/calc2graph/def/lat"));
			 assertEquals("400",result.stringValue());
			}catch(Exception e){
				fail();
				e.printStackTrace();
			}
	}
	
	/**
	 * Test 9.
	 */
	@Test
	@Order(10)
	void test_9() {
		
		try{
			
			ObjectValue objectValue = new ObjectValue();
			org.eclipse.rdf4j.model.Value result = objectValue.evaluate(repositoryTripleSource,
					iri("http://inova8.com/calc2graph/id/BatteryLimit1"),
					iri("http://inova8.com/calc2graph/def/massYield"),
					literal("$this.prefix(\"<http://inova8.com/calc2graph/def/>\").prefix(\"id\",\"<http://inova8.com/calc2graph/id/>\");var result= $this.getFact(\":Location@:appearsOn[eq id:Calc2Graph1]#/:lat\"); result;",
							iri("http://inova8.com/script/groovy")));
			assertEquals("400", result.stringValue());
			}catch(Exception e){
				fail();
				e.printStackTrace();
			}
	}
	
	/**
	 * Test 10.
	 */
	@Test
	@Order(11)
	void test_10() {
		
		try{
			
			ObjectValue objectValue = new ObjectValue();
			org.eclipse.rdf4j.model.Value result = objectValue.evaluate(repositoryTripleSource,
					iri("http://inova8.com/calc2graph/id/BatteryLimit1"),
					iri("http://inova8.com/calc2graph/def/massYield"),
					literal("$this.prefix(\"<http://inova8.com/calc2graph/def/>\").prefix(\"id\",\"<http://inova8.com/calc2graph/id/>\");var result= $this.getFact(\":Location@:appearsOn[eq id:Calc2Graph1]#\").getFact(\":lat\"); result;",
							iri("http://inova8.com/script/groovy")));
			assertEquals("400", result.stringValue());
			}catch(Exception e){
				fail();
				e.printStackTrace();
			}
	}
	
	/**
	 * Test 11.
	 */
	@Test
	@Order(12)
	void test_11() {
		
		try{
			
			ObjectValue objectValue = new ObjectValue();
			org.eclipse.rdf4j.model.Value result = objectValue.evaluate(repositoryTripleSource,
					iri("http://inova8.com/calc2graph/id/BatteryLimit1"),
					iri("http://inova8.com/calc2graph/def/massYield"),
					literal("$this.prefix(\"<http://inova8.com/calc2graph/def/>\").prefix(\"id\",\"<http://inova8.com/calc2graph/id/>\");var result= $this.getFact(\":Location@:appearsOn[eq id:Calc2Graph2]#\"); result;",
							iri("http://inova8.com/script/groovy")));
			if(result!=null) 
				assertEquals("http://inova8.com/calc2graph/id/Location_BL1_2", result.stringValue());
			
			}catch(Exception e){
				fail();
				e.printStackTrace();
			}
	}
	
	/**
	 * Test 12.
	 */
	@Test
	@Order(13)
	void test_12() {		
		try{
			
			FactValue factValue = new FactValue();
			org.eclipse.rdf4j.model.Value result = factValue.evaluate(repositoryTripleSource,
					iri("http://inova8.com/calc2graph/id/BatteryLimit1"),
					iri("http://inova8.com/calc2graph/def/long"));
			 assertEquals("501",result.stringValue());
			}catch(Exception e){
				fail();
				e.printStackTrace();
			}
	}

	/**
	 * Test 15.
	 */
	@Test
	@Order(16)
	void test_15() {
		
		try{
			
			ObjectValue objectValue = new ObjectValue();
			org.eclipse.rdf4j.model.Value result = objectValue.evaluate(repositoryTripleSource,
					iri("http://inova8.com/calc2graph/id/Calc2Graph1"),
					iri("http://inova8.com/calc2graph/def/massYield"),
					literal("$this.prefix(\"<http://inova8.com/calc2graph/def/>\").prefix(\"id\",\"<http://inova8.com/calc2graph/id/>\");var result= $this.getFact(\"^:Location@:appearsOn[eq id:BatteryLimit2]#\"); result;",
							iri("http://inova8.com/script/groovy")));
			assertEquals("http://inova8.com/calc2graph/id/Location_BL2", result.stringValue());
			}catch(Exception e){
				fail();
				e.printStackTrace();
			}
	}
	
	/**
	 * Test 16.
	 */
	@Test
	@Order(17)
	void test_16() {
		
		try{
			
			ObjectValue objectValue = new ObjectValue();
			org.eclipse.rdf4j.model.Value result = objectValue.evaluate(repositoryTripleSource,
					iri("http://inova8.com/calc2graph/id/Calc2Graph1"),
					iri("http://inova8.com/calc2graph/def/massYield"),
					literal("$this.prefix(\"<http://inova8.com/calc2graph/def/>\").prefix(\"id\",\"<http://inova8.com/calc2graph/id/>\");var result= $this.getFact(\"^:Location@:appearsOn[eq id:BatteryLimit2]\"); result;",
							iri("http://inova8.com/script/groovy")));
			assertEquals("http://inova8.com/calc2graph/id/BatteryLimit2", result.stringValue());
			}catch(Exception e){
				fail();
				e.printStackTrace();
			}
	}
	
	/**
	 * Test 17.
	 */
	@Test
	@Order(18)
	void test_17() {
		
		try{
			
			ObjectValue objectValue = new ObjectValue();
			org.eclipse.rdf4j.model.Value result = objectValue.evaluate(repositoryTripleSource,
					iri("http://inova8.com/calc2graph/id/Calc2Graph1"),
					iri("http://inova8.com/calc2graph/def/massYield"),
					literal("$this.prefix(\"<http://inova8.com/calc2graph/def/>\").prefix(\"id\",\"<http://inova8.com/calc2graph/id/>\");var result= $this.getFact(\"^:Location@:appearsOn[eq id:BatteryLimit1]#/:lat\"); result;",
							iri("http://inova8.com/script/groovy")));
			assertEquals("400", result.stringValue());
			}catch(Exception e){
				e.printStackTrace();
			}
	}
	
	/**
	 * Test 18.
	 */
	@Test
	@Order(19)
	void test_18() {
		
		try{
			
			ObjectValue objectValue = new ObjectValue();
			org.eclipse.rdf4j.model.Value result = objectValue.evaluate(repositoryTripleSource,
					iri("http://inova8.com/calc2graph/id/BatteryLimit1"),
					iri("http://inova8.com/calc2graph/def/massYield"),
					literal("$this.prefix(\"<http://inova8.com/calc2graph/def/>\").prefix(\"rdfs\",\"<http://www.w3.org/2000/01/rdf-schema#>\").prefix(\"id\",\"<http://inova8.com/calc2graph/id/>\");var result= $this.getFact(\":Location@:appearsOn[eq [ rdfs:label 'Calc2Graph2']]#\"); result;",
							iri("http://inova8.com/script/groovy")));
			assertEquals("http://inova8.com/calc2graph/id/Location_BL1_2", result.stringValue());
			}catch(Exception e){
				e.printStackTrace();
			}
	}
	
	/**
	 * Test 19.
	 */
	@Test
	@Order(20)
	void test_19() {
		
		try{
			
			ObjectValue objectValue = new ObjectValue();
			org.eclipse.rdf4j.model.Value result = objectValue.evaluate(repositoryTripleSource,
					iri("http://inova8.com/calc2graph/id/BatteryLimit1"),
					iri("http://inova8.com/calc2graph/def/massYield"),
					literal("$this.prefix(\"<http://inova8.com/calc2graph/def/>\").prefix(\"rdfs\",\"<http://www.w3.org/2000/01/rdf-schema#>\").prefix(\"id\",\"<http://inova8.com/calc2graph/id/>\");var result= $this.getFact(\":Location@:appearsOn[eq [ rdfs:label 'Calc2Graph2']]#/:lat\"); result;",
							iri("http://inova8.com/script/groovy")));
			assertEquals("400", result.stringValue());
			}catch(Exception e){
				e.printStackTrace();
			}
	}
	
	/**
	 * Test 20.
	 */
	@Test
	@Order(21)
	void test_20() {
		
		try{
			
			ObjectValue objectValue = new ObjectValue();
			org.eclipse.rdf4j.model.Value result = objectValue.evaluate(repositoryTripleSource,
					iri("http://inova8.com/calc2graph/id/BatteryLimit1"),
					iri("http://inova8.com/calc2graph/def/massYield"),
					literal("$this.prefix(\"<http://inova8.com/calc2graph/def/>\").prefix(\"rdfs\",\"<http://www.w3.org/2000/01/rdf-schema#>\").prefix(\"id\",\"<http://inova8.com/calc2graph/id/>\");var result= $this.getFact(\":Location@:appearsOn#[:location.Map  id:Calc2Graph1 ]/:long\"); result;",
							iri("http://inova8.com/script/groovy")));
			assertEquals("501", result.stringValue());
			}catch(Exception e){
				e.printStackTrace();
			}
	}
	
	/**
	 * Test 20 1.
	 */
	@Test
	@Order(21)
	void test_20_1() {
		
		try{
			
			ObjectValue objectValue = new ObjectValue();
			org.eclipse.rdf4j.model.Value result = objectValue.evaluate(repositoryTripleSource,
					iri("http://inova8.com/calc2graph/id/BatteryLimit1"),
					iri("http://inova8.com/calc2graph/def/massYield"),
					literal("$this.prefix(\"<http://inova8.com/calc2graph/def/>\").prefix(\"rdfs\",\"<http://www.w3.org/2000/01/rdf-schema#>\").prefix(\"id\",\"<http://inova8.com/calc2graph/id/>\");var result= $this.getFact(\":Location@:appearsOn#[:location.Map  id:Calc2Graph2 ]/:long\"); result;",
							iri("http://inova8.com/script/groovy")));
			assertEquals("502", result.stringValue());
			}catch(Exception e){
				e.printStackTrace();
			}
	}
	
	/**
	 * Test 21.
	 */
	@Test
	@Order(22)
	void test_21() {
		
		try{
			
			ObjectValue objectValue = new ObjectValue();
			org.eclipse.rdf4j.model.Value result = objectValue.evaluate(repositoryTripleSource,
					iri("http://inova8.com/calc2graph/id/BatteryLimit1"),
					iri("http://inova8.com/calc2graph/def/massYield"),
					literal("$this.prefix(\"<http://inova8.com/calc2graph/def/>\").prefix(\"rdfs\",\"<http://www.w3.org/2000/01/rdf-schema#>\").prefix(\"id\",\"<http://inova8.com/calc2graph/id/>\");var result= $this.getFact(\":Location@:appearsOn#[:location.Map  id:Calc2Graph2 ]\"); result;",
							iri("http://inova8.com/script/groovy")));
			assertEquals("http://inova8.com/calc2graph/id/Location_BL1_2", result.stringValue());
			}catch(Exception e){
				e.printStackTrace();
			}
	}
	
	/**
	 * Test 22.
	 */
	@Test
	@Order(23)
	void test_22() {
		
		try{
			
			ObjectValue objectValue = new ObjectValue();
			org.eclipse.rdf4j.model.Value result = objectValue.evaluate(repositoryTripleSource,
					iri("http://inova8.com/calc2graph/id/Unit2"),
					iri("http://inova8.com/calc2graph/def/massYield"),
					literal("$this.prefix(\"<http://inova8.com/calc2graph/def/>\").prefix(\"rdfs\",\"<http://www.w3.org/2000/01/rdf-schema#>\").prefix(\"id\",\"<http://inova8.com/calc2graph/id/>\");var result= $this.getFact(\":massThroughput\"); result;",
							iri("http://inova8.com/script/groovy")));
			assertEquals("23.43999981880188", result.stringValue());
			}catch(Exception e){
				e.printStackTrace();
			}
	}
	
	/**
	 * Test 23.
	 */
	@Test
	@Order(24)
	void test_23() {
		
		try{
			
			ObjectValue objectValue = new ObjectValue();
			org.eclipse.rdf4j.model.Value result = objectValue.evaluate(repositoryTripleSource,
					iri("http://inova8.com/calc2graph/id/Unit2"),
					iri("http://inova8.com/calc2graph/def/massYield"),
					literal("$this.prefix(\"<http://inova8.com/calc2graph/def/>\").prefix(\"rdfs\",\"<http://www.w3.org/2000/01/rdf-schema#>\").prefix(\"id\",\"<http://inova8.com/calc2graph/id/>\");var result= $this.getFact(\":massThroughput\"); result;",
							iri("http://inova8.com/script/groovy")));
			assertEquals("23.43999981880188", result.stringValue());
			}catch(Exception e){
				e.printStackTrace();
			}
	}
	
	/**
	 * Test 24.
	 */
	@Test
	@Order(25)
	void test_24() {
		
		try{
			
			ObjectValue objectValue = new ObjectValue();
			org.eclipse.rdf4j.model.Value result = objectValue.evaluate(repositoryTripleSource,
					iri("http://inova8.com/calc2graph/id/Unit3"),
					iri("http://inova8.com/calc2graph/def/massYield"),
					literal("$this.prefix(\"<http://inova8.com/calc2graph/def/>\").prefix(\"rdfs\",\"<http://www.w3.org/2000/01/rdf-schema#>\").prefix(\"id\",\"<http://inova8.com/calc2graph/id/>\");var result= $this.getFact(\":massThroughput\"); result;",
							iri("http://inova8.com/script/groovy")));
			assertEquals("24.77999922633171", result.stringValue());
			}catch(Exception e){
				e.printStackTrace();
			}
	}
	
	/**
	 * Test 25.
	 */
	@Test
	@Order(26)
	void test_25() {
		
		try{
			
			ObjectValue objectValue = new ObjectValue();
			org.eclipse.rdf4j.model.Value result = objectValue.evaluate(repositoryTripleSource,
					iri("http://inova8.com/calc2graph/id/Unit3"),
					iri("http://inova8.com/calc2graph/def/massYield"),
					literal("$this.prefix(\"<http://inova8.com/calc2graph/def/>\").prefix(\"rdfs\",\"<http://www.w3.org/2000/01/rdf-schema#>\").prefix(\"id\",\"<http://inova8.com/calc2graph/id/>\");var result= $this.getFact(\":massFlowBalance\"); result;",
							iri("http://inova8.com/script/groovy")));
			assertEquals("1.339999407529831", result.stringValue());
			}catch(Exception e){
				e.printStackTrace();
			}
	}
	
	/**
	 * Test 100.
	 */
	@Test
	@Order(100)
	//literal("$this.prefix(\"<http://inova8.com/calc2graph/def/>\");var result= $this.getFact(\":volumeFlow\").floatValue()* $this.getFact(\":Attribute@:density\").floatValue();  result;",
	void test_100() {
		try {
			ClearCache clearCache = new ClearCache();
			org.eclipse.rdf4j.model.Value result = clearCache.evaluate(repositoryTripleSource,
					iri("http://inova8.com/calc2graph/id/BatteryLimit1"),
					iri("http://inova8.com/calc2graph/def/testProperty4"),
					literal("$this.prefix(\"<http://inova8.com/calc2graph/def/>\");var result= $this.getFact(\":volumeFlow\").floatValue()* $this.getFact(\":Attribute@:density\").floatValue();  result;",
							iri("http://inova8.com/script/groovy")));
			assertEquals("true", result.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
}
