package controller;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ListIterator;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import adapter.HexagonAdapter;
import dialogs.DlgCircle;
import dialogs.DlgDonut;
import dialogs.DlgHexagon;
import dialogs.DlgLine;
import dialogs.DlgPoint;
import dialogs.DlgRectangle;
import model.DrawingModel;
import shapes.Circle;
import shapes.Donut;
import shapes.Line;
import shapes.Point;
import shapes.Rectangle;
import shapes.Shape;
import view.DrawingFrame;

public class DrawingController {
	
	private DrawingModel model;
	private DrawingFrame frame;
	
	
	
	private DlgPoint dlgPoint = new DlgPoint();
	private DlgLine dlgLine = new DlgLine();
	
	private Shape selectedShape;
	
	private boolean lineWaitingForSecondPoint = false;
	private Point lineFirstPoint;
	
	public DrawingController(DrawingModel model, DrawingFrame frame) {
		this.model=model;
		this.frame=frame;
	}
	
	public void mouseClicked(MouseEvent e){
		
		Point mouseClick = new Point (e.getX(), e.getY());
		
		//SELEKCIJA
		if(frame.getBtnSelect().isSelected()) {
			ListIterator<Shape> it = model.getShapes().listIterator();
			while (it.hasNext()) {
				selectedShape = it.next();
				if (selectedShape.contains(mouseClick.getX(), mouseClick.getY())) {
					if(selectedShape.isSelected()) {
						selectedShape.setSelected(false); // ukoliko je vec selektovano - deselektuj
					}else	{
						selectedShape.setSelected(true); // ukoliko nije selektovano - selektuj
						}
					}
				frame.getView().repaint();
				}
			
		}
		else 
		{
			//CRTANJE
			
			if(!frame.getBtnLine().isSelected()) lineWaitingForSecondPoint = false;
			
			if(frame.getBtnPoint().isSelected()) {
				model.add(new Point(mouseClick.getX(), mouseClick.getY(), dlgPoint.getColor()));
				frame.getView().repaint();
				return;
				
			} else if(frame.getBtnLine().isSelected()) {
				if(lineWaitingForSecondPoint) {
					model.add(new Line(lineFirstPoint, mouseClick, dlgLine.getCol()));
					lineWaitingForSecondPoint=false;
					frame.getView().repaint();
					return;
					
				}
				lineFirstPoint = mouseClick;
				lineWaitingForSecondPoint = true;
				return;
				
			} else if(frame.getBtnRectangle().isSelected()) {
				DlgRectangle dlgRectangle = new DlgRectangle();
				dlgRectangle.setTxtXKoordinataEnabled(false);
				dlgRectangle.setTxtYKoordinataEnabled(false);
				dlgRectangle.setTxtXCoordinate(Integer.toString(e.getX()));
				dlgRectangle.setTxtYCoordinate(Integer.toString(e.getY()));
				dlgRectangle.setTxtHeight("");
				dlgRectangle.setTxtWidth("");
				dlgRectangle.setEdgeColor(frame.getBtnEdgeColor().getBackground());
				dlgRectangle.setInnerColor(frame.getBtnInnerColor().getBackground());
				dlgRectangle.pack();
				dlgRectangle.setVisible(true);
				
				if(dlgRectangle.isOk() ) {
					int width = Integer.parseInt(dlgRectangle.getTxtWidth());
					int height = Integer.parseInt(dlgRectangle.getTxtHeight());
					Rectangle rectangle = new Rectangle(new Point(mouseClick.getX(), mouseClick.getY()), height, width,
							dlgRectangle.getInnerColor(), dlgRectangle.getEdgeColor());
					model.add(rectangle);
					frame.getView().repaint();
					
				}
				
			} else if (frame.getBtnCircle().isSelected()) {
				DlgCircle dlgCircle = new DlgCircle();
				
				dlgCircle.setTxtKoordXEdt(false);
				dlgCircle.setTxtKoordYEdt(false);
				dlgCircle.setTxtXCoordinate(Integer.toString(e.getX()));
				dlgCircle.setTxtYCoordinate(Integer.toString(e.getY()));
				dlgCircle.setTxtRadius("");
				dlgCircle.setEdgeColor(frame.getBtnEdgeColor().getBackground());
				dlgCircle.setInnerColor(frame.getBtnInnerColor().getBackground());
				dlgCircle.pack();
				dlgCircle.setVisible(true);
				
				if (dlgCircle.isOk()) {
					int radius = Integer.parseInt(dlgCircle.getTxtRadius());
					Circle circle = new Circle(new Point(e.getX(), e.getY()), radius);
					circle.setEdgeColor(dlgCircle.getEdgeColor());
					circle.setInnerColor(dlgCircle.getInnerColor());
					model.add(circle);
					frame.getView().repaint();
				}
			} else if(frame.getBtnDonut().isSelected()) {
				DlgDonut dlgDonut = new DlgDonut();
				
				dlgDonut.setTxtXCoordEditable(false);
				dlgDonut.setTxtYCoordEditable(false);
				dlgDonut.setTxtXCoordinate(Integer.toString(e.getX()));
				dlgDonut.setTxtYCoordinate(Integer.toString(e.getY()));
				dlgDonut.setTxtInnerRadius("");
				dlgDonut.setTxtOuterRadius("");
				dlgDonut.setEdgeColor(frame.getBtnEdgeColor().getBackground());
				dlgDonut.setInnerColor(frame.getBtnInnerColor().getBackground());
				dlgDonut.setVisible(true);
				try {
				if(dlgDonut.isOk()) {
					int radius = Integer.parseInt(dlgDonut.getTxtOuterRadius());
					int innerRadius = Integer.parseInt(dlgDonut.getTxtInnerRadius());
					Donut donut = new Donut(new Point(e.getX(), e.getY()), radius, innerRadius);
					donut.setInnerColor(dlgDonut.getInnerColor());
					donut.setEdgeColor(dlgDonut.getEdgeColor());
					model.add(donut);
					frame.getView().repaint();
					} 
				}catch (Exception e1) {
					JOptionPane.showMessageDialog(new JFrame(), "Inner radius shoud be less than outer radius!", "Error",
							JOptionPane.WARNING_MESSAGE);
				}
				
			} else if (frame.getBtnHexagon().isSelected())  {
				DlgHexagon dlgHexagon = new DlgHexagon();
				
				dlgHexagon.setTxtKoordXEdt(false);
				dlgHexagon.setTxtKoordYEdt(false);
				dlgHexagon.setTxtXCoordinate(Integer.toString(e.getX()));
				dlgHexagon.setTxtYCoordinate(Integer.toString(e.getY()));
				dlgHexagon.setTxtRadius("");
				dlgHexagon.setEdgeColor(frame.getBtnEdgeColor().getBackground());
				dlgHexagon.setInnerColor(frame.getBtnInnerColor().getBackground());
				dlgHexagon.pack();
				dlgHexagon.setVisible(true);
				
				if (dlgHexagon.isOk()) {
					int radius = Integer.parseInt(dlgHexagon.getTxtRadius());
					HexagonAdapter hexagon = new HexagonAdapter(new Point(e.getX(), e.getY()), radius,
							dlgHexagon.getInnerColor(), dlgHexagon.getEdgeColor());
					model.add(hexagon);
					frame.getView().repaint();
				}
			}
		}
		
	}
	
	//BRISANJE
	public void delete(ActionEvent e) {
 		if (model.isEmpty()) return;
		if (JOptionPane.showConfirmDialog(null, "Da li zaista zelite da obrisete selektovane oblike?", "Potvrda", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == 0) model.removeSelected();
		frame.getView().repaint();
	}
	
	
	//MODIFIKACIJA
	
	public void edit(ActionEvent e) {
		
		int index  = model.getSelected();
		Shape shape = model.getSelectedShapes().get(0);
		
		if(shape instanceof Point) {
			DlgPoint dlgPoint = new DlgPoint();
			Point oldState = (Point) shape;
			dlgPoint.setTxtXEdt(true);
			dlgPoint.setTxtYEdt(true);
			dlgPoint.setTxtX(Integer.toString(oldState.getX()));
			dlgPoint.setTxtY(Integer.toString(oldState.getY()));
			dlgPoint.setColor(oldState.getEdgeColor());
			dlgPoint.setVisible(true);
			
			if (dlgPoint.isOk()) {
				Point newState = new Point(Integer.parseInt(dlgPoint.getTxtX()),
						Integer.parseInt(dlgPoint.getTxtY()), dlgPoint.getColor());
				model.setShape(index, newState);
				frame.getView().repaint();
			
			}
		} else if (shape instanceof Line) {
			DlgLine dlgLine = new DlgLine();
			Line oldState = (Line) shape;
			dlgLine.setTxtStartPointXEdt(true);
			dlgLine.setTxtStartPointYEdt(true);
			dlgLine.setTxtEndPointXEdt(true);
			dlgLine.setTxtEndPointYEdt(true);
			dlgLine.setTxtStartPointX(Integer.toString(oldState.getStartPoint().getX()));
			dlgLine.setTxtStartPointY(Integer.toString(oldState.getStartPoint().getY()));
			dlgLine.setTxtEndPointX(Integer.toString(oldState.getEndPoint().getX()));
			dlgLine.setTxtEndPointY(Integer.toString(oldState.getEndPoint().getY()));
			dlgLine.setCol(oldState.getEdgeColor());
			dlgLine.setVisible(true);

			if (dlgLine.isOk()) {
				Line newState = new Line(
						new Point(Integer.parseInt(dlgLine.getTxtStartPointX()),
								Integer.parseInt(dlgLine.getTxtStartPointY())),
						new Point(Integer.parseInt(dlgLine.getTxtEndPointX()),
								Integer.parseInt(dlgLine.getTxtEndPointY())),
						dlgLine.getCol());
				model.setShape(index, newState);
				frame.getView().repaint();
			
					}
		} else if (shape instanceof Rectangle) {
			Rectangle oldState = (Rectangle) shape;
			DlgRectangle dlgRectangle = new DlgRectangle();
			dlgRectangle.setTxtXKoordinataEnabled(true);
			dlgRectangle.setTxtYKoordinataEnabled(true);
			dlgRectangle.setTxtXCoordinate(Integer.toString(oldState.getUpperLeftPoint().getX()));
			dlgRectangle.setTxtYCoordinate(Integer.toString(oldState.getUpperLeftPoint().getY()));
			dlgRectangle.setTxtHeight(Integer.toString(oldState.getHeight()));
			dlgRectangle.setTxtWidth(Integer.toString(oldState.getWidth()));
			dlgRectangle.setEdgeColor(oldState.getEdgeColor());
			dlgRectangle.setInnerColor(oldState.getInnerColor());
			dlgRectangle.pack();
			dlgRectangle.setVisible(true);
			
			if (dlgRectangle.isOk()) {
				Rectangle newState = new Rectangle(
						new Point(Integer.parseInt(dlgRectangle.getTxtXCoordinate()),
								Integer.parseInt(dlgRectangle.getTxtYCoordinate())),
						Integer.parseInt(dlgRectangle.getTxtHeight()),
						Integer.parseInt(dlgRectangle.getTxtWidth()), dlgRectangle.getInnerColor(),
						dlgRectangle.getEdgeColor());
				model.setShape(index, newState);
				frame.getView().repaint();
			}

					
		} else if(shape instanceof Donut) {
			Donut oldState = (Donut) shape;
			DlgDonut dglDonut = new DlgDonut();
			dglDonut.setTxtXCoordEditable(true);
			dglDonut.setTxtYCoordEditable(true);
			dglDonut.setTxtXCoordinate(Integer.toString(oldState.getCenter().getX()));
			dglDonut.setTxtYCoordinate(Integer.toString(oldState.getCenter().getY()));
			dglDonut.setTxtInnerRadius(Integer.toString(oldState.getInnerRadius()));
			dglDonut.setTxtOuterRadius(Integer.toString(oldState.getRadius()));
			dglDonut.setEdgeColor(oldState.getEdgeColor());
			dglDonut.setInnerColor(oldState.getInnerColor());
			// dialogDonut.pack();
			dglDonut.setVisible(true);
			
			if (dglDonut.isOk()) {
				try {
					Donut newState = new Donut(
							new Point(Integer.parseInt(dglDonut.getTxtXCoordinate()),
									Integer.parseInt(dglDonut.getTxtYCoordinate())),
							Integer.parseInt(dglDonut.getTxtOuterRadius()),
							Integer.parseInt(dglDonut.getTxtInnerRadius()));
					newState.setEdgeColor(dglDonut.getEdgeColor());
					newState.setInnerColor(dglDonut.getInnerColor());
					model.setShape(index, newState);
					frame.getView().repaint();
					
				} catch (NumberFormatException e3) {
					JOptionPane.showMessageDialog(new JFrame(), "Wrong entry!", "Error", JOptionPane.WARNING_MESSAGE);
				} catch (Exception e4) {
					JOptionPane.showMessageDialog(new JFrame(), "Inner radius shoud be less than outer radius!",
							"Error", JOptionPane.WARNING_MESSAGE);
				}
			}
	
		
		} else if (shape instanceof Circle) {
			
			Circle oldState = (Circle) shape;
			DlgCircle dlgCircle = new DlgCircle();
			dlgCircle.setTxtKoordXEdt(true);
			dlgCircle.setTxtKoordYEdt(true);
			dlgCircle.setTxtXCoordinate(Integer.toString(oldState.getCenter().getX()));
			dlgCircle.setTxtYCoordinate(Integer.toString(oldState.getCenter().getY()));
			dlgCircle.setTxtRadius(Integer.toString(oldState.getRadius()));
			dlgCircle.setInnerColor(oldState.getInnerColor());
			dlgCircle.setEdgeColor(oldState.getEdgeColor());
			dlgCircle.pack();
			dlgCircle.setVisible(true);

			if (dlgCircle.isOk()) {
				Circle newState = new Circle(
						new Point(Integer.parseInt(dlgCircle.getTxtXCoordinate()),
								Integer.parseInt(dlgCircle.getTxtYCoordinate())),
						Integer.parseInt(dlgCircle.getTxtRadius()), dlgCircle.getInnerColor(),
						dlgCircle.getEdgeColor());
				model.setShape(index, newState);
				frame.getView().repaint();
				}	
			
		} else if (shape instanceof HexagonAdapter) {
			
			HexagonAdapter oldState =(HexagonAdapter) shape;
			DlgHexagon dlgHexagon = new DlgHexagon();
			
			dlgHexagon.setTxtKoordXEdt(true);
			dlgHexagon.setTxtKoordYEdt(true);
			dlgHexagon.setTxtXCoordinate(Integer.toString(oldState.getHexagon().getX()));
			dlgHexagon.setTxtYCoordinate(Integer.toString(oldState.getHexagon().getY()));
			dlgHexagon.setTxtRadius(Integer.toString(oldState.getHexagon().getR()));
			dlgHexagon.setEdgeColor(oldState.getHexagon().getBorderColor());
			dlgHexagon.setInnerColor(oldState.getHexagon().getAreaColor());
			dlgHexagon.pack();
			dlgHexagon.setVisible(true);
			
			if (dlgHexagon.isOk()) {
				HexagonAdapter newState = new HexagonAdapter(
						new Point(Integer.parseInt(dlgHexagon.getTxtXCoordinate()),
								Integer.parseInt(dlgHexagon.getTxtYCoordinate())),
						Integer.parseInt(dlgHexagon.getTxtRadius()), dlgHexagon.getInnerColor(),
						dlgHexagon.getEdgeColor());
				model.setShape(index, newState);
				frame.getView().repaint();
				}	
		}
	}
}
