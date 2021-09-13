package command;

import model.DrawingModel;
import shapes.Shape;

public class CmdToFront implements Command {
	
	private Shape shape;
	private DrawingModel model;
	private int index;
	private String cmdLog;
	
	public CmdToFront(Shape shape, DrawingModel model) {
		this.shape=shape;
		this.model=model;
	}

	@Override
	public void execute() {
		index = model.getIndex(shape);
		model.removeShape(shape);
		model.addShapeAtIndex(shape, index+1);
		
		cmdLog = "EXECUTE_TO-FRONT_" +shape;
	}

	@Override
	public void unexecute() {
		model.removeShape(shape);
		model.addShapeAtIndex(shape, index);
		
		cmdLog = "UNEXECUTE_TO-FRONT_" +shape;
		
	}

	public String getCmdLog() {
		return cmdLog;
	}

	public void setCmdLog(String cmdLog) {
		this.cmdLog = cmdLog;
	}
}
