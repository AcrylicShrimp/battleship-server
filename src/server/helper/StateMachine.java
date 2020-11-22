package server.helper;

import java.util.HashMap;

public class StateMachine<T> implements StateMachineController {
	private final HashMap<String, State<T>> stateMap;
	private       State<T>                  state;
	private       T                         context;

	public StateMachine() {
		this.stateMap = new HashMap<>();
		this.state = null;
		this.context = null;
	}

	public StateMachine<T> state(String name, State<T> state) {
		this.stateMap.put(name, state);
		return this;
	}

	public void init(String name, T context) {
		this.state = this.stateMap.get(name);
		this.context = context;
	}

	@Override
	public void transit(String name) throws TransitionException {
		this.state = this.stateMap.get(name);
		throw new TransitionException();
	}

	public void step() {
		try {
			this.state.run(this, this.context);
		} catch (TransitionException e) {
		}
	}

	public interface State<T> {
		void run(StateMachineController controller, T context) throws TransitionException;
	}
}
