import java.util.Arrays;
import java.util.Random;

/*
 * A game of connect 4. 
 */
public class ConnectFourBoard {

	static final byte EMPTY = 0;
	static final byte P1 = 1;
	static final byte P2 = 2;
	static final byte TIE = 123;

	private byte[][] board;
	int r, c;

	public ConnectFourBoard(int n, int m) {
		r = n;
		c = m;
		board = new byte[n][m];
	}

	public ConnectFourBoard(byte[][] board) {
		this.board = board;
		r = board.length;
		c = board[0].length;
	}

	/*
	 * Checks if the game is over. Returns the winner's value, or EMPTY if the
	 * game is not won.
	 */
	public int isWon() {
		boolean isTie = true;
		for (int i = 0; i < r; i++) {
			for (int j = 0; j < c; j++) {

				byte val = get(i, j);
				if (val == EMPTY) {
					isTie = false;
					continue;
				}

				// Check straight down
				if (get(i + 1, j) == val && get(i + 2, j) == val
						&& get(i + 3, j) == val)
					return val;
				// Check to the right
				if (get(i, j + 1) == val && get(i, j + 2) == val
						&& get(i, j + 3) == val)
					return val;
				// Check down-right
				if (get(i + 1, j + 1) == val && get(i + 2, j + 2) == val
						&& get(i + 3, j + 3) == val)
					return val;
				// Check down-left
				if (get(i + 1, j - 1) == val && get(i + 2, j - 2) == val
						&& get(i + 3, j - 3) == val)
					return val;
			}
		}
		return (isTie) ? TIE : EMPTY;
	}

	/* Wrapper for board access */
	private byte get(int i, int j) {
		if (i < 0 || i >= r || j < 0 || j >= c)
			return -1;

		return board[i][j];
	}

	/* Drops PLAYER's token in the given column, provided there is room */
	public void play(int player, int column) {
		if (!canPlay(column)) {
			System.err.println("ERROR -- Attempting to play in a full column");
			return;
		}

		int idx = r - 1;
		while (board[idx][column] != EMPTY)
			idx--;

		board[idx][column] = (byte) player;
	}

	/* Returns true if the column specified is not full */
	public boolean canPlay(int column) {
		return board[0][column] == EMPTY;
	}

	// ---- Utility functions ----

	/* Returns a random valid move */
	public int randomMove() {
		Random r = new Random();
		int col = r.nextInt(c);
		while (!canPlay(col))
			col = r.nextInt(c);

		return col;
	}

	@Override
	public String toString() {
		String result = "";
		for (int i = 0; i < r; i++) {
			result += "|";
			for (int j = 0; j < c; j++) {
				if (board[i][j] != EMPTY)
					result += board[i][j] + "|";
				else
					result += " |";
			}
			result += "\n";
		}
		result += " ";
		for (int i = 0; i < c; i++) {
			result += "- ";
		}
		return result;
	}

	@Override
	public Object clone() {
		byte[][] boardclone = new byte[r][];
		for (int i = 0; i < r; i++)
			boardclone[i] = this.board[i].clone();
		ConnectFourBoard b = new ConnectFourBoard(boardclone);
		return b;
	}

	@Override
	public int hashCode() {
		return Arrays.deepHashCode(board);
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof ConnectFourBoard))
			return false;

		ConnectFourBoard b = (ConnectFourBoard) o;
		return Arrays.deepEquals(b.board, this.board);
	}

}
