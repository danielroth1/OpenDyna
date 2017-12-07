package client.ai.component;

import server.components.ArrayPosition;

public class PathElement {
	
	private ArrayPosition arrayPosition;
	private double value;
	private PathElement lastPathElement;
	
	
	public PathElement(ArrayPosition arrayPosition, double value, PathElement p) {
		this.arrayPosition = arrayPosition;
		this.value = value;
		this.lastPathElement = p;
	}


	public ArrayPosition getArrayPosition() {
		return arrayPosition;
	}


	public double getValue() {
		return value;
	}


	public PathElement getLastPathElement() {
		return lastPathElement;
	}


	public void setArrayPosition(ArrayPosition arrayPosition) {
		this.arrayPosition = arrayPosition;
	}


	public void setValue(double value) {
		this.value = value;
	}


	public void setLastArrayPosition(PathElement lastPathElement) {
		this.lastPathElement = lastPathElement;
	}
	
	@Override
	public boolean equals(Object o){
		PathElement p = (PathElement) o;
		return arrayPosition.equals(p.getArrayPosition());
	}
	
	
	
}
