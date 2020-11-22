package server.logic;

public interface ClientLogic {
	void onInit();

	void onFin();

	ClientLogic step();
}
