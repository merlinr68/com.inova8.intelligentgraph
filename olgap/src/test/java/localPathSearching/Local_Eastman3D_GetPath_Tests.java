/*
 * inova8 2020
 */
package localPathSearching;

import static org.junit.jupiter.api.Assertions.*;

import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.inova8.intelligentgraph.dashjoin.PathSteps;
import com.inova8.intelligentgraph.intelligentGraphRepository.IntelligentGraphRepository;
import com.inova8.intelligentgraph.path.Path;
import com.inova8.intelligentgraph.pathQLModel.Thing;
import com.inova8.intelligentgraph.pathQLResults.PathResults;
import com.inova8.intelligentgraph.pathQLResults.ResourceResults;
import utilities.Query;

/**
 * The Class ThingTests.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Local_Eastman3D_GetPath_Tests {
	

	
	/** The source. */
	private static IntelligentGraphRepository source;
	

	static org.eclipse.rdf4j.repository.Repository workingRep ;
	/**
	 * Sets the up before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		workingRep = Query.createNativeLuceneIntelligentGraphRepository("src/test/resources/datadir/eastman3d/");
		Query.addFile(workingRep, "src/test/resources/eastman3d/Eastman.3d.data.rev2.ttl");
		Query.addFile(workingRep, "src/test/resources/eastman3d/icore.def.ttl");
		Query.addFile(workingRep, "src/test/resources/eastman3d/Plant.3d.def.rev2.ttl");
		RepositoryConnection conn = workingRep.getConnection();
		conn.setNamespace("eastman", "http://inova8.com/eastman/id/");
		conn.setNamespace("eastman.Signal", "http://inova8.com/eastman/id/Signal/");
		conn.setNamespace("eastman.BatteryLimit", "http://inova8.com/eastman/id/BatteryLimit/");
		conn.setNamespace("eastman.Valve", "http://inova8.com/eastman/id/Valve/");
		conn.setNamespace("plant", "http://inova8.com/plant/def/");
		conn.setNamespace("plant.TransferenceKind", "http://inova8.com/plant/def/TransferenceKind/");
		
		source = IntelligentGraphRepository.create(workingRep);


	}
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
	@Test
	@Order(0)
	void test_0() {
		try {
			Thing _this =source.getThing("eastman.BatteryLimit:Stripper.Bottoms");
			 ResourceResults facts = _this.getFacts("^plant:Transference@plant.TransferenceKind:ProcessFlow{1,3}");//
			assertEquals("[http://inova8.com/eastman/id/Valve/U8;http://inova8.com/eastman/id/Pump/G103;http://inova8.com/eastman/id/Vessel/V103;]", facts.toString());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
	@Test
	@Order(0)
	void test_01() {
		try {
			Thing _this =source.getThing("eastman.BatteryLimit:Stripper.Bottoms");
			PathResults paths = _this.getPaths("^plant:Transference@plant.TransferenceKind:ProcessFlow{1,3}");//
			assertEquals("Path=[[http://inova8.com/eastman/id/BatteryLimit/Stripper.Bottoms,<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/Valve/U8,INVERSE,false]\r\n"
					+ "]\r\n"
					+ "Path=[[http://inova8.com/eastman/id/BatteryLimit/Stripper.Bottoms,<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/Valve/U8,INVERSE,false]\r\n"
					+ "[http://inova8.com/eastman/id/Valve/U8,<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/Pump/G103,INVERSE,false]\r\n"
					+ "]\r\n"
					+ "Path=[[http://inova8.com/eastman/id/BatteryLimit/Stripper.Bottoms,<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/Valve/U8,INVERSE,false]\r\n"
					+ "[http://inova8.com/eastman/id/Valve/U8,<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/Pump/G103,INVERSE,false]\r\n"
					+ "[http://inova8.com/eastman/id/Pump/G103,<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/Vessel/V103,INVERSE,false]\r\n"
					+ "]\r\n"
					+ "", paths.toString());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
	@Test
	@Order(1)
	void test_1() {
		try {
			Thing _this =source.getThing("eastman.Valve:U8");
			PathResults paths =  _this.getPaths("plant:Transference@plant.TransferenceKind:ProcessFlow{1,3}");//
			assertEquals("Path=[[http://inova8.com/eastman/id/Valve/U8,<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/BatteryLimit/Stripper.Bottoms,DIRECT,false]\r\n"
					+ "]\r\n"
					+ "", paths.toString());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
	@Test
	@Order(2)
	void test_2() {
		try {
			Thing _this =source.getThing("eastman.BatteryLimit:Stripper.Bottoms");
			PathResults paths =  _this.getPaths("^plant:Transference@plant.TransferenceKind:ProcessFlow{1,4}");//
			assertEquals("Path=[[http://inova8.com/eastman/id/BatteryLimit/Stripper.Bottoms,<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/Valve/U8,INVERSE,false]\r\n"
					+ "]\r\n"
					+ "Path=[[http://inova8.com/eastman/id/BatteryLimit/Stripper.Bottoms,<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/Valve/U8,INVERSE,false]\r\n"
					+ "[http://inova8.com/eastman/id/Valve/U8,<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/Pump/G103,INVERSE,false]\r\n"
					+ "]\r\n"
					+ "Path=[[http://inova8.com/eastman/id/BatteryLimit/Stripper.Bottoms,<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/Valve/U8,INVERSE,false]\r\n"
					+ "[http://inova8.com/eastman/id/Valve/U8,<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/Pump/G103,INVERSE,false]\r\n"
					+ "[http://inova8.com/eastman/id/Pump/G103,<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/Vessel/V103,INVERSE,false]\r\n"
					+ "]\r\n"
					+ "Path=[[http://inova8.com/eastman/id/BatteryLimit/Stripper.Bottoms,<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/Valve/U8,INVERSE,false]\r\n"
					+ "[http://inova8.com/eastman/id/Valve/U8,<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/Pump/G103,INVERSE,false]\r\n"
					+ "[http://inova8.com/eastman/id/Pump/G103,<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/Vessel/V103,INVERSE,false]\r\n"
					+ "[http://inova8.com/eastman/id/Vessel/V103,<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/Boiler/B103,INVERSE,false]\r\n"
					+ "]\r\n"
					+ "Path=[[http://inova8.com/eastman/id/BatteryLimit/Stripper.Bottoms,<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/Valve/U8,INVERSE,false]\r\n"
					+ "[http://inova8.com/eastman/id/Valve/U8,<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/Pump/G103,INVERSE,false]\r\n"
					+ "[http://inova8.com/eastman/id/Pump/G103,<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/Vessel/V103,INVERSE,false]\r\n"
					+ "[http://inova8.com/eastman/id/Vessel/V103,<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/BatteryLimit/Stripper.Condensate,INVERSE,false]\r\n"
					+ "]\r\n"
					+ "Path=[[http://inova8.com/eastman/id/BatteryLimit/Stripper.Bottoms,<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/Valve/U8,INVERSE,false]\r\n"
					+ "[http://inova8.com/eastman/id/Valve/U8,<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/Pump/G103,INVERSE,false]\r\n"
					+ "[http://inova8.com/eastman/id/Pump/G103,<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/Vessel/V103,INVERSE,false]\r\n"
					+ "[http://inova8.com/eastman/id/Vessel/V103,<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/BatteryLimit/Stripper.Feed,INVERSE,false]\r\n"
					+ "]\r\n"
					+ "", paths.toString());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
	@Test
	@Order(3)
	void test_3() {
		try {
			Thing _this =source.getThing("eastman.Signal:XMEAS17");
			PathResults paths =  _this.getPaths("^plant:attribute.providedBy/plant:attribute.of.PlantItem/^plant:Transference@plant.TransferenceKind:ProcessFlow{1,3}");//
			assertEquals("Path=[[http://inova8.com/eastman/id/Signal/XMEAS17,http://inova8.com/plant/def/attribute.providedBy,http://inova8.com/eastman/id/Attribute/Stripper.Bottoms.volumeFlow.XMEAS17,INVERSE]\r\n"
					+ "[http://inova8.com/eastman/id/Attribute/Stripper.Bottoms.volumeFlow.XMEAS17,http://inova8.com/plant/def/attribute.of.PlantItem,http://inova8.com/eastman/id/BatteryLimit/Stripper.Bottoms,DIRECT]\r\n"
					+ "[http://inova8.com/eastman/id/BatteryLimit/Stripper.Bottoms,<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/Valve/U8,INVERSE,false]\r\n"
					+ "]\r\n"
					+ "Path=[[http://inova8.com/eastman/id/Signal/XMEAS17,http://inova8.com/plant/def/attribute.providedBy,http://inova8.com/eastman/id/Attribute/Stripper.Bottoms.volumeFlow.XMEAS17,INVERSE]\r\n"
					+ "[http://inova8.com/eastman/id/Attribute/Stripper.Bottoms.volumeFlow.XMEAS17,http://inova8.com/plant/def/attribute.of.PlantItem,http://inova8.com/eastman/id/BatteryLimit/Stripper.Bottoms,DIRECT]\r\n"
					+ "[http://inova8.com/eastman/id/BatteryLimit/Stripper.Bottoms,<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/Valve/U8,INVERSE,false]\r\n"
					+ "[http://inova8.com/eastman/id/Valve/U8,<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/Pump/G103,INVERSE,false]\r\n"
					+ "]\r\n"
					+ "Path=[[http://inova8.com/eastman/id/Signal/XMEAS17,http://inova8.com/plant/def/attribute.providedBy,http://inova8.com/eastman/id/Attribute/Stripper.Bottoms.volumeFlow.XMEAS17,INVERSE]\r\n"
					+ "[http://inova8.com/eastman/id/Attribute/Stripper.Bottoms.volumeFlow.XMEAS17,http://inova8.com/plant/def/attribute.of.PlantItem,http://inova8.com/eastman/id/BatteryLimit/Stripper.Bottoms,DIRECT]\r\n"
					+ "[http://inova8.com/eastman/id/BatteryLimit/Stripper.Bottoms,<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/Valve/U8,INVERSE,false]\r\n"
					+ "[http://inova8.com/eastman/id/Valve/U8,<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/Pump/G103,INVERSE,false]\r\n"
					+ "[http://inova8.com/eastman/id/Pump/G103,<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/Vessel/V103,INVERSE,false]\r\n"
					+ "]\r\n"
					+ "", paths.toString());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
	@Test
	@Order(4)
	void test_4() {
		try {
			Thing _this =source.getThing("eastman.Signal:XMEAS17");
			 ResourceResults facts = _this.getFacts("^plant:attribute.providedBy/plant:attribute.of.PlantItem/^plant:Transference@plant.TransferenceKind:ProcessFlow{1,6}");//
			assertEquals("[http://inova8.com/eastman/id/Valve/U8;http://inova8.com/eastman/id/Pump/G103;http://inova8.com/eastman/id/Vessel/V103;http://inova8.com/eastman/id/Boiler/B103;http://inova8.com/eastman/id/BatteryLimit/Stripper.Condensate;http://inova8.com/eastman/id/BatteryLimit/Stripper.Feed;http://inova8.com/eastman/id/Vessel/V103;http://inova8.com/eastman/id/Boiler/B103;http://inova8.com/eastman/id/BatteryLimit/Stripper.Condensate;http://inova8.com/eastman/id/BatteryLimit/Stripper.Feed;]", facts.toString());
//			assertEquals("[http://inova8.com/eastman/id/Valve/U8;http://inova8.com/eastman/id/Pump/G103;http://inova8.com/eastman/id/Vessel/V103;http://inova8.com/eastman/id/Boiler/B103;http://inova8.com/eastman/id/BatteryLimit/Stripper.Condensate;http://inova8.com/eastman/id/BatteryLimit/Stripper.Feed;http://inova8.com/eastman/id/Vessel/V103;http://inova8.com/eastman/id/Boiler/B103;http://inova8.com/eastman/id/BatteryLimit/Stripper.Condensate;http://inova8.com/eastman/id/BatteryLimit/Stripper.Feed;]", facts.toString());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
	@Test
	@Order(5)
	void test_5() {
		try {
			Thing _this =source.getThing("eastman.Signal:XMEAS17");
			ResourceResults facts = _this.getFacts("^plant:attribute.providedBy/plant:attribute.of.PlantItem/^plant:Transference@plant.TransferenceKind:ProcessFlow{1,6}/^plant:attribute.of.PlantItem/plant:attribute.providedBy");//
			assertEquals("[http://inova8.com/eastman/id/Signal/XMV8;http://inova8.com/eastman/id/Signal/XMEAS15;http://inova8.com/eastman/id/Signal/XMEAS18;http://inova8.com/eastman/id/Signal/XMEAS14;http://inova8.com/eastman/id/Signal/XMEAS4;http://inova8.com/eastman/id/Signal/XMEAS15;http://inova8.com/eastman/id/Signal/XMEAS18;http://inova8.com/eastman/id/Signal/XMEAS14;http://inova8.com/eastman/id/Signal/XMEAS4;]", facts.toString());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
	@Test
	@Order(6)
	void test_6() {
		try {
			Thing _this =source.getThing("eastman.BatteryLimit:Stripper.Bottoms");
			PathResults paths =  _this.getPaths("^plant:Transference@plant.TransferenceKind:ProcessFlow{3,3}");
			for(Path path: paths) {
					assertEquals("[\r\n"
							+ "start:http://inova8.com/eastman/id/BatteryLimit/Stripper.Bottoms;\r\n"
							+ "steps:[\r\n"
							+ "\r\n"
							+ "	[	end:http://inova8.com/eastman/id/Valve/U8;\r\n"
							+ "		edge:[\r\n"
							+ "			edge.Reification:http://inova8.com/plant/def/Transference;\r\n"
							+ "			edge.Dereified:false;\r\n"
							+ "			edge.Predicate:http://inova8.com/plant/def/TransferenceKind/ProcessFlow;\r\n"
							+ "			edge.Direction:INVERSE;\r\n"
							+ "		]\r\n"
							+ "	]\r\n"
							+ "	,\r\n"
							+ "	[	end:http://inova8.com/eastman/id/Pump/G103;\r\n"
							+ "		edge:[\r\n"
							+ "			edge.Reification:http://inova8.com/plant/def/Transference;\r\n"
							+ "			edge.Dereified:false;\r\n"
							+ "			edge.Predicate:http://inova8.com/plant/def/TransferenceKind/ProcessFlow;\r\n"
							+ "			edge.Direction:INVERSE;\r\n"
							+ "		]\r\n"
							+ "	]\r\n"
							+ "	,\r\n"
							+ "	[	end:http://inova8.com/eastman/id/Vessel/V103;\r\n"
							+ "		edge:[\r\n"
							+ "			edge.Reification:http://inova8.com/plant/def/Transference;\r\n"
							+ "			edge.Dereified:false;\r\n"
							+ "			edge.Predicate:http://inova8.com/plant/def/TransferenceKind/ProcessFlow;\r\n"
							+ "			edge.Direction:INVERSE;\r\n"
							+ "		]\r\n"
							+ "	]\r\n"
							+ "	];\r\n"
							+ "]\r\n"
							+ "", (new PathSteps(path)).toString());
				break;	
			}

		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
}
