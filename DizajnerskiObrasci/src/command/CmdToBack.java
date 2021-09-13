package command;
import model.DrawingModel;
import shapes.Shape;

public class CmdToBack implements Command {

	private Shape shape;
	private DrawingModel model;
	private int index;
	
	public CmdToBack(Shape shape, DrawingModel model) {
		this.shape=shape;
		this.model=model;
	}
	
	@Override
	public void execute() {
		index= model.getIndex(shape);
		model.removeShape(shape);
		model.addShapeAtIndex(shape, index-1);
		
	}

	@Override
	public void unexecute() {
		model.removeShape(shape);
		model.addShapeAtIndex(shape, index);
		
	}

}
