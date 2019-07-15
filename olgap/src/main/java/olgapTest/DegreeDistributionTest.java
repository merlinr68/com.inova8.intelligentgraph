package olgapTest;

import java.util.List;

import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.sail.memory.MemoryStore;
import org.eclipse.rdf4j.sail.spin.SpinSail;

public class DegreeDistributionTest {
	public static void main(String[] args) {
		
		Repository workingRep = new SailRepository(new SpinSail(new MemoryStore()));
		workingRep.init();
		try (RepositoryConnection conn = workingRep.getConnection();
				RepositoryConnection workingConn = workingRep.getConnection();) {	
			
			String queryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>  \n";
			queryString += "PREFIX olgap: <http://inova8.com/olgap/> \n";
			queryString += "SELECT ?degree ?count \n";
			queryString += "WHERE { \n";		
			queryString += "   { select ?degree ?count  {";
			queryString += "   BIND( \"http://localhost:8082/rdf4j-server/\" as ?service)";
			queryString += "   BIND( \"tfl\" as ?repository)";
			//queryString += "   (?service ?repository)  olgap:degreeDistributionProperty (?degree  ?count )  .";
			queryString += "   (?degree  ?count ) olgap:degreeDistributionProperty   (?service ?repository)  .";
			queryString += "   }}}  order by ?degree";

			TupleQuery query = conn.prepareTupleQuery(queryString);

			try (TupleQueryResult result = query.evaluate()) {
				List<String> bindingNames = result.getBindingNames();
				while (result.hasNext()) {
					BindingSet solution = result.next();
					StringBuilder aResult = new StringBuilder();
					for (String bindingName : bindingNames) {
						aResult.append(bindingName).append(" = ").append(solution.getValue(bindingName).stringValue()).append("; ");
					}
					System.out.println(aResult);
				}
				System.out.println("Finished!");
			}
		} finally {
			workingRep.shutDown();
		}
	}

}
