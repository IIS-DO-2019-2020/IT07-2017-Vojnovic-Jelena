package model;

import java.util.ArrayList;
import java.util.List;

import shapes.Shape;

public class DrawingModel {
	
	private List<Shape> shapes = new ArrayList<Shape>();
	private List<Shape> selectedShapes = new ArrayList<Shape>();
	
	public List<Shape> getShapes() {
		return shapes;
	}
	
	public List<Shape> getSelectedShapes() {
		return selectedShapes;
	}
	
	public void add(Shape p) {
		shapes.add(p);
	}
	
	public void remove(Shape p) {
		shapes.remove(p);
	}
	
	public Shape get(int i) {
		return shapes.get(i);
	}	
	
}
