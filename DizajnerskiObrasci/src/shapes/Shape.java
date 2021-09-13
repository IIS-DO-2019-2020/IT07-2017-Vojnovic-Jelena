package shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;

public abstract class Shape implements Movable, Comparable, Serializable {
	
	private boolean selected;
	private Color edgeColor, innerColor;

	public Shape() {
		
	}
	
	public Shape(boolean selected) {
		this.selected = selected;
	}
	
	public abstract boolean contains(int x, int y);
	public abstract void draw(Graphics g);
	public abstract Shape clone();

	
	
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public Color getEdgeColor() {
		return edgeColor;
	}

	public void setEdgeColor(Color edgeColor) {
		this.edgeColor = edgeColor;
	}

	public Color getInnerColor() {
		return innerColor;
	}

	public void setInnerColor(Color innerColor) {
		this.innerColor = innerColor;
	}
}
