package Players;

import Game.MainBoard;
import Game.Mark;

/**
 * Created by Steven on 3/29/2016.
 * Completed by Steven on 4/11/2016
 */
public class Reactive_II extends AI implements Strategy {
    //dont need to random these, sabatoge should take care of it with rand indexes
    private int m_iWinnableSB[][];//-1 will be first in 2nd array if no moves
    private int m_iLosableSB[][];
    private boolean m_bGameWinningSB[];
    private boolean m_bGameLosingSB[];

    public Reactive_II(Difficulty d, MainBoard mb, MainBoard[] SB) {
        this.m_iValidSpaces = new int[82];
        this.m_iWinnableSB = new int[9][10];
        this.m_iLosableSB = new int[9][10];
        this.m_bGameWinningSB = new boolean[10];
        this.m_bGameLosingSB = new boolean[10];
        this.m_mbBoard = mb;
        this.m_sbSmallboards = SB;

        for(int i = 0; i < 10; i++){
            m_bGameWinningSB[i] = false;
            m_bGameLosingSB[i] = false;
        }
    }

    public int execute() {
        fillValidArrays();

        // shuffle valid space and smallboard arrays to make it more realistic
        ShuffleValidArrays();

        //find winning moves
        int move;
        move = FindWinningMoves(m_mPlayerMark);
        if (move != MainBoard.Null)
            return move;

        //find blocking moves with a SB win
        move = FindWinningMoves(m_mOpponentMark);
        if (move != MainBoard.Null)
            return move;

        //find a blocking move with a SB block
        int RiskyBlock = MainBoard.Null;
        move = FindWinningMoves(m_mOpponentMark, true);
        if (move != MainBoard.Null){
            int SB = (move / 9);
            int SQ = (move + SB) % 10;
            //if the block does not result in a game over
            if (!m_bGameLosingSB[SQ] && m_iLosableSB[SQ][0] == MainBoard.Null){
                if(m_mbBoard.spaceIsUnmarked(SQ)){//but in case of freemove
                    if(!ThereExistsAWinningSpaceFor(false, SB)) // and there are no other moves that could win
                        if (!ThereExistsAWinningSpaceFor(true, SB)) // and there are no other moves that could block me
                            return move;
                        else
                            RiskyBlock = move;
                }
                else{
                    if (!m_bGameLosingSB[SQ] && m_iLosableSB[SQ][0] == MainBoard.Null)
                        return move;
                    else
                        RiskyBlock = move;
                }
            }
        }


        //-----------------------look for non game winning moves------------------

        //if there are no blocking moves
        //evaluate boards
        EvaluateBoards();

        //********* SMALLBOARD WINS
        //try to win a SmallBoard, non desperately
        move = SearchForSmallBoardWin(false);
        if (move != MainBoard.Null)
            return move;

        //try to win a SmallBoard, desperately, and look for safe trades
        int trade = MainBoard.Null;
        move = SearchForSmallBoardWin(true);
        if (move > MainBoard.Null)
            return move;
        else if (move < MainBoard.Null) //if there is a trade
            trade = (move)/(-2) - 1;
        //********* END SMALLBOARD WINS


        //********* SMALLBOARD BLOCKING
        //try tp block a SmallBoard, non desperately
        move = SearchForSmallBoardBlocks(false);
        if (move != MainBoard.Null)
            return move;

        int evenBlockTrade = MainBoard.Null; //a block but could result in opponent blocking next turn
        int unevenBlockTrade = MainBoard.Null; // a block but could result in opponent winning a board next turn
        move = SearchForSmallBoardBlocks(true);
        if (move > MainBoard.Null)
            return move;
        else if (move < MainBoard.Null) { // if there is a trade
            //translate moves
            if (move%2 == 0)//find out which type of trade it is
                evenBlockTrade = (move)/(-2) - 1;
            else
                unevenBlockTrade = (move + 1)/(-2) - 1;
        }
        //********* END SMALLBOARD BLOCKING


        //********* SABOTAGE
        //if you can't win a smallboard or block try sabotage x2
        move = TrySabotage(false); // not desperate yet!
        if (move != MainBoard.Null)
            return move;

        //see if block trade
        if (evenBlockTrade != MainBoard.Null)
            return evenBlockTrade;

        // TrySabotage Desperately
        int safeLoser = MainBoard.Null;
        int block = MainBoard.Null;
        move = TrySabotage(true);// we are now desperate
        if (move > MainBoard.Null)
            return move;
        else if (move < MainBoard.Null) {
            //translate moves
            if (move%2 == 0)//find out which type of trade it is
                block = (move)/(-2) - 1;
            else
                safeLoser = (move + 1)/(-2) - 1;
        }
        //********* END SABOTAGE


        //********* FINAL DECISIONS
        //see if we have a trade
        if (trade != MainBoard.Null)
            return trade;

        //see if we have a opponent block
        if (block != MainBoard.Null)
            return block;

        //see if we have an uneven block trade
        if (unevenBlockTrade != MainBoard.Null)
            return unevenBlockTrade;

        //see if we have a safe Loser
        if (safeLoser != MainBoard.Null)
            return safeLoser;

        //pick if nothing else, a gamewinning block, but it will result in the opponent blocking a winning move of AI
        if (RiskyBlock != MainBoard.Null)
            return RiskyBlock;
        //********* END FINAL DECISIONS


        //********* ALL HOPE IS LOST!
        return m_iValidSpaces[0];
    }



    /**
     * finds winning moves for a specific player, but smallboard wins for the AI
     * @param lookingforblocks if AI is looking for blocks
     * @param PM player that is being checked for winning moves
     * @return a move or -1 for none found
     */
    private int FindWinningMoves(Mark PM, boolean lookingforblocks) {
        int i = 0;
        //see if there is a smallBoard that can be won
        while (m_iValidSmallBoards[i] != MainBoard.Null) {
            int winTest;
            winTest = m_mbBoard.CheckForWinInt(PM, m_iValidSmallBoards[i]);
            if (winTest != 0)//if the current SB can be used to win
            {
                //try to win that small board
                int move;
                if(lookingforblocks)
                    move = TryToWinSmallBoard(m_iValidSmallBoards[i], m_mOpponentMark);
                else
                    move = TryToWinSmallBoard(m_iValidSmallBoards[i], m_mPlayerMark);

                if (move != MainBoard.Null)
                    return move;
            } //end if the current SB can be used to win
            i++;
        } // end see if there is a smallBoard that can be won
        return MainBoard.Null;//no Winning move found
    }

    /**
     * Overloaded function for block seeking for the other player of this function
     * @param PM player mark
     * @return move
     */
    private int FindWinningMoves(Mark PM){
        return FindWinningMoves(PM, false);
    }

    /**
     * Finds if there Exist Winning Spaces For the AI or Opponent, and could check all but one
     * @param AI if looking for AI
     * @param exceptThis a Smallboard that could be ignored
     * @return the answer
     */
    private boolean ThereExistsAWinningSpaceFor(boolean AI, int exceptThis){
        for(int i = 0; i < 9; i++){
            if (i != exceptThis){
                if (AI){
                    if(m_bGameWinningSB[i] && m_iWinnableSB[i][0] != MainBoard.Null)
                        return true;
                }
                else{
                    if(m_bGameLosingSB[i] && m_iLosableSB[i][0] != MainBoard.Null)
                        return true;
                }
            }
        }
        return false;
    }

    /**
     * @return if there are winning moves on the field, or a block for the opponent
     */
    private boolean areNoWinningMoves(){
        return (!(m_bGameWinningSB[9] || m_bGameLosingSB [9]));
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
     * evaluated the boards
     * determines which boards are
     *      Winnable
     *          finds Square moves
     *      Losable
     *          finds Square moves
     *      GameWinning
     *      GameLosing
     *      and Marked
     */
    private void EvaluateBoards(){
        for (int SB = 0; SB < 9; SB++)
        {
            if (m_mbBoard.spaceIsUnmarked(SB))
            {
                int winTest;
                winTest = m_mbBoard.CheckForWinInt(m_mPlayerMark, SB);
                if (winTest != 0)
                    m_bGameWinningSB[SB] = true;

                winTest = m_mbBoard.CheckForWinInt(m_mOpponentMark, SB);
                if (winTest != 0)
                    m_bGameLosingSB[SB] = true;

                int j = 0;
                int i = 0;
                int SP;
                for (SP = 0; SP < 9; SP++) {
                    if (m_sbSmallboards[SB].spaceIsUnmarked(SP)) {
                        winTest = m_sbSmallboards[SB].CheckForWinInt(m_mPlayerMark, SP);
                        if (winTest != 0) {
                            m_iWinnableSB[SB][i] = SP;
                            i++;
                        }

                        winTest = m_sbSmallboards[SB].CheckForWinInt(m_mOpponentMark, SP);
                        if (winTest != 0) {
                            m_iLosableSB[SB][j] = SP;
                            j++;
                        }
                    }
                }

                m_iWinnableSB[SB][i] = MainBoard.Null;
                m_iLosableSB[SB][j] = MainBoard.Null;
            }
            else
            {
                //null if marked
                m_bGameWinningSB[SB] = false;
                m_bGameLosingSB[SB] = false;
                m_iWinnableSB[SB][0] = MainBoard.Null;
                m_iLosableSB[SB][0] = MainBoard.Null;
            }

            //if there exist winning moves
            if (m_bGameWinningSB[SB] && m_iWinnableSB[SB][0] != MainBoard.Null)
                m_bGameWinningSB[9] = true;
            if (m_bGameLosingSB[SB] && m_iLosableSB[SB][0] != MainBoard.Null)
                m_bGameLosingSB [9] = true;
        }

    }

    /**
     * Function searches for SmallBoard wins, if it is desperate, it will look for trades
     * It will however never take bait, or trades that result in game winning or blocking moves
     * @param Desperate The level the AI will choose moves
     * @return an int move, if move is below -1, it is a trade and needs to be converted with n/(-2) - 1
     */
    private int SearchForSmallBoardWin(boolean Desperate){
        int SB;
        int SQ;
        int trade = MainBoard.Null;
        int i = 0;
        //for all valid moves
        while (m_iValidSpaces[i] != MainBoard.Null) {
            //translate move
            SB = (m_iValidSpaces[i] / 9);
            SQ = (m_iValidSpaces[i] + SB) % 10;

            //if it does not produce a free move and is winnable
            if (SBiswinnable(true, SB, SQ))
                if((m_mbBoard.spaceIsUnmarked(SQ) && SB != SQ) || Desperate && areNoWinningMoves()){
                    if (!(m_bGameLosingSB[SQ] || m_bGameWinningSB[SQ])) {
                        //if the move wont lose the game
                        //if not bait
                        if (m_iLosableSB[SQ][0] == MainBoard.Null){
                            if(Desperate)
                                return m_iValidSpaces[i];
                            else
                                if(m_iWinnableSB[SQ][0] == MainBoard.Null)//if AI cannot win there
                                    return m_iValidSpaces[i];
                        }
                        else
                            if(Desperate)
                                trade = (m_iValidSpaces[i] + 1)*(-2);
                    }
                    else {
                        if (Desperate) {
                            if (!(m_iWinnableSB[SQ][0] != MainBoard.Null || m_iLosableSB[SQ][0] != MainBoard.Null))
                                return m_iValidSpaces[i];
                        }
                    }
            }
            i++;
        }
        return trade;
    }

    /**
     * Searches if a move can win a certain small board
     * @param SBWinning if true, looking for AI winnables, else losables
     * @param SB the Smallboard
     * @param SP the Space
     * @return the answer
     */
    private boolean SBiswinnable(boolean SBWinning, int SB, int SQ){
        int i = 0;
        if (SBWinning){
            while (m_iWinnableSB[SB][i] != MainBoard.Null){
                if (m_iWinnableSB[SB][i] == SQ)
                    return true;
                i++;
            }
        }
        else{
            while (m_iLosableSB[SB][i] != MainBoard.Null){
                if (m_iLosableSB[SB][i] == SQ)
                    return true;
                i++;
            }
        }
        return false;
    }

    /**
     * Searches for a blocking move amoung the current valid moves.
     * @param Desperate level AI is going to look
     * @return a Move,
     *      -1 if none found
     *      > -1 if a good(free) block
     *      negative even (-inf, -2] if evenBlockTrade
     *      negative odd (-inf, -3] if unevenBlockTrade
     */
    private int SearchForSmallBoardBlocks(boolean Desperate){
        int SB;
        int SQ;
        int evenTrade = MainBoard.Null;
        int unevenTrade = MainBoard.Null;
        int i = 0;
        int check1 = 0;
        int check2 = 0;
        //for all valid moves
        while (m_iValidSpaces[i] != MainBoard.Null) {
            //translate move
            SB = (m_iValidSpaces[i] / 9);
            SQ = (m_iValidSpaces[i] + SB) % 10;

            if (SB == SQ) {
                if (m_iLosableSB[SQ][0] == SQ)
                    check1 = 1;
                else if (m_iWinnableSB[SQ][0] == SQ)
                    check2 = 1;
            }
            else{
                check1 = check2 = 0;
            }
            //if it does not produce a free move and is blockable
            if (SBiswinnable(false, SB, SQ))
                if(m_mbBoard.spaceIsUnmarked(SQ)){
                    if (!(m_bGameLosingSB[SQ] || m_bGameWinningSB[SQ])) {
                        //if the move wont lose the game
                        //if not bait
                        if (m_iLosableSB[SQ][check1] == MainBoard.Null){
                            if(Desperate)
                                evenTrade = (m_iValidSpaces[i] + 1)*(-2); // translate, block trade, uses even negative numbers
                            else
                                if(m_iWinnableSB[SQ][check2] == MainBoard.Null)//if AI cannot win there
                                    return m_iValidSpaces[i];
                        }
                        else
                            if(Desperate)
                                unevenTrade = (m_iValidSpaces[i] + 1)*(-2) - 1; //uneven Block trade, get it? it uses the odd negative numbers
                    }
                    else {
                        if (Desperate) {
                            if (!(m_iWinnableSB[SQ][check2] != MainBoard.Null || m_iLosableSB[SQ][check1] != MainBoard.Null) )
                                return m_iValidSpaces[i];
                        }
                    }
                }
            i++;
        }
        //if there was a even trade, return it, else return the uneventrade
        if (evenTrade != MainBoard.Null)
            return evenTrade;
        return unevenTrade;
    }

    /**
     *
     * if you can't win a smallboard
     * send them to a small board that is ummarked and one that cannot win the game
     * find a smallboard that cant win the game
     * @param Desperate level AI is going to look
     * @return a Move,
     *      -1 if none found
     *      > -1 if can send to a SB they cannot win
     *      negative even (-inf, -2] if block for them
     *      negative odd (-inf, -3] if safe loser
     */
    private int TrySabotage(boolean Desperate){
        int SB;
        int SQ;
        int block = MainBoard.Null;
        int safeLoser = MainBoard.Null;
        int i = 0;
        //for all valid moves
        while (m_iValidSpaces[i] != MainBoard.Null) {
            //translate move
            SB = (m_iValidSpaces[i] / 9);
            SQ = (m_iValidSpaces[i] + SB) % 10;

            if(m_mbBoard.spaceIsUnmarked(SQ)){
                if (!(m_bGameLosingSB[SQ] || m_bGameWinningSB[SQ])) {
                    //if the move wont lose the game
                    //if not bait
                    if (m_iLosableSB[SQ][0] == MainBoard.Null){
                        if(Desperate)
                            block = (m_iValidSpaces[i] + 1)*(-2); // translate, block , uses even negative numbers
                        else
                            if(m_iWinnableSB[SQ][0] == MainBoard.Null)//if AI cannot win there
                                return m_iValidSpaces[i];
                    }
                    else
                    if(Desperate)
                        safeLoser = (m_iValidSpaces[i] + 1)*(-2) - 1; //safe winner it uses the odd negative numbers
                }
                else {
                    if (Desperate) {
                        if (!(m_iWinnableSB[SQ][0] != MainBoard.Null || m_iLosableSB[SQ][0] != MainBoard.Null))
                            return m_iValidSpaces[i];
                    }
                }
            }
            i++;
        }
        //if there was a even trade, return it, else return the uneventrade
        if (block != MainBoard.Null)
            return block;
        return safeLoser;
    }

}
