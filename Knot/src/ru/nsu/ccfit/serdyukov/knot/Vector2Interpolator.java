package ru.nsu.ccfit.serdyukov.knot;

public class Vector2Interpolator {
	public Vector2Interpolator(double startArgument, double argument0, double argument1, Vector2 value0, Vector2 value1) {
		valueDelta = (value1.subtract(value0)).divide(argument1 - argument0);
		currentValue = value0.add(valueDelta.multiply(startArgument - argument0));
	}
	
	public Vector2 get() {
		return currentValue;
	}
	
	public void step() {
		currentValue = currentValue.add(valueDelta);
	}
	private Vector2 currentValue;
	private Vector2 valueDelta;
}
