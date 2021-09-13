package command;

import shapes.Rectangle;

public class CmdUpdateRectangle implements Command {
	
	private Rectangle oldState;
	private Rectangle newState;
	private Rectangle original;
	private String cmdLog;
	
	public CmdUpdateRectangle (Rectangle oldState, Rectangle newState) {
		this.oldState=oldState;
		this.newState = newState;
		original = (Rectangle) oldState.clone();
	}

	@Override
	public void execute() {
		oldState.getUpperLeftPoint().setX(newState.getUpperLeftPoint().getX());
		oldState.getUpperLeftPoint().setY(newState.getUpperLeftPoint().getY());
		oldState.setHeight(newState.getHeight());
		oldState.setWidth(newState.getWidth());
		oldState.setEdgeColor(newState.getEdgeColor());
		oldState.setInnerColor(newState.getInnerColor());
		oldState.setSelected(newState.isSelected());
		
		cmdLog = "EXECUTE_UPDATE_" + original + "-->" + newState;
		
	}

	@Override
	public void unexecute() {
		oldState.getUpperLeftPoint().setX(original.getUpperLeftPoint().getX());
		oldState.getUpperLeftPoint().setY(original.getUpperLeftPoint().getY());
		oldState.setHeight(original.getHeight());
		oldState.setWidth(original.getWidth());
		oldState.setEdgeColor(original.getEdgeColor());
		oldState.setInnerColor(original.getInnerColor());
		oldState.setSelected(original.isSelected());
		
		cmdLog = "UNEXECUTE_UPDATE_" + newState + "-->" + original;
		
	}
	
	public String getCmdLog() {
		return cmdLog;
	}

	public void setCmdLog(String cmdLog) {
		this.cmdLog = cmdLog;
	}

}
