package controller;

import java.awt.Color;

import command.CmdAddShape;
import command.Command;
import shapes.Rectangle;

import shapes.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Stack;

import javax.swing.JOptionPane;

import adapter.HexagonAdapter;
import dialogs.DlgCircle;
import dialogs.DlgDonut;
import dialogs.DlgHexagon;
import dialogs.DlgLine;
import dialogs.DlgPoint;
import dialogs.DlgRectangle;
import model.DrawingModel;
import observer.Observer;
import observer.Subject;
import shapes.Circle;
import shapes.Donut;
import shapes.Line;
import shapes.Point;
import view.FrameDrawing;

public class DrawingController implements Subject {
	
	private DrawingModel model;
	private FrameDrawing frame;
	private int currState;
	//for command pattern
	private Stack<Command> executedCmd;
	private Stack<Command> unexecutedCmd;
	private boolean isRedo;
	
	private boolean lineWaitingForSecondPoint = false;
	private Point lineFirstPoint;
	private Color edgeColor, innerColor = Color.WHITE;
	
	private ArrayList<Observer> observers;
	
	public void setCurrState(int state) {
		this.currState=state;
		notifyObservers();
	}
	
	public DrawingController(DrawingModel model, FrameDrawing frame) {
		this.model=model;
		this.frame=frame;
		
		this.edgeColor=Color.BLACK;
		this.innerColor=Color.WHITE;
		
		this.executedCmd = new Stack<Command>();
		this.unexecutedCmd = new Stack<Command>();
		
		this.observers= new ArrayList<Observer>();
	}
	
	public void OperationDrawing(MouseEvent e) {
		Point mouseClick = new Point (e.getX(), e.getY());
		CmdAddShape cmdAddShape;
		
		if(currState == frame.getOPERATION_EDIT_DELETE()) {
			model.getShapes().forEach(shape -> {
				if(shape.contains(e.getX(), e.getY())) {
					if(shape.isSelected()) {
						shape.setSelected(false);
					}
					else {
						shape.setSelected(true);
					}
					notifyObservers();
				}
			});
			
			frame.getView().repaint();
			return;
		}
		
		else
		
			model.deselect();
			frame.getView().repaint();
			
			if(!frame.getBtnShapeLine().isSelected()) lineWaitingForSecondPoint = false;
			
			
			if(frame.getBtnShapePoint().isSelected()) {
				Point point = new Point(mouseClick.getX(), mouseClick.getY(), edgeColor);
				point.setEdgeColor(frame.getBtnColorEdge().getBackground());
				cmdAddShape = new CmdAddShape(point, model);
				execute(cmdAddShape);
				
				return;
				
				
			} else if (frame.getBtnShapeLine().isSelected()) {
				
				if(lineWaitingForSecondPoint) {
					Line line = new Line(lineFirstPoint, mouseClick, edgeColor);
					line.setEdgeColor(frame.getBtnColorEdge().getBackground());
					cmdAddShape = new CmdAddShape(line, model);
					execute(cmdAddShape);
					
					lineWaitingForSecondPoint=false;
					return;
				}
				
				lineFirstPoint = mouseClick;
				lineWaitingForSecondPoint = true;
				return;
				
				
			} else if (frame.getBtnShapeRectangle().isSelected()) {
				
				DlgRectangle dlgRectangle  = new DlgRectangle();
				dlgRectangle.setPoint(mouseClick);
				dlgRectangle.setColors(frame.getBtnColorEdge().getBackground(), frame.getBtnColorInner().getBackground());
				dlgRectangle.setVisible(true);
				
				if(dlgRectangle.getRectangle() != null) {
					frame.getBtnColorEdge().setBackground(dlgRectangle.getEdgeColor());
					frame.getBtnColorInner().setBackground(dlgRectangle.getInnerColor());
					cmdAddShape = new CmdAddShape(dlgRectangle.getRectangle(), model);
					execute(cmdAddShape);
				}
				return;
					
					
			} else if (frame.getBtnShapeCircle().isSelected()) {
				
				DlgCircle dlgCircle = new DlgCircle();
				dlgCircle.setPoint(mouseClick);
				dlgCircle.setColors(frame.getBtnColorEdge().getBackground(), frame.getBtnColorInner().getBackground());
				dlgCircle.setVisible(true);
				
				if(dlgCircle.getCircle() != null) {
					frame.getBtnColorEdge().setBackground(dlgCircle.getEdgeColor());
					frame.getBtnColorInner().setBackground(dlgCircle.getInnerColor());
					
					cmdAddShape = new CmdAddShape(dlgCircle.getCircle(), model);
					execute(cmdAddShape);
				}
				
				return;

			} else if (frame.getBtnShapeDonut().isSelected()) {
				
				DlgDonut dlgDonut = new DlgDonut ();
				dlgDonut.setPoint(mouseClick);
				dlgDonut.setColors(frame.getBtnColorEdge().getBackground(), frame.getBtnColorInner().getBackground());
				dlgDonut.setVisible(true);
				
				if(dlgDonut.getDonut() != null) {
					frame.getBtnColorEdge().setBackground(dlgDonut.getEdgeColor());
					frame.getBtnColorInner().setBackground(dlgDonut.getInnerColor());
					
					cmdAddShape = new CmdAddShape (dlgDonut.getDonut(), model);
					execute(cmdAddShape);
				}
				return;
				
			}  else if (frame.getBtnShapeHexagon().isSelected()) {
				
				DlgHexagon dlgHexagon = new DlgHexagon();
				dlgHexagon.setPoint(mouseClick);
				dlgHexagon.setColors(frame.getBtnColorEdge().getBackground(), frame.getBtnColorInner().getBackground());
				dlgHexagon.setVisible(true);
				
				if(dlgHexagon.getHexagon()!= null) {
					frame.getBtnColorEdge().setBackground(dlgHexagon.getEdgeColor());
				    frame.getBtnColorInner().setBackground(dlgHexagon.getInnerColor());
				    cmdAddShape = new CmdAddShape(dlgHexagon.getHexagon(), model);
				    execute(cmdAddShape);
				}
				return;
			}	
	}
	
	 	public void OperationEdit(ActionEvent e) {
	 		int index = model.getSelected();
	 		if (index ==-1) return;
	 		
	 		Shape shape = model.getShape(index);
	 		
	 		if (shape instanceof Point) {
	 			DlgPoint dlgPoint = new DlgPoint();
	 			dlgPoint.setPoint((Point)shape);
	 			dlgPoint.setVisible(true);
	 			
	 			if(dlgPoint.getPoint()!=null) {
	 				model.setShape(index, dlgPoint.getPoint());
	 				frame.getView().repaint();
	 			}
	 		} else if (shape instanceof Line) {
	 			DlgLine dlgLine = new DlgLine();
	 			dlgLine.setLine((Line)shape);
	 			dlgLine.setVisible(true);
	 			
	 			if(dlgLine.getLine() !=null) {
	 				model.setShape(index, dlgLine.getLine());
	 				frame.getView().repaint();
	 			}
	 		} else if (shape instanceof Rectangle) {
	 			DlgRectangle dlgRectangle = new DlgRectangle();
	 			dlgRectangle.setRectangle((Rectangle)shape);
	 			dlgRectangle.setVisible(true);
	 			
	 			if(dlgRectangle.getRectangle() !=null) {
	 				model.setShape(index, dlgRectangle.getRectangle());
	 				frame.getView().repaint();
	 			}
	 			
	 		} else if (shape instanceof Donut) {
	 			DlgDonut dlgDonut = new DlgDonut();
	 			dlgDonut.setDonut((Donut)shape);
	 			dlgDonut.setVisible(true);
	 			
	 			if(dlgDonut.getDonut() !=null) {
	 				model.setShape(index, dlgDonut.getDonut());
	 				frame.getView().repaint();
	 			}
	 		} else if (shape instanceof Circle) {
	 			DlgCircle dlgCircle = new DlgCircle();
	 			dlgCircle.setCircle((Circle)shape);
	 			dlgCircle.setVisible(true);
	 			
	 			if(dlgCircle.getCircle() !=null) {
	 				model.setShape(index, dlgCircle.getCircle());
	 				frame.getView().repaint();
	 			}
	 		} else if (shape instanceof HexagonAdapter) {
				DlgHexagon dlgHexagon = new DlgHexagon();
				dlgHexagon.setHexagon((HexagonAdapter) shape);
				dlgHexagon.setVisible(true);
				
				if(dlgHexagon.getHexagon()!=null) {
					model.setShape(index, dlgHexagon.getHexagon());
					frame.getView().repaint();
				}
	 		}
	 	}
	 	
	 	public void OperationDelete(ActionEvent e) {
	 		if (model.isEmpty()) return;
			if (JOptionPane.showConfirmDialog(null, "Da li zaista zelite da obrisete selektovane oblike?", "Potvrda", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == 0) model.removeSelected();
			frame.getView().repaint();
		}
	 	
	 	public void execute (Command cmd) {
	 		if(!isRedo) unexecutedCmd.clear();
	 		cmd.execute();
	 		executedCmd.push(cmd);
	 		frame.getView().repaint();
	 		notifyObservers();
	 	}
	 	
	 	public void unexecute(Command cmd) {
	 		cmd.unexecute();
	 		unexecutedCmd.push(cmd);
	 		frame.getView().repaint();
	 		notifyObservers();
	 	}
	 	
	 	public void undoCommand() {
	 		if(!executedCmd.isEmpty()) {
	 			Command undoCmd = executedCmd.pop();
	 			unexecute(undoCmd);
	 		}
	 	}
	 	
	 	public void redoCommand() {
	 		if(!unexecutedCmd.isEmpty()) {
	 			Command redoCmd = unexecutedCmd.pop();
	 			isRedo = true;
	 			execute(redoCmd);
	 		}
	 		isRedo=false;
	 	}
	 	
	 	public int numOfSelectedShapes() {
	 		int numOfSelectedShapes = model.getSelectedShapes().size();
	 		return numOfSelectedShapes;
	 	}
		
		public Color getEdgeColor() {
			return edgeColor;
		}
		
		public Color getInnerColor() {
			return innerColor;
		}
		public void setEdgeColor(Color edgeColor) {
			this.edgeColor = edgeColor;
		}
		
		public void setInnerColor(Color innerColor) {
			this.innerColor = innerColor;
		}

		@Override
		public void addObserver(Observer observer) {
			observers.add(observer);
			
		}

		@Override
		public void removeObserver(Observer observer) {
			observers.remove(observer);
			
		}

		@Override
		public void notifyObservers() {
			for(Observer observer: observers) {
				observer.update(currState, numOfSelectedShapes(), model.getShapes().size());
			}
			
		}
		
}
