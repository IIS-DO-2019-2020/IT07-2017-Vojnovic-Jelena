package observer;

public interface Observer {
	void update(int currState, int numOfSelectedShapes, int numOfShapes, int numOfUnexecutedCmd, int numOfExecutedCmd,int numOfLogs,String typeOfFile);
}
