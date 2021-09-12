package command;

import java.util.List;

import model.DrawingModel;
import shapes.Shape;
public class CmdDeleteShapes implements Command {
	
	private DrawingModel model;
	private List<Shape> shapesForDelete;
	
	public CmdDeleteShapes(List<Shape> shapes, DrawingModel model) {
		this.model=model;
		shapesForDelete=shapes;
	}

	@Override
	public void execute() {
		shapesForDelete.forEach(shape -> {
			model.removeShape(shape);
		});
		
	}

	@Override
	public void unexecute() {
		shapesForDelete.forEach(shape -> {
			model.addShape(shape);
			shape.setSelected(true);
		});
		
	}

}
