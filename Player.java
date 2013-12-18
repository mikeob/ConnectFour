/*
 * Interface for a player. All that's required is
 * that a move is provided given the board state.
 */
public interface Player {
	public int determinePlay(ConnectFourBoard b);
	public void observeLoss();
	public void observeWin();
	public void observeTie();
}
