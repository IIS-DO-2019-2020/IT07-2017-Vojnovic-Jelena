package controller;

import java.awt.Color;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import adapter.HexagonAdapter;
import command.CmdAddShape;
import command.CmdBringToBack;
import command.CmdBringToFront;
import command.CmdCircleRemove;
import command.CmdDeleteShapes;
import command.CmdDeselectShape;
import command.CmdDonutRemove;
import command.CmdHexagonRemove;
import command.CmdLineRemove;
import command.CmdPointRemove;
import command.CmdRectangleRemove;
import command.CmdSelectShape;
import command.CmdToBack;
import command.CmdToFront;
import command.CmdUndoRedoStack;
import command.CmdUpdateCircle;
import command.CmdUpdateDonut;
import command.CmdUpdateHexagon;
import command.CmdUpdateLine;
import command.CmdUpdatePoint;
import command.CmdUpdateRectangle;
import dialogs.DlgCircle;
import dialogs.DlgDonut;
import dialogs.DlgHexagon;
import dialogs.DlgLine;
import dialogs.DlgPoint;
import dialogs.DlgRectangle;
import model.DrawingModel;
import observer.EnablingButtonsObserver;
import observer.EnablingButtonsObserverUpdate;
import shapes.Circle;
import shapes.Donut;
import shapes.Line;
import shapes.Point;
import shapes.Rectangle;
import shapes.Shape;
import strategy.LoadLog;
import strategy.LoadManager;
import strategy.LoadShapes;
import strategy.SaveLog;
import strategy.SaveManager;
import strategy.SaveShapes;
import view.DrawingFrame;


public class DrawingController {
	
	private DrawingModel model;
	private DrawingFrame frame;
	
	private DlgPoint dlgPoint = new DlgPoint();
	private DlgLine dlgLine = new DlgLine();
	
	private Shape selectedShape;
	
	private boolean lineWaitingForSecondPoint = false;
	private Point lineFirstPoint;
	
	private CmdUndoRedoStack cmdDeque = new CmdUndoRedoStack();
	
	//OBSERVER
	private EnablingButtonsObserver buttonsObserver = new EnablingButtonsObserver();
	private EnablingButtonsObserverUpdate buttonsObserverUpdate;
	
	
	//LOG
	private SaveManager saveManager;
	private SaveManager saveLog;
	private LoadManager loadManager;
	
	private JFileChooser fileChooser;
	
	private String logLine="";
	private ArrayList<String> logList = new ArrayList<String>();
	private int logCounter =0;
	private Shape shape = null;
	
	
	public DrawingController(DrawingModel model, DrawingFrame frame) {
		this.model=model;
		this.frame=frame;
		
		buttonsObserverUpdate = new EnablingButtonsObserverUpdate(frame);
		buttonsObserver.addPropertyChangeListener(buttonsObserverUpdate);
	}
	
	public void mouseClicked(MouseEvent e){
		
		Point mouseClick = new Point (e.getX(), e.getY());

		//SELEKCIJA
		if (frame.getBtnSelect().isSelected()) {
			ListIterator<Shape> it = model.getShapes().listIterator();
			while (it.hasNext()) {
				selectedShape = it.next();
				if (selectedShape.contains(mouseClick.getX(), mouseClick.getY())) {
					if (selectedShape.isSelected() == false) { // oblik nije vec selektovan -> selektuj
						CmdSelectShape cmdSelect = new CmdSelectShape(model, selectedShape);
						cmdSelect.execute();
						cmdDeque.getUndoDeque().offerLast(cmdSelect);
						frame.getTextArea().append(cmdSelect.log() + "\n");

					} else { // oblik je vec selektovan --> deselect
						CmdDeselectShape cmdDeselect = new CmdDeselectShape(model, selectedShape);
						cmdDeselect.execute();
						frame.getBtnUndo().setEnabled(true);
						frame.getBtnRedo().setEnabled(false);
						cmdDeque.getUndoDeque().offerLast(cmdDeselect);
						frame.getTextArea().append(cmdDeselect.log() + "\n");
					}
				}
				frame.getView().repaint();
			} 
		}
		else 
		{
			//CRTANJE
			
			if(!frame.getBtnLine().isSelected()) lineWaitingForSecondPoint = false;
			
			//POINT
			if(frame.getBtnPoint().isSelected()) {
				Point point = new Point(mouseClick.getX(), mouseClick.getY(), dlgPoint.getColor());
				point.setEdgeColor(frame.getBtnEdgeColor().getBackground());
				CmdAddShape cmd = new CmdAddShape(model, point);
				cmd.execute();
				cmdDeque.getUndoDeque().offerLast(cmd); //smestam oblik u listu
				frame.getBtnUndo().setEnabled(true);
				frame.getBtnRedo().setEnabled(false);
				frame.getTextArea().append(cmd.log() + "\n");
				frame.getBtnEdgeColor().setBackground(point.getEdgeColor());
				frame.getView().repaint();
				return;
				
			//LINE
			} else if(frame.getBtnLine().isSelected()) {
				
				if(lineWaitingForSecondPoint) {
					
					dlgLine.setTxtStartPointXEdt(false);
					dlgLine.setTxtStartPointYEdt(false);
					dlgLine.setTxtEndPointXEdt(false);
					dlgLine.setTxtEndPointYEdt(false);
					dlgLine.setTxtStartPointX(Integer.toString(lineFirstPoint.getX()));
					dlgLine.setTxtStartPointY(Integer.toString(lineFirstPoint.getY()));
					dlgLine.setTxtEndPointX(Integer.toString(mouseClick.getX()));
					dlgLine.setTxtEndPointY(Integer.toString(mouseClick.getY()));
					dlgLine.setCol(frame.getBtnEdgeColor().getBackground());
					// dialogLine.pack();
					dlgLine.setVisible(true);
					
					if (dlgLine.isOk()) {
						Line l = new Line(lineFirstPoint, mouseClick, dlgLine.getCol());
						CmdAddShape cmd = new CmdAddShape(model, l);
						cmdDeque.getUndoDeque().offerLast(cmd);
						cmd.execute();
						cmdDeque.getUndoDeque().offerLast(cmd);
						frame.getBtnUndo().setEnabled(true);
						frame.getBtnRedo().setEnabled(false);
						frame.getTextArea().append(cmd.log() + "\n");
						frame.getBtnEdgeColor().setBackground(l.getEdgeColor());
						frame.getView().repaint();
						
						lineWaitingForSecondPoint=false;
						
						return;
				}
				
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
					frame.getBtnEdgeColor().setBackground(dlgRectangle.getEdgeColor());
					frame.getBtnInnerColor().setBackground(dlgRectangle.getInnerColor());
					CmdAddShape cmd = new CmdAddShape(model, rectangle);
					cmd.execute();
					cmdDeque.getUndoDeque().offerLast(cmd);
					frame.getBtnUndo().setEnabled(true);
					frame.getBtnRedo().setEnabled(false);
					frame.getTextArea().append(cmd.log() + "\n");
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
					frame.getBtnEdgeColor().setBackground(dlgCircle.getEdgeColor());
					frame.getBtnInnerColor().setBackground(dlgCircle.getInnerColor());
					CmdAddShape cmd = new CmdAddShape(model, circle);
					cmd.execute();
					cmdDeque.getUndoDeque().offerLast(cmd);
					frame.getBtnUndo().setEnabled(true);
					frame.getBtnRedo().setEnabled(false);
					frame.getTextArea().append(cmd.log() + "\n");
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
					frame.getBtnEdgeColor().setBackground(dlgDonut.getEdgeColor());
					frame.getBtnInnerColor().setBackground(dlgDonut.getInnerColor());
					CmdAddShape cmd = new CmdAddShape(model, donut);
					cmd.execute();
					cmdDeque.getUndoDeque().offerLast(cmd);
					frame.getBtnUndo().setEnabled(true);
					frame.getBtnRedo().setEnabled(false);
					frame.getTextArea().append(cmd.log() + "\n");
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
					frame.getBtnEdgeColor().setBackground(dlgHexagon.getEdgeColor());
					frame.getBtnInnerColor().setBackground(dlgHexagon.getInnerColor());
					CmdAddShape cmd = new CmdAddShape(model, hexagon);
					cmd.execute();
					cmdDeque.getUndoDeque().offerLast(cmd);
					frame.getBtnUndo().setEnabled(true);
					frame.getBtnRedo().setEnabled(false);
					frame.getTextArea().append(cmd.log() + "\n");
					frame.getView().repaint();
				}
			}
		}
		redoCleaner();
		enablingEditAndDeleteButtons();
	}
	
	// BRISANJE
		public void delete(ActionEvent arg0) {
			if (model.getSelectedShapes().size() != 0) {
				if (JOptionPane.showConfirmDialog(new JFrame(), "Please confirm deletion.", "Confirm",
						JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					for (int i = model.getSelectedShapes().size() - 1; i >= 0; i--) {
						if (model.getSelectedShapes().get(i) instanceof Point) {
							CmdPointRemove cmd = new CmdPointRemove(model, (Point) model.getSelectedShapes().get(i));
							cmd.execute();
							cmdDeque.getUndoDeque().offerLast(cmd);
							frame.getTextArea().append(cmd.log() + "\n");
						} else if (model.getSelectedShapes().get(i) instanceof Line) {
							CmdLineRemove cmd = new CmdLineRemove(model, (Line) model.getSelectedShapes().get(i));
							cmd.execute();
							cmdDeque.getUndoDeque().offerLast(cmd);
							frame.getTextArea().append(cmd.log() + "\n");
						} else if (model.getSelectedShapes().get(i) instanceof Rectangle) {
							CmdRectangleRemove cmd = new CmdRectangleRemove(model,
									(Rectangle) model.getSelectedShapes().get(i));
							cmd.execute();
							cmdDeque.getUndoDeque().offerLast(cmd);
							frame.getTextArea().append(cmd.log() + "\n");
						} else if (model.getSelectedShapes().get(i) instanceof Circle) {
							CmdCircleRemove cmd = new CmdCircleRemove(model, (Circle) model.getSelectedShapes().get(i));
							cmd.execute();
							cmdDeque.getUndoDeque().offerLast(cmd);
							frame.getTextArea().append(cmd.log() + "\n");
						} else if (model.getSelectedShapes().get(i) instanceof Donut) {
							CmdDonutRemove cmd = new CmdDonutRemove(model, (Donut) model.getSelectedShapes().get(i));
							cmd.execute();
							cmdDeque.getUndoDeque().offerLast(cmd);
							frame.getTextArea().append(cmd.log() + "\n");
						} else if (model.getSelectedShapes().get(i) instanceof HexagonAdapter) {
							CmdHexagonRemove cmd = new CmdHexagonRemove(model,
									(HexagonAdapter) model.getSelectedShapes().get(i));
							cmd.execute();
							cmdDeque.getUndoDeque().offerLast(cmd);
							frame.getTextArea().append(cmd.log() + "\n");
						}
						redoCleaner();
						frame.getView().repaint();
						model.getSelectedShapes().remove(i);
						enablingEditAndDeleteButtons();
						frame.getBtnUndo().setEnabled(true);
						frame.getBtnRedo().setEnabled(false);
					}
				}
			}
		}
	
	
	//MODIFIKACIJA
	
	public void edit(ActionEvent e) {
		
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
			CmdUpdatePoint cmd = new CmdUpdatePoint(oldState,newState);
			cmd.execute();
			cmdDeque.getUndoDeque().offerLast(cmd);
			frame.getBtnEdgeColor().setBackground(newState.getEdgeColor()); //da se oboji i dugme na frame-u
			//frame.getView().repaint();
			frame.getTextArea().append(cmd.log() + "\n");
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
				CmdUpdateLine  cmd = new CmdUpdateLine(oldState,newState);
				cmd.execute();
				cmdDeque.getUndoDeque().offerLast(cmd);
				frame.getBtnEdgeColor().setBackground(newState.getEdgeColor());
				//frame.getView().repaint();
				frame.getTextArea().append(cmd.log() + "\n");
			
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
				CmdUpdateRectangle cmd = new CmdUpdateRectangle(oldState, newState);
				cmd.execute();
				cmdDeque.getUndoDeque().offerLast(cmd);
				frame.getBtnEdgeColor().setBackground(newState.getEdgeColor());
				frame.getBtnInnerColor().setBackground(newState.getInnerColor());
				//frame.getView().repaint();
				frame.getTextArea().append(cmd.log() + "\n");
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
					CmdUpdateDonut cmd = new CmdUpdateDonut(oldState, newState);
					cmd.execute();
					cmdDeque.getUndoDeque().offerLast(cmd);
					frame.getBtnEdgeColor().setBackground(newState.getEdgeColor());
					frame.getBtnInnerColor().setBackground(newState.getInnerColor());
					//frame.getView().repaint();
					frame.getTextArea().append(cmd.log() + "\n");
					
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
				CmdUpdateCircle cmd = new CmdUpdateCircle(oldState, newState);
				cmd.execute();
				cmdDeque.getUndoDeque().offerLast(cmd);
				frame.getBtnEdgeColor().setBackground(newState.getEdgeColor());
				frame.getBtnInnerColor().setBackground(newState.getInnerColor());
				//frame.getView().repaint();
				frame.getTextArea().append(cmd.log() + "\n");
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
				CmdUpdateHexagon cmd = new CmdUpdateHexagon(oldState, newState);
				cmd.execute();
				cmdDeque.getUndoDeque().offerLast(cmd);
				frame.getBtnEdgeColor().setBackground(newState.getEdgeColor());
				frame.getBtnInnerColor().setBackground(newState.getInnerColor());
				//frame.getView().repaint();
				frame.getTextArea().append(cmd.log() + "\n");
				}	
		}
		redoCleaner();
		frame.getView().repaint();
		//enablingEditAndDeleteButtons();
	}
	
	//TO BACK
	public void back() {
		int index = model.getSelected();
 		Shape selectedShape = model.getShape(index);

 		if(index==0) return;
		CmdToBack cmd = new CmdToBack(selectedShape, model);
		cmd.execute();
		cmdDeque.getUndoDeque().offerLast(cmd);
		frame.getTextArea().append(cmd.log() + "\n");
		redoCleaner();
		frame.getView().repaint();
	}

	//TO FRONT
	public void front() {
		int index = model.getSelected();
 		Shape selectedShape = model.getShape(index);
 		if(index== model.getShapes().size()-1) return;
 		
		CmdToFront cmd = new CmdToFront(selectedShape, model);
		cmd.execute();
		cmdDeque.getUndoDeque().offerLast(cmd);
		frame.getTextArea().append(cmd.log() + "\n");
		redoCleaner();
		frame.getView().repaint();
	}
	
	//BRING TO FRONT
	public void toFront() {
		int index = model.getSelected();
 		Shape selectedShape = model.getShape(index);

 		if(index== model.getShapes().size()-1) return;
 		
 		CmdBringToFront cmd = new CmdBringToFront(selectedShape,model);
		cmd.execute();
		cmdDeque.getUndoDeque().offerLast(cmd);
		frame.getTextArea().append(cmd.log() + "\n");
		redoCleaner();
		frame.getView().repaint();
	}
	
	//BRING TO BACK
	public void toBack() {
		int index=model.getSelected();
 		Shape selectedShape = model.getShape(index);

 		if(index==0) return;
 		
 		CmdBringToBack cmd = new CmdBringToBack(selectedShape, model);
		cmd.execute();
		cmdDeque.getUndoDeque().offerLast(cmd);
		frame.getTextArea().append(cmd.log() + "\n");
		redoCleaner();
		frame.getView().repaint();
	}
	
	//UNDO
	public void undo() {
		if (!cmdDeque.getUndoDeque().isEmpty()) {
			
			String undoLog = "Undo: " + cmdDeque.getUndoDeque().peekLast().log();
			frame.getTextArea().append(undoLog + "\n");
			cmdDeque.unexecute();
			
			enablingEditAndDeleteButtons();
			redoCleaner();
			frame.getView().repaint();
			
			if (cmdDeque.getUndoDeque().size() == 0) {
				frame.getBtnUndo().setEnabled(false);
			}
		}
	}
	
	//REDO
	public void redo() {
		if (!cmdDeque.getRedoDeque().isEmpty()) {
			
			String redoLog = "Redo: " + cmdDeque.getRedoDeque().peekLast().log();
			frame.getTextArea().append(redoLog + "\n");
			cmdDeque.execute();
			
			enablingEditAndDeleteButtons();
			frame.getView().repaint();
			
			if (cmdDeque.getRedoDeque().size() == 0) {
				frame.getBtnRedo().setEnabled(false);
			}
		}
	}
	
	//OBSERVER
	public void enablingEditAndDeleteButtons() {
		if (model.getSelectedShapes().size() != 0) {
			
			if (model.getSelectedShapes().size() == 1) {
				buttonsObserver.setEditEnabled(true);
				
				buttonsObserver.setBringToBackEnabled(true);
				buttonsObserver.setBringToFrontEnabled(true);
				buttonsObserver.setToBackEnabled(true);
				buttonsObserver.setToFrontEnabled(true);
				
				
			} else {
				buttonsObserver.setEditEnabled(false);

				buttonsObserver.setBringToBackEnabled(false);
				buttonsObserver.setBringToFrontEnabled(false);
				buttonsObserver.setToBackEnabled(false);
				buttonsObserver.setToFrontEnabled(false);
			}
			buttonsObserver.setDeleteEnabled(true);
		} else {
			buttonsObserver.setDeleteEnabled(false);
			buttonsObserver.setEditEnabled(false);

			buttonsObserver.setBringToBackEnabled(false);
			buttonsObserver.setBringToFrontEnabled(false);
			buttonsObserver.setToBackEnabled(false);
			buttonsObserver.setToFrontEnabled(false);
		}
	}

	//REDO CLEANER
	public void redoCleaner() {
		if (frame.getTextArea().getLineCount() > 2) { // izvrsene barem tri metode
			// Pokupi sve lines iz text area i smesti u array
			String[] lines = frame.getTextArea().getText().split("\n");
			ArrayList<String> linesAsAL = new ArrayList<String>();
			for (String line : lines) {
				linesAsAL.add(line);
			}

			boolean isCommandUndo = linesAsAL.get(linesAsAL.size() - 2).startsWith("Undo"); // pretposlednja komanda -
																							// Undo

			boolean lastCommand = linesAsAL.get(linesAsAL.size() - 1).contains("Undo:"); // poslednja komanda -
																							// razlicita od Undo

			if (lastCommand == false && isCommandUndo == true) {
				cmdDeque.getRedoDeque().clear();
				// System.out.println("Redo: " + cmdDeque.getRedoDeque().size());
			}
		}
	}
	
	//SAVE LOG
	public void saveLog() {

		String log = frame.getTextArea().getText();

		// saveManager = new SaveManager(new SaveShapes());
		saveLog = new SaveManager(new SaveLog());

		fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory()); // file explorer
		fileChooser.setAcceptAllFileFilterUsed(false);

		if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			String logPath = selectedFile.getAbsolutePath() + ".txt";

			if (logPath != null) {
				saveLog.saveData(log, logPath);
			}
		}
	}
	
	//SAVE DRAWING
	public void saveDrawing() {

		ArrayList<Object> shapes = new ArrayList<Object>(); // ovde cuvam sve shapes sa panela
		shapes.add(model.getShapes());

		// Kada cuvam drawing, cuva se i log
		saveManager = new SaveManager(new SaveShapes());
		saveLog = new SaveManager(new SaveLog());

		fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory()); // File explorer

		if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile(); // mesto koje je korisnik kliknuo da zeli da mu se tamo
																// sacuva
			String drawingPath = selectedFile.getAbsolutePath() + ".bin";
			String logPath = selectedFile.getAbsolutePath() + ".txt";

			if (drawingPath != null && logPath != null) {
				saveManager.saveData(shapes, drawingPath);
				saveLog.saveData(frame.getTextArea().getText(), logPath);
			}
		}

	}
	
	//LOAD LOG
	public void loadLog() {

		frame.getTextArea().setText("");
		model.getShapes().clear();
		model.getSelectedShapes().clear();
		cmdDeque.getUndoDeque().clear();
		cmdDeque.getRedoDeque().clear();
		frame.getBtnUndo().setEnabled(false);
		frame.getBtnRedo().setEnabled(false);

		//enablingMovingButtons();
		enablingEditAndDeleteButtons();

		frame.getBtnExecuteLog().setEnabled(true);

		frame.getBtnInnerColor().setBackground(SystemColor.control);
		frame.getBtnEdgeColor().setBackground(Color.black);

		loadManager = new LoadManager(new LoadLog());
		JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "txt");
		fileChooser.setFileFilter(filter);

		if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
			String logPath = fileChooser.getSelectedFile().getAbsolutePath();
			// File Reader cita karaktere fajla i prevodi ih iz binarnog jezika u stringove
			// A BufferedReaderu prosledjujemo fileReader i oni rade ZAJEDNO
			FileReader fileReader = (FileReader) loadManager.loadData(logPath);
			BufferedReader bufferLog = new BufferedReader(fileReader);

			try {
				while ((logLine = bufferLog.readLine()) != null) {
					System.out.println(logLine);
					logList.add(logLine);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	//LOAD DRAWING
	@SuppressWarnings("unchecked")
	public void loadDrawing() {

		frame.getTextArea().setText("");
		model.getShapes().clear();
		model.getSelectedShapes().clear();
		cmdDeque.getUndoDeque().clear();
		cmdDeque.getRedoDeque().clear();
		frame.getBtnUndo().setEnabled(false);
		frame.getBtnRedo().setEnabled(false);

		enablingEditAndDeleteButtons();
		//enablingMovingButtons();

		frame.getBtnInnerColor().setBackground(SystemColor.control);
		frame.getBtnEdgeColor().setBackground(Color.black);

		loadManager = new LoadManager(new LoadShapes());
		fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

		// Posto je drawing tipa .bin --> filtriramo prikaz
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Bin files", "bin");
		fileChooser.setFileFilter(filter);

		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			String drawingPath = selectedFile.getAbsolutePath();

			@SuppressWarnings({})
			ArrayList<Object> shapes = (ArrayList<Object>) loadManager.loadData(drawingPath);
			for (Shape s : (ArrayList<Shape>) shapes.get(0)) {
				model.add(s);
			}
		}
		frame.getView().repaint();
	}
	//EXECUTE LOG
	public void executeLog() {
		if (logCounter < logList.size()) {
			logLine = logList.get(logCounter);

			if (logLine.contains("Point")) {
				int x = Integer.parseInt(logLine.substring(logLine.indexOf("(") + 1, logLine.indexOf(",")));
				int y = Integer.parseInt(logLine.substring(logLine.indexOf(",") + 2, logLine.indexOf(")")));
				String color = logLine.substring(logLine.lastIndexOf("(") + 1, logLine.lastIndexOf(")"));
				Color col = new Color(Integer.parseInt(color));
				shape = new Point(x, y, col);
				// CmdPointAdd cmd = new CmdPointAdd(model, (Point) shape); cmd.execute();
			} else if (logLine.contains("Line")) {
				int xS = Integer.parseInt(logLine.substring(logLine.indexOf("(") + 1, logLine.indexOf(",")));
				int yS = Integer.parseInt(logLine.substring(logLine.indexOf(",") + 2, logLine.indexOf(")")));
				int xE = Integer.parseInt(logLine.substring(logLine.indexOf(";") + 3, logLine.lastIndexOf(",")));
				int yE = Integer.parseInt(logLine.substring(logLine.lastIndexOf(",") + 2, logLine.lastIndexOf(")")));
				String color = logLine.substring(logLine.indexOf("{") + 1, logLine.indexOf("}"));
				Color col = new Color(Integer.parseInt(color));
				shape = new Line(new Point(xS, yS), new Point(xE, yE), col);
				// CmdLineAdd cmd = new CmdLineAdd(model, (Line) shape); cmd.execute();
			} else if (logLine.contains("Rectangle")) {
				int x = Integer.parseInt(logLine.substring(logLine.indexOf("(") + 1, logLine.indexOf(",")));
				int y = Integer.parseInt(logLine.substring(logLine.indexOf(",") + 2, logLine.indexOf(")")));
				int h = Integer.parseInt(logLine.substring(logLine.indexOf("=") + 2, logLine.indexOf(";")));
				int w = Integer.parseInt(logLine.substring(logLine.lastIndexOf("=") + 2, logLine.lastIndexOf(";")));
				String insideColor = logLine.substring(logLine.indexOf("{") + 1, logLine.indexOf("}"));
				String outsideColor = logLine.substring(logLine.lastIndexOf("{") + 1, logLine.lastIndexOf("}"));
				Color insCol = new Color(Integer.parseInt(insideColor));
				Color outCol = new Color(Integer.parseInt(outsideColor));
				shape = new Rectangle(new Point(x, y), h, w, insCol, outCol);
				// CmdRectangleAdd cmd = new CmdRectangleAdd(model, (Rectangle) shape);
				// cmd.execute();
			} else if (logLine.contains("Circle")) {
				int x = Integer.parseInt(logLine.substring(logLine.indexOf("(") + 1, logLine.indexOf(",")));
				int y = Integer.parseInt(logLine.substring(logLine.indexOf(",") + 2, logLine.indexOf(")")));
				int r = Integer.parseInt(logLine.substring(logLine.indexOf("=") + 2, logLine.indexOf(";")));
				String insideColor = logLine.substring(logLine.indexOf("{") + 1, logLine.indexOf("}"));
				String outsideColor = logLine.substring(logLine.lastIndexOf("{") + 1, logLine.lastIndexOf("}"));
				Color insCol = new Color(Integer.parseInt(insideColor));
				Color outCol = new Color(Integer.parseInt(outsideColor));
				shape = new Circle(new Point(x, y), r, insCol, outCol);
				// CmdCircleAdd cmd = new CmdCircleAdd(model, (Circle) shape); cmd.execute();
			} else if (logLine.contains("Donut")) {
				int x = Integer.parseInt(logLine.substring(logLine.indexOf("(") + 1, logLine.indexOf(",")));
				int y = Integer.parseInt(logLine.substring(logLine.indexOf(",") + 2, logLine.indexOf(")")));
				int outerR = Integer.parseInt(logLine.substring(logLine.indexOf("=") + 2, logLine.indexOf(";")));
				int innerR = Integer
						.parseInt(logLine.substring(logLine.lastIndexOf("=") + 2, logLine.lastIndexOf(";")));
				String insideColor = logLine.substring(logLine.indexOf("{") + 1, logLine.indexOf("}"));
				String outsideColor = logLine.substring(logLine.lastIndexOf("{") + 1, logLine.lastIndexOf("}"));
				Color insCol = new Color(Integer.parseInt(insideColor));
				Color outCol = new Color(Integer.parseInt(outsideColor));
				try {
					shape = new Donut(new Point(x, y), outerR, innerR, insCol, outCol);
					// CmdDonutAdd cmd = new CmdDonutAdd(model, (Donut) shape); cmd.execute();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (logLine.contains("Hexagon")) {
				int x = Integer.parseInt(logLine.substring(logLine.indexOf("(") + 1, logLine.indexOf(",")));
				int y = Integer.parseInt(logLine.substring(logLine.indexOf(",") + 2, logLine.indexOf(")")));
				int r = Integer.parseInt(logLine.substring(logLine.indexOf("=") + 2, logLine.indexOf(";")));
				String insideColor = logLine.substring(logLine.indexOf("{") + 1, logLine.indexOf("}"));
				String outsideColor = logLine.substring(logLine.lastIndexOf("{") + 1, logLine.lastIndexOf("}"));
				Color insCol = new Color(Integer.parseInt(insideColor));
				Color outCol = new Color(Integer.parseInt(outsideColor));
				shape = new HexagonAdapter(new Point(x, y), r, insCol, outCol);
				// CmdHexagonAdd cmd = new CmdHexagonAdd(model, (HexagonAdapter) shape);
				// cmd.execute();
			}

			if (logLine.startsWith("Redo")) {
				// System.out.println(cmdDeque.getUndoDeque().size());
				redo();
			} else if (logLine.startsWith("Undo")) {
				// System.out.println(cmdDeque.getUndoDeque().size());
				undo();
				// Ovaj deo je potreban kako bi obezbedili da se nakon ponovnog dodavanja
				// obrisanog oblika pomocu undo obezbedi i njegova selekcija kada se ponovo
				// iscrta na panelu!
				if (cmdDeque.getRedoDeque().peekLast().log().contains("Deleted")) {
					for (int i = 0; i < model.getShapes().size(); i++) {
						if (model.getShapes().get(i).equals(shape)) {
							CmdSelectShape cmd = new CmdSelectShape(model, model.getShapes().get(i));
							cmd.execute();
						}
					}
				} else if (cmdDeque.getRedoDeque().peekLast().log().contains("Selected")) {
					for (int i = 0; i < model.getShapes().size(); i++) {
						if (model.getShapes().get(i).equals(shape)) {
							CmdDeselectShape cmd = new CmdDeselectShape(model, model.getShapes().get(i));
							cmd.execute();
						}
					}
				}
				redoCleaner();
			} else if (logLine.contains("Added")) {
				CmdAddShape cmd = new CmdAddShape(model, shape);
				cmd.execute();

				frame.getTextArea().append(logLine + "\n");
				cmdDeque.getUndoDeque().offerLast(cmd);
				redoCleaner();
			} else if (logLine.contains("Selected")) {
				for (int i = 0; i < model.getShapes().size(); i++) {
					if (shape.equals(model.getShapes().get(i))) {
						shape = model.getShapes().get(i);
					}
				}
				CmdSelectShape cmd = new CmdSelectShape(model, shape);
				cmd.execute();

				frame.getTextArea().append(logLine + "\n");
				cmdDeque.getUndoDeque().offerLast(cmd);
				redoCleaner();
			} else if (logLine.contains("Deselected")) {
				for (int i = 0; i < model.getShapes().size(); i++) {
					if (shape.equals(model.getShapes().get(i))) {
						shape = model.getShapes().get(i);
					}
				}
				CmdDeselectShape cmd = new CmdDeselectShape(model, shape);
				cmd.execute();

				frame.getTextArea().append(logLine + "\n");
				cmdDeque.getUndoDeque().offerLast(cmd);
				redoCleaner();
			} else if (logLine.contains("Deleted")) {

				CmdDeleteShapes cmdRemove = new CmdDeleteShapes(model, shape);
				cmdRemove.execute();

				frame.getTextArea().append(logLine + "\n");
				cmdDeque.getUndoDeque().offerLast(cmdRemove);
				redoCleaner();
			} else if (logLine.contains("Edited")) {
				if (shape instanceof Point) {
					Point oldState = (Point) model.getSelectedShapes().get(0);
					int x = Integer.parseInt(logLine.substring(logLine.indexOf("(") + 1, logLine.indexOf(",")));
					int y = Integer.parseInt(logLine.substring(logLine.indexOf(",") + 2, logLine.indexOf(")")));
					String color = logLine.substring(logLine.lastIndexOf("(") + 1, logLine.lastIndexOf(")"));
					Color col = new Color(Integer.parseInt(color));
					Point newState = new Point(x, y, col);

					CmdUpdatePoint cmd = new CmdUpdatePoint(oldState, newState);
					cmd.execute();

					frame.getTextArea().append(logLine + "\n");
					cmdDeque.getUndoDeque().offerLast(cmd);
					redoCleaner();
				} else if (shape instanceof Line) {
					Line oldState = (Line) model.getSelectedShapes().get(0);
					int xS = Integer.parseInt(logLine.substring(logLine.indexOf("(") + 1, logLine.indexOf(",")));
					int yS = Integer.parseInt(logLine.substring(logLine.indexOf(",") + 2, logLine.indexOf(")")));
					int xE = Integer.parseInt(logLine.substring(logLine.indexOf(";") + 3, logLine.lastIndexOf(",")));
					int yE = Integer
							.parseInt(logLine.substring(logLine.lastIndexOf(",") + 2, logLine.lastIndexOf(")")));
					String color = logLine.substring(logLine.indexOf("{") + 1, logLine.indexOf("}"));
					Color col = new Color(Integer.parseInt(color));
					Line newState = new Line(new Point(xS, yS), new Point(xE, yE), col);

					CmdUpdateLine cmd = new CmdUpdateLine(oldState, newState);
					cmd.execute();

					frame.getTextArea().append(logLine + "\n");
					cmdDeque.getUndoDeque().offerLast(cmd);
					redoCleaner();
				} else if (shape instanceof Rectangle) {
					Rectangle oldState = (Rectangle) model.getSelectedShapes().get(0);
					int x = Integer.parseInt(logLine.substring(logLine.indexOf("(") + 1, logLine.indexOf(",")));
					int y = Integer.parseInt(logLine.substring(logLine.indexOf(",") + 2, logLine.indexOf(")")));
					int h = Integer.parseInt(logLine.substring(logLine.indexOf("=") + 2, logLine.indexOf(";")));
					int w = Integer.parseInt(logLine.substring(logLine.lastIndexOf("=") + 2, logLine.lastIndexOf(";")));
					String insideColor = logLine.substring(logLine.indexOf("{") + 1, logLine.indexOf("}"));
					String outsideColor = logLine.substring(logLine.lastIndexOf("{") + 1, logLine.lastIndexOf("}"));
					Color insCol = new Color(Integer.parseInt(insideColor));
					Color outCol = new Color(Integer.parseInt(outsideColor));
					Rectangle newState = new Rectangle(new Point(x, y), h, w, insCol, outCol);

					CmdUpdateRectangle cmd = new CmdUpdateRectangle(oldState, newState);
					cmd.execute();

					frame.getTextArea().append(logLine + "\n");
					cmdDeque.getUndoDeque().offerLast(cmd);
					redoCleaner();
				} else if (shape instanceof Donut) {
					Donut oldState = (Donut) model.getSelectedShapes().get(0);
					int x = Integer.parseInt(logLine.substring(logLine.indexOf("(") + 1, logLine.indexOf(",")));
					int y = Integer.parseInt(logLine.substring(logLine.indexOf(",") + 2, logLine.indexOf(")")));
					int outerR = Integer.parseInt(logLine.substring(logLine.indexOf("=") + 2, logLine.indexOf(";")));
					int innerR = Integer
							.parseInt(logLine.substring(logLine.lastIndexOf("=") + 2, logLine.lastIndexOf(";")));
					String insideColor = logLine.substring(logLine.indexOf("{") + 1, logLine.indexOf("}"));
					String outsideColor = logLine.substring(logLine.lastIndexOf("{") + 1, logLine.lastIndexOf("}"));
					Color insCol = new Color(Integer.parseInt(insideColor));
					Color outCol = new Color(Integer.parseInt(outsideColor));

					try {
						Donut newState = new Donut(new Point(x, y), outerR, innerR, insCol, outCol);
						CmdUpdateDonut cmd = new CmdUpdateDonut(oldState, newState);
						cmd.execute();
						System.out.println("Boja" + newState.getEdgeColor());

						frame.getTextArea().append(logLine + "\n");
						cmdDeque.getUndoDeque().offerLast(cmd);
						redoCleaner();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (shape instanceof Circle) {
					Circle oldState = (Circle) model.getSelectedShapes().get(0);
					int x = Integer.parseInt(logLine.substring(logLine.indexOf("(") + 1, logLine.indexOf(",")));
					int y = Integer.parseInt(logLine.substring(logLine.indexOf(",") + 2, logLine.indexOf(")")));
					int r = Integer.parseInt(logLine.substring(logLine.indexOf("=") + 2, logLine.indexOf(";")));
					String insideColor = logLine.substring(logLine.indexOf("{") + 1, logLine.indexOf("}"));
					String outsideColor = logLine.substring(logLine.lastIndexOf("{") + 1, logLine.lastIndexOf("}"));

					Color insCol = new Color(Integer.parseInt(insideColor));
					Color outCol = new Color(Integer.parseInt(outsideColor));
					Circle newState = new Circle(new Point(x, y), r, insCol, outCol);

					CmdUpdateCircle cmd = new CmdUpdateCircle(oldState, newState);
					cmd.execute();

					frame.getTextArea().append(logLine + "\n");
					cmdDeque.getUndoDeque().offerLast(cmd);
					redoCleaner();
				} else if (shape instanceof HexagonAdapter) {
					HexagonAdapter oldState = (HexagonAdapter) model.getSelectedShapes().get(0);
					int x = Integer.parseInt(logLine.substring(logLine.indexOf("(") + 1, logLine.indexOf(",")));
					int y = Integer.parseInt(logLine.substring(logLine.indexOf(",") + 2, logLine.indexOf(")")));
					int r = Integer.parseInt(logLine.substring(logLine.indexOf("=") + 2, logLine.indexOf(";")));
					String insideColor = logLine.substring(logLine.indexOf("{") + 1, logLine.indexOf("}"));
					String outsideColor = logLine.substring(logLine.lastIndexOf("{") + 1, logLine.lastIndexOf("}"));
					Color insCol = new Color(Integer.parseInt(insideColor));
					Color outCol = new Color(Integer.parseInt(outsideColor));
					HexagonAdapter newState = new HexagonAdapter(new Point(x, y), r, insCol, outCol);

					CmdUpdateHexagon cmd = new CmdUpdateHexagon(oldState, newState);
					cmd.execute();

					frame.getTextArea().append(logLine + "\n");
					cmdDeque.getUndoDeque().offerLast(cmd);
					redoCleaner();
				}
			} else if (logLine.contains("Backward for one position")) {
				CmdToBack cmd = new CmdToBack(shape,model);
				cmd.execute();

				frame.getTextArea().append(logLine + "\n");
				cmdDeque.getUndoDeque().offerLast(cmd);
				redoCleaner();
			} else if (logLine.contains("Forward for one position")) {
				CmdToFront cmd = new CmdToFront(shape,model);
				cmd.execute();

				frame.getTextArea().append(logLine + "\n");
				cmdDeque.getUndoDeque().offerLast(cmd);
				redoCleaner();
			} else if (logLine.contains("Bring to the back")) {
				CmdBringToBack cmd = new CmdBringToBack(shape,model);
				cmd.execute();

				frame.getTextArea().append(logLine + "\n");
				cmdDeque.getUndoDeque().offerLast(cmd);
				redoCleaner();
			} else if (logLine.contains("Bring to the front")) {
				CmdBringToFront cmd = new CmdBringToFront(shape,model);
				cmd.execute();

				frame.getTextArea().append(logLine + "\n");
				cmdDeque.getUndoDeque().offerLast(cmd);
				redoCleaner();
			}
			enablingEditAndDeleteButtons();

			frame.getView().repaint();
			logCounter += 1; // prelazak na sledecu liniju

			if (logCounter == logList.size()) {
				frame.getBtnExecuteLog().setEnabled(false);
			}
		}

	}

	
	public CmdUndoRedoStack getCmdDeque() {
		return cmdDeque;
	}

}
