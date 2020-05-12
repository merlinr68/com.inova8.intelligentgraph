package olgap;

import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.query.Update;
import org.eclipse.rdf4j.query.algebra.evaluation.ValueExprEvaluationException;
import org.eclipse.rdf4j.query.algebra.evaluation.function.Function;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.sail.memory.MemoryStore;

/*

	INSERT DATA{  GRAPH <ng:labels> { <http://in4mium.com/londontube/id/Baker_Street> <label:flag> 1 }	}
	
#while not saturated:

	INSERT{   GRAPH<ng:labels>{ ?neighbor <label:flag>  ?iter .	}  }
	#SELECT *
	WHERE{
	  SELECT distinct ?neighbor ((?iter_no +1) as ?iter)
	WHERE{
	    BIND(1 as ?iter_no )
	    { SERVICE <http://localhost:8080/rdf4j-server/repositories/tfl>
	      {
	          {?node ?edge ?neighbor.}
	      UNION
	          {?neighbor ?edge ?node.}
	      }
	      FILTER( isIRI(?neighbor) || isBlank(?neighbor))
	    }
	    { GRAPH<ng:labels>{ ?node <label:flag> ?iter_no  .}	}
		FILTER NOT EXISTS{
	    	GRAPH<ng:labels>{
	    		?neighbor <label:flag> ?any.
	  		}
	  	}
	    FILTER( isIRI(?neighbor) )
	  }
	}	
*/		
		
public class EccentricityFunction  implements Function{
	public RepositoryConnection conn;
	public Repository workingRep;
	private String service;
	public EccentricityFunction() {
		super();
		workingRep = new SailRepository(new MemoryStore());
		workingRep.init();
	}
	public static final String NAMESPACE = "http://inova8.com/olgap/";
	@Override
	public String getURI() {
		return NAMESPACE + "eccentricity";
	}
	@Override
	public Value evaluate(ValueFactory valueFactory, Value... args) throws ValueExprEvaluationException {

		Repository rep = new HTTPRepository(args[0].stringValue(),args[1].stringValue());
		rep.init();	
		int iter_no = 0;
		int count = 1;
		service =  "SERVICE <" + args[0].toString() + "?distinct=true&infer=false>"; 
		Resource node = (Resource) args[1];
		try (RepositoryConnection workingConn = workingRep.getConnection();RepositoryConnection conn = rep.getConnection(); ) {

			//Resource graph = workingRep.getValueFactory().createIRI("ng:labels");
			//workingConn.add(workingRep.getValueFactory().createStatement(node,  workingRep.getValueFactory().createIRI("<label:flag>"),workingRep.getValueFactory().createLiteral(0) ), graph);
			
			String initializeString = "INSERT DATA{ GRAPH <ng:labels> { <" + node + "> <label:flag> 1 }}";
			Update initialize = workingConn.prepareUpdate(initializeString);
			initialize.execute();
			
			boolean saturated = false ;
			
			while (!saturated && (iter_no < 100)) {
				String incrementString = "	INSERT{   GRAPH<ng:labels>{ ?neighbor <label:flag>  ?iter .	}  }\r\n" + 
						"	#SELECT *\r\n" + 
						"	WHERE{\r\n" + 
						"	  SELECT distinct ?neighbor ((?iter_no +1) as ?iter)\r\n" + 
						"	WHERE{\r\n" + 
						"	    BIND(" + iter_no + " as ?iter_no )\r\n" + 
						"	    { " + service + "\r\n" + 
						"	      {\r\n" + 
						"	          {?node ?edge ?neighbor.}\r\n" + 
						"	      UNION\r\n" + 
						"	          {?neighbor ?edge ?node.}\r\n" + 
						"	      }\r\n" + 
						"	      FILTER( isIRI(?neighbor) || isBlank(?neighbor))\r\n" + 
						"	    }\r\n" + 
						"	    { GRAPH<ng:labels>{ ?node <label:flag> 1  .}	}\r\n" + 
						"		FILTER NOT EXISTS{\r\n" + 
						"	    	GRAPH<ng:labels>{\r\n" + 
						"	    		?neighbor <label:flag> ?any.\r\n" + 
						"	  		}\r\n" + 
						"	  	}\r\n" + 
						"	    FILTER( isIRI(?neighbor) )\r\n" + 
						"	  }\r\n" + 
						"	}	";
				Update increment = workingConn.prepareUpdate(incrementString);
				increment.execute();
				String queryString = "SELECT(COUNT(DISTINCT ?s) AS ?count)\r\n" 
						+ "WHERE{\r\n" 
						+ "  {\r\n" 
						+ "		GRAPH<ng:labels>   {\r\n"
						+ "      ?s ?p ?o.\r\n" 
						+ "    }\r\n" 
						+ "  }\r\n" 
						+ "}";

				TupleQuery query = workingConn.prepareTupleQuery(queryString);

				try (TupleQueryResult result = query.evaluate()) {
					while (result.hasNext()) {
						BindingSet solution = result.next();
						Literal valueOfUpdatedCount = (Literal) solution.getValue("count");
						int updatedCount = Integer.parseInt(valueOfUpdatedCount.stringValue());
						if (updatedCount == count ){
							saturated = true;				
						}else {
							iter_no++;
							count = updatedCount;
						}
					}
				}						
			}
			
		}
		return valueFactory.createLiteral(iter_no);
	}
}
