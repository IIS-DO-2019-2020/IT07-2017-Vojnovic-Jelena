package command;

import shapes.Donut;
import shapes.Rectangle;

public class CmdUpdateDonut implements Command {
	
	private Donut oldState;
	private Donut newState;
	private Donut original;
	
	public CmdUpdateDonut (Donut oldState, Donut newState) {
		this.oldState=oldState;
		this.newState = newState;
		original = (Donut) oldState.clone();
	}

	@Override
	public void execute() {
		oldState.getCenter().setX(newState.getCenter().getX());
		oldState.getCenter().setY(newState.getCenter().getY());
		oldState.setRadius(newState.getRadius());
		oldState.setInnerRadius(newState.getInnerRadius());
		oldState.setEdgeColor(newState.getEdgeColor());
		oldState.setInnerColor(newState.getInnerColor());
		oldState.setSelected(newState.isSelected());
		
	}

	@Override
	public void unexecute() {
		oldState.getCenter().setX(original.getCenter().getX());
		oldState.getCenter().setY(original.getCenter().getY());
		oldState.setRadius(original.getRadius());
		oldState.setInnerRadius(original.getInnerRadius());
		oldState.setEdgeColor(original.getEdgeColor());
		oldState.setInnerColor(original.getInnerColor());
		oldState.setSelected(original.isSelected());
		
	}

}
