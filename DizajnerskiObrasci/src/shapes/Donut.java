package shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;

public class Donut extends Circle {
	
	private int innerRadius;
	
	public Donut() {
		
	}
	
	public Donut(Point center, int radius, int innerRadius) {
		super(center, radius);
		this.innerRadius = innerRadius;
	}
	
	public Donut(Point center, int radius, int innerRadius, Color edgeColor, Color innerColor) {
		super(center, radius, edgeColor, innerColor);
		this.innerRadius = innerRadius;
		setEdgeColor(edgeColor);
		setInnerColor(innerColor);
	}
	
	public Donut(Point center, int radius, int innerRadius, boolean selected) {
		this(center, radius, innerRadius);
		setSelected(selected);
	}
	
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		Path2D path = new Path2D.Double(Path2D.WIND_EVEN_ODD);
		
		path.append(new Ellipse2D.Double(getCenter().getX() - getRadius(), this.getCenter().getY() - getRadius(), getRadius()*2, getRadius()*2), false);
		path.append(new Ellipse2D.Double(getCenter().getX() - getInnerRadius(), this.getCenter().getY() - getInnerRadius(), getInnerRadius()*2, getInnerRadius()*2), false);
		
		g2d.setColor(getInnerColor());
		g2d.fill(path);
		
		g2d.setColor(getEdgeColor());
		g2d.drawOval(getCenter().getX() - getRadius(), this.getCenter().getY() - getRadius(), getRadius()*2, getRadius()*2);
		g2d.drawOval(getCenter().getX() - getInnerRadius(), this.getCenter().getY() - getInnerRadius(), getInnerRadius()*2, getInnerRadius()*2);
		
		if (isSelected()) {
			g2d.setColor(Color.BLUE);
			g2d.drawRect(getCenter().getX() - 3, getCenter().getY() - 3, 6, 6);
			g2d.drawRect(getCenter().getX() - 3 - getRadius(), getCenter().getY() - 3, 6, 6);
			g2d.drawRect(getCenter().getX() - 3 + getRadius(), getCenter().getY() - 3, 6, 6);
			g2d.drawRect(getCenter().getX() - 3, getCenter().getY() - 3 + getRadius() , 6, 6);
			g2d.drawRect(getCenter().getX() - 3, getCenter().getY() - 3 - getRadius(), 6, 6);
		}
	}
	
	public int compareTo(Object o) {
		if (o instanceof Donut) {
			return (int) (this.area() - ((Donut) o).area());
		}
		return 0;
	}
	
	public boolean contains(int x, int y) {
		double dFromCenter = this.getCenter().distance(x, y);
		return super.contains(x, y) && dFromCenter > innerRadius;
	}
	
	public boolean contains(Point p) {
		double dFromCenter = this.getCenter().distance(p.getX(), p.getY());
		return super.contains(p.getX(), p.getY()) && dFromCenter > innerRadius;
	}
	
	public double area() {
		return super.area() - innerRadius * innerRadius * Math.PI;
	}
	
	public boolean equals(Object obj) {
		if (obj instanceof Donut) {
			Donut d = (Donut) obj;
			if (this.getCenter().equals(d.getCenter()) &&
					this.getRadius() == d.getRadius() &&
					innerRadius == d.getInnerRadius()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public int getInnerRadius() {
		return innerRadius;
	}
	public void setInnerRadius(int innerRadius) {
		this.innerRadius = innerRadius;
	}
	
	public String toString() {
		return "Donut Center(" + super.getCenter().getX() + "|" + super.getCenter().getY() + ")|Radius(" + super.getRadius() + ")|InnerRadius("+ innerRadius +")|EdgeColor("+getEdgeColor().getRGB()+")|InnerColor("+getInnerColor().getRGB() +")";

	}
	
	public static Donut parse(String shape) {
		shape = shape.replace("Donut Center(", "").replace(")", "");
		
		String [] params = shape.split("\\|");
		
		int x = Integer.parseInt(params[0]);
		int y = Integer.parseInt(params[1]);
		int r = Integer.parseInt(params[2].replace("Radius(", ""));
		int ir = Integer.parseInt(params[3].replace("InnerRadius(", ""));
		Color edgeColor = Color.decode(params[4].replace("EdgeColor(", ""));
		Color innerColor = Color.decode(params[5].replace("InnerColor(", ""));
		
		return new Donut(new Point(x,y),r,ir,edgeColor,innerColor);
	}
	
	public Shape clone() {
		Donut donut = new Donut(new Point(getCenter().getX(), getCenter().getY()), getRadius(), getInnerRadius(), getEdgeColor(), getInnerColor() );
		//donut.setEdgeColor(getEdgeColor());
		//donut.setInnerColor(getInnerColor());
		donut.setSelected(isSelected());
		
		return donut;
	}

}
