import java.util.Scanner;

public class HumanPlayer implements Player {

	Scanner in;

	public HumanPlayer() {
		in = new Scanner(System.in);
	}

	@Override
	public int determinePlay(ConnectFourBoard b) {
		System.out.println("Pick your move!");
		int move = in.nextInt();
		while (move < 0 || move >= b.c || !b.canPlay(move)) {
			if (!b.canPlay(move))
				System.out.println("Invalid move!");
			else
				System.out.printf("Please pick within [0, %d]\n", b.c - 1);
			move = in.nextInt();
		}
		return move;
	}

	@Override
	public void observeLoss() {
		System.out.println("You lose.");
	}

	@Override
	public void observeWin() {
		System.out.println("You win!");
	}

	@Override
	public void observeTie() {
		System.out.println("Tie game!");
	}

}
