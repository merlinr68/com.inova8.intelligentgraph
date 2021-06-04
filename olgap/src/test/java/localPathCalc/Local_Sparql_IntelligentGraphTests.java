/*
 * inova8 2020
 */
package localPathCalc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.antlr.v4.runtime.RecognitionException;
import org.apache.commons.io.FileUtils;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Namespace;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.eclipse.rdf4j.repository.evaluation.RepositoryTripleSource;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.sail.Sail;
import org.eclipse.rdf4j.sail.lucene.LuceneSail;
import org.eclipse.rdf4j.sail.nativerdf.NativeStore;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import intelligentGraph.IntelligentGraphConfig;
import intelligentGraph.IntelligentGraphFactory;
import intelligentGraph.IntelligentGraphSail;
import pathCalc.Evaluator;
import pathCalc.Thing;
import pathPatternProcessor.PathPatternException;
import pathQL.PathQL;
import pathQLModel.Resource;
import pathQLRepository.Graph;
import pathQLRepository.PathQLRepository;
import pathQLResults.FactResults;
import pathQLResults.PathQLResults;
import utilities.Query;

import static org.eclipse.rdf4j.model.util.Values.iri;
/**
 * The Class PathQLTests.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Local_Sparql_IntelligentGraphTests {
	
	private static RepositoryConnection conn;
	private static PathQLRepository source;
	


	private static org.eclipse.rdf4j.repository.Repository workingRep;
	/**
	 * Sets the up before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		workingRep = Query.createNativeLuceneIntelligentGraphRepository("src/test/resources/datadir/Local_Sparql_IntelligentGraphTests/");
		Query.addFile(workingRep, "src/test/resources/calc2graph.data.ttl");
		Query.addFile(workingRep, "src/test/resources/calc2graph.def.ttl");
		
//		
//		File dataDir = new File("src/test/resources/datadir/Local_Sparql_IntelligentGraphTests/");
//		FileUtils.deleteDirectory(dataDir);
//		
//		IntelligentGraphConfig intelligentGraphConfig = new IntelligentGraphConfig();
//		IntelligentGraphFactory intelligentGraphFactory = new IntelligentGraphFactory();
//		IntelligentGraphSail intelligentGraphSail= (IntelligentGraphSail)intelligentGraphFactory.getSail(intelligentGraphConfig);
//		//IntelligentGraphSail intelligentGraphSail = new IntelligentGraphSail();		
//		
//		LuceneSail lucenesail = new LuceneSail();
//		lucenesail.setParameter(LuceneSail.LUCENE_RAMDIR_KEY, "true");
//		
////		Sail baseSail = new NativeStore(dataDir);		
////		intelligentGraphSail.setBaseSail(baseSail);
////		org.eclipse.rdf4j.repository.Repository workingRep = new SailRepository(intelligentGraphSail);
//		
//		Sail baseSail = new NativeStore(dataDir);		
//		lucenesail.setBaseSail(baseSail);
//		intelligentGraphSail.setBaseSail(lucenesail);
//		org.eclipse.rdf4j.repository.Repository workingRep = new SailRepository(intelligentGraphSail);
//		
////		Sail baseSail = new NativeStore(dataDir);		
////		intelligentGraphSail.setBaseSail(baseSail);		
////		lucenesail.setBaseSail(intelligentGraphSail);	
////		org.eclipse.rdf4j.repository.Repository workingRep = new SailRepository(lucenesail);
//		
//		String dataFilename = "src/test/resources/calc2graph.data.ttl";
//		InputStream dataInput = new FileInputStream(dataFilename);
//		Model dataModel = Rio.parse(dataInput, "", RDFFormat.TURTLE);
//		conn = workingRep.getConnection();
//		conn.add(dataModel.getStatements(null, null, null),iri("http://default"));
//
//		String modelFilename = "src/test/resources/calc2graph.def.ttl";
//		InputStream modelInput = new FileInputStream(modelFilename);
//		Model modelModel = Rio.parse(modelInput, "", RDFFormat.TURTLE);
		conn = workingRep.getConnection();
//		conn.add(modelModel.getStatements(null, null, null),iri("http://default"));
//		boolean namespace = conn.getNamespaces().hasNext();
//		boolean context = conn.getContextIDs().hasNext();
//		long size = conn.size();
//		int i=1;
		conn.setNamespace("", "http://inova8.com/calc2graph/def/");
		conn.setNamespace("rdfs","http://www.w3.org/2000/01/rdf-schema#");
		source = PathQLRepository.create(workingRep);
	}

	/**
	 * Adds the graph 2.
	 *
	 * @return the thing
	 * @throws RecognitionException the recognition exception
	 * @throws PathPatternException the path pattern exception
	 */
	private Thing addGraph2() throws RecognitionException, PathPatternException {
		source.removeGraph("<http://inova8.com/calc2graph/testGraph2>");
		Graph graph = source.addGraph("<http://inova8.com/calc2graph/testGraph2>");
		Thing myCountry = graph.getThing(":Country1");
		myCountry.addFact(":sales", "1");
		myCountry.addFact(":sales", "2");
		myCountry.addFact(":sales", "3");
		myCountry.addFact(":sales", "4");
		myCountry.addFact(":sales", "5");
		myCountry.addFact(":sales", "60");
		String averageSalesScript = "totalSales=0; count=0;for(sales in $this.getFacts(\"<http://inova8.com/calc2graph/def/sales>\")){totalSales +=  sales.doubleValue();count++}; return totalSales/count;";
		myCountry.addFact(":averageSales", averageSalesScript, Evaluator.GROOVY) ;
		return myCountry;
	}
	
	/**
	 * Adds the graph 3.
	 *
	 * @return the thing
	 * @throws RecognitionException the recognition exception
	 * @throws PathPatternException the path pattern exception
	 */
	private Thing addGraph3() throws RecognitionException, PathPatternException {
		source.removeGraph("<http://inova8.com/calc2graph/testGraph3>");
		Graph graph = source.addGraph("<http://inova8.com/calc2graph/testGraph3>");
		Thing myCountry = graph.getThing(":Country1");
		myCountry.addFact(":sales", "10");
		myCountry.addFact(":sales", "20");
		myCountry.addFact(":sales", "30");
		myCountry.addFact(":sales", "40");
		myCountry.addFact(":sales", "50");
		String totalSalesScript = "return $this.getFacts(\":sales\").total();";
		myCountry.addFact(":totalSales", totalSalesScript, Evaluator.GROOVY) ;
		return myCountry;
	}
	
	/**
	 * Adds the graph 4.
	 *
	 * @return the thing
	 * @throws RecognitionException the recognition exception
	 * @throws PathPatternException the path pattern exception
	 */
	private Thing addGraph4() throws RecognitionException, PathPatternException {
		source.removeGraph("<http://inova8.com/calc2graph/testGraph4>");
		Graph graph = source.addGraph("<http://inova8.com/calc2graph/testGraph4>");
		Thing myCountry = graph.getThing(":Country1");
		myCountry.addFact(":sales", "100");
		myCountry.addFact(":sales", "200");
		myCountry.addFact(":sales", "300");
		myCountry.addFact(":sales", "400");
		myCountry.addFact(":sales", "500");
		String averageSalesScript = "return $this.getFacts(\":sales\").average();";
		myCountry.addFact(":averageSales", averageSalesScript, Evaluator.GROOVY) ;
		return myCountry;
	}
	
	/**
	 * Ig 0.
	 */
	@Test
	@Order(0)
	void ig_0() {

		try {
			String queryString1 = "ASK { <file://calc2graph.def.ttl> <http://inova8.com/script/isPrivate> \"true\"^^<http://www.w3.org/2001/XMLSchema#boolean> }";

			Boolean result = Query.runBoolean(conn, queryString1);
			assertEquals(false
					,result);
		} catch (Exception e) {

			fail();
			e.printStackTrace();
		}
	}
	
	/**
	 * Ig 1.
	 */
	@Test
	@Order(1)
	void ig_1() {

		try {
			String queryString1 = "select $o  ?time FROM <http://default> WHERE {VALUES(?time){(41)(42)} <http://inova8.com/calc2graph/id/BatteryLimit1> <http://inova8.com/calc2graph/def/testProperty6> $o } ";

			String result = Query.runSPARQL(conn, queryString1);
			assertEquals("time=41;o=41;time=42;o=42;"
					,result); 
		} catch (Exception e) {

			fail();
			e.printStackTrace();
		}
	}

	/**
	 * Ig 2.
	 */
	@Test
	@Order(2)
	void ig_2() {

		try {
			//RepositoryResult<Statement> results = conn.getStatements(null, iri("http://inova8.com/calc2graph/def/volumeFlow"), null);
			//   RepositoryResult<Statement> results = conn.getStatements(iri("http://inova8.com/calc2graph/id/BatteryLimit1"), null, null);
			RepositoryResult<Statement> results = conn.getStatements(iri("http://inova8.com/calc2graph/id/BatteryLimit1"), iri("http://inova8.com/calc2graph/def/volumeFlow"), null, iri("http://default"));
			Statement result;
			while( results.hasNext()) {
				result=results.next();
				org.eclipse.rdf4j.model.Resource subject = result.getSubject();
				 Value object = result.getObject();
				 assertEquals("(http://inova8.com/calc2graph/id/BatteryLimit1, http://inova8.com/calc2graph/def/volumeFlow, \"59\"^^<http://www.w3.org/2001/XMLSchema#int>, http://default) [http://default]",result.toString());
				 break;
				
			}
		//	
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}

	/**
	 * Ig 3.
	 */
	@Test
	@Order(3)
	void ig_3() {

		try {
			String queryString1 = "select ?time $o $o_SCRIPT $o_TRACE  WHERE {VALUES(?time){(41)} <http://inova8.com/calc2graph/id/BatteryLimit1> <http://inova8.com/calc2graph/def/volumeFlow> $o,$o_SCRIPT, $o_TRACE } limit 1";

			String result = Query.runSPARQL(conn, queryString1);
			Query.assertEqualsWOSpaces("o_SCRIPT=59;;o_TRACE=<ol style='list-style-type:none;'><ol style='list-style-type:none;'><li>Evaluating predicate <a href='http://inova8.com/calc2graph/def/volumeFlow' target='_blank'>volumeFlow</a> of <a href='http://inova8.com/calc2graph/id/BatteryLimit1' target='_blank'>BatteryLimit1</a>, by invoking <b>javascript</b> script\n"
					+ "</li>\n"
					+ "<li><div  style='border: 1px solid black;'> <pre><code >59;</code></pre></div></li>\n"
					+ "<ol style='list-style-type:none;'></ol><li>Evaluated <a href='http://inova8.com/calc2graph/def/volumeFlow' target='_blank'>volumeFlow</a> of <a href='http://inova8.com/calc2graph/id/BatteryLimit1' target='_blank'>BatteryLimit1</a> =  59^^<a href='http://www.w3.org/2001/XMLSchema#int' target='_blank'>int</a></li>\n"
					+ "</ol><li>Calculated <a href='http://inova8.com/calc2graph/def/volumeFlow' target='_blank'>volumeFlow</a> of <a href='http://inova8.com/calc2graph/id/BatteryLimit1' target='_blank'>BatteryLimit1</a> = 59^^<a href='http://www.w3.org/2001/XMLSchema#int' target='_blank'>int</a></li>\n"
					+ "</ol>;time=41;o=59;",result);
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	
	/**
	 * Ig 4.
	 */
	@Test
	@Order(4)
	void ig_4() {

		try {
			String queryString1 = "select * {VALUES(?time){(41)} <http://inova8.com/calc2graph/id/BatteryLimit1> <http://inova8.com/calc2graph/def/massFlow> $o} limit 1";

			String result = Query.runQuery(conn, queryString1);
			assertEquals("time=41;o=24.77999922633171;"
					,result);
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	
	/**
	 * Ig 5.
	 */
	@Test
	@Order(5)
	void ig_5() {

		try {
			String queryString1 = "select * {BIND(\"42*3;\"^^<http://inova8.com/script/groovy> as ?result) } ";

			String result = Query.runQuery(conn, queryString1);
			assertEquals("126",result);
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	
	/**
	 * Ig 6.
	 */
	@Test
	@Order(6)
	void ig_6() {
		
		try {
			addGraph2();
			String queryString1 = "PREFIX : <http://inova8.com/calc2graph/def/> select  ?o \n"
					+ "FROM <http://inova8.com/calc2graph/testGraph2>\n"
					+ "FROM <file://calc2graph.data.ttl>\n"
					+ "FROM <file://calc2graph.def.ttl>\n"
					+ "{\n"
					+ "  ?s  :averageSales  ?o ,?o_SCRIPT,?o_TRACE} limit 10";


			String result = Query.runSPARQL(conn, queryString1);
			assertEquals("o=12.5;",result);
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	
	/**
	 * Ig 7.
	 */
	@Test
	@Order(7)
	void ig_7() {

		try {
			addGraph3();
			String queryString1 = "PREFIX : <http://inova8.com/calc2graph/def/> select ?o "
					+ "FROM <http://inova8.com/calc2graph/testGraph3>\n"
					+ "FROM <file://calc2graph.data.ttl>\n"
					+ "FROM <file://calc2graph.def.ttl>\n"
					+ "{\n"
					+ "  ?s  :totalSales  ?o ,?o_SCRIPT,?o_TRACE} limit 10";


			String result = Query.runSPARQL(conn, queryString1);
			assertEquals("o=150.0;",result);
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	
	/**
	 * Ig 8.
	 */
	@Test
	@Order(8)
	void ig_8() {

		try {
			addGraph4();
			String queryString1 = "PREFIX : <http://inova8.com/calc2graph/def/> select ?o "
					+ "FROM <http://inova8.com/calc2graph/testGraph4>\n"
					+ "FROM <file://calc2graph.data.ttl>\n"
					+ "FROM <file://calc2graph.def.ttl>\n"
					+ "{\n"
					+ "  ?s  :averageSales  ?o ,?o_SCRIPT,?o_TRACE} limit 10";


			String result = Query.runSPARQL(conn, queryString1);
			assertEquals("o=300.0;",result);
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	
	/**
	 * Ig 9.
	 */
	@Test
	@Order(9)
	void ig_9() {
		try {
			addGraph4();
			String queryString1 = "select ?s   ?o "
					+ "{\n"
					+ "  ?s  <http://inova8.com/script/scriptCode> ?o } limit 1";


			String result = Query.runSPARQL(conn, queryString1);
			assertEquals("s=http://inova8.com/calc2graph/id/calculateLatitude;o=$this.prefix(\"<http://inova8.com/calc2graph/def/>\").prefix(\"id\",\"<http://inova8.com/calc2graph/id/>\");\r\n"
					+ "return $this.getFact(\":Location@:appearsOn[eq id:Calc2Graph1]#\").getFact(\":lat\").integerValue();;",result);
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	
	/**
	 * Ig 10.
	 */
	@Test
	@Order(10)
	void ig_10() {
		try {
			String queryString1 = "select   ?o "
					+ "{\n"
					+ "  <http://inova8.com/calc2graph/id/Attribute_3>  <http://inova8.com/calc2graph/def/attribute.value> ?o }";
			String result = Query.runSPARQL(conn, queryString1);
			assertEquals("o=.5;",result);
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	
	/**
	 * Ig 11.
	 */
	@Test
	@Order(11)
	void ig_11() {
		try {
			String queryString1 = "select   ?o "
					+ "{\n"
					+ "  <http://inova8.com/calc2graph/id/BatteryLimit1>  <http://inova8.com/calc2graph/def/testProperty2> ?o }";
			String result = Query.runSPARQL(conn, queryString1);
			assertEquals("o=javax.script.ScriptException: java.lang.NumberFormatException: For input string: \\\"javax.script.ScriptException: java.lang.NumberFormatException: For input string: \\\"Circular reference encountered when evaluating <http:\\/\\/inova8.com\\/calc2graph\\/def\\/testProperty2> of <http:\\/\\/inova8.com\\/calc2graph\\/id\\/BatteryLimit1>.\\r\\n[<http:\\/\\/inova8.com\\/calc2graph\\/def\\/testProperty2> <http:\\/\\/inova8.com\\/calc2graph\\/id\\/BatteryLimit1>; queryOptions={o=\\\"$this.prefix(\\\"<http:\\/\\/inova8.com\\/calc2graph\\/def\\/>\\\"); $this.getFact(\\\":testProperty3\\\").doubleValue()\\\"^^<http:\\/\\/inova8.com\\/script\\/groovy>}\\r\\n, <http:\\/\\/inova8.com\\/calc2graph\\/def\\/testProperty3> <http:\\/\\/inova8.com\\/calc2graph\\/id\\/BatteryLimit1>; queryOptions={o=\\\"$this.prefix(\\\"<http:\\/\\/inova8.com\\/calc2graph\\/def\\/>\\\"); $this.getFact(\\\":testProperty3\\\").doubleValue()\\\"^^<http:\\/\\/inova8.com\\/script\\/groovy>}\\r\\n]\\\"\\\";",result);
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	
	/**
	 * Ig 12.
	 */
	@Test
	@Order(12)
	void ig_12() {
		try {
			String queryString1 = "CONSTRUCT {\n"
					+ "	#targetEntityIdentifier\n"
					+ "	?BatteryLimit_s <http://targetEntity> <http://inova8.com/calc2graph/def/BatteryLimit> .\n"
					+ "	?BatteryLimit_s <http://www.w3.org/1999/02/22-rdf-syntax-ns#subjectId> ?BatteryLimit_s .\n"
					+ "	#constructPath\n"
					+ "	?BatteryLimit_s ?BatteryLimit_p ?BatteryLimit_o .\n"
					+ "	#constructComplex\n"
					+ "	?BatteryLimit_s <http://inova8.com/calc2graph/def/item.attribute> ?BatteryLimititem_attribute_s .\n"
					+ "	#constructExpandSelect\n"
					+ "}\n"
					+ "WHERE {\n"
					+ "	{\n"
					+ "	#selectExpand\n"
					+ "	{	SELECT *\n"
					+ "		#selectExpandWhere\n"
					+ "		{\n"
					+ "			#selectPath\n"
					+ "			{	SELECT DISTINCT\n"
					+ "					?BatteryLimit_s\n"
					+ "				WHERE {\n"
					+ "					#clausesPath_URI1\n"
					+ "					VALUES(?class){(<http://inova8.com/calc2graph/def/BatteryLimit>)}\n"
					+ "					?BatteryLimit_s <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?class .\n"
					+ "					#search\n"
					+ "					#clausesFilter\n"
					+ "					#clausesExpandFilter\n"
					+ "					#filter\n"
					+ "				} LIMIT 152\n"
					+ "			}\n"
					+ "		}\n"
					+ "	}\n"
					+ "	#clausesPathProperties\n"
					+ "	VALUES(?BatteryLimit_p){\n"
					+ "      (<http://inova8.com/calc2graph/def/appearsOn>)\n"
					+ "   #   (<http://inova8.com/calc2graph/def/density>)(<http://inova8.com/calc2graph/def/lat>)\n"
					+ "      (<http://inova8.com/calc2graph/def/long>)\n"
					+ "     (<http://inova8.com/calc2graph/def/massFlow>)\n"
					+ "      #(<http://inova8.com/calc2graph/def/massYield>)\n"
					+ "    #  (<http://inova8.com/calc2graph/def/testProperty1>)\n"
					+ "      #(<http://inova8.com/calc2graph/def/testProperty2>)\n"
					+ "   #  (<http://inova8.com/calc2graph/def/testProperty3>)\n"
					+ "      (<http://inova8.com/calc2graph/def/testProperty4>)\n"
					+ "     # (<http://inova8.com/calc2graph/def/testProperty5>)\n"
					+ "      (<http://inova8.com/calc2graph/def/volumeFlow>)\n"
					+ "      (<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>)\n"
					+ "      (<http://www.w3.org/2000/01/rdf-schema#comment>)\n"
					+ "      (<http://www.w3.org/2000/01/rdf-schema#label>)\n"
					+ "    }\n"
					+ "		?BatteryLimit_s ?BatteryLimit_p ?BatteryLimit_o .\n"
					+ "	#clausesComplex\n"
					+ "	}\n"
					+ "	#clausesExpandSelect\n"
					+ "} LIMIT 10000000";
			String result = Query.runCONSTRUCT(conn, queryString1);
			assertEquals("(http://inova8.com/calc2graph/id/BatteryLimit1, http://targetEntity, http://inova8.com/calc2graph/def/BatteryLimit)\n"
					+ "(http://inova8.com/calc2graph/id/BatteryLimit1, http://www.w3.org/1999/02/22-rdf-syntax-ns#subjectId, http://inova8.com/calc2graph/id/BatteryLimit1)\n"
					+ "(http://inova8.com/calc2graph/id/BatteryLimit1, http://inova8.com/calc2graph/def/long, \"501\"^^<http://www.w3.org/2001/XMLSchema#integer>)\n"
					+ "(http://inova8.com/calc2graph/id/BatteryLimit1, http://inova8.com/calc2graph/def/massFlow, \"24.77999922633171\"^^<http://www.w3.org/2001/XMLSchema#double>)\n"
					+ "(http://inova8.com/calc2graph/id/BatteryLimit1, http://inova8.com/calc2graph/def/testProperty4, \"0.21670999999999999\"^^<http://www.w3.org/2001/XMLSchema#double>)\n"
					+ "(http://inova8.com/calc2graph/id/BatteryLimit1, http://inova8.com/calc2graph/def/volumeFlow, \"59\"^^<http://www.w3.org/2001/XMLSchema#int>)\n"
					+ "(http://inova8.com/calc2graph/id/BatteryLimit1, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://inova8.com/calc2graph/def/BatteryLimit)\n"
					+ "(http://inova8.com/calc2graph/id/BatteryLimit1, http://www.w3.org/2000/01/rdf-schema#label, \"BatteryLimit1\")\n"
					+ "(http://inova8.com/calc2graph/id/BatteryLimit2, http://targetEntity, http://inova8.com/calc2graph/def/BatteryLimit)\n"
					+ "(http://inova8.com/calc2graph/id/BatteryLimit2, http://www.w3.org/1999/02/22-rdf-syntax-ns#subjectId, http://inova8.com/calc2graph/id/BatteryLimit2)\n"
					+ "(http://inova8.com/calc2graph/id/BatteryLimit2, http://inova8.com/calc2graph/def/long, \"600\"^^<http://www.w3.org/2001/XMLSchema#integer>)\n"
					+ "(http://inova8.com/calc2graph/id/BatteryLimit2, http://inova8.com/calc2graph/def/massFlow, \"27.999999523162842\"^^<http://www.w3.org/2001/XMLSchema#double>)\n"
					+ "(http://inova8.com/calc2graph/id/BatteryLimit2, http://inova8.com/calc2graph/def/volumeFlow, \"40\"^^<http://www.w3.org/2001/XMLSchema#int>)\n"
					+ "(http://inova8.com/calc2graph/id/BatteryLimit2, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://inova8.com/calc2graph/def/BatteryLimit)\n"
					+ "(http://inova8.com/calc2graph/id/BatteryLimit2, http://www.w3.org/2000/01/rdf-schema#label, \"BatteryLimit2\")\n"
					+ "(http://inova8.com/calc2graph/id/BatteryLimit3, http://targetEntity, http://inova8.com/calc2graph/def/BatteryLimit)\n"
					+ "(http://inova8.com/calc2graph/id/BatteryLimit3, http://www.w3.org/1999/02/22-rdf-syntax-ns#subjectId, http://inova8.com/calc2graph/id/BatteryLimit3)\n"
					+ "(http://inova8.com/calc2graph/id/BatteryLimit3, http://inova8.com/calc2graph/def/long, \"0\"^^<http://www.w3.org/2001/XMLSchema#integer>)\n"
					+ "(http://inova8.com/calc2graph/id/BatteryLimit3, http://inova8.com/calc2graph/def/massFlow, \"10.0\"^^<http://www.w3.org/2001/XMLSchema#double>)\n"
					+ "(http://inova8.com/calc2graph/id/BatteryLimit3, http://inova8.com/calc2graph/def/volumeFlow, \"20\"^^<http://www.w3.org/2001/XMLSchema#int>)\n"
					+ "(http://inova8.com/calc2graph/id/BatteryLimit3, http://www.w3.org/1999/02/22-rdf-syntax-ns#type, http://inova8.com/calc2graph/def/BatteryLimit)\n"
					+ "(http://inova8.com/calc2graph/id/BatteryLimit3, http://www.w3.org/2000/01/rdf-schema#label, \"BatteryLimit3\")\n"
					+ "",result);
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}

	}		
	@Test
	@Order(13)
	void ig_13() {

		try {
			String queryString1 = "CONSTRUCT{<http://inova8.com/context>  <http://inova8.com/context/time>   42. ?s <http://inova8.com/calc2graph/def/testProperty6> $o   }FROM <http://default> WHERE {select ?time ?s $o {VALUES(?time){(41)} ?s <http://inova8.com/calc2graph/def/testProperty6> $o }} ";

			String result = Query.runCONSTRUCT(conn, queryString1);
			assertEquals("(http://inova8.com/context, http://inova8.com/context/time, \"42\"^^<http://www.w3.org/2001/XMLSchema#integer>)\n"
					+ "(http://inova8.com/calc2graph/id/BatteryLimit1, http://inova8.com/calc2graph/def/testProperty6, \"42\"^^<http://www.w3.org/2001/XMLSchema#integer>)\n"
					+ ""
					,result); 
		} catch (Exception e) {

			fail();
			e.printStackTrace();
		}
	}
}