package controller;

import java.awt.Color;

import command.CmdAddShape;
import command.CmdBringToBack;
import command.CmdBringToFront;
import command.CmdDeleteShapes;
import command.CmdToBack;
import command.CmdToFront;
import command.CmdUpdateCircle;
import command.CmdUpdateDonut;
import command.CmdUpdateHexagon;
import command.CmdUpdateLine;
import command.CmdUpdatePoint;
import command.CmdUpdateRectangle;
import command.Command;
import shapes.Rectangle;

import shapes.Shape;
import strategy.Context;
import strategy.FileSerialization;
import strategy.LogFile;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Stack;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
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
	private String typeOfFile = null;
	
	private boolean lineWaitingForSecondPoint = false;
	private Point lineFirstPoint;
	private Color edgeColor, innerColor = Color.WHITE;
	
	private Context context;
	private FileSerialization fileSerialization;
	private LogFile logFile;
	private DefaultListModel<String> log; 
	
	
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
		
		this.log = frame.getDefaultListLogModel();
		
		this.fileSerialization = new FileSerialization(model);
		this.logFile = new LogFile(this, model, frame);
		
		this.observers= new ArrayList<Observer>();
		
		isRedo = false;
	}
	
	public void OperationDrawing(MouseEvent e) {
		Point mouseClick = new Point (e.getX(), e.getY());
		CmdAddShape cmdAddShape;
		
		if(currState == frame.getOPERATION_EDIT_DELETE()) {
			model.getShapes().forEach(shape -> {
				if(shape.contains(e.getX(), e.getY())) {
					if(shape.isSelected()) {
						shape.setSelected(false);
						log.addElement("DESELECT_"+ shape + "|MouseClick_(" + e.getX() +"|"+ e.getY()+")");
					}
					else {
						shape.setSelected(true);
						
						log.addElement("SELECT_" + shape + "|MouseClick_(" + e.getX() +"|"+ e.getY()+")");
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
				dlgHexagon.setColors( frame.getBtnColorEdge().getBackground(), frame.getBtnColorInner().getBackground());
				dlgHexagon.setVisible(true);
				
				if(dlgHexagon.getHexagon()!= null) {
				    frame.getBtnColorInner().setBackground(dlgHexagon.getInnerColor());
					frame.getBtnColorEdge().setBackground(dlgHexagon.getEdgeColor());
				    cmdAddShape = new CmdAddShape(dlgHexagon.getHexagon(), model);
				    execute(cmdAddShape);
				}
				return;
			}	
	}
	
	public void selectDeselectShapeFromLog(int x, int y)
	{
		frame.setActiveOperation(frame.getOPERATION_EDIT_DELETE());
		setCurrState(frame.getOPERATION_EDIT_DELETE());
		MouseEvent e = new MouseEvent(frame.getView(), MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, x,y, 1, false);
		OperationDrawing(e);
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
	 				frame.getBtnColorEdge().setBackground(dlgPoint.getEdgeColor());
					CmdUpdatePoint cmdUpdatePoint = new CmdUpdatePoint((Point) shape , dlgPoint.getPoint());
					execute(cmdUpdatePoint);
	 			}
	 			
	 		} else if (shape instanceof Line) {
	 			DlgLine dlgLine = new DlgLine();
	 			dlgLine.setLine((Line)shape);
	 			dlgLine.setVisible(true);
	 			
	 			if(dlgLine.getLine() !=null) {
	 				frame.getBtnColorEdge().setBackground(dlgLine.getEdgeColor());
					CmdUpdateLine cmdUpdateLine = new CmdUpdateLine((Line) shape , dlgLine.getLine());
					execute(cmdUpdateLine);
	 			}
	 			
	 		} else if (shape instanceof Rectangle) {
	 			DlgRectangle dlgRectangle = new DlgRectangle();
	 			dlgRectangle.setRectangle((Rectangle) shape);
	 			dlgRectangle.setVisible(true);
	 			
	 			if(dlgRectangle.getRectangle() !=null) {
	 				frame.getBtnColorEdge().setBackground(dlgRectangle.getEdgeColor());
					frame.getBtnColorInner().setBackground(dlgRectangle.getInnerColor());
					CmdUpdateRectangle cmdUpdateRectangle = new CmdUpdateRectangle((Rectangle) shape, dlgRectangle.getRectangle());
					execute(cmdUpdateRectangle);
	 			}
	 			
	 		} else if (shape instanceof Circle) {
	 			if (shape instanceof Donut) {
					DlgDonut dlgDonut = new DlgDonut();
					dlgDonut.setDonut((Donut) shape);
					dlgDonut.setVisible(true);
					
					if(dlgDonut.getDonut()!=null) {
						frame.getBtnColorEdge().setBackground(dlgDonut.getEdgeColor());
						frame.getBtnColorInner().setBackground(dlgDonut.getInnerColor());
						CmdUpdateDonut cmdUpdateDonut = new CmdUpdateDonut((Donut) shape, dlgDonut.getDonut());
						execute(cmdUpdateDonut);	
					} 
				 } else {
					 DlgCircle dlgCircle = new DlgCircle();
						dlgCircle.setCircle((Circle) shape);
						dlgCircle.setVisible(true);
						
						if(dlgCircle.getCircle()!=null) {
							frame.getBtnColorEdge().setBackground(dlgCircle.getEdgeColor());
							frame.getBtnColorInner().setBackground(dlgCircle.getInnerColor());
							CmdUpdateCircle cmdUpdateCircle = new CmdUpdateCircle((Circle) shape, dlgCircle.getCircle());
							execute(cmdUpdateCircle);
						}	
				 }
			 
	 			
	 		
	 		} else if (shape instanceof HexagonAdapter) {
				DlgHexagon dlgHexagon = new DlgHexagon();
				dlgHexagon.setHexagon((HexagonAdapter) shape);
				dlgHexagon.setVisible(true);
				
				if(dlgHexagon.getHexagon()!=null) {
					frame.getBtnColorEdge().setBackground(dlgHexagon.getEdgeColor());
					frame.getBtnColorInner().setBackground(dlgHexagon.getInnerColor());
					CmdUpdateHexagon cmdUpdateHexagon = new CmdUpdateHexagon((HexagonAdapter) shape, dlgHexagon.getHexagon());
					execute(cmdUpdateHexagon);
				}
	 		}
	 	}
	 	
	 	public void OperationDelete(ActionEvent e) {
	 		if (model.isEmpty()) return;
			if (JOptionPane.showConfirmDialog(null, "Da li zaista zelite da obrisete selektovane oblike?", "Potvrda", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == 0) {
				CmdDeleteShapes cmdDeleteShapes = new CmdDeleteShapes(model.getSelectedShapes(), model);
				execute(cmdDeleteShapes);
			} 
		}
	 	
	 	
	 	public void readCommand () {
			logFile.readCommand();
		}


		public void saveFile() {

			if(!model.getShapes().isEmpty()) frame.getSaveFileChooser().setFileFilter(frame.getDrawFilter());
			if(!log.isEmpty()) frame.getSaveFileChooser().setFileFilter(frame.getLogFilter());

			if (frame.getSaveFileChooser().showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
				if(frame.getSaveFileChooser().getFileFilter().getDescription() == "Crtez")
					context = new Context(fileSerialization);
				else if(frame.getSaveFileChooser().getFileFilter().getDescription() == "Log")
					context = new Context(logFile);

				context.SaveFile(frame.getSaveFileChooser().getSelectedFile());
			}

			frame.getSaveFileChooser().setVisible(false);

		}

		public void openFile() {

		if(frame.getOpenFileChooser().showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
		{
			log.removeAllElements();

			if(frame.getOpenFileChooser().getFileFilter().getDescription() == "Crtez") 
			{
				context = new Context(fileSerialization); typeOfFile = "Crtez";

			}
			if(frame.getOpenFileChooser().getFileFilter().getDescription() == "Log") context = new Context(logFile); typeOfFile = "Log"; 

			context.OpenFile(frame.getOpenFileChooser().getSelectedFile());
			log.addElement("Imported file from " + frame.getOpenFileChooser().getSelectedFile().toString());
			frame.getView().repaint();
		}

		frame.getOpenFileChooser().setVisible(false);
		notifyObservers();

	}
	 	
	 	public void execute (Command cmd) {
	 		if(!isRedo) unexecutedCmd.clear();
	 		cmd.execute();
	 		if(cmd instanceof CmdAddShape) log.addElement(((CmdAddShape)cmd).getCmdLog());
			else if(cmd instanceof CmdUpdatePoint) log.addElement(((CmdUpdatePoint)cmd).getCmdLog());
			else if(cmd instanceof CmdUpdateCircle) log.addElement(((CmdUpdateCircle)cmd).getCmdLog());
			else if(cmd instanceof CmdUpdateDonut) log.addElement(((CmdUpdateDonut)cmd).getCmdLog());
			else if(cmd instanceof CmdUpdateLine) log.addElement(((CmdUpdateLine)cmd).getCmdLog());
			else if(cmd instanceof CmdUpdateRectangle) log.addElement(((CmdUpdateRectangle)cmd).getCmdLog());
			else if(cmd instanceof CmdUpdateHexagon) log.addElement(((CmdUpdateHexagon)cmd).getCmdLog());
			else if(cmd instanceof CmdDeleteShapes) log.addElement(((CmdDeleteShapes)cmd).getCmdLog());
			else if (cmd instanceof CmdBringToBack) log.addElement(((CmdBringToBack)cmd).getCmdLog());
			else if (cmd instanceof CmdBringToFront) log.addElement(((CmdBringToFront)cmd).getCmdLog());
			else if (cmd instanceof CmdToBack) log.addElement(((CmdToBack)cmd).getCmdLog());
			else if (cmd instanceof CmdToFront) log.addElement(((CmdToFront)cmd).getCmdLog());
	 		executedCmd.push(cmd);
	 		frame.getView().repaint();
	 		notifyObservers();
	 	}
	 	
	 	public void unexecute(Command cmd) {
	 		cmd.unexecute();
	 		if(cmd instanceof CmdAddShape) log.addElement(((CmdAddShape)cmd).getCmdLog());	
			else if(cmd instanceof CmdUpdatePoint) log.addElement(((CmdUpdatePoint)cmd).getCmdLog());
			else if(cmd instanceof CmdUpdateCircle) log.addElement(((CmdUpdateCircle)cmd).getCmdLog());
			else if(cmd instanceof CmdUpdateDonut) log.addElement(((CmdUpdateDonut)cmd).getCmdLog());
			else if(cmd instanceof CmdUpdateLine) log.addElement(((CmdUpdateLine)cmd).getCmdLog());
			else if(cmd instanceof CmdUpdateRectangle) log.addElement(((CmdUpdateRectangle)cmd).getCmdLog());
			else if(cmd instanceof CmdUpdateHexagon) log.addElement(((CmdUpdateHexagon)cmd).getCmdLog());
			else if(cmd instanceof CmdDeleteShapes) log.addElement(((CmdDeleteShapes)cmd).getCmdLog());
			else if (cmd instanceof CmdBringToBack) log.addElement(((CmdBringToBack)cmd).getCmdLog());
			else if (cmd instanceof CmdBringToFront) log.addElement(((CmdBringToFront)cmd).getCmdLog());
			else if (cmd instanceof CmdToBack) log.addElement(((CmdToBack)cmd).getCmdLog());
			else if (cmd instanceof CmdToFront) log.addElement(((CmdToFront)cmd).getCmdLog());
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
	 	
	 	public void toFront() {
	 		int index = model.getSelected();
	 		Shape selectedShape = model.getShape(index);
	 		
	 		if(index== model.getShapes().size()-1) return;
	 		
	 		CmdToFront cmdToFront = new CmdToFront(selectedShape, model);
	 		execute(cmdToFront);
	 		
	 	}
	 	
	 	public void toBack () {
	 		int index = model.getSelected();
	 		Shape selectedShape = model.getShape(index);
	 		
	 		if(index==0) return;
	 		
	 		CmdToBack cmdToBack = new CmdToBack(selectedShape, model);
	 		execute(cmdToBack);
	 	}
	 	
	 	public void bringToFront () {
	 		int index = model.getSelected();
	 		Shape selectedShape = model.getShape(index);
	 		
	 		if(index== model.getShapes().size()-1) return;
	 		
	 		CmdBringToFront cmdBringToFront = new CmdBringToFront(selectedShape, model);
	 		execute(cmdBringToFront);
	 	}
	 	
	 	public void bringToBack() {
	 		int index=model.getSelected();
	 		Shape selectedShape = model.getShape(index);
	 		
	 		if(index==0) return;
	 		
	 		CmdBringToBack cmdBringToBack = new CmdBringToBack(selectedShape, model);
	 		execute(cmdBringToBack);
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
				observer.update(currState, numOfSelectedShapes(), model.getShapes().size(), unexecutedCmd.size(), executedCmd.size(), log.size(), typeOfFile);
			}
			
		}
		
}
