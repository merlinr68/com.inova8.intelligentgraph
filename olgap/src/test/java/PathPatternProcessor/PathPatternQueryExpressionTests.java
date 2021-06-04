/*
 * inova8 2020
 */
package PathPatternProcessor;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import static org.eclipse.rdf4j.model.util.Values.iri;
import PathPattern.PathPatternLexer;
import PathPattern.PathPatternParser;
import PathPattern.PathPatternParser.PathPatternContext;
import pathCalc.Thing;
import pathPatternElement.PathElement;
import pathPatternProcessor.PathConstants;
import pathPatternProcessor.PathErrorListener;
import pathPatternProcessor.PathPatternVisitor;
import pathQL.PathParser;
import pathQLRepository.PathQLRepository;

/**
 * The Class PathPatternQueryExpressionTests.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PathPatternQueryExpressionTests {
	
	/** The thing. */
	static Thing thing;
	
	/** The source. */
	static PathQLRepository source;
	
	/** The indices. */
	static ArrayList<Integer> indices = new ArrayList<Integer>();
	
	/**
	 * Sets the up before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		source= new PathQLRepository();
		source.addReificationType(PathConstants.RDF_STATEMENT_IRI, PathConstants.RDF_SUBJECT_IRI, PathConstants.RDF_PREDICATE_IRI, PathConstants.RDF_OBJECT_IRI, null, null, null);
		source.addReificationType(iri("http://default/Attribute"), PathConstants.RDF_SUBJECT_IRI, PathConstants.RDF_PREDICATE_IRI, PathConstants.RDF_OBJECT_IRI, PathConstants.RDF_ISSUBJECTOF_IRI, PathConstants.RDF_ISPREDICATEOF_IRI, PathConstants.RDF_ISOBJECTOF_IRI);
		source.addReificationType(iri("http://default/Location"), PathConstants.RDF_SUBJECT_IRI, PathConstants.RDF_PREDICATE_IRI, PathConstants.RDF_OBJECT_IRI, null, null, null);
		source.setIsLazyLoaded(true);
		source.prefix("http://default/").prefix("local","http://local/").prefix("rdfs","http://rdfs/").prefix("id","http://id/").prefix("xsd","http://www.w3.org/2001/XMLSchema#");
		//Dummy
		thing = source.getThing( "http://",null);
		indices.add(0, 0);
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
	 * Prepare element.
	 *
	 * @param input the input
	 * @return the path element
	 * @throws RecognitionException the recognition exception
	 */
	private PathElement prepareElement(CharStream input) throws RecognitionException {
		PathPatternLexer lexer = new PathPatternLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		PathPatternParser parser = new PathPatternParser(tokens);
		PathPatternContext pathPatternTree = parser.pathPattern();
		PathPatternVisitor pathPatternVisitor = new PathPatternVisitor(thing);
		PathElement element = pathPatternVisitor.visit(pathPatternTree);
		element.indexVisitor(null, 0,null);
		return element;
	}	
	
	/**
	 * Removes the white spaces.
	 *
	 * @param input the input
	 * @return the string
	 */
	String removeWhiteSpaces(String input) {
	    //return input.replaceAll("\\s+", "");
	    return input;
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
	 * Test 05.
	 */
	@Test
	@Order(0)
	void test_05() {
		CharStream input = CharStreams.fromString( ":parent1/:parent2/:parent3");
		PathElement element = prepareElement(input);
		assertEqualsWOSpaces ("Join\r\n"
				+ "   Join\r\n"
				+ "      StatementPattern\r\n"
				+ "         Variable (name=n0)\r\n"
				+ "         Var (name=p0_1, value=http://default/parent1)\r\n"
				+ "         Variable (name=n1)\r\n"
				+ "      StatementPattern\r\n"
				+ "         Variable (name=n1)\r\n"
				+ "         Var (name=p1_2, value=http://default/parent2)\r\n"
				+ "         Variable (name=n2)\r\n"
				+ "   StatementPattern\r\n"
				+ "      Variable (name=n2)\r\n"
				+ "      Var (name=p2_3, value=http://default/parent3)\r\n"
				+ "      Variable (name=n3)\r\n"
				+ "" , element.pathPatternQuery(thing,null,null).toString());
	}


	/**
	 * Test 0.
	 */
	@Test
	@Order(0)
	void test_0() {
		CharStream input = CharStreams.fromString( ":parent1[:gender :female; :birthplace [rdfs:label 'Maidstone']]/:parent2[:gender :male]/:parent3");
		PathElement element = prepareElement(input);
		assertEqualsWOSpaces ("Join\r\n"
				+ "   Join\r\n"
				+ "      Join\r\n"
				+ "         Join\r\n"
				+ "            StatementPattern\r\n"
				+ "               Variable (name=n0)\r\n"
				+ "               Var (name=p0_1, value=http://default/parent1)\r\n"
				+ "               Variable (name=n1)\r\n"
				+ "            StatementPattern\r\n"
				+ "               Variable (name=n1)\r\n"
				+ "               Var (name=p1_0_1, value=http://default/gender)\r\n"
				+ "               Variable (name=n1_1, value=http://default/female)\r\n"
				+ "         Join\r\n"
				+ "            StatementPattern\r\n"
				+ "               Variable (name=n1)\r\n"
				+ "               Var (name=p1_0_1, value=http://default/birthplace)\r\n"
				+ "               Variable (name=n1_1)\r\n"
				+ "            StatementPattern\r\n"
				+ "               Variable (name=n1_1)\r\n"
				+ "               Var (name=p1_0_1, value=http://rdfs/label)\r\n"
				+ "               Variable (name=n1_1, value=\"Maidstone\")\r\n"
				+ "      Join\r\n"
				+ "         StatementPattern\r\n"
				+ "            Variable (name=n1)\r\n"
				+ "            Var (name=p1_2, value=http://default/parent2)\r\n"
				+ "            Variable (name=n2)\r\n"
				+ "         StatementPattern\r\n"
				+ "            Variable (name=n2)\r\n"
				+ "            Var (name=p2_0_1, value=http://default/gender)\r\n"
				+ "            Variable (name=n2_1, value=http://default/male)\r\n"
				+ "   StatementPattern\r\n"
				+ "      Variable (name=n2)\r\n"
				+ "      Var (name=p2_3, value=http://default/parent3)\r\n"
				+ "      Variable (name=n3)\r\n"
				+ ""
				 , element.pathPatternQuery(thing,null,null).toString());
	}

	/**
	 * Test 1.
	 */
	@Test
	@Order(1)
	void test_1() {
		CharStream input = CharStreams.fromString( "^:Attribute@:volumeFlow");
		PathElement element = prepareElement(input);
		assertEqualsWOSpaces ("Join\r\n"
				+ "   Join\r\n"
				+ "      Union\r\n"
				+ "         StatementPattern\r\n"
				+ "            Variable (name=r1)\r\n"
				+ "            Var (name=object1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#object)\r\n"
				+ "            Variable (name=n0)\r\n"
				+ "         StatementPattern\r\n"
				+ "            Variable (name=n0)\r\n"
				+ "            Var (name=isObjectOf1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#object)\r\n"
				+ "            Variable (name=r1)\r\n"
				+ "      Union\r\n"
				+ "         StatementPattern\r\n"
				+ "            Variable (name=r1)\r\n"
				+ "            Var (name=property1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate)\r\n"
				+ "            Variable (name=p0_1, value=http://default/volumeFlow)\r\n"
				+ "         StatementPattern\r\n"
				+ "            Variable (name=p0_1, value=http://default/volumeFlow)\r\n"
				+ "            Var (name=isPropertyOf1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate)\r\n"
				+ "            Variable (name=r1)\r\n"
				+ "   Union\r\n"
				+ "      StatementPattern\r\n"
				+ "         Variable (name=r1)\r\n"
				+ "         Var (name=subject1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#subject)\r\n"
				+ "         Variable (name=n1)\r\n"
				+ "      StatementPattern\r\n"
				+ "         Variable (name=n1)\r\n"
				+ "         Var (name=isSubjectOf1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#subject)\r\n"
				+ "         Variable (name=r1)\r\n"
				+ "" ,element.pathPatternQuery(thing,null,null).toString());
	}
	
	/**
	 * Test 2.
	 */
	@Test
	@Order(2)
	void test_2() {
		CharStream input = CharStreams.fromString( "<http://local#volumeFlow>");
		PathElement element = prepareElement(input);
		assertEqualsWOSpaces ("StatementPattern\r\n"
				+ "   Variable (name=n0)\r\n"
				+ "   Var (name=p0_1, value=http://local#volumeFlow)\r\n"
				+ "   Variable (name=n1)\r\n"
				+ "" ,element.pathPatternQuery(thing,null,null).toString());
	}
	
	/**
	 * Test 3.
	 */
	@Test
	@Order(3)
	void test_3() {
		CharStream input = CharStreams.fromString( "^:hasProductBatteryLimit>:massThroughput");
		PathElement element = prepareElement(input);
		assertEqualsWOSpaces ("Join\r\n"
				+ "   StatementPattern\r\n"
				+ "      Variable (name=n1)\r\n"
				+ "      Var (name=p0_1, value=http://default/hasProductBatteryLimit)\r\n"
				+ "      Variable (name=n0)\r\n"
				+ "   StatementPattern\r\n"
				+ "      Variable (name=n1)\r\n"
				+ "      Var (name=p1_2, value=http://default/massThroughput)\r\n"
				+ "      Variable (name=n2)\r\n"
				+ "" ,element.pathPatternQuery(thing,null,null).toString());
	}
	
	/**
	 * Test 4.
	 */
	@Test
	@Order(4)
	void test_4() {
		CharStream input = CharStreams.fromString( ":volumeFlow [ gt \"35\" ]");
		PathElement element = prepareElement(input);
		assertEqualsWOSpaces ("Filter\r\n"
				+ "   Compare (>)\r\n"
				+ "      Variable (name=n1)\r\n"
				+ "      ValueConstant (value=\"35\")\r\n"
				+ "   StatementPattern\r\n"
				+ "      Variable (name=n0)\r\n"
				+ "      Var (name=p0_1, value=http://default/volumeFlow)\r\n"
				+ "      Variable (name=n1)\r\n"
				+ "" ,element.pathPatternQuery(thing,null,null).toString());
	}
	
	/**
	 * Test 5.
	 */
	@Test
	@Order(5)
	void test_5() {
		CharStream input = CharStreams.fromString( ":Location@:appearsOn[ rdfs:label \"eastman3d\" ]#/:lat");
		PathElement element = prepareElement(input);
		assertEqualsWOSpaces ("Join\r\n"
				+ "   Join\r\n"
				+ "      Join\r\n"
				+ "         Join\r\n"
				+ "            StatementPattern\r\n"
				+ "               Variable (name=r1)\r\n"
				+ "               Var (name=subject1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#subject)\r\n"
				+ "               Variable (name=n0)\r\n"
				+ "            StatementPattern\r\n"
				+ "               Variable (name=r1)\r\n"
				+ "               Var (name=property1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate)\r\n"
				+ "               Variable (name=p0_1, value=http://default/appearsOn)\r\n"
				+ "         StatementPattern\r\n"
				+ "            Variable (name=r1)\r\n"
				+ "            Var (name=object1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#object)\r\n"
				+ "            Variable (name=n1)\r\n"
				+ "      StatementPattern\r\n"
				+ "         Variable (name=n1)\r\n"
				+ "         Var (name=p1_0_1, value=http://rdfs/label)\r\n"
				+ "         Variable (name=n1_1, value=\"eastman3d\")\r\n"
				+ "   StatementPattern\r\n"
				+ "      Variable (name=r1)\r\n"
				+ "      Var (name=p1_2, value=http://default/lat)\r\n"
				+ "      Variable (name=n2)\r\n"
				+ "" ,element.pathPatternQuery(thing,null,null).toString());
	}
	
	/**
	 * Test 6.
	 */
	@Test
	@Order(6)
	void test_6() {
		CharStream input = CharStreams.fromString( ":Location@:appearsOn[ eq [ rdfs:label \"Calc2Graph1\"] ]#/^:lat/:long/^:left/:right");
		PathElement element = prepareElement(input);
		assertEqualsWOSpaces ("Join\r\n"
				+ "   Join\r\n"
				+ "      Join\r\n"
				+ "         Join\r\n"
				+ "            Join\r\n"
				+ "               Join\r\n"
				+ "                  StatementPattern\r\n"
				+ "                     Variable (name=r1)\r\n"
				+ "                     Var (name=subject1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#subject)\r\n"
				+ "                     Variable (name=n0)\r\n"
				+ "                  StatementPattern\r\n"
				+ "                     Variable (name=r1)\r\n"
				+ "                     Var (name=property1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate)\r\n"
				+ "                     Variable (name=p0_1, value=http://default/appearsOn)\r\n"
				+ "               StatementPattern\r\n"
				+ "                  Variable (name=r1)\r\n"
				+ "                  Var (name=object1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#object)\r\n"
				+ "                  Variable (name=n1)\r\n"
				+ "            StatementPattern\r\n"
				+ "               Variable (name=n2)\r\n"
				+ "               Var (name=p1_2, value=http://default/lat)\r\n"
				+ "               Variable (name=r1)\r\n"
				+ "         StatementPattern\r\n"
				+ "            Variable (name=n2)\r\n"
				+ "            Var (name=p2_3, value=http://default/long)\r\n"
				+ "            Variable (name=n3)\r\n"
				+ "      StatementPattern\r\n"
				+ "         Variable (name=n4)\r\n"
				+ "         Var (name=p3_4, value=http://default/left)\r\n"
				+ "         Variable (name=n3)\r\n"
				+ "   StatementPattern\r\n"
				+ "      Variable (name=n4)\r\n"
				+ "      Var (name=p4_5, value=http://default/right)\r\n"
				+ "      Variable (name=n5)\r\n"
				+ "" ,element.pathPatternQuery(thing,null,null).toString());
	}
	
	/**
	 * Test 7.
	 */
	@Test
	@Order(7)
	void test_7() {
		CharStream input = CharStreams.fromString( ":volumeFlow [ eq \"36\" ; gt \"35\" ; rdfs:label \"Calc2Graph1\" ; eq [ rdfs:label \"Calc2Graph2\"] , :Calc2Graph3 ,\"Calc2Graph4\" ]");
		PathElement element = prepareElement(input);
		assertEqualsWOSpaces ("Join\r\n"
				+ "   Filter\r\n"
				+ "      Compare (>)\r\n"
				+ "         Variable (name=n1, value=\"36\")\r\n"
				+ "         ValueConstant (value=\"35\")\r\n"
				+ "      StatementPattern\r\n"
				+ "         Variable (name=n0)\r\n"
				+ "         Var (name=p0_1, value=http://default/volumeFlow)\r\n"
				+ "         Variable (name=n1, value=\"36\")\r\n"
				+ "   StatementPattern\r\n"
				+ "      Variable (name=n1, value=\"36\")\r\n"
				+ "      Var (name=p1_0_1, value=http://rdfs/label)\r\n"
				+ "      Variable (name=n1_1, value=\"Calc2Graph1\")\r\n"
				+ "" ,element.pathPatternQuery(thing,null,null).toString());
	}
	
	/**
	 * Test 8.
	 */
	@Test
	@Order(8)
	void test_8() {
		CharStream input = CharStreams.fromString( ":Location@:appearsOn[ rdfs:label \"eastman3d\" ]#[a :Location ]/:lat");
		PathElement element = prepareElement(input);
		assertEqualsWOSpaces ("Join\r\n"
				+ "   Join\r\n"
				+ "      Join\r\n"
				+ "         Join\r\n"
				+ "            Join\r\n"
				+ "               StatementPattern\r\n"
				+ "                  Variable (name=r1)\r\n"
				+ "                  Var (name=subject1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#subject)\r\n"
				+ "                  Variable (name=n0)\r\n"
				+ "               StatementPattern\r\n"
				+ "                  Variable (name=r1)\r\n"
				+ "                  Var (name=property1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate)\r\n"
				+ "                  Variable (name=p0_1, value=http://default/appearsOn)\r\n"
				+ "            StatementPattern\r\n"
				+ "               Variable (name=r1)\r\n"
				+ "               Var (name=object1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#object)\r\n"
				+ "               Variable (name=n1)\r\n"
				+ "         StatementPattern\r\n"
				+ "            Variable (name=n1)\r\n"
				+ "            Var (name=p1_0_1, value=http://rdfs/label)\r\n"
				+ "            Variable (name=n1_1, value=\"eastman3d\")\r\n"
				+ "      StatementPattern\r\n"
				+ "         Variable (name=r1)\r\n"
				+ "         Var (name=p0_0_1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#type)\r\n"
				+ "         Variable (name=n0_1, value=http://default/Location)\r\n"
				+ "   StatementPattern\r\n"
				+ "      Variable (name=r1)\r\n"
				+ "      Var (name=p1_2, value=http://default/lat)\r\n"
				+ "      Variable (name=n2)\r\n"
				+ "" ,element.pathPatternQuery(thing,null,null).toString());
	}
	
	/**
	 * Test 9.
	 */
	@Test
	@Order(9)
	void test_9() {
		CharStream input = CharStreams.fromString( ":Location@:appearsOn#[:location.Map  id:Calc2Graph2 ]/:long");
		PathElement element = prepareElement(input);
		assertEqualsWOSpaces ("Join\r\n"
				+ "   Join\r\n"
				+ "      Join\r\n"
				+ "         Join\r\n"
				+ "            StatementPattern\r\n"
				+ "               Variable (name=r1)\r\n"
				+ "               Var (name=subject1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#subject)\r\n"
				+ "               Variable (name=n0)\r\n"
				+ "            StatementPattern\r\n"
				+ "               Variable (name=r1)\r\n"
				+ "               Var (name=property1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate)\r\n"
				+ "               Variable (name=p0_1, value=http://default/appearsOn)\r\n"
				+ "         StatementPattern\r\n"
				+ "            Variable (name=r1)\r\n"
				+ "            Var (name=object1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#object)\r\n"
				+ "            Variable (name=n1)\r\n"
				+ "      StatementPattern\r\n"
				+ "         Variable (name=r1)\r\n"
				+ "         Var (name=p0_0_1, value=http://default/location.Map)\r\n"
				+ "         Variable (name=n0_1, value=http://id/Calc2Graph2)\r\n"
				+ "   StatementPattern\r\n"
				+ "      Variable (name=r1)\r\n"
				+ "      Var (name=p1_2, value=http://default/long)\r\n"
				+ "      Variable (name=n2)\r\n"
				+ "" ,element.pathPatternQuery(thing,null,null).toString());
	}
	
	/**
	 * Test 10.
	 */
	@Test
	@Order(10)
	void test_10() {
		CharStream input = CharStreams.fromString( ":Location@:appearsOn[eq [ rdfs:label 'Calc2Graph1']]#/:lat");
		PathElement element = prepareElement(input);
		assertEqualsWOSpaces ("Join\r\n"
				+ "   Join\r\n"
				+ "      Join\r\n"
				+ "         StatementPattern\r\n"
				+ "            Variable (name=r1)\r\n"
				+ "            Var (name=subject1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#subject)\r\n"
				+ "            Variable (name=n0)\r\n"
				+ "         StatementPattern\r\n"
				+ "            Variable (name=r1)\r\n"
				+ "            Var (name=property1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate)\r\n"
				+ "            Variable (name=p0_1, value=http://default/appearsOn)\r\n"
				+ "      StatementPattern\r\n"
				+ "         Variable (name=r1)\r\n"
				+ "         Var (name=object1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#object)\r\n"
				+ "         Variable (name=n1)\r\n"
				+ "   StatementPattern\r\n"
				+ "      Variable (name=r1)\r\n"
				+ "      Var (name=p1_2, value=http://default/lat)\r\n"
				+ "      Variable (name=n2)\r\n"
				+ "" ,element.pathPatternQuery(thing,null,null).toString());
	}
	
	/**
	 * Test 11.
	 */
	@Test 
	@Order(11)
	void test_11() {
		try {
			String expression = ":Location@:appearsOn[eq id:Calc2Graph2]#";
			CharStream input = CharStreams.fromString(expression);
			PathPatternLexer lexer = new PathPatternLexer(input);
			 PathErrorListener errorListener = new PathErrorListener(expression);
			lexer.removeErrorListeners(); 
			lexer.addErrorListener(errorListener); 
			CommonTokenStream tokens = new CommonTokenStream(lexer);
			PathPatternParser parser = new PathPatternParser(tokens);
			parser.removeErrorListeners(); 
			parser.addErrorListener(errorListener); 
			PathPatternContext pathPatternTree = parser.pathPattern();
			PathPatternVisitor pathPatternVisitor = new PathPatternVisitor(thing);
			PathElement element = pathPatternVisitor.visit(pathPatternTree); 
			assertEqualsWOSpaces ("Join\r\n"
					+ "   Join\r\n"
					+ "      StatementPattern\r\n"
					+ "         Variable (name=rnull)\r\n"
					+ "         Var (name=subjectnull, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#subject)\r\n"
					+ "         Variable (name=nnull)\r\n"
					+ "      StatementPattern\r\n"
					+ "         Variable (name=rnull)\r\n"
					+ "         Var (name=propertynull, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate)\r\n"
					+ "         Variable (name=pnull_null, value=http://default/appearsOn)\r\n"
					+ "   StatementPattern\r\n"
					+ "      Variable (name=rnull)\r\n"
					+ "      Var (name=objectnull, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#object)\r\n"
					+ "      Variable (name=rnull, value=http://id/Calc2Graph2)\r\n"
					+ "" ,element.pathPatternQuery(thing,null,null).toString());
		}catch(Exception e){
			assertEqualsWOSpaces ("<http://default/Location>@<http://default/appearsOn>[eq <http://id/Calc2Graph2> ;]#","" );
		}
	}
//	@Test 
//	@Order(12)
//	void test_12() {
//		try {
//			
//			PathProcessor2 pathProcessor = new PathProcessor2();
//			PathElement element = PathProcessor2.parsePathPattern(thing, ":Location@:appearsOn][eq id:Calc2Graph2]#");
//		}catch(Exception e){
//			assertEqualsWOSpaces ("[line 1:20 in \":Location@:appearsOn][eq id:Calc2Graph2]#\": mismatched input ']' expecting {<EOF>, '|', '/', '{', '[', '#'}]"
//					,e.getMessage() );
//		}
/**
 * Test 13.
 */
//	}
	@Test 
	@Order(13)
	void test_13() {
		try {

			PathElement element = PathParser.parsePathPattern(thing, ":Location@:appearsOn[eq id:Calc2Graph1, id:Calc2Graph2]#");
			assertEqualsWOSpaces ("Join\r\n"
					+ "   Join\r\n"
					+ "      StatementPattern\r\n"
					+ "         Variable (name=r1)\r\n"
					+ "         Var (name=subject1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#subject)\r\n"
					+ "         Variable (name=n0)\r\n"
					+ "      StatementPattern\r\n"
					+ "         Variable (name=r1)\r\n"
					+ "         Var (name=property1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate)\r\n"
					+ "         Variable (name=p0_1, value=http://default/appearsOn)\r\n"
					+ "   Join\r\n"
					+ "      StatementPattern\r\n"
					+ "         Variable (name=r1)\r\n"
					+ "         Var (name=object1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#object)\r\n"
					+ "         Variable (name=r1, value=http://id/Calc2Graph1)\r\n"
					+ "      StatementPattern\r\n"
					+ "         Variable (name=r1)\r\n"
					+ "         Var (name=object1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#object)\r\n"
					+ "         Variable (name=r1, value=http://id/Calc2Graph2)\r\n"
					+ "" ,element.pathPatternQuery(thing,null,null).toString());
		}catch(Exception e){
			fail();
		}
	}

/**
 * Test 14.
 */
@Test 
@Order(14)
void test_14() {
	try {

		PathElement element = PathParser.parsePathPattern(thing, "^:hasProductBatteryLimit{1, 42}/:massThroughput");
		assertEqualsWOSpaces ("Join\r\n"
				+ "   StatementPattern\r\n"
				+ "      Variable (name=n1)\r\n"
				+ "      Var (name=p0_1, value=http://default/hasProductBatteryLimit)\r\n"
				+ "      Variable (name=n0)\r\n"
				+ "   StatementPattern\r\n"
				+ "      Variable (name=n1)\r\n"
				+ "      Var (name=p1_2, value=http://default/massThroughput)\r\n"
				+ "      Variable (name=n2)\r\n"
				+ "" ,element.pathPatternQuery(thing,null,null).toString());
	}catch(Exception e){
		fail();
	}
}

/**
 * Test 15.
 */
@Test 
@Order(15)
void test_15() {
	try {

		PathElement element = PathParser.parsePathPattern(thing, "^:hasProductBatteryLimit{1,}/:massThroughput");
		assertEqualsWOSpaces ("Join\r\n"
				+ "   StatementPattern\r\n"
				+ "      Variable (name=n1)\r\n"
				+ "      Var (name=p0_1, value=http://default/hasProductBatteryLimit)\r\n"
				+ "      Variable (name=n0)\r\n"
				+ "   StatementPattern\r\n"
				+ "      Variable (name=n1)\r\n"
				+ "      Var (name=p1_2, value=http://default/massThroughput)\r\n"
				+ "      Variable (name=n2)\r\n"
				+ "" ,element.pathPatternQuery(thing,null,null).toString());
	}catch(Exception e){
		fail();
	}
}

/**
 * Test 16.
 */
@Test 
@Order(16)
void test_16() {
	try {
		PathElement element = PathParser.parsePathPattern(thing, "(^:hasProductBatteryLimit/:massThroughput){1,2}");
		assertEqualsWOSpaces ("Join\r\n"
				+ "   StatementPattern\r\n"
				+ "      Variable (name=n1)\r\n"
				+ "      Var (name=p0_1, value=http://default/hasProductBatteryLimit)\r\n"
				+ "      Variable (name=n0)\r\n"
				+ "   StatementPattern\r\n"
				+ "      Variable (name=n1)\r\n"
				+ "      Var (name=p1_2, value=http://default/massThroughput)\r\n"
				+ "      Variable (name=n2)\r\n"
				+ "" ,element.pathPatternQuery(thing,null,null).toString());
	}catch(Exception e){
		fail();
	}
}

/**
 * Test 17.
 */
@Test 
@Order(17)
void test_17() {
	try {

		PathElement element = PathParser.parsePathPattern(thing, "(^:hasProductBatteryLimit/:massThroughput){1, 2}/:massThroughput");
		assertEqualsWOSpaces ("Join\r\n"
				+ "   Join\r\n"
				+ "      StatementPattern\r\n"
				+ "         Variable (name=n1)\r\n"
				+ "         Var (name=p0_1, value=http://default/hasProductBatteryLimit)\r\n"
				+ "         Variable (name=n0)\r\n"
				+ "      StatementPattern\r\n"
				+ "         Variable (name=n1)\r\n"
				+ "         Var (name=p1_2, value=http://default/massThroughput)\r\n"
				+ "         Variable (name=n2)\r\n"
				+ "   StatementPattern\r\n"
				+ "      Variable (name=n2)\r\n"
				+ "      Var (name=p2_3, value=http://default/massThroughput)\r\n"
				+ "      Variable (name=n3)\r\n"
				+ "" ,element.pathPatternQuery(thing,null,null).toString());
	}catch(Exception e){
		fail();
	}
}

/**
 * Test 18.
 */
@Test 
@Order(18)
void test_18() {
	try {

		PathElement element = PathParser.parsePathPattern(thing, "*");
		assertEqualsWOSpaces ("StatementPattern\r\n"
				+ "   Variable (name=n0)\r\n"
				+ "   Var (name=p0_1)\r\n"
				+ "   Variable (name=n1)\r\n"
				+ "" ,element.pathPatternQuery(thing,null,null).toString());
	}catch(Exception e){
		fail();
	}
}
/**
 * Test 19.
 */
@Test 
@Order(19)
void test_19() {
	try {

		PathElement element = PathParser.parsePathPattern(thing, "(^:hasProductBatteryLimit/:massThroughput){1, 2}/*");
		assertEqualsWOSpaces ("Join\r\n"
				+ "   Join\r\n"
				+ "      StatementPattern\r\n"
				+ "         Variable (name=n1)\r\n"
				+ "         Var (name=p0_1, value=http://default/hasProductBatteryLimit)\r\n"
				+ "         Variable (name=n0)\r\n"
				+ "      StatementPattern\r\n"
				+ "         Variable (name=n1)\r\n"
				+ "         Var (name=p1_2, value=http://default/massThroughput)\r\n"
				+ "         Variable (name=n2)\r\n"
				+ "   StatementPattern\r\n"
				+ "      Variable (name=n2)\r\n"
				+ "      Var (name=p2_3)\r\n"
				+ "      Variable (name=n3)\r\n"
				+ "" ,element.pathPatternQuery(thing,null,null).toString());
	}catch(Exception e){
		fail();
	}
}

/**
 * Test 20.
 */
@Test 
@Order(20)
void test_20() {
	try {

		PathElement element = PathParser.parsePathPattern(thing, "(^:hasProductBatteryLimit/*){1, 2}/:massThroughput");
		assertEqualsWOSpaces ("Join\r\n"
				+ "   Join\r\n"
				+ "      StatementPattern\r\n"
				+ "         Variable (name=n1)\r\n"
				+ "         Var (name=p0_1, value=http://default/hasProductBatteryLimit)\r\n"
				+ "         Variable (name=n0)\r\n"
				+ "      StatementPattern\r\n"
				+ "         Variable (name=n1)\r\n"
				+ "         Var (name=p1_2)\r\n"
				+ "         Variable (name=n2)\r\n"
				+ "   StatementPattern\r\n"
				+ "      Variable (name=n2)\r\n"
				+ "      Var (name=p2_3, value=http://default/massThroughput)\r\n"
				+ "      Variable (name=n3)\r\n"
				+ "" ,element.pathPatternQuery(thing,null,null).toString());
	}catch(Exception e){
		fail();
	}
}

/**
 * Test 21.
 */
@Test 
@Order(21)
void test_21() {
	try {

		PathElement element = PathParser.parsePathPattern(thing, "(*){1, 2}/:massThroughput");

		assertEqualsWOSpaces ("Join\r\n"
				+ "   StatementPattern\r\n"
				+ "      Variable (name=n0)\r\n"
				+ "      Var (name=p0_1)\r\n"
				+ "      Variable (name=n1)\r\n"
				+ "   StatementPattern\r\n"
				+ "      Variable (name=n1)\r\n"
				+ "      Var (name=p1_2, value=http://default/massThroughput)\r\n"
				+ "      Variable (name=n2)\r\n"
				+ "" ,element.pathPatternQuery(thing,null,null).toString());
	}catch(Exception e){
		fail();
	}
}

/**
 * Test 22.
 */
@Test 
@Order(22)
void test_22() {
	try {

		PathElement element = PathParser.parsePathPattern(thing, "^:hasProductBatteryLimit/*");

		assertEqualsWOSpaces ("Join\r\n"
				+ "   StatementPattern\r\n"
				+ "      Variable (name=n1)\r\n"
				+ "      Var (name=p0_1, value=http://default/hasProductBatteryLimit)\r\n"
				+ "      Variable (name=n0)\r\n"
				+ "   StatementPattern\r\n"
				+ "      Variable (name=n1)\r\n"
				+ "      Var (name=p1_2)\r\n"
				+ "      Variable (name=n2)\r\n"
				+ "" ,element.pathPatternQuery(thing,null,null).toString());
	}catch(Exception e){
		fail();
	}
}

/**
 * Test 23.
 */
@Test 
@Order(23)
void test_23() {
	try {

		PathElement element = PathParser.parsePathPattern(thing, "^:hasProductBatteryLimit/(:massFlow |:volumeFlow)");

		assertEqualsWOSpaces ("Join\r\n"
				+ "   StatementPattern\r\n"
				+ "      Variable (name=n1)\r\n"
				+ "      Var (name=p0_1, value=http://default/hasProductBatteryLimit)\r\n"
				+ "      Variable (name=n0)\r\n"
				+ "   Union\r\n"
				+ "      StatementPattern\r\n"
				+ "         Variable (name=n1)\r\n"
				+ "         Var (name=p1_2, value=http://default/massFlow)\r\n"
				+ "         Variable (name=n2)\r\n"
				+ "      StatementPattern\r\n"
				+ "         Variable (name=n1)\r\n"
				+ "         Var (name=p1_2, value=http://default/volumeFlow)\r\n"
				+ "         Variable (name=n2)\r\n"
				+ "" ,element.pathPatternQuery(thing,null,null).toString());
	}catch(Exception e){
		fail();
	}
}

/**
 * Test 24.
 */
@Test 
@Order(24)
void test_24() {
	try {

		PathElement element = PathParser.parsePathPattern(thing, "^:hasProductBatteryLimit/(:massFlow |:volumeFlow  |:density)");
		assertEqualsWOSpaces ("Join\r\n"
				+ "   StatementPattern\r\n"
				+ "      Variable (name=n1)\r\n"
				+ "      Var (name=p0_1, value=http://default/hasProductBatteryLimit)\r\n"
				+ "      Variable (name=n0)\r\n"
				+ "   Union\r\n"
				+ "      Union\r\n"
				+ "         StatementPattern\r\n"
				+ "            Variable (name=n1)\r\n"
				+ "            Var (name=p1_2, value=http://default/massFlow)\r\n"
				+ "            Variable (name=n2)\r\n"
				+ "         StatementPattern\r\n"
				+ "            Variable (name=n1)\r\n"
				+ "            Var (name=p1_2, value=http://default/volumeFlow)\r\n"
				+ "            Variable (name=n2)\r\n"
				+ "      StatementPattern\r\n"
				+ "         Variable (name=n1)\r\n"
				+ "         Var (name=p1_2, value=http://default/density)\r\n"
				+ "         Variable (name=n2)\r\n"
				+ "" ,element.pathPatternQuery(thing,null,null).toString());
	}catch(Exception e){
		fail();
	}
}

/**
 * Test 25.
 */
@Test 
@Order(25)
void test_25() {
	try {
		
		PathElement element = PathParser.parsePathPattern(thing, "^:hasProductBatteryLimit/(:temp | (:massFlow |! :volumeFlow  |! :density))");
		assertEqualsWOSpaces ("Join\r\n"
				+ "   StatementPattern\r\n"
				+ "      Variable (name=n1)\r\n"
				+ "      Var (name=p0_1, value=http://default/hasProductBatteryLimit)\r\n"
				+ "      Variable (name=n0)\r\n"
				+ "   Union\r\n"
				+ "      StatementPattern\r\n"
				+ "         Variable (name=n1)\r\n"
				+ "         Var (name=p1_2, value=http://default/temp)\r\n"
				+ "         Variable (name=n2)\r\n"
				+ "      Union\r\n"
				+ "         Union\r\n"
				+ "            StatementPattern\r\n"
				+ "               Variable (name=n1)\r\n"
				+ "               Var (name=p1_2, value=http://default/massFlow)\r\n"
				+ "               Variable (name=n2)\r\n"
				+ "            StatementPattern\r\n"
				+ "               Variable (name=n1)\r\n"
				+ "               Var (name=p1_2)\r\n"
				+ "               Variable (name=n2)\r\n"
				+ "         StatementPattern\r\n"
				+ "            Variable (name=n1)\r\n"
				+ "            Var (name=p1_2)\r\n"
				+ "            Variable (name=n2)\r\n"
				+ "" ,element.pathPatternQuery(thing,null,null).toString());
	}catch(Exception e){
		fail();
	}
}

/**
 * Test 26.
 */
@Test 
@Order(26)
void test_26() {
	try {
		
		PathElement element = PathParser.parsePathPattern(thing, "^:hasProductBatteryLimit/(* | !(:massFlow |:volumeFlow  |:density))");
		assertEqualsWOSpaces ("Join\r\n"
				+ "   StatementPattern\r\n"
				+ "      Variable (name=n1)\r\n"
				+ "      Var (name=p0_1, value=http://default/hasProductBatteryLimit)\r\n"
				+ "      Variable (name=n0)\r\n"
				+ "   Union\r\n"
				+ "      StatementPattern\r\n"
				+ "         Variable (name=n1)\r\n"
				+ "         Var (name=p1_2)\r\n"
				+ "         Variable (name=n2)\r\n"
				+ "      Union\r\n"
				+ "         Union\r\n"
				+ "            StatementPattern\r\n"
				+ "               Variable (name=n1)\r\n"
				+ "               Var (name=p1_2, value=http://default/massFlow)\r\n"
				+ "               Variable (name=n2)\r\n"
				+ "            StatementPattern\r\n"
				+ "               Variable (name=n1)\r\n"
				+ "               Var (name=p1_2, value=http://default/volumeFlow)\r\n"
				+ "               Variable (name=n2)\r\n"
				+ "         StatementPattern\r\n"
				+ "            Variable (name=n1)\r\n"
				+ "            Var (name=p1_2, value=http://default/density)\r\n"
				+ "            Variable (name=n2)\r\n"
				+ "" ,element.pathPatternQuery(thing,null,null).toString());
	}catch(Exception e){
		fail();
	}
}

/**
 * Test 27.
 */
@Test 
@Order(27)
void test_27() {
	try {

		PathElement element = PathParser.parsePathPattern(thing, "(* | !^:hasProductBatteryLimit)/(* | !(:massFlow |:volumeFlow  |:density))");
		assertEqualsWOSpaces ("Join\r\n"
				+ "   Union\r\n"
				+ "      StatementPattern\r\n"
				+ "         Variable (name=n0)\r\n"
				+ "         Var (name=p0_1)\r\n"
				+ "         Variable (name=n1)\r\n"
				+ "      Filter\r\n"
				+ "         Compare (!=)\r\n"
				+ "            Var (name=p0_1)\r\n"
				+ "            Var (name=p2, value=http://default/hasProductBatteryLimit)\r\n"
				+ "         StatementPattern\r\n"
				+ "            Variable (name=n1)\r\n"
				+ "            Var (name=p0_1)\r\n"
				+ "            Variable (name=n0)\r\n"
				+ "   Union\r\n"
				+ "      StatementPattern\r\n"
				+ "         Variable (name=n1)\r\n"
				+ "         Var (name=p1_2)\r\n"
				+ "         Variable (name=n2)\r\n"
				+ "      Union\r\n"
				+ "         Union\r\n"
				+ "            StatementPattern\r\n"
				+ "               Variable (name=n1)\r\n"
				+ "               Var (name=p1_2, value=http://default/massFlow)\r\n"
				+ "               Variable (name=n2)\r\n"
				+ "            StatementPattern\r\n"
				+ "               Variable (name=n1)\r\n"
				+ "               Var (name=p1_2, value=http://default/volumeFlow)\r\n"
				+ "               Variable (name=n2)\r\n"
				+ "         StatementPattern\r\n"
				+ "            Variable (name=n1)\r\n"
				+ "            Var (name=p1_2, value=http://default/density)\r\n"
				+ "            Variable (name=n2)\r\n"
				+ "" ,element.pathPatternQuery(thing,null,null).toString());
	}catch(Exception e){
		fail();
	}
}

/**
 * Test 28.
 */
@Test 
@Order(28)
void test_28() {
	try {

		PathElement element = PathParser.parsePathPattern(thing, "(:Attribute@:density  |:density)");
		assertEqualsWOSpaces ("Union\r\n"
				+ "   Join\r\n"
				+ "      Join\r\n"
				+ "         Union\r\n"
				+ "            StatementPattern\r\n"
				+ "               Variable (name=r1)\r\n"
				+ "               Var (name=subject1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#subject)\r\n"
				+ "               Variable (name=n0)\r\n"
				+ "            StatementPattern\r\n"
				+ "               Variable (name=n0)\r\n"
				+ "               Var (name=isSubjectOf1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#subject)\r\n"
				+ "               Variable (name=r1)\r\n"
				+ "         Union\r\n"
				+ "            StatementPattern\r\n"
				+ "               Variable (name=r1)\r\n"
				+ "               Var (name=property1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate)\r\n"
				+ "               Variable (name=p0_1, value=http://default/density)\r\n"
				+ "            StatementPattern\r\n"
				+ "               Variable (name=p0_1, value=http://default/density)\r\n"
				+ "               Var (name=isPropertyOf1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate)\r\n"
				+ "               Variable (name=r1)\r\n"
				+ "      Union\r\n"
				+ "         StatementPattern\r\n"
				+ "            Variable (name=r1)\r\n"
				+ "            Var (name=object1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#object)\r\n"
				+ "            Variable (name=n1)\r\n"
				+ "         StatementPattern\r\n"
				+ "            Variable (name=n1)\r\n"
				+ "            Var (name=isObjectOf1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#object)\r\n"
				+ "            Variable (name=r1)\r\n"
				+ "   StatementPattern\r\n"
				+ "      Variable (name=n0)\r\n"
				+ "      Var (name=p0_1, value=http://default/density)\r\n"
				+ "      Variable (name=n1)\r\n"
				+ "" ,element.pathPatternQuery(thing,null,null).toString());
	}catch(Exception e){
		fail();
	}
}

/**
 * Test 29.
 */
@Test 
@Order(29)
void test_29() {
	try {

		PathElement element = PathParser.parsePathPattern(thing, "[ eq :Unit1]/:hasProductBatteryLimit");
		assertEqualsWOSpaces ("StatementPattern\r\n"
				+ "   Variable (name=n0)\r\n"
				+ "   Var (name=p0_1, value=http://default/hasProductBatteryLimit)\r\n"
				+ "   Variable (name=n1)\r\n"
				+ "" 
				,element.pathPatternQuery(thing,null,null).toString());
		assertEqualsWOSpaces ("BIND(<http://default/Unit1> as ?n0)\n"	+ "" , element.getLeftPathElement().toSPARQL());
	}catch(Exception e){
		fail();
	}
}

/**
 * Test 30.
 */
@Test 
@Order(30)
void test_30() {
	try {

		PathElement element = PathParser.parsePathPattern(thing, "[ a :Unit]/:hasProductBatteryLimit");
		assertEqualsWOSpaces ("StatementPattern\r\n"
				+ "   Variable (name=n0)\r\n"
				+ "   Var (name=p0_1, value=http://default/hasProductBatteryLimit)\r\n"
				+ "   Variable (name=n1)\r\n"
				+ "" ,element.pathPatternQuery(thing,null,null).toString());
		assertEqualsWOSpaces ("?n0 <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://default/Unit> .\n"
				+ "" , element.getLeftPathElement().toSPARQL());
	}catch(Exception e){
		fail();
	}
}

/**
 * Test 31.
 */
@Test 
@Order(31)
void test_31() {
	try {

		PathElement element = PathParser.parsePathPattern(thing, "[ like \"Unit1\"]/:hasProductBatteryLimit");
		assertEqualsWOSpaces ("StatementPattern\r\n"
				+ "   Variable (name=n0)\r\n"
				+ "   Var (name=p0_1, value=http://default/hasProductBatteryLimit)\r\n"
				+ "   Variable (name=n1)\r\n"
				+ "" ,element.pathPatternQuery(thing,null,null).toString());
		assertEqualsWOSpaces ("?n0 <http://www.openrdf.org/contrib/lucenesail#matches> [<http://www.openrdf.org/contrib/lucenesail#query> 'Unit1'; <http://www.openrdf.org/contrib/lucenesail#property> ?property_0;<http://www.openrdf.org/contrib/lucenesail#score> ?score_0;<http://www.openrdf.org/contrib/lucenesail#snippet> ?snippet_0]." , element.getLeftPathElement().toSPARQL());
	}catch(Exception e){
		fail();
	}
}

/**
 * Test 32.
 */
@Test 
@Order(32)
void test_32() {
	try {

		PathElement element = PathParser.parsePathPattern(thing, "[ like \"Unit* NOT (location OR product*)\"]/:hasProductBatteryLimit");
		assertEqualsWOSpaces ("StatementPattern\r\n"
				+ "   Variable (name=n0)\r\n"
				+ "   Var (name=p0_1, value=http://default/hasProductBatteryLimit)\r\n"
				+ "   Variable (name=n1)\r\n"
				+ "" ,element.pathPatternQuery(thing,null,null).toString());
		assertEqualsWOSpaces ("?n0 <http://www.openrdf.org/contrib/lucenesail#matches> [<http://www.openrdf.org/contrib/lucenesail#query> 'Unit* NOT (location OR product*)'; <http://www.openrdf.org/contrib/lucenesail#property> ?property_0;<http://www.openrdf.org/contrib/lucenesail#score> ?score_0;<http://www.openrdf.org/contrib/lucenesail#snippet> ?snippet_0]." , element.getLeftPathElement().toSPARQL());
	}catch(Exception e){
		fail();
	}
}

/**
 * Test 33.
 */
@Test 
@Order(33)
void test_33() {
	try {

		PathElement element = PathParser.parsePathPattern(thing, "[ like \"Unit\" ; a :Unit]/:hasProductBatteryLimit");
		assertEqualsWOSpaces ("StatementPattern\r\n"
				+ "   Variable (name=n0)\r\n"
				+ "   Var (name=p0_1, value=http://default/hasProductBatteryLimit)\r\n"
				+ "   Variable (name=n1)\r\n"
				+ "" ,element.pathPatternQuery(thing,null,null).toString());
		assertEqualsWOSpaces ("?n0 <http://www.openrdf.org/contrib/lucenesail#matches> [<http://www.openrdf.org/contrib/lucenesail#query> 'Unit'; <http://www.openrdf.org/contrib/lucenesail#property> ?property_0;<http://www.openrdf.org/contrib/lucenesail#score> ?score_0;<http://www.openrdf.org/contrib/lucenesail#snippet> ?snippet_0].?n0 <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://default/Unit> .\n"
				+ "" , element.getLeftPathElement().toSPARQL());
	}catch(Exception e){
		fail();
	}
}

/**
 * Test 34.
 */
@Test 
@Order(34)
void test_34() {
	try {

		PathElement element = PathParser.parsePathPattern(thing, ":hasProductBatteryLimit");
		assertEqualsWOSpaces ("StatementPattern\r\n"
				+ "   Variable (name=n0)\r\n"
				+ "   Var (name=p0_1, value=http://default/hasProductBatteryLimit)\r\n"
				+ "   Variable (name=n1)\r\n"
				+ "" ,element.pathPatternQuery(thing,null,null).toString());
	}catch(Exception e){
		fail();
	}
}

/**
 * Test 35.
 */
@Test 
@Order(35)
void test_35() {
	try {

		PathElement element = PathParser.parsePathPattern(thing, ":hasProductBatteryLimit[a  :BatteryLimit]");
		assertEqualsWOSpaces ("Join\r\n"
				+ "   StatementPattern\r\n"
				+ "      Variable (name=n0)\r\n"
				+ "      Var (name=p0_1, value=http://default/hasProductBatteryLimit)\r\n"
				+ "      Variable (name=n1)\r\n"
				+ "   StatementPattern\r\n"
				+ "      Variable (name=n1)\r\n"
				+ "      Var (name=p1_0_1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#type)\r\n"
				+ "      Variable (name=n1_1, value=http://default/BatteryLimit)\r\n"
				+ "" ,element.pathPatternQuery(thing,null,null).toString());
	}catch(Exception e){
		fail();
	}
}
}
