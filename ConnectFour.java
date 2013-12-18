import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class ConnectFour
{

    public static void main(String[] args)
        throws FileNotFoundException,
        UnsupportedEncodingException
    {

//        for (int i = 0; i <= 10; i++)
//        {
//            ReinforcementLearner l = new ReinforcementLearner();
//            l.GAMMA = 0.1 * i;
//            System.out.print(l.GAMMA + "     "
//                + runTrials(100000, l, new RandomPlayer(), 5, 5, true));
//        }

        for (int size = 4; size <= 7; size++)
        {
            System.out.println(size);
            ReinforcementLearner l = new ReinforcementLearner();
            String filename = size + "x" + size + ".csv";
            PrintWriter writer = new PrintWriter(filename, "UTF-8");
            writer.println("wins,ties,losses");
            for (int i = 1; i <= 100; i++)
            {
                runTrials(1000 * i, l, new RandomPlayer(), size, size, false);
                l.learning = false;
                String result =
                    runTrials(100, l, new RandomPlayer(), size, size, true);
                l.learning = true;

                writer.print(result);
            }
            writer.close();
            l.encountered.clear();
        }

    }


    /* Plays howMany games, then outputs the results */
    public static String runTrials(
        int howMany,
        Player one,
        Player two,
        int rows,
        int cols,
        boolean verbose)
    {
        double oneWins = 0;
        double twoWins = 0;
        double ties = 0;
        for (int i = 0; i < howMany; i++)
        {
            int outcome = play(one, two, rows, cols);
            if (outcome == 1)
                oneWins++;
            else if (outcome == 2)
                twoWins++;
            else
                ties++;
        }
        double wins = oneWins / howMany;
        double tie = ties / howMany;
        double loss = twoWins / howMany;
//        if (verbose)
//            System.out.printf("%.4f,%.4f,%.4f%n", wins, tie, loss);

        return String.format("%.2f,%.2f,%.2f%n", wins, tie, loss);
    }


    /*
     * Plays a game of Connect Four between Player one and Player two.
     * 
     * Returns the winner.
     */
    public static int play(Player one, Player two, int rows, int cols)
    {
        ConnectFourBoard b = new ConnectFourBoard(rows, cols);
        boolean verbose = false;

        if (one instanceof HumanPlayer || two instanceof HumanPlayer)
            verbose = true;

        while (true)
        {

            // Player one's move
            b.play(1, one.determinePlay(b));
            if (verbose)
            {
                System.out.println(b);
                System.out.println();
            }

            // Player one wins or TIE
            int outcome = b.isWon();
            if (outcome == ConnectFourBoard.P1)
            {
                one.observeWin();
                two.observeLoss();
                return 1;
            }
            else if (outcome == ConnectFourBoard.TIE)
            {
                one.observeTie();
                two.observeTie();
                return 0;
            }

            // Player two's move
            b.play(2, two.determinePlay(b));
            if (verbose)
            {
                System.out.println(b);
                System.out.println();
            }

            // Player two wins or TIE
            outcome = b.isWon();
            if (b.isWon() == ConnectFourBoard.P2)
            {
                two.observeWin();
                one.observeLoss();
                return 2;
            }
            else if (outcome == ConnectFourBoard.TIE)
            {
                one.observeTie();
                two.observeTie();
                return 0;
            }
        }

    }

}
