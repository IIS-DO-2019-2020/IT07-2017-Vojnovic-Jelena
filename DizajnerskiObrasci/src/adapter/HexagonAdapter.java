package adapter;

import java.awt.Color;
import java.awt.Graphics;

import hexagon.Hexagon;
import shapes.Point;
import shapes.SurfaceShape;

public class HexagonAdapter extends SurfaceShape {
	
	private Hexagon hexagon;
	
	public HexagonAdapter() {
		
	}
	
	public HexagonAdapter(Point center, int radius, Color innerColor, Color edgeColor) {
		this.hexagon = new Hexagon(center.getX(), center.getY(), radius);
		this.hexagon.setAreaColor(innerColor);
		this.hexagon.setBorderColor(edgeColor);
	}
	
	public Hexagon getHexagon() {
		return hexagon;
	}
	
	public void setHexagon(Point center, int radius, Color innerColor, Color edgeColor) {
		this.hexagon = new Hexagon(center.getX(), center.getY(), radius);
		this.hexagon.setAreaColor(innerColor);
		this.hexagon.setBorderColor(edgeColor);
		hexagon.setSelected(true); // da bi ostao selektovan nakon modifikacije
	}

	@Override
	public void moveBy(int byX, int byY) {
		hexagon.setX(byX);
		hexagon.setY(byY);
		
	}

	@Override
	public int compareTo(Object o) {
		if (o instanceof HexagonAdapter) {
			HexagonAdapter h = (HexagonAdapter) o;
			return (int) (hexagon.getR() - h.getHexagon().getR());
		} else
			return 0;
	}

	@Override
	public boolean contains(int x, int y) {
		return hexagon.doesContain(x, y);
	}

	@Override
	public void draw(Graphics g) {
		hexagon.paint(g);
		
	}
	
	@Override
	public void fill(Graphics g) {
		g.setColor(hexagon.getAreaColor());
	}
	
	public boolean equals(Object obj) {
		if (obj instanceof HexagonAdapter) {
			HexagonAdapter hexagonAdapter = (HexagonAdapter) obj;
			Point center1 = new Point(hexagon.getX(), hexagon.getY());
			Point center2 = new Point(hexagonAdapter.hexagon.getX(), hexagonAdapter.hexagon.getY());
			if (center1.equals(center2) && hexagon.getR() == hexagonAdapter.getHexagon().getR())
				return true;
			else
				return false;

		} else
			return false;
	}
	
	public boolean isSelected() {
		return hexagon.isSelected();
	}
	
	public void setSelected(boolean selected) {
		hexagon.setSelected(selected);
	}
	
	public Color getInnerColor() {
		return hexagon.getAreaColor();
	}
	
	public void setInnerColor(Color innerColor) {
		hexagon.setAreaColor(innerColor);
	}

	public Color getEdgeColor() {
		return hexagon.getBorderColor();
	}
	
	public void setEdgeColor(Color edgeColor) {
		hexagon.setBorderColor(edgeColor);
	}
	
	public double area() {
		return hexagon.getR() * hexagon.getR() * Math.PI;
	}

}
