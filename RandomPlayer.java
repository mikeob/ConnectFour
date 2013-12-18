
public class RandomPlayer implements Player{

	@Override
	public int determinePlay(ConnectFourBoard b) {
		return b.randomMove();
	}

	@Override
	public void observeLoss() {
		// Do nothing
	}

	@Override
	public void observeWin() {
		// Do nothing
	}

	@Override
	public void observeTie() {
		// Do nothing
	}

}
