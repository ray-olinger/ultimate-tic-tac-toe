package Players;

import Game.MainBoard;
import Game.Mark;

import java.util.concurrent.ThreadLocalRandom;//for random int

public class Reactive_I extends AI implements Strategy {

    public Reactive_I(Difficulty d, MainBoard mb, MainBoard[] SB) {
        this.m_iValidSpaces = new int[82];
        this.m_mbBoard = mb;
        this.m_sbSmallboards = SB;
    }

    public int execute() {
        fillValidArrays();
        // shuffle valid space and smallboard arrays to make it more realistic
        ShuffleValidArrays();

        //find winning moves
        int move;
        move = FindWinningMoves(m_mPlayerMark);

        //if there were no winning moves
        if (move == MainBoard.Null) {
            //see if there is a blocking move
            move = FindWinningMoves(m_mOpponentMark);

            //if there are no blocking moves
            if (move == MainBoard.Null) {
                //try to win a small board, search all valid smallboards
                int i = 0;
                while (m_iValidSmallBoards[i] != MainBoard.Null) {
                    move = TryToWinSmallBoard(m_iValidSmallBoards[i], m_mPlayerMark);
                    if (move != MainBoard.Null)//play a small board winning move if possible
                        return move;
                    i++;
                }

                //if you can't win a smallboard (=> you fall out of loop) try sabotage x2
                move = TrySabotage(false); // not desperate yet!
                if (move == MainBoard.Null) // could find any
                {
                    move = TrySabotage(true);// we are now desperate
                    if (move == MainBoard.Null) {
                        //if all else fails pick the first valid move
                        move = m_iValidSpaces[0];
                    }
                }
            }
        }
        return move;
    }

    /**
     * finds winning moves for a specific player, but smallboard wins for the AI
     *
     * @param PM player that is being checked for winning moves
     * @return a move or -1 for none found
     */
    private int FindWinningMoves(Mark PM) {
        int i = 0;

        //see if there is a smallBoard that can be won
        while (m_iValidSmallBoards[i] != MainBoard.Null) {
            int winTest;
            winTest = m_mbBoard.CheckForWinInt(PM, m_iValidSmallBoards[i]);

            if (winTest != 0) {//if the current SB can be used to win
                //try to win that small board
                int move;
                move = TryToWinSmallBoard(m_iValidSmallBoards[i], m_mPlayerMark);

                if (move != MainBoard.Null)
                    return move;
            } //end if the current SB can be used to win
            i++;
        } // end see if there is a smallBoard that can be won
        return MainBoard.Null;//no Winning move found
    }

    /**
     * searches the small board's valid spaces and sees if there is a move it can make to win the small board
     *
     * @param SB the smallboard index
     * @return a move or -1 to signal there were none found
     */
    private int TryToWinSmallBoard(int SB, Mark PM) {
        //try to win that small board
        int temp[];
        temp = m_sbSmallboards[SB].getValidIDs();

        int j = 0;
        while (temp[j] != MainBoard.Null) {
            int winTest;
            //check all valid squares in that small board to see if one will produce a win
            winTest = m_sbSmallboards[SB].CheckForWinInt(PM, (temp[j] + SB) % 10);

            //if there exists a winning move play it
            if (winTest != 0)
                return temp[j];
            j++;
        } //end try to win that SmallBoard
        return MainBoard.Null;//cannot win smallboard
    }

    /**
     * if you can't win a smallboard
     * send them to a small board that is ummarked and one that cannot win the game
     * find a smallboard that cant win the game
     *
     * @param Desperate the level of depth the AI will go to
     * @return a move or null for none found
     */
    private int TrySabotage(boolean Desperate) {
        //TrySabotage Desperate may do the wrong thing, this is because It can hit an opponent winnable
        // Before it hits a safe potential game winner
        // It will stay here, it is fixed in Reactive_II
        int move;
        int randadjust = ThreadLocalRandom.current().nextInt(0, 100);
        int winTest;

        //for all smallboards
        for (int SB = 0, i = 0; i < 9; SB++, i++) {
            SB = (SB + randadjust)%9;
            if (m_mbBoard.spaceIsUnmarked(SB)) {//will not produce free move
                //see if the other player can win there, potentially
                winTest = m_mbBoard.CheckForWinInt(m_mOpponentMark, SB);

                if (winTest == 0) {//if the space cannot win, search for a matching space
                    //check for each valid space, see if there is one that will send them where we want
                    int SP = 0;

                    while (m_iValidSpaces[SP] != MainBoard.Null) {
                        if ((m_iValidSpaces[SP] + m_iValidSpaces[SP]/9) % 10 == SB) {//there is a valid square that is the same idx as Smallboard
                            if (!Desperate) {
                                move = TryToWinSmallBoard(SB, m_mOpponentMark);
                                if (move == MainBoard.Null)//if the opponent cannot win that square play it
                                    return m_iValidSpaces[SP];
                            }
                            else {
                                return m_iValidSpaces[SP]; //just play that space
                            }
                        }
                        SP++; //else go to next valid space
                    }
                }
                else {//if it is a potential win spot for enemy, go deeper(only if no other option)
                    if (Desperate) {//if desperate pick the smallboard they cannot win within
                        move = TryToWinSmallBoard(SB, m_mOpponentMark);

                        if (move == MainBoard.Null) {//if they cannot win there play there
                            //find a space that matches the SmallBoard
                            int SP = 0;
                            while (m_iValidSpaces[SP] != MainBoard.Null) {
                                if ((m_iValidSpaces[SP] + m_iValidSpaces[SP]/9) % 10 == SB)//there is a valid square that is the same idx as Smallboard
                                    return m_iValidSpaces[SP]; //just play that space
                                SP++; //else go to next valid space
                            }
                        }
                    }
                }
            }
        }

        //no move found that satisfies standards
        return MainBoard.Null;
    }
}
