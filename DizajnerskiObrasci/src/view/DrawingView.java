package view;

import shapes.Shape;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import model.DrawingModel;

public class DrawingView extends JPanel {

	private DrawingModel model;
	private Shape shape;
	
	public void setModel (DrawingModel model) {
		this.model=model;
	}
	
	public DrawingView() {
		setBackground(Color.WHITE);
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		
		if(model!=null) {
			model.getShapes().forEach(shape -> shape.draw(g));
		}
	}
	
}
