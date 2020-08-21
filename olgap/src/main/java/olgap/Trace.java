package olgap;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleLiteral;
import org.eclipse.rdf4j.query.algebra.evaluation.ValueExprEvaluationException;
import org.eclipse.rdf4j.query.algebra.evaluation.function.Function;

public class Trace extends Evaluator implements Function{

	@Override
	public String getURI() {
		return NAMESPACE + "trace";
	}

	@SuppressWarnings("static-access")
	@Override
	public Value evaluate(ValueFactory valueFactory, Value... args) throws ValueExprEvaluationException {
		try {
			boolean setTraceOn = ((SimpleLiteral)args[0]).booleanValue();
			Evaluator.setTrace( setTraceOn);

		}catch(Exception e) {
			//Do nothing but return state of trace
		}
		return valueFactory.createLiteral(super.trace);
	}
}
