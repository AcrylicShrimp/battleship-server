package server.helper;

public interface StateMachineController {
	void transit(String name) throws TransitionException;
}
