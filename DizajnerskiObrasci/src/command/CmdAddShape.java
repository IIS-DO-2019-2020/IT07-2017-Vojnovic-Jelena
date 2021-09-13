package command;

import model.DrawingModel;
import shapes.Shape;

public class CmdAddShape implements Command {
	
	private Shape shape;
	private DrawingModel model;
	private String cmdLog;
	
	public CmdAddShape(Shape shape, DrawingModel model) {
		this.shape=shape;
		this.model=model;
	}

	@Override
	public void execute() {
		this.model.addShape(shape);
		cmdLog = "EXECUTE_ADD_" + shape;
		
	}

	@Override
	public void unexecute() {
		this.model.removeShape(shape);
		cmdLog = "UNEXECUTE_ADD_" + shape;
		
	}
	
	public String getCmdLog() {
		return cmdLog;
	}

	public void setCmdLog(String cmdLog) {
		this.cmdLog = cmdLog;
	}


}
