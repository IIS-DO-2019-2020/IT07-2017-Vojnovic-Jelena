package command;

import model.DrawingModel;
import shapes.Shape;

public class CmdBringToFront implements Command {

	private Shape shape;
	private DrawingModel model;
	private int index;
	private String cmdLog;
	
	public CmdBringToFront(Shape shape, DrawingModel model) {
		this.shape=shape;
		this.model=model;
	}
	
	@Override
	public void execute() {
		index=model.getIndex(shape);
		model.removeShape(shape);
		model.addShapeAtIndex(shape, model.getShapes().size());
		
		cmdLog = "EXECUTE_BRING-TO-FRONT_" +shape;
		
	}

	@Override
	public void unexecute() {
		if(index>model.getShapes().size()-1) return;
		
		model.removeShape(shape);
		model.addShapeAtIndex(shape, index);
		
		cmdLog = "UNEXECUTE_BRING-TO-FRONT_" +shape;
		
	}
	
	public String getCmdLog() {
		return cmdLog;
	}

	public void setCmdLog(String cmdLog) {
		this.cmdLog = cmdLog;
	}

}
