package shapes;

import java.awt.Color;
import java.awt.Graphics;

public abstract class Shape implements Moveable, Comparable<Object>, Cloneable {
	
	private boolean selected = false;
	private Color edgeColor;
	
	public Shape() {
		
	}
	
	public Shape(boolean selected) {
		this.selected=selected;
	}
	
	public abstract boolean contains(int x, int y);
	public abstract void draw(Graphics g);
	
	public boolean isSelected() {
		return selected;
	}
	
	public void setSelected(boolean selected) {
		this.selected=selected;
	}
	
	public Color getEdgeColor() {
		return edgeColor;
	}
	
	public void setEdgeColor(Color edgeColor) {
		this.edgeColor=edgeColor;
	}

}
