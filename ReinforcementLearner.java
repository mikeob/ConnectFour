import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class ReinforcementLearner
    implements Player
{

    static double              GAMMA = 0.7;

    boolean                          learning;
    boolean                          verbose;
    HashMap<ConnectFourBoard, State> encountered;
    List<State>                      episode;
    List<Integer>                    action;


    public ReinforcementLearner()
    {
        learning = true;
        encountered = new HashMap<ConnectFourBoard, State>();
        episode = new ArrayList<State>();
        action = new ArrayList<Integer>();
    }


    private void episodeAnalysis(int reward)
    {

        double curReward = reward;

        // If we're supposed to be learning
        if (learning)
        {

            // Work our way from the goal state backward
            while (!episode.isEmpty())
            {
                State s = episode.remove(episode.size() - 1);
                int curAction = action.remove(action.size() - 1);
                curReward *= GAMMA;
                s.updateQ(curAction, curReward);
            }
        }

        episode.clear();
        action.clear();
    }


    @Override
    public int determinePlay(ConnectFourBoard b)
    {
        // TODO Possibly do not add things if
        // we're not learning atm

        int bestMove;
        // If we've seen this state, use from memory
        if (encountered.containsKey(b))
        {
            bestMove = encountered.get(b).getBestMove();
        }
        else if (!learning)
        {
            return b.randomMove();
        }
        else
        {
            // Otherwise, 'make' a memory
            ConnectFourBoard clone = (ConnectFourBoard)b.clone();
            State newState = new State(clone);
            encountered.put(clone, newState);
            bestMove = newState.getBestMove();
        }

        // Update our episode
        action.add(bestMove);
        episode.add(encountered.get(b));

        return bestMove;
    }


    @Override
    public void observeLoss()
    {
        episodeAnalysis(-1);
    }


    @Override
    public void observeWin()
    {
        episodeAnalysis(1);
    }


    @Override
    public void observeTie()
    {
        episodeAnalysis(0);
    }


    /*
     * Container for State information.
     */
    private class State
    {

        static final double ARG_MAX_DIST = .9;
        static final double EQUAL_DIST   = 0.1;

        ConnectFourBoard    board;
        double[]            pi;
        double[]            Q;
        double[]            timesOccurred;
        int                 choices;
        int                 validChoices;


        /*
         * Initialize our state with the board and random pi/Q values
         */
        public State(ConnectFourBoard b)
        {
            this.board = b;
            this.choices = b.c;
            pi = new double[choices];
            Q = new double[choices];
            timesOccurred = new double[choices];

            for (int i = 0; i < choices; i++)
                validChoices += (b.canPlay(i)) ? 1 : 0;

            initializePi();
        }


        public void updateQ(int action, double value)
        {
            Q[action] += value;
            timesOccurred[action]++;
            this.updatePolicy();
        }


        public void initializePi()
        {

            for (int i = 0; i < choices; i++)
            {
                if (board.canPlay(i))
                    pi[i] = 1.0 / validChoices;
                else
                    pi[i] = 0;
            }

        }


        /*
         * Uses our policy's distribution to get our next best move. Walks along
         * the distribution, subtracting until we've found our 'slot'
         */
        public int getBestMove()
        {
            Random r = new Random();
            double sample = r.nextDouble();
            int action = 0;

            if (verbose)
            {
                System.out.println(Arrays.toString(Q));
                System.out.println(Arrays.toString(timesOccurred));
            }

            // Grab a sample from our policy
            for (int i = 0; i < pi.length; i++)
            {
                if (pi[i] == 0)
                    continue;
                if (sample <= pi[i])
                    return i;

                sample -= pi[i];
            }

            return -1;
        }


        private void updatePolicy()
        {
            // Find argmax of a
            int argmax = 0;
            while (!board.canPlay(argmax))
                argmax++;

            for (int i = 0; i < choices; i++)
            {
                if (timesOccurred[i] == 0)
                    continue;
                if (Q[i] / timesOccurred[i] > Q[argmax] / timesOccurred[argmax])
                    argmax = i;
            }

            Arrays.fill(pi, 0);
            // Give 90% to our argmax, and split the 10% amongst everything
            pi[argmax] = ARG_MAX_DIST;
            for (int i = 0; i < choices; i++)
            {
                if (board.canPlay(i))
                    pi[i] += EQUAL_DIST / validChoices;
            }
        }

    }

}
