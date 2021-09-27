package command;

import java.util.List;

import model.DrawingModel;
import shapes.Shape;

public class CmdDeleteShapes implements Command {
	
	private DrawingModel model;
	private List<Shape> shapesForDelete;
	
	public CmdDeleteShapes(DrawingModel model, List<Shape> shapes) {
		this.model = model;
		shapesForDelete = shapes;
	}

	@Override
	public void execute() {
		shapesForDelete.forEach(shape -> {
			model.remove(shape);
		});
		
	}

	@Override
	public void unexecute() {
		shapesForDelete.forEach(shape -> {
			model.add(shape);
			shape.setSelected(true);
		});
		
	}

}
