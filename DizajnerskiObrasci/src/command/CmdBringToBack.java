package command;

import model.DrawingModel;
import shapes.Shape;

public class CmdBringToBack implements Command {
	
	private Shape shape;
	private DrawingModel model;
	private int index;
	private String cmdLog;

	public CmdBringToBack(Shape shape, DrawingModel model) {
		this.shape=shape;
		this.model=model;
	}
	
	@Override
	public void execute() {
		index= model.getIndex(shape);
		model.removeShape(shape);
		model.addShapeAtIndex(shape, 0);
		
		cmdLog = "EXECUTE_BRING-TO-BACK_" +shape;
	}

	@Override
	public void unexecute() {
		if(index>model.getShapes().size()-1) return;
		
		model.removeShape(shape);
		model.addShapeAtIndex(shape, index);
		
		cmdLog =  "UNEXECUTE_BRING-TO-BACK_" +shape;
	}
	
	public String getCmdLog() {
		return cmdLog;
	}

	public void setCmdLog(String cmdLog) {
		this.cmdLog = cmdLog;
	}

}
