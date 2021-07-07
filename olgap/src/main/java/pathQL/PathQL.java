/*
 * inova8 2020
 */
package pathQL;

import org.antlr.v4.runtime.RecognitionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.MalformedQueryException;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.algebra.TupleExpr;
import org.eclipse.rdf4j.query.algebra.evaluation.EvaluationStrategy;
import org.eclipse.rdf4j.query.algebra.evaluation.QueryBindingSet;
import org.eclipse.rdf4j.query.algebra.evaluation.impl.StrictEvaluationStrategy;
import org.eclipse.rdf4j.query.impl.SimpleDataset;
import org.eclipse.rdf4j.repository.RepositoryException;
import pathCalc.CustomQueryOptions;
import pathCalc.Thing;
import pathPatternElement.PathElement;
import pathPatternProcessor.PathPatternException;
import pathQLQueryIterator.MatchJoinIterator;
import pathQLRepository.PathQLRepository;
import pathQLResults.FactResults;
import pathQLResults.PathQLResults;
import pathQLResults.ResourceResults;

/**
 * The Class PathQL.
 */
public class PathQL {
	
	/** The Constant logger. */
	private final static Logger logger = LoggerFactory.getLogger(PathQL.class);

	/**
	 * Evaluate.
	 *
	 * @param source the source
	 * @param pathQL the path QL
	 * @return the resource results
	 * @throws RecognitionException the recognition exception
	 * @throws PathPatternException the path pattern exception
	 */
	public static ResourceResults evaluate(PathQLRepository source,String pathQL) throws RecognitionException, PathPatternException {	
		PathElement element = PathParser.parsePathPattern(source, pathQL);		
		if(element!=null ) {
			return evaluate(source, element,null);
		}else {
			throw new  PathPatternException();
		}
	}
	
	/**
	 * Evaluate.
	 *
	 * @param thing the thing
	 * @param pathQL the path QL
	 * @return the path QL results. resource results
	 * @throws PathPatternException the path pattern exception
	 */
	public  static pathQLResults.ResourceResults  evaluate(Thing thing, String pathQL) throws PathPatternException{
		return evaluate( thing,  pathQL,null);
	}
	public  static pathQLResults.ResourceResults  evaluate(Thing thing, String pathQL,CustomQueryOptions customQueryOptions) throws PathPatternException {
		PathElement pathElement =  PathParser.parsePathPattern(thing, pathQL);
		if(pathElement!=null ) {
			CustomQueryOptions pathCustomQueryOptions = pathElement.getCustomQueryOptions();
			if(pathCustomQueryOptions!=null) {
				pathCustomQueryOptions.addInherited(customQueryOptions);
				return evaluate(thing, pathElement,pathCustomQueryOptions);
			}else {
				return evaluate(thing, pathElement,customQueryOptions);
			}
		}else {
			throw new  PathPatternException();
		}
	}
	
	/**
	 * Evaluate.
	 *
	 * @param source the source
	 * @param element the element
	 * @return the path QL results. resource results
	 * @throws RepositoryException the repository exception
	 * @throws MalformedQueryException the malformed query exception
	 * @throws QueryEvaluationException the query evaluation exception
	 */
	private static pathQLResults.ResourceResults evaluate(PathQLRepository source,PathElement element,CustomQueryOptions customQueryOptions )
			throws RepositoryException, MalformedQueryException, QueryEvaluationException {
		PathQLResults boundResultsIterator = null ;
		if (element.getIsBound()) {
			PathElement pathPattern = element.getRightPathElement();
			String boundQuery;
			if(pathPattern!=null) {
				boundQuery = "SELECT DISTINCT ?n0 {"+ element.getLeftPathElement().toSPARQL()+"}";
			}else {
				boundQuery = "SELECT * {"+ element.getLeftPathElement().toSPARQL()+"}";
			}
			logger.debug("boundPattern=\n"+ boundQuery);
			TupleQuery tupleBoundQuery = source.getContextAwareConnection().prepareTupleQuery(boundQuery);
			boundResultsIterator = new PathQLResults(tupleBoundQuery.evaluate());
			
			if(pathPattern!=null ) {
				EvaluationStrategy evaluationStrategy = new StrictEvaluationStrategy(source.getTripleSource(), null);
				TupleExpr rightArgTupleExpr = element.getRightPathElement().pathPatternQuery(null, null, null).getTupleExpr();
				MatchJoinIterator matchJoinIterator = new MatchJoinIterator(evaluationStrategy, boundResultsIterator.getResourceSet(),rightArgTupleExpr);
			
				//return new PathQLResultsIterator(matchJoinIterator);
				return new FactResults(matchJoinIterator,source,element.getRightPathElement(), customQueryOptions);
			}else {
				return boundResultsIterator;
			}
		}else {
			//Cannot support an unbound PathQL query
			logger.error("Cannot support unbounded queries:"+ element.toString());
		}
		return boundResultsIterator;
	}
	public static pathQLResults.ResourceResults  evaluate(Thing thing, PathElement pathElement){
		return evaluate( thing,  pathElement,null);
	}
	/**
	 * Evaluate.
	 *
	 * @param thing the thing
	 * @param pathElement the path element
	 * @return the path QL results. resource results
	 */
	public static pathQLResults.ResourceResults  evaluate(Thing thing, PathElement pathElement,CustomQueryOptions customQueryOptions) {
		SimpleDataset dataset = thing.getDataset(customQueryOptions);
		
		EvaluationStrategy evaluationStrategy = new StrictEvaluationStrategy(thing.getSource().getTripleSource(),dataset, null);
		TupleExpr pathElementPattern = pathElement.pathPatternQuery(thing,null,null).getTupleExpr();
		pathElement.getSourceVariable().setValue( thing.getValue());
		BindingSet bindings = new QueryBindingSet();
		CloseableIteration<BindingSet, QueryEvaluationException> resultsIterator = evaluationStrategy.evaluate(pathElementPattern,bindings);
		logger.debug("Thing="+thing.toString());
		logger.debug( "pathQL="+ pathElement.toString());
		logger.debug("sourceVariable="+ pathElement.getTargetSubject().toString()+" targetPredicate="+ pathElement.getTargetPredicate().toString()+" targetVariable="+ pathElement.getTargetVariable().toString());
		logger.debug("pathPattern=\n"+ pathElementPattern.toString());
		return new FactResults( resultsIterator,thing, pathElement, customQueryOptions);
	}



}
