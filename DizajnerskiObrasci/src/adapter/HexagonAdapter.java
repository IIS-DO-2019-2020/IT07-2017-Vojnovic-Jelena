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
	
	public HexagonAdapter (Point centar, int radius) {
		this.hexagon = new Hexagon (centar.getX(), centar.getY(), radius);
	}
	
	public HexagonAdapter (Hexagon hexagon, Color edge, Color inner) {
		this.hexagon=hexagon;
		this.hexagon.setAreaColor(inner);
		this.hexagon.setBorderColor(edge);
	}
	
	public HexagonAdapter (Point centar, int radius,  Color innerColor, Color edgeColor ) {
		this(centar, radius);
		this.hexagon.setAreaColor(innerColor);
		this.hexagon.setBorderColor(edgeColor);
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
	
	@Override
	public Shape clone() {
		HexagonAdapter hexagon = new HexagonAdapter(new Point(getHexagon().getX(), getHexagon().getY()), getHexagon().getR());
		hexagon.setEdgeColor(getEdgeColor());
		hexagon.setInnerColor(getInnerColor());
		hexagon.setSelected(isSelected());
		
		return hexagon;
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

	public String toString () {
		return "Hexagon Center(" + hexagon.getX() + "|"+hexagon.getY() + ")|Radius(" + hexagon.getR() + ")|EdgeColor("+getEdgeColor().getRGB()+")|InnerColor("+getInnerColor().getRGB() + ")";	
	}

	
	public static HexagonAdapter parse(String shape) {
		shape = shape.replace("Hexagon Center(", "").replace(")", "");
		String[] params = shape.split("\\|");

		int x = Integer.parseInt(params[0]);
		int y = Integer.parseInt(params[1]);
		int r = Integer.parseInt(params[2].replace("Radius(", ""));
		Color edgeColor = Color.decode(params[3].replace("EdgeColor(", ""));
		Color innerColor = Color.decode(params[4].replace("InnerColor(", ""));

		return new HexagonAdapter(new Point(x,y), r, innerColor, edgeColor);
	}

	


	
	public Hexagon getHexagon() {
		return hexagon;
	}
	
	public void setHexagon(Hexagon hexagon) {
		this.hexagon = hexagon;
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
		return this.hexagon.getBorderColor();
	}
	
	public void setEdgeColor(Color edge) {
		this.hexagon.setBorderColor(edge);
	}
	

}
