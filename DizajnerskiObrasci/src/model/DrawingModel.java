package model;

import java.util.ArrayList;

import shapes.Point;
import shapes.Shape;

public class DrawingModel {
	
	private ArrayList<Shape> listOfShapes = new ArrayList<Shape>();
	private int i;
	
	public void addShape(Shape shape) {
		listOfShapes.add(shape);
	}
	
	public void removeShape(Shape s) {
		listOfShapes.remove(s);
	}
	
	public Shape getShape(int index) {
		return listOfShapes.get(index);
	}
	
	
	public int getIndex(Shape shape) {
		return listOfShapes.indexOf(shape);
	}
	
	public void setShape(int index, Shape shape) {
		listOfShapes.set(index, shape);
	}
	
	public void addShapeAtIndex(Shape shape, int ind) {
		listOfShapes.add(ind, shape);
	}
	
	public void removeSelected() {
		listOfShapes.removeIf(shape -> shape.isSelected());
	}
	
	public void deselect() {
		listOfShapes.forEach(shape -> shape.setSelected(false));
	}
	
	public void addAll(ArrayList<Shape> shapes) {
		listOfShapes.addAll(shapes);
	}
	
	public void select(Point point) {
		for (i  = listOfShapes.size()-1; i >= 0; i--) {
			if (listOfShapes.get(i).contains(point.getX(), point.getY())) {
				if(listOfShapes.get(i).isSelected()) listOfShapes.get(i).setSelected(false);
				listOfShapes.get(i).setSelected(true);
				return;
			}
		}
	}
	
	public ArrayList<Shape> getSelectedShapes() {
		ArrayList<Shape> selectedShapes = new ArrayList<Shape>();
		
		listOfShapes.forEach(shape -> {
			if(shape.isSelected())
				selectedShapes.add(shape);
		});
		return selectedShapes;
	}
	
	public int getSelected() {
		for (i=listOfShapes.size()-1; i>=0; i--) {
			if(listOfShapes.get(i).isSelected()) {
				return i;
			}
		}
		
		return -1;
	}
	
	public boolean isEmpty() {
		return listOfShapes.isEmpty();
		}
	public ArrayList<Shape> getShapes() {
		return listOfShapes;
	}
		
}

