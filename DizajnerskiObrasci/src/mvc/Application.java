package mvc;

import controller.DrawingController;
import model.DrawingModel;
import view.FrameDrawing;

public class Application {

	public static void main(String[] args) {
		
		DrawingModel model = new DrawingModel();
		FrameDrawing frame = new FrameDrawing(); 
		
		frame.getView().setModel(model);
		
		DrawingController controller = new DrawingController(frame, model);
		controller.addObserver(frame);
		
		frame.setController(controller);
		
		frame.getBtnColorEdge().setBackground(controller.getEdgeColor());
		frame.getBtnColorInner().setBackground(controller.getInnerColor());
		
		frame.setVisible(true);
		//System.out.println("Dizajnerski obrasci.");

	}

}
