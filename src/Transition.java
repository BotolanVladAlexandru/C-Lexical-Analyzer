public class Transition {
	
	private int state;
	private char symbol;
	private int nextState;
	
	public Transition(int state, char symbol, int nextState) {
		this.state = state;
		this.symbol = symbol;
		this.nextState = nextState;
	}
	
	
	
	@Override
	public boolean equals(Object obj) {
		Transition transition = (Transition) obj;
			return (this.state == transition.state && this.symbol == transition.symbol && this.nextState == transition.nextState);
	}
	
	

	public boolean accepts(int state, char symbol){
		if(this.state == state && this.symbol == symbol)
			return true;
		return false;
	}

	@Override
	public String toString() {
		return "(" + state + "," + symbol + "," + nextState + ")\n";
	}



	public int getNexState() {
		return this.nextState;
	}
	
}
