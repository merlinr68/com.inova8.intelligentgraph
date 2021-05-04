/*
 * inova8 2020
 */
package olgap;

import java.security.NoSuchAlgorithmException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.ParameterizedMessage;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.query.algebra.evaluation.TripleSource;
import org.eclipse.rdf4j.query.algebra.evaluation.ValueExprEvaluationException;
import org.eclipse.rdf4j.query.algebra.evaluation.function.Function;

import pathCalc.Evaluator;

/**
 * The Class ClearCache.
 */
public class ClearCache extends Evaluator implements Function{
	
	/** The logger. */
	private final Logger logger = LogManager.getLogger(ClearCache.class);
	
	/**
	 * Instantiates a new clear cache.
	 *
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 */
	public ClearCache() throws NoSuchAlgorithmException {
		super();
		logger.info(new ParameterizedMessage("Initiating ClearCache"));
	}
	
	/**
	 * Gets the uri.
	 *
	 * @return the uri
	 */
	@Override
	public String getURI() {
		return OLGAPNAMESPACE + "clearCache";
	}
	
	/**
	 * Evaluate.
	 *
	 * @param tripleSource the triple source
	 * @param args the args
	 * @return the value
	 * @throws ValueExprEvaluationException the value expr evaluation exception
	 */
	@Override
	public Value evaluate(TripleSource tripleSource, Value... args) throws ValueExprEvaluationException {

		//TODO
//		if(!sources.containsKey(tripleSource.hashCode()) ){
//			sources.put(tripleSource.hashCode(),  new Source(tripleSource));
//		}
//		Source source = sources.get(tripleSource.hashCode());
//		source.clearCache(args);
		//Workaround for now
		String keys = sources.getKeys().toString();
		clearCache();
		logger.error(new ParameterizedMessage("Caches cleared {}",keys));
		return tripleSource.getValueFactory().createLiteral(true);
	}
	
	/**
	 * Evaluate.
	 *
	 * @param valueFactory the value factory
	 * @param args the args
	 * @return the value
	 * @throws ValueExprEvaluationException the value expr evaluation exception
	 */
	@Override
	public Value evaluate(ValueFactory valueFactory, Value... args) throws ValueExprEvaluationException {
		// TODO Auto-generated method stub
		return null;
	}
}
