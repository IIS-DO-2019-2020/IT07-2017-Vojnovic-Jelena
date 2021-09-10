package adapter;

import java.awt.Color;
import java.awt.Graphics;

import hexagon.Hexagon;
import shapes.Point;
import shapes.Shape;

public class HexagonAdapter extends Shape {
	
	private Hexagon hexagon;
	
	public HexagonAdapter (Hexagon hexagon) {
		this.hexagon=hexagon;
	}
	
	public HexagonAdapter (Point center, int radius) {
		this.hexagon = new Hexagon (center.getX(), center.getY(), radius);
	}
	
	public HexagonAdapter (Hexagon hexagon, Color inner, Color edge) {
		this.hexagon=hexagon;
		this.hexagon.setAreaColor(inner);
		this.hexagon.setAreaColor(edge);
	}
	
	public HexagonAdapter (Point center, int radius, Color inner, Color edge) {
		this(center, radius);
		this.hexagon.setAreaColor(inner);
		this.hexagon.setAreaColor(edge);
	}
	

	@Override
	public void moveBy(int byX, int byY) {
		hexagon.setX(byX);
		hexagon.setY(byY);
		
	}

	@Override
	public int compareTo(Object o) {
		if(o instanceof HexagonAdapter)
			return hexagon.getR() - ((HexagonAdapter)o).hexagon.getR();
		return 0;
	}

	@Override
	public boolean contains(int x, int y) {
		return hexagon.doesContain(x, y);
	}

	@Override
	public void draw(Graphics g) {
		this.hexagon.paint(g);
		
	}
	
	public boolean isSelected() {
		return this.hexagon.isSelected();
	}
	
	public void setSelected(boolean selected) {
		this.hexagon.setSelected(selected);
	}
	
	public Color getInnerColor() {
		return this.hexagon.getAreaColor();
	}
	
	public void setInnerColor(Color inner) {
		this.hexagon.setAreaColor(inner);
	}
	
	public Color getEdgeColor () {
		return this.hexagon.getAreaColor();
	}
	
	public void setEdgeColor(Color edge) {
		this.hexagon.setAreaColor(edge);
	}
	
	public Hexagon getHexagon() {
		return hexagon;
	}
	
	public String toString () {
		return "Hexagon Center(" + hexagon.getX() + "|" + hexagon.getY() + ")| Radius(" + hexagon.getR() + ")|EdgeColor("+getEdgeColor().getRGB()+")|InnerColor("+getInnerColor().getRGB() + ")";	
	}


	
	public boolean equals(Object obj) {
		if (obj instanceof HexagonAdapter) {
			HexagonAdapter hex = (HexagonAdapter) obj;
			
			if(hexagon.getX() == hex.hexagon.getX() && hexagon.getY() == hex.hexagon.getY() && hexagon.getR() == hex.hexagon.getR()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

}
