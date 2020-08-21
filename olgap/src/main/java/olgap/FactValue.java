package olgap;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleLiteral;
import org.eclipse.rdf4j.query.algebra.evaluation.ValueExprEvaluationException;
import org.eclipse.rdf4j.query.algebra.evaluation.function.Function;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.evaluation.RepositoryTripleSource;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;
import org.eclipse.rdf4j.sail.memory.MemoryStore;
import org.eclipse.rdf4j.sail.nativerdf.ValueStore;
import org.eclipse.rdf4j.query.algebra.evaluation.TripleSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.message.FormattedMessage;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import groovy.lang.GroovyShell;

public class FactValue extends Evaluator implements Function {
	private final Logger logger = LogManager.getLogger(FactValue.class);
	private HashMap<TripleSource, Source> sources = new HashMap<TripleSource, Source>();
	public static Repository cacheRep;
	private String cacheService;
	public RepositoryConnection cacheConn;

	public FactValue() throws NoSuchAlgorithmException {
		super();
//		if(hosted) {
//			//Use a host triplestore for the path manipulation
//			String rdf4jServer = "http://localhost:8080/rdf4j-server/";
//			String repositoryID = "olgap";
//			cacheRep = new HTTPRepository(rdf4jServer, repositoryID);	
//			cacheConn = cacheRep.getConnection();	
//			cacheService ="SERVICE <" + rdf4jServer + "repositories/" + repositoryID + "?distinct=true&infer=false>";
//			logger.info("FactValue hosted at:" + cacheService);
//
//		}else {
//			//Use a in-memory triplestore for the path manipulation, however the SPARQ:L cannot use SERVICE call backs to this memory store
//			cacheRep = new SailRepository(new MemoryStore());
//			logger.info("FactValue hosted in memory");
//		}	
		cacheRep.init();
		logger.info(new FormattedMessage("Initiating {}",this));
	}



	@Override
	public String getURI() {
		return NAMESPACE + "factValue";
	}

	@Override
	public Value evaluate(TripleSource tripleSource, Value... args) throws ValueExprEvaluationException {
		if( this.tripleSource==null) {
			this.tripleSource =tripleSource;
//			try {
//				ValueStore dataset = (ValueStore)FieldUtils.readField(tripleSource, "vf", true);
//				IRI subject = tripleSource.getValueFactory().createIRI("http://Subject");
//				IRI predicate = tripleSource.getValueFactory().createIRI("http://predicate");
//				IRI object = tripleSource.getValueFactory().createIRI("http://object");
//				IRI context = tripleSource.getValueFactory().createIRI("http://context");
//				dataset.createStatement(subject, predicate, object, context);
//				//Object conn = FieldUtils.readField(dataset, "dataset", true);
//
//			} catch (IllegalAccessException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}

		}
		
		logger.traceEntry(new FormattedMessage("Evaluate for <{}> with args <{}>",tripleSource, args));
		if(args.length <2) {
			FormattedMessage message = new FormattedMessage("At least subject, and predicate arguments required");
			logger.error(message);
			return tripleSource.getValueFactory().createLiteral(message.toString());
		}else {

			IRI subject ;
			IRI predicate;
			try {
				subject = (IRI) args[0];
				predicate = (IRI) args[1];
			} catch(Exception e) {
				FormattedMessage message = new FormattedMessage("Subject and predicate must be valid IRI");
				logger.error(message);
				return tripleSource.getValueFactory().createLiteral(message.toString());
			}
			SimpleLiteral literalValue;
			try{
				if(!sources.containsKey(tripleSource) ){
					sources.put(tripleSource,  new Source(tripleSource));
				}
				Source source = sources.get(tripleSource);
				Repository rep = new SPARQLRepository(args[0].stringValue());
				RepositoryConnection conn = rep.getConnection();
				RepositoryTripleSource triplestore = new RepositoryTripleSource(conn);
				Value[] arguments = Arrays.copyOfRange(args, 2, args.length);
				Thing subjectThing = thingFactory( subject);
				olgap.Value fact = subjectThing.getFact( predicate, arguments);
				if( fact != null) {
					Value result = fact.getValue();
					FormattedMessage message = new FormattedMessage(StringUtils.repeat('_', 5) + "Evaluate result {}",result);
					logger.traceExit(message);
					return  result;
				}else {
					return null;
				}			

			}catch(Exception e) {
				return args[2];
			}
		}
	}

	@Override
	public Value evaluate(ValueFactory valueFactory, Value... args) throws ValueExprEvaluationException {
		return null;
	}

}
