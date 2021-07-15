/*
 * inova8 2020
 */
package pathPatternElement;

import org.eclipse.rdf4j.query.algebra.Join;
import path.PathBinding;
import path.PathTupleExpr;
import pathCalc.Thing;
import pathPatternProcessor.PathConstants;
import pathPatternProcessor.PathConstants.EdgeCode;
import pathQLRepository.PathQLRepository;

/**
 * The Class SequencePathElement.
 */
public class SequencePathElement extends PathElement {

	/** The is negated. */
	private Boolean isNegated = false;

	/**
	 * Instantiates a new sequence path element.
	 *
	 * @param source
	 *            the source
	 */
	public SequencePathElement(PathQLRepository source) {
		super(source);
		operator = PathConstants.Operator.SEQUENCE;
	}

	/**
	 * To SPARQL.
	 *
	 * @return the string
	 */
	@Override
	public String toSPARQL() {

		String sequenceString = getMinCardinalityString();
		sequenceString += getLeftPathElement().toSPARQL();
		sequenceString += getRightPathElement().toSPARQL();
		sequenceString += getMaxCardinalityString();
		return sequenceString;
	}

	/**
	 * To HTML.
	 *
	 * @return the string
	 */
	@Override
	public String toHTML() {
		String sequenceString = getLeftPathElement().toHTML() + " / " + getRightPathElement().toHTML();
		return addCardinality(sequenceString);
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		String sequenceString = getLeftPathElement().toString() + " / " + getRightPathElement().toString();
		return addCardinality(sequenceString);
	}

	/**
	 * Path pattern query.
	 *
	 * @param thing
	 *            the thing
	 * @param sourceVariable
	 *            the source variable
	 * @param targetVariable
	 *            the target variable
	 * @return the tuple expr
	 */
	@Override
	public PathTupleExpr pathPatternQuery(Thing thing, Variable sourceVariable, Variable targetVariable) {
		return pathPatternQuery(thing, sourceVariable, targetVariable, 0);
	}
	public Boolean hasNextCardinality(Integer iteration) {
			
			if (getLeftPathElement() != null) {
				if (getRightPathElement() != null) {
					if (getRightPathElement().hasNextCardinality(iteration)) {
						setCardinality(iteration,getCardinality(iteration-1));
						return true;
					} else {
						if (getLeftPathElement().hasNextCardinality(iteration)) {
							setCardinality(iteration,getCardinality(iteration-1));
							return true;
						} else {
							 Integer cardinality = getCardinality(iteration-1);
							 if( cardinality < getMaxCardinality()) {
								cardinality++;
								setCardinality(iteration,cardinality);
								return true;
							 }else {
								setCardinality(iteration,getMinCardinality());
								return false;
							 }
						}
					}
				}
			}
			return false;

	 }
	@Override
	public PathTupleExpr pathPatternQuery(Thing thing, Variable sourceVariable, Variable targetVariable,
			Integer pathIteration) {
		/*			
				 	TupleExpr leftPattern = getLeftPathElement().pathPatternQuery(thing,sourceVariable,targetVariable,pathIteration) ;
					getRightPathElement().setSourceVariable(getLeftPathElement().getTargetVariable());
					TupleExpr rightPattern = getRightPathElement().pathPatternQuery(thing,sourceVariable,targetVariable,pathIteration);
					Join joinPattern = new Join(leftPattern,rightPattern); 
					return joinPattern;
		*/

		if (sourceVariable == null)	sourceVariable = this.getSourceVariable();
		if(targetVariable==null)targetVariable = this.getTargetVariable();	
		Join intermediateJoinPattern = null;
		PathTupleExpr joinPattern = null;

		if (getCardinality(pathIteration) > 0) {
			Variable intermediateSourceVariable = null;
			Variable intermediateVariable = null;
			Variable intermediateTargetVariable = null;
			Variable priorIntermediateTargetVariable = null;
			for (int iteration = 1; iteration <= getCardinality(pathIteration); iteration++) {
				if (iteration == 1) {
					intermediateSourceVariable = sourceVariable;
					intermediateVariable= getLeftPathElement().getTargetVariable();
					intermediateTargetVariable=targetVariable;
				}
				if (iteration < getCardinality(pathIteration)) {
					if (iteration > 1) 
						intermediateSourceVariable = priorIntermediateTargetVariable;
					intermediateTargetVariable = new Variable(sourceVariable.getName() + "_i" + iteration);
					intermediateVariable = new Variable(sourceVariable.getName() + "_in" + iteration);
					priorIntermediateTargetVariable = intermediateTargetVariable;
				}
				if (iteration == getCardinality(pathIteration)) {
					if (iteration > 1) {
						intermediateSourceVariable = priorIntermediateTargetVariable;
						intermediateVariable = new Variable(sourceVariable.getName() + "_in" + iteration);
						intermediateTargetVariable = targetVariable;
					}
				}
				PathTupleExpr leftPattern = getLeftPathElement().pathPatternQuery(thing, intermediateSourceVariable,
						intermediateVariable, pathIteration);			
				PathTupleExpr rightPattern = getRightPathElement().pathPatternQuery(thing, intermediateVariable, intermediateTargetVariable,
						pathIteration);
				intermediateJoinPattern = new Join(leftPattern.getTupleExpr(), rightPattern.getTupleExpr());
				if (joinPattern == null) {
					joinPattern = new PathTupleExpr(intermediateJoinPattern);

				} else {
					joinPattern.setTupleExpr(new Join(joinPattern.getTupleExpr(), intermediateJoinPattern));
				}
				joinPattern.getPath().addAll(leftPattern.getPath());
				joinPattern.getPath().addAll(rightPattern.getPath());
			}
			return joinPattern;
		} else {
			return null;
		}

	}

	/**
	 * Gets the checks if is negated.
	 *
	 * @return the checks if is negated
	 */
	public Boolean getIsNegated() {
		return isNegated;
	}

	/**
	 * Sets the checks if is negated.
	 *
	 * @param isNegated
	 *            the new checks if is negated
	 */
	public void setIsNegated(Boolean isNegated) {
		this.isNegated = isNegated;
	}

	/**
	 * Index visitor.
	 *
	 * @param baseIndex
	 *            the base index
	 * @param entryIndex
	 *            the entry index
	 * @param edgeCode
	 *            the edge code
	 * @return the integer
	 */
	@Override
	public Integer indexVisitor(Integer baseIndex, Integer entryIndex, EdgeCode edgeCode) {
		setBaseIndex(baseIndex);
		setEntryIndex(entryIndex);
		Integer leftExitIndex = getLeftPathElement().indexVisitor(baseIndex, entryIndex, edgeCode);

		if (getLeftPathElement().getOperator().equals(PathConstants.Operator.PREDICATE)
				&& ((PredicateElement) getLeftPathElement()).getIsDereified()) {
			setEdgeCode(EdgeCode.DEREIFIED);
		}

		Integer rightExitIndex = getRightPathElement().indexVisitor(baseIndex, leftExitIndex, getEdgeCode());
		setExitIndex(rightExitIndex);
		return rightExitIndex;
	}

	/**
	 * Visit path.
	 *
	 * @param path
	 *            the path
	 * @return the path
	 */
	@Override
	public PathBinding visitPath(PathBinding path) {
		path = getLeftPathElement().visitPath(path);
		path = getRightPathElement().visitPath(path);
		return path;
	}

}
