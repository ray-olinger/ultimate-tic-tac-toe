package Game;

import Implementation.ImageIconCreator;
import Implementation.JBackgroundPanel;

import javax.swing.*;

/**
 * Custom class of the JBackgroundPanel that functions as the main board.
 */
public class MainBoard extends JBackgroundPanel {

    // The instance of the ImageIconCreator object
    private ImageIconCreator icon = ImageIconCreator.getInstance();

    // Array of Spaces that will hold the 9 Space objects
    protected Space[] m_sSpaces;
    // Boolean value to determine if a free move state has occurred
    private boolean m_bFreeMove;
    // Boolean value to determine the current player
    private boolean m_bPlayerOne = true;

    /**
     * Constructs a MainBoard object.
     */
    protected MainBoard() {
        // Initial move is a free move
        m_bFreeMove = true;

        // Sets the background image to main board with player 1's turn
        this.setIcon(icon.MAIN_BOARD_P1);
        this.setOpaque(false);
        this.setLayout(null);
    }

    /**
     * Builds the 9 SmallBoard objects and returns an array of them.
     * @return An array of SmallBoards that will either be used by AI or destroyed.
     */
    MainBoard[] BuildBoard() {
        // Create an array of 9 SmallBoard objects
        SmallBoard[] SB = new SmallBoard[9];

        // For each SmallBoard, set the ID, add to the MainBoard, set the position, and set as valid
        for (int i = 0; i < 9; i++) {
            SB[i] = new SmallBoard();
            SB[i].setID(i);
            this.add(SB[i]);
            SB[i].setBounds(160*(i%3 + 1) + 5*(i%3), 60 + 165*(i/3), 150, 150);
            SB[i].setAsValid();
        }

        // Set the array of Spaces to the array of SmallBoard objects
        this.m_sSpaces = SB;
        return SB;
    }

    /**
     * Sends the user input down to the space of the current board.
     * @param SB The SmallBoard on the MainBoard.
     * @param SQ The Square on the SmallBoard.
     * @param playerMark The Mark of the current Player.
     * @return Returns the event that occurred.
     */
    protected Event SendUserInput(int SB, int SQ, Mark playerMark) {
        // Sends the move to the SmallBoard and returns the event that occurred on it
        switch (m_sSpaces[SB].SendUserMove(playerMark, SQ)) {
            case INVALIDSPACE:
                return Event.INVALIDSPACE;

            case INVALIDSECTOR:
                return Event.INVALIDSECTOR;

            case VALIDMOVE:
                DetermineNextMove(SQ);
                return Event.VALIDMOVE;

            case WINNINGMOVE:
                // If win occurred on SmallBoard, draw the appropriate mark
                m_sSpaces[SB].setMark(playerMark);
                DrawOnSpace(SB, playerMark);

                WinStrike Strike = CheckForWin(playerMark, SB);
                // If a win occurred, draw the strike through the win
                if (Strike != WinStrike.NONE) {
                    DrawStrike(Strike);
                    return Event.WINNINGMOVE;
                }
                // Else a valid move
                else {
                    // Check for draw
                    if (CheckForCats())
                        return Event.CATS;
                    else {
                        DetermineNextMove(SQ);
                        return Event.VALIDMOVE;
                    }
                }

            case CATS:
                // If draw occurred on SmallBoard, draw the appropriate mark
                m_sSpaces[SB].setMark(Mark.CATS);
                DrawOnSpace(SB, Mark.CATS);

                if (CheckForCats())
                    return Event.CATS;
                else {
                    DetermineNextMove(SQ);
                    return Event.VALIDMOVE;
                }
        }
        throw new IllegalStateException("Illegal Game State: MainBoard SendUserInput");
    }

    /**
     * A drawing function that draws a mark on the SmallBoard.
     * @param n The index of the SmallBoard that needs to be drawn on.
     * @param spaceMark The Mark of the current Player.
     */
    protected void DrawOnSpace(int n, Mark spaceMark) {
        JLabel label = new JLabel();

        if (spaceMark == Mark.X)
            label.setIcon(icon.SMALL_BOARD_X);
        else if (spaceMark == Mark.O)
            label.setIcon(icon.SMALL_BOARD_O);
        else
            label.setIcon(icon.SMALL_BOARD_C);

        ((SmallBoard)m_sSpaces[n]).setWinLabel(label);
        ((SmallBoard)m_sSpaces[n]).reDraw();
    }

    /**
     * Does not draw a strike for a win on MainBoard, but sets all the SmallBoards as invalid.
     * @param Strike The certain strike that needs to be drawn.
     */
    protected void DrawStrike(WinStrike Strike) {
        // If the last move was a free move
        if (m_bFreeMove) {
            m_bFreeMove = false;
            for (int i = 0; i < 9; i++)
                m_sSpaces[i].setAsInValid();
        }
    }

    /**
     * Determines the next SmallBoard that is valid.
     * @param SP The ID of the Square that the user just played in.
     */
    protected void DetermineNextMove(int SP) {
        // If the next move is a free move
        if (NextMoveIsFreeMove(SP)) {
            m_bFreeMove = true;
            // Set all unmarked SmallBoards to valid
            for (int i = 0; i < 9; i++) {
                if (m_sSpaces[i].getMark() == Mark.UNMARKED)
                    m_sSpaces[i].setAsValid();
            }
        }
        // Else next move is not a free move
        else {
            // If the previous move was a free move
            if (m_bFreeMove) {
                m_bFreeMove = false;
                for (int i = 0; i < 9; i++)
                    m_sSpaces[i].setAsInValid();
            }
            m_sSpaces[SP].setAsValid();
        }
    }

    /**
     * Checks to see if a draw has occurred.
     * @return Returns a Boolean for if the game is a draw.
     */
    protected boolean CheckForCats(){
        for (int i = 0; i < 9; i++) {
            if (m_sSpaces[i].getMark() == Mark.UNMARKED)
                return false;
        }
        return true;
    }

    /**
     * Changes the MainBoard image to show the current Player.
     */
    public void changePlayerIcon() {
        // If Player 1 just went, change to Player 2 image
        if (m_bPlayerOne) {
            this.setIcon(icon.MAIN_BOARD_P2);
            m_bPlayerOne = false;
        }
        // Else change to Player 1 image
        else {
            this.setIcon(icon.MAIN_BOARD_P1);
            m_bPlayerOne = true;
        }
    }

    /*----------------------------AI CHECKING METHODS AND FIELDS----------------------------*/

    public static final int Null = -1;

    /**
     * Ensures that the AI will make a valid move.
     * @return Returns an array of ints of the valid IDs.
     */
    public int[] getValidIDs() {
        int[] temp = new int[10];
        int j = 0;

        // Gets valid IDs
        for (int i = 0; i < 9; i++) {
            if (m_sSpaces[i].isValid()) {
                temp[j] = m_sSpaces[i].getID();
                j++;
            }
        }

        temp[j] = Null; // Adds a null terminator
        return temp;
    }

    /**
     * Returns whether a Space is unmarked.
     * @param SP The index of the Space.
     * @return Returns a Boolean for if the Space is unmarked.
     */
    public boolean spaceIsUnmarked(int SP) {
        return m_sSpaces[SP].getMark() == Mark.UNMARKED;
    }

    /**
     * Returns whether the next move is a free move.
     * @param SP The ID of the Square that the user just played in.
     * @return Returns a Boolean for if the next move is a free move.
     */
    private boolean NextMoveIsFreeMove(int SP) {
        return m_sSpaces[SP].getMark() != Mark.UNMARKED;
    }

    /**
     * Converts an integer to an enumerated type WinStrike.
     * @param playerMark The Mark of the current Player object.
     * @param n The ID of the Space.
     * @return Returns a WinStrike value.
     */
    public WinStrike CheckForWin(Mark playerMark, int n) {
        return WinStrike.values()[CheckForWinInt(playerMark, n)];
    }

    /**
     * Determines where the win occurred.
     * @param playerMark The Mark of the current Player object.
     * @param n The ID of the Space.
     * @return Returns an int to describe where the win occurred.
     */
    public int CheckForWinInt(Mark playerMark, int n) {
        // If the last move was in the center
        if (n == 4) {
            if (m_sSpaces[2].getMark() == playerMark)
                if (m_sSpaces[6].getMark() == playerMark)
                return 1; // WinStrike.values()[1];

            if (m_sSpaces[0].getMark() == playerMark)
                if (m_sSpaces[8].getMark() == playerMark)
                    return 2; // WinStrike.values()[2];

            if (m_sSpaces[3].getMark() == playerMark)
                if (m_sSpaces[5].getMark() == playerMark)
                    return 4; // WinStrike.values()[4];

            if (m_sSpaces[1].getMark() == playerMark)
                if (m_sSpaces[7].getMark() == playerMark)
                    return 7; // WinStrike.values()[7];
        }
        else {
            // Horizontals
            switch (n%3) {
                // Top Horizontal
                case 0:
                    if (m_sSpaces[n + 1].getMark() == playerMark)
                        if (m_sSpaces[n + 2].getMark() == playerMark)
                            return 3 + n / 3;
                    break;
                // Middle Horizontal
                case 1:
                    if (m_sSpaces[n - 1].getMark() == playerMark)
                        if (m_sSpaces[n + 1].getMark() == playerMark)
                            return 3 + n / 3;
                    break;
                // Bottom Horizontal
                case 2:
                    if (m_sSpaces[n - 1].getMark() == playerMark)
                        if (m_sSpaces[n - 2].getMark() == playerMark)
                            return 3 + n / 3;
                    break;
            }

            // Verticals
            if (m_sSpaces[(n+3)%9].getMark() == playerMark)
                if (m_sSpaces[(n+6)%9].getMark() == playerMark)
                    return n%3 + 6;

            // Diagonals
            if (n%2 == 0)
                if (m_sSpaces[4].getMark() == playerMark)
                    if (m_sSpaces[8 - n].getMark() == playerMark)
                        return (n%4 + 1)%3 + 1;
        }

        return 0; // If no win occurred
    }
}
