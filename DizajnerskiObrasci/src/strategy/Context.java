package strategy;

import java.io.File;

public class Context implements Strategy {
	
	private Strategy strategy;
	

	public Context(Strategy strategy) {
		this.strategy = strategy;
	}

	@Override
	public void SaveFile(File file) {
		strategy.SaveFile(file);
		
	}

	@Override
	public void OpenFile(File file) {
		strategy.OpenFile(file);
		
	}

}
