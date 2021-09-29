package command;
import model.DrawingModel;
import shapes.Line;

public class CmdLineRemove implements Command {

	private DrawingModel model;
	private Line line;

	public CmdLineRemove(DrawingModel model, Line line) {
		this.model = model;
		this.line = line;
	}

	@Override
	public void execute() {
		model.remove(line);
	}

	@Override
	public void unexecute() {
		model.add(line);
	}

	@Override
	public String log() {
		return "Deleted: " + line.toString();
	}

}