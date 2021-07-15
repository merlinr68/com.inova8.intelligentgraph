package path;

import org.eclipse.rdf4j.query.algebra.TupleExpr;

public class PathTupleExpr {
	TupleExpr tupleExpr;
	PathBinding path = new PathBinding();
	
	public PathTupleExpr(TupleExpr tupleExpr) {
		super();
		this.tupleExpr = tupleExpr;
	}
//	public PathTupleExpr(TupleExpr predicatePattern, Path path) {
//		super();
//		this.tupleExpr = tupleExpr;
//		this.path = path;
//	}
	public TupleExpr getTupleExpr() {
		return tupleExpr;
	}
	public PathBinding getPath() {
		return path;
	}
	public String toString(){
		return getTupleExpr().toString();
	}
	public void setTupleExpr(TupleExpr tupleExpr) {
		this.tupleExpr = tupleExpr;
	}
	public Object pathToString() {
		return getPath().toString();
	}

}
