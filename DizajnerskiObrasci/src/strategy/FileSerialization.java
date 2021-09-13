package strategy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import model.DrawingModel;
import shapes.Shape;

public class FileSerialization implements Strategy {
	
	private FileOutputStream fileOutputStream;
	private FileInputStream fileInputStream;
	private DrawingModel model;
	

	public FileSerialization(DrawingModel model) {
		this.model = model;
	}

	@Override
	public void SaveFile(File file) {
		try
		{
			fileOutputStream = new FileOutputStream(file + ".draw");
			ObjectOutputStream outputObjStream = new ObjectOutputStream(fileOutputStream);
			outputObjStream.writeObject(model.getShapes());
			outputObjStream.close();
			fileOutputStream.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}

	@Override
	public void OpenFile(File file) {
		try {
			fileInputStream = new FileInputStream(file);
			ObjectInputStream inputObjStream = new ObjectInputStream(fileInputStream);
			ArrayList<Shape> listOfShapes = (ArrayList<Shape>) inputObjStream.readObject();
			model.addAll(listOfShapes);
			inputObjStream.close();
			fileInputStream.close();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}

}
