package Players;

import Game.MainBoard;
import Game.Mark;

import java.util.concurrent.ThreadLocalRandom;//for random int

public class AI extends Player {

    protected int m_iNumValidSP;//num of valid spaces
    protected int m_iNumValidSB;
    protected int m_iValidSmallBoards[];
    protected int m_iValidSpaces[];
    private Strategy m_sStrategy;
    protected MainBoard m_mbBoard;
    protected MainBoard m_sbSmallboards[]; //HA HA! Smallboards are declared as MainBoards so you can't access Space functions
                                            // YAY!
    protected AI() {

    }

    public AI(Difficulty d, MainBoard mb, MainBoard[] SB, Mark m, int pn) {
        this(d, mb, SB, m);
        m_iPlayerNumber = pn;
    }

    public AI(Difficulty d, MainBoard mb, MainBoard[] SB, Mark m) {
        switch (d) {
            case RANDOM:
                m_sStrategy = new RandomStrategy(d, mb, SB);
                break;
            case REACTIVE_I:
                m_sStrategy = new Reactive_I(d, mb, SB);
                break;
            case REACTIVE_II:
                m_sStrategy = new Reactive_II(d, mb, SB);
                break;
            case PROACTIVE:
                m_sStrategy = new Proactive(d, mb, SB);
                break;
        }

        ((AI)m_sStrategy).setMark(m);
        m_iPlayerNumber = 2;
        Human = false;
    }

    /**
     * uses an strategy algorithm to determine the next move the AI will take
     * overwrites GetPlayerMove from Player
     * @return an int square ID of where to play
     */
    @Override
    public int GetPlayerMove() {
        int move;
        move = m_sStrategy.execute();//execute strategy to get move
        return move;
    }

    /**
     * fills in the Valid Space and Smallboard Arrays in the AI's member variables
     * also stores number of valid moves and SBs
     */
    protected void fillValidArrays() {
        //get the valid spaces
        int temp[];
        int temp2[];
        temp = m_mbBoard.getValidIDs();

        // this integer k will be used to keep track of how many valid spaces exist
        // then used in the random move
        int k = 0;

        //for every valid smallboard get its valid squares
        int i = 0;
        while (temp[i] != MainBoard.Null) {
            temp2 = m_sbSmallboards[temp[i]].getValidIDs();

            //for every valid square copy the square id into valid spaces
            int j = 0;
            while (temp2[j] != MainBoard.Null) {
                m_iValidSpaces[k] = temp2[j];
                j++;
                k++;
            }
            i++;
        }
        m_iValidSpaces[k] = MainBoard.Null;
        m_iValidSmallBoards = temp;
        m_iNumValidSP = k;
        m_iNumValidSB = i;
    }

    /**
     * the method shuffles the valid space and small board arrays
     * it does this inorder to make the AI seem more real
     * it also prevents the player "reading" the AI;
     */
    protected void ShuffleValidArrays() {
        int i = 0;
        int temp, temp2;
        int tempIndex;

        //shuffle smallboards
        while (m_iValidSmallBoards[i] != MainBoard.Null) {
            tempIndex = ThreadLocalRandom.current().nextInt(0, m_iNumValidSB);
            temp = m_iValidSmallBoards[i];
            temp2 = m_iValidSmallBoards[tempIndex];

            m_iValidSmallBoards[i] = temp2;
            m_iValidSmallBoards[tempIndex] = temp;
            i++;
        }
        //shuffle spaces
        i = 0;
        while (m_iValidSpaces[i] != MainBoard.Null) {
            tempIndex = ThreadLocalRandom.current().nextInt(0, m_iNumValidSP);
            temp = m_iValidSpaces[i];
            temp2 = m_iValidSpaces[tempIndex];

            m_iValidSpaces[i] = temp2;
            m_iValidSpaces[tempIndex] = temp;
            i++;
        }
    }//end shuffle arrays

    @Override
    public void setPlayerMove(int id) {

    }
}