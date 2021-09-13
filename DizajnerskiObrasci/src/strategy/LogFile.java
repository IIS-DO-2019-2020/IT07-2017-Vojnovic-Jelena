package strategy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;

import adapter.HexagonAdapter;
import command.CmdBringToBack;
import command.CmdAddShape;
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
import controller.DrawingController;
import model.DrawingModel;
import shapes.Circle;
import shapes.Donut;
import shapes.Line;
import shapes.Point;
import shapes.Rectangle;
import shapes.Shape;
import view.FrameDrawing;

public class LogFile implements Strategy {

	private DrawingController controller;
	private DrawingModel model;
	private FrameDrawing frame;
	private BufferedReader reader;
	private BufferedWriter writer;
	
	public LogFile(DrawingController controller, DrawingModel model, FrameDrawing frame) {
		this.controller = controller;
		this.model = model;
		this.frame = frame;
	}

	@Override
	public void SaveFile(File file) {
		try {
			writer = new BufferedWriter(new FileWriter(file + ".log"));
			DefaultListModel<String> dlm = frame.getDefaultListLogModel();
			for(int i = 0; i < dlm.size(); i++) {
				writer.write(dlm.getElementAt(i));
				writer.newLine();
			}
			writer.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}	
		
	}

	@Override
	public void OpenFile(File file) {
		try {
			reader = new BufferedReader(new FileReader(file));
	
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	public void readCommand() {
		try {
			Command command;
			int index;
			String operation= reader.readLine();
			
			if(operation== null) 
			{
				frame.getBtnReadCommand().setEnabled(false);
				return;
			}
			
			String [] cmdOperation = operation.split("_");
				if(cmdOperation[0].equals("EXECUTE")) {
					switch(cmdOperation[1]) {
					case "ADD":
						command = new CmdAddShape(parseShape(cmdOperation[2]),model);
						controller.execute(command);
						break;
					case "UPDATE":
						String[] arrayOfShapes;
						
						if(cmdOperation[2].startsWith("Point")) {
							arrayOfShapes = cmdOperation[2].split("-->");
							Point oldState = Point.parse(arrayOfShapes[0]);
							Point newState = Point.parse(arrayOfShapes[1]);
							
							index = model.getIndex(oldState);
							
							command = new CmdUpdatePoint((Point)model.getShape(index), newState);
							controller.execute(command);
						}
						else if(cmdOperation[2].startsWith("Line")) {
							arrayOfShapes = cmdOperation[2].split("-->");
							Line oldState = Line.parse(arrayOfShapes[0]);
							Line newState = Line.parse(arrayOfShapes[1]);
							
							index = model.getIndex(oldState);
							
							command = new CmdUpdateLine((Line)model.getShape(index), newState);
							controller.execute(command);
						}
						else if(cmdOperation[2].startsWith("Circle")) {
							arrayOfShapes = cmdOperation[2].split("-->");
							Circle oldState = Circle.parse(arrayOfShapes[0]);
							Circle newState = Circle.parse(arrayOfShapes[1]);
							
							index = model.getIndex(oldState);
							
							command = new CmdUpdateCircle((Circle)model.getShape(index), newState);
							controller.execute(command);
						}
						else if(cmdOperation[2].startsWith("Donut")) {
							arrayOfShapes = cmdOperation[2].split("-->");
							Donut oldState = Donut.parse(arrayOfShapes[0]);
							Donut newState = Donut.parse(arrayOfShapes[1]);
							
							index = model.getIndex(oldState);
							
							command = new CmdUpdateDonut((Donut)model.getShape(index), newState);
							controller.execute(command);
						}
						else if(cmdOperation[2].startsWith("Rectangle")) {
							arrayOfShapes = cmdOperation[2].split("-->");
							Rectangle oldState = Rectangle.parse(arrayOfShapes[0]);
							Rectangle newState = Rectangle.parse(arrayOfShapes[1]);
							
							index = model.getIndex(oldState);
							
							command = new CmdUpdateRectangle((Rectangle)model.getShape(index), newState);
							controller.execute(command);
						}
						else if (cmdOperation[2].startsWith("Hexagon")) {
							arrayOfShapes = cmdOperation[2].split("-->");
							HexagonAdapter oldState = HexagonAdapter.parse(arrayOfShapes[0]);
							HexagonAdapter newState = HexagonAdapter.parse(arrayOfShapes[1]);
							
							index = model.getIndex(oldState);
							
							command = new CmdUpdateHexagon((HexagonAdapter)model.getShape(index), newState);
							controller.execute(command);
						}
						break;
					case "DELETE":
						String shapes = cmdOperation[2].replace("[", "").replace("]", "");
						ArrayList<Shape> shapesForDelete = new ArrayList<Shape>();
						String [] s = shapes.split(",");
						
						for(int i = 0; i < s.length; i++) {
							shapesForDelete.add(parseShape(s[i].trim()));
						}
						
						command = new CmdDeleteShapes(shapesForDelete, model);
						controller.execute(command);
						break;
					case "BRING-TO-BACK":
						index = model.getIndex(parseShape(cmdOperation[2]));
						command = new CmdBringToBack(model.getShape(index), model);
						controller.execute(command);
						break;
					case "BRING-TO-FRONT":
						index = model.getIndex(parseShape(cmdOperation[2]));
						command = new CmdBringToFront(model.getShape(index), model);
						controller.execute(command);
						break;
					case "TO-BACK":
						index = model.getIndex(parseShape(cmdOperation[2]));
						command = new CmdToBack(model.getShape(index), model);
						controller.execute(command);
						break;
					case "TO-FRONT":
						index = model.getIndex(parseShape(cmdOperation[2]));
						command = new CmdToFront(model.getShape(index), model);
						controller.execute(command);
						break;
					}
				} 
				else if(cmdOperation[0].equals("UNEXECUTE")) {
					switch(cmdOperation[1]) {
					case "ADD":
						controller.undoCommand();
						break;
					case "UPDATE":
						controller.undoCommand();
						break;
					case "DELETE":
						controller.undoCommand();
						break;
					case "BRING-TO-BACK":
						controller.undoCommand();
						break;
					case "BRING-TO-FRONT":
						controller.undoCommand();
						break;
					case "TO-BACK":
						controller.undoCommand();
						break;
					case "TO-FRONT":
						controller.undoCommand();
						break;
					}
				}
				else if (cmdOperation[0].equals("SELECT")) {
					String e = cmdOperation[2];
					String [] pointForSelect = e.split("\\|");
					int x = Integer.parseInt(pointForSelect[0].replace("(", ""));
					int y = Integer.parseInt(pointForSelect[1].replace(")", ""));
					controller.selectDeselectShapeFromLog(x, y);
				}
				else if(cmdOperation[0].equals("DESELECT")) {
					String e = cmdOperation[2];
					e.replace("(", "").replace(")", "");
					String [] pointForDeselect = e.split("\\|");
					int x = Integer.parseInt(pointForDeselect[0].replace("(", ""));
					int y = Integer.parseInt(pointForDeselect[1].replace(")", ""));
					controller.selectDeselectShapeFromLog(x, y);
				}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
		Shape parseShape(String cmd) {
			
			if(cmd.startsWith("Point"))
				return Point.parse(cmd);
			else if(cmd.startsWith("Line"))
				return Line.parse(cmd);
			else if(cmd.startsWith("Circle"))
				return Circle.parse(cmd);
			else if(cmd.startsWith("Donut"))
				return Donut.parse(cmd);
			else if(cmd.startsWith("Rectangle"))
				return Rectangle.parse(cmd);
			else if(cmd.startsWith("Hexagon"))
				return HexagonAdapter.parse(cmd);
			else 
				return null;
		}
		
	}

