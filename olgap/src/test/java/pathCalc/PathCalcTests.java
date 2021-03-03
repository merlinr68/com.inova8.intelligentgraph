package pathCalc;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.eclipse.rdf4j.model.util.Values.literal;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Stack;

import org.apache.commons.io.FileUtils;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.evaluation.RepositoryTripleSource;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.sail.nativerdf.NativeStore;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import olgap.ClearCache;
import olgap.Evaluator;
import olgap.FactValue;
import olgap.ObjectValue;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PathCalcTests {
	private static RepositoryConnection conn;
	static RepositoryTripleSource repositoryTripleSource;
	private static Source source;
	private static Evaluator evaluator;
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		File dataDir = new File("src/test/resources/datadir/");
		FileUtils.deleteDirectory(dataDir);
		Repository workingRep = new SailRepository(new NativeStore(dataDir));

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
		source = new Source(repositoryTripleSource);
		evaluator = new Evaluator();
	}
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
							iri("http://inova8.com/calc2graph/def/groovy")));
			assertEquals("true", result.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
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
							iri("http://inova8.com/calc2graph/def/groovy")));
			assertEquals("24.77999922633171", result.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	@Test
	@Order(4)
	void test_2() {
		try {
			ObjectValue objectValue = new ObjectValue();
			org.eclipse.rdf4j.model.Value result = objectValue.evaluate(repositoryTripleSource,
					iri("http://inova8.com/calc2graph/id/BatteryLimit1"),
					iri("http://inova8.com/calc2graph/def/testProperty4"), 
					literal("$this.prefix(\"<http://inova8.com/calc2graph/def/>\");var result= $this.getFact(\":massFlow\").floatValue();  result;",
							iri("http://inova8.com/calc2graph/def/groovy")));
			assertEquals("24.779999", result.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
		
	}
	@Test
	@Order(5)
	void test_4() {
		
		try {
			ObjectValue objectValue = new ObjectValue();
			org.eclipse.rdf4j.model.Value result = objectValue.evaluate(repositoryTripleSource,
					iri("http://inova8.com/calc2graph/id/Unit1"),
					iri("http://inova8.com/calc2graph/def/test"),
					literal("$this.prefix(\"<http://inova8.com/calc2graph/def/>\");var result= $this.getFact(\":massThroughput\").floatValue();  result;",
							iri("http://inova8.com/calc2graph/def/groovy")));
			assertEquals("23.44", result.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		} 
	}
	@Test
	@Order(5)
	void test_4_3() {
		
		try {
			ObjectValue objectValue = new ObjectValue();
			org.eclipse.rdf4j.model.Value result = objectValue.evaluate(repositoryTripleSource,
					iri("http://inova8.com/calc2graph/id/BatteryLimit1"),
					iri("http://inova8.com/calc2graph/def/density"),
					literal("$this.prefix(\"def\",\"<http://inova8.com/calc2graph/def/>\");var result= $this.getFact(\"def:Attribute@def:density\").floatValue();  result;",
							iri("http://inova8.com/calc2graph/def/groovy")));
			if(result!=null) 
					assertEquals("0.42", result.stringValue());
			else
				fail();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
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
	void test_4_1() {
		
		try {
			FactValue objectValue = new FactValue();
			org.eclipse.rdf4j.model.Value result = objectValue.evaluate(repositoryTripleSource,
					iri("http://inova8.com/calc2graph/id/Unit1"),
					iri("http://inova8.com/calc2graph/def/massThroughput"));
			assertEquals("23.43999981880188", result.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		} 
	}
	@Test
	@Order(6)
	void test_5() {
		
		try{
			
			ObjectValue objectValue = new ObjectValue();
			org.eclipse.rdf4j.model.Value result = objectValue.evaluate(repositoryTripleSource,
					iri("http://inova8.com/calc2graph/id/BatteryLimit2"),
					iri("http://inova8.com/calc2graph/def/massYield"),
					literal("$this.prefix(\"<http://inova8.com/calc2graph/def/>\");var result= $this.getFact(\"^:hasProductBatteryLimit\").getFact(\":massThroughput\").floatValue();  result;",
							iri("http://inova8.com/calc2graph/def/groovy")));
			assertEquals("23.44", result.stringValue());
			}catch(Exception e){
				fail();
				e.printStackTrace();
			}
	}
	@Test
	@Order(7)
	void test_6() {
		
		try{
			
			ObjectValue objectValue = new ObjectValue();
			org.eclipse.rdf4j.model.Value result = objectValue.evaluate(repositoryTripleSource,
					iri("http://inova8.com/calc2graph/id/BatteryLimit2"),
					iri("http://inova8.com/calc2graph/def/massYield"),
					literal("$this.prefix(\"<http://inova8.com/calc2graph/def/>\");var result= $this.getFact(\":massFlow\").floatValue()/ $this.getFact(\"^:hasProductBatteryLimit\").getFact(\":massThroughput\").floatValue();  result;",
							iri("http://inova8.com/calc2graph/def/groovy")));
			assertEquals("0.5221842786792522", result.stringValue());
			}catch(Exception e){
				fail();
				e.printStackTrace();
			}
	}
	@Test
	@Order(8)
	void test_7() {		
		try{
			
			ObjectValue objectValue = new ObjectValue();
			org.eclipse.rdf4j.model.Value result = objectValue.evaluate(repositoryTripleSource,
					iri("http://inova8.com/calc2graph/id/BatteryLimit2"),
					iri("http://inova8.com/calc2graph/def/massThroughput"),
					literal("$this.prefix(\"<http://inova8.com/calc2graph/def/>\");var result=  $this.getFact(\"^:hasProductBatteryLimit/:massThroughput\").floatValue();  result;",
							iri("http://inova8.com/calc2graph/def/groovy")));
			 assertEquals("23.44",result.stringValue());
			}catch(Exception e){
				fail();
				e.printStackTrace();
			}
	}
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
	@Test
	@Order(10)
	void test_9() {
		
		try{
			
			ObjectValue objectValue = new ObjectValue();
			org.eclipse.rdf4j.model.Value result = objectValue.evaluate(repositoryTripleSource,
					iri("http://inova8.com/calc2graph/id/BatteryLimit1"),
					iri("http://inova8.com/calc2graph/def/massYield"),
					literal("$this.prefix(\"<http://inova8.com/calc2graph/def/>\").prefix(\"id\",\"<http://inova8.com/calc2graph/id/>\");var result= $this.getFact(\":Location@:appearsOn[eq id:Calc2Graph1]#/:lat\"); result;",
							iri("http://inova8.com/calc2graph/def/groovy")));
			assertEquals("400", result.stringValue());
			}catch(Exception e){
				fail();
				e.printStackTrace();
			}
	}
	@Test
	@Order(11)
	void test_10() {
		
		try{
			
			ObjectValue objectValue = new ObjectValue();
			org.eclipse.rdf4j.model.Value result = objectValue.evaluate(repositoryTripleSource,
					iri("http://inova8.com/calc2graph/id/BatteryLimit1"),
					iri("http://inova8.com/calc2graph/def/massYield"),
					literal("$this.prefix(\"<http://inova8.com/calc2graph/def/>\").prefix(\"id\",\"<http://inova8.com/calc2graph/id/>\");var result= $this.getFact(\":Location@:appearsOn[eq id:Calc2Graph1]#\").getFact(\":lat\"); result;",
							iri("http://inova8.com/calc2graph/def/groovy")));
			assertEquals("400", result.stringValue());
			}catch(Exception e){
				fail();
				e.printStackTrace();
			}
	}
	@Test
	@Order(12)
	void test_11() {
		
		try{
			
			ObjectValue objectValue = new ObjectValue();
			org.eclipse.rdf4j.model.Value result = objectValue.evaluate(repositoryTripleSource,
					iri("http://inova8.com/calc2graph/id/BatteryLimit1"),
					iri("http://inova8.com/calc2graph/def/massYield"),
					literal("$this.prefix(\"<http://inova8.com/calc2graph/def/>\").prefix(\"id\",\"<http://inova8.com/calc2graph/id/>\");var result= $this.getFact(\":Location@:appearsOn[eq id:Calc2Graph2]#\"); result;",
							iri("http://inova8.com/calc2graph/def/groovy")));
			if(result!=null) 
				assertEquals("http://inova8.com/calc2graph/id/Location_BL1_2", result.stringValue());
			
			}catch(Exception e){
				fail();
				e.printStackTrace();
			}
	}
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

	@Test
	@Order(16)
	void test_15() {
		
		try{
			
			ObjectValue objectValue = new ObjectValue();
			org.eclipse.rdf4j.model.Value result = objectValue.evaluate(repositoryTripleSource,
					iri("http://inova8.com/calc2graph/id/Calc2Graph1"),
					iri("http://inova8.com/calc2graph/def/massYield"),
					literal("$this.prefix(\"<http://inova8.com/calc2graph/def/>\").prefix(\"id\",\"<http://inova8.com/calc2graph/id/>\");var result= $this.getFact(\"^:Location@:appearsOn[eq id:BatteryLimit2]#\"); result;",
							iri("http://inova8.com/calc2graph/def/groovy")));
			assertEquals("http://inova8.com/calc2graph/id/Location_BL2", result.stringValue());
			}catch(Exception e){
				fail();
				e.printStackTrace();
			}
	}
	@Test
	@Order(17)
	void test_16() {
		
		try{
			
			ObjectValue objectValue = new ObjectValue();
			org.eclipse.rdf4j.model.Value result = objectValue.evaluate(repositoryTripleSource,
					iri("http://inova8.com/calc2graph/id/Calc2Graph1"),
					iri("http://inova8.com/calc2graph/def/massYield"),
					literal("$this.prefix(\"<http://inova8.com/calc2graph/def/>\").prefix(\"id\",\"<http://inova8.com/calc2graph/id/>\");var result= $this.getFact(\"^:Location@:appearsOn[eq id:BatteryLimit2]\"); result;",
							iri("http://inova8.com/calc2graph/def/groovy")));
			assertEquals("http://inova8.com/calc2graph/id/BatteryLimit2", result.stringValue());
			}catch(Exception e){
				fail();
				e.printStackTrace();
			}
	}
	@Test
	@Order(18)
	void test_17() {
		
		try{
			
			ObjectValue objectValue = new ObjectValue();
			org.eclipse.rdf4j.model.Value result = objectValue.evaluate(repositoryTripleSource,
					iri("http://inova8.com/calc2graph/id/Calc2Graph1"),
					iri("http://inova8.com/calc2graph/def/massYield"),
					literal("$this.prefix(\"<http://inova8.com/calc2graph/def/>\").prefix(\"id\",\"<http://inova8.com/calc2graph/id/>\");var result= $this.getFact(\"^:Location@:appearsOn[eq id:BatteryLimit1]#/:lat\"); result;",
							iri("http://inova8.com/calc2graph/def/groovy")));
			assertEquals("400", result.stringValue());
			}catch(Exception e){
				e.printStackTrace();
			}
	}
	@Test
	@Order(19)
	void test_18() {
		
		try{
			
			ObjectValue objectValue = new ObjectValue();
			org.eclipse.rdf4j.model.Value result = objectValue.evaluate(repositoryTripleSource,
					iri("http://inova8.com/calc2graph/id/BatteryLimit1"),
					iri("http://inova8.com/calc2graph/def/massYield"),
					literal("$this.prefix(\"<http://inova8.com/calc2graph/def/>\").prefix(\"rdfs\",\"<http://www.w3.org/2000/01/rdf-schema#>\").prefix(\"id\",\"<http://inova8.com/calc2graph/id/>\");var result= $this.getFact(\":Location@:appearsOn[eq [ rdfs:label 'Calc2Graph2']]#\"); result;",
							iri("http://inova8.com/calc2graph/def/groovy")));
			assertEquals("http://inova8.com/calc2graph/id/Location_BL1_2", result.stringValue());
			}catch(Exception e){
				e.printStackTrace();
			}
	}
	@Test
	@Order(20)
	void test_19() {
		
		try{
			
			ObjectValue objectValue = new ObjectValue();
			org.eclipse.rdf4j.model.Value result = objectValue.evaluate(repositoryTripleSource,
					iri("http://inova8.com/calc2graph/id/BatteryLimit1"),
					iri("http://inova8.com/calc2graph/def/massYield"),
					literal("$this.prefix(\"<http://inova8.com/calc2graph/def/>\").prefix(\"rdfs\",\"<http://www.w3.org/2000/01/rdf-schema#>\").prefix(\"id\",\"<http://inova8.com/calc2graph/id/>\");var result= $this.getFact(\":Location@:appearsOn[eq [ rdfs:label 'Calc2Graph2']]#/:lat\"); result;",
							iri("http://inova8.com/calc2graph/def/groovy")));
			assertEquals("400", result.stringValue());
			}catch(Exception e){
				e.printStackTrace();
			}
	}
	@Test
	@Order(21)
	void test_20() {
		
		try{
			
			ObjectValue objectValue = new ObjectValue();
			org.eclipse.rdf4j.model.Value result = objectValue.evaluate(repositoryTripleSource,
					iri("http://inova8.com/calc2graph/id/BatteryLimit1"),
					iri("http://inova8.com/calc2graph/def/massYield"),
					literal("$this.prefix(\"<http://inova8.com/calc2graph/def/>\").prefix(\"rdfs\",\"<http://www.w3.org/2000/01/rdf-schema#>\").prefix(\"id\",\"<http://inova8.com/calc2graph/id/>\");var result= $this.getFact(\":Location@:appearsOn#[:location.Map  id:Calc2Graph1 ]/:long\"); result;",
							iri("http://inova8.com/calc2graph/def/groovy")));
			assertEquals("501", result.stringValue());
			}catch(Exception e){
				e.printStackTrace();
			}
	}
	@Test
	@Order(21)
	void test_20_1() {
		
		try{
			
			ObjectValue objectValue = new ObjectValue();
			org.eclipse.rdf4j.model.Value result = objectValue.evaluate(repositoryTripleSource,
					iri("http://inova8.com/calc2graph/id/BatteryLimit1"),
					iri("http://inova8.com/calc2graph/def/massYield"),
					literal("$this.prefix(\"<http://inova8.com/calc2graph/def/>\").prefix(\"rdfs\",\"<http://www.w3.org/2000/01/rdf-schema#>\").prefix(\"id\",\"<http://inova8.com/calc2graph/id/>\");var result= $this.getFact(\":Location@:appearsOn#[:location.Map  id:Calc2Graph2 ]/:long\"); result;",
							iri("http://inova8.com/calc2graph/def/groovy")));
			assertEquals("502", result.stringValue());
			}catch(Exception e){
				e.printStackTrace();
			}
	}
	@Test
	@Order(22)
	void test_21() {
		
		try{
			
			ObjectValue objectValue = new ObjectValue();
			org.eclipse.rdf4j.model.Value result = objectValue.evaluate(repositoryTripleSource,
					iri("http://inova8.com/calc2graph/id/BatteryLimit1"),
					iri("http://inova8.com/calc2graph/def/massYield"),
					literal("$this.prefix(\"<http://inova8.com/calc2graph/def/>\").prefix(\"rdfs\",\"<http://www.w3.org/2000/01/rdf-schema#>\").prefix(\"id\",\"<http://inova8.com/calc2graph/id/>\");var result= $this.getFact(\":Location@:appearsOn#[:location.Map  id:Calc2Graph2 ]\"); result;",
							iri("http://inova8.com/calc2graph/def/groovy")));
			assertEquals("http://inova8.com/calc2graph/id/Location_BL1_2", result.stringValue());
			}catch(Exception e){
				e.printStackTrace();
			}
	}
	@Test
	@Order(23)
	void test_22() {
		
		try{
			
			ObjectValue objectValue = new ObjectValue();
			org.eclipse.rdf4j.model.Value result = objectValue.evaluate(repositoryTripleSource,
					iri("http://inova8.com/calc2graph/id/Unit2"),
					iri("http://inova8.com/calc2graph/def/massYield"),
					literal("$this.prefix(\"<http://inova8.com/calc2graph/def/>\").prefix(\"rdfs\",\"<http://www.w3.org/2000/01/rdf-schema#>\").prefix(\"id\",\"<http://inova8.com/calc2graph/id/>\");var result= $this.getFact(\":massThroughput\"); result;",
							iri("http://inova8.com/calc2graph/def/groovy")));
			assertEquals("23.43999981880188", result.stringValue());
			}catch(Exception e){
				e.printStackTrace();
			}
	}
	@Test
	@Order(24)
	void test_23() {
		
		try{
			
			ObjectValue objectValue = new ObjectValue();
			org.eclipse.rdf4j.model.Value result = objectValue.evaluate(repositoryTripleSource,
					iri("http://inova8.com/calc2graph/id/Unit2"),
					iri("http://inova8.com/calc2graph/def/massYield"),
					literal("$this.prefix(\"<http://inova8.com/calc2graph/def/>\").prefix(\"rdfs\",\"<http://www.w3.org/2000/01/rdf-schema#>\").prefix(\"id\",\"<http://inova8.com/calc2graph/id/>\");var result= $this.getFact(\":massThroughput\"); result;",
							iri("http://inova8.com/calc2graph/def/groovy")));
			assertEquals("23.43999981880188", result.stringValue());
			}catch(Exception e){
				e.printStackTrace();
			}
	}
	@Test
	@Order(25)
	void test_24() {
		
		try{
			
			ObjectValue objectValue = new ObjectValue();
			org.eclipse.rdf4j.model.Value result = objectValue.evaluate(repositoryTripleSource,
					iri("http://inova8.com/calc2graph/id/Unit3"),
					iri("http://inova8.com/calc2graph/def/massYield"),
					literal("$this.prefix(\"<http://inova8.com/calc2graph/def/>\").prefix(\"rdfs\",\"<http://www.w3.org/2000/01/rdf-schema#>\").prefix(\"id\",\"<http://inova8.com/calc2graph/id/>\");var result= $this.getFact(\":massThroughput\"); result;",
							iri("http://inova8.com/calc2graph/def/groovy")));
			assertEquals("24.77999922633171", result.stringValue());
			}catch(Exception e){
				e.printStackTrace();
			}
	}
	@Test
	@Order(26)
	void test_25() {
		
		try{
			
			ObjectValue objectValue = new ObjectValue();
			org.eclipse.rdf4j.model.Value result = objectValue.evaluate(repositoryTripleSource,
					iri("http://inova8.com/calc2graph/id/Unit3"),
					iri("http://inova8.com/calc2graph/def/massYield"),
					literal("$this.prefix(\"<http://inova8.com/calc2graph/def/>\").prefix(\"rdfs\",\"<http://www.w3.org/2000/01/rdf-schema#>\").prefix(\"id\",\"<http://inova8.com/calc2graph/id/>\");var result= $this.getFact(\":massFlowBalance\"); result;",
							iri("http://inova8.com/calc2graph/def/groovy")));
			assertEquals("1.339999407529831", result.stringValue());
			}catch(Exception e){
				e.printStackTrace();
			}
	}
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
							iri("http://inova8.com/calc2graph/def/groovy")));
			assertEquals("true", result.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
}
