package command;

import model.DrawingModel;
import shapes.Shape;

public class CmdDeleteShapes implements Command {
	
	private DrawingModel model;
	private Shape shape;
	
	public CmdDeleteShapes(DrawingModel model, Shape shape) {
		this.model = model;
		this.shape = shape;
	}

	@Override
	public void execute() {
		model.remove(shape);
		
	}

	@Override
	public void unexecute() {
		model.add(shape);
	}

	@Override
	public String log() {
		return "Deleted: " + shape.toString();
	}

}
