package command;

import shapes.Point;

public class CmdUpdatePoint implements Command {
	
	private Point oldState;
	private Point newState;
	private Point original;
	private String cmdLog;
	
	public CmdUpdatePoint (Point oldState, Point newState) {
		this.oldState= oldState;
		this.newState=newState;
		original = (Point) oldState.clone();
	}

	@Override
	public void execute() {
		oldState.setX(newState.getX());
		oldState.setY(newState.getY());
		oldState.setEdgeColor(newState.getEdgeColor());
		oldState.setSelected(newState.isSelected());
		
		cmdLog ="EXECUTE_UPDATE_" + original + "-->" + newState;
	}

	@Override
	public void unexecute() {
		oldState.setX(original.getX());
		oldState.setY(original.getY());
		oldState.setEdgeColor(original.getEdgeColor());
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
