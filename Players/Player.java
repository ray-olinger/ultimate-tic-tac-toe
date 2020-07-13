package Players;

import Game.Mark;

/**
 * The class representing a player in the game.
 */
public class Player {

    // Mark of the Player
    protected Mark m_mPlayerMark;
    // Boolean for if the Player is human or AI
    protected boolean Human;
    // Integer used for relaying the ID of the Square that the Player selected
    private int m_iMove;
    // For AI, tells the opponent Player's Mark
    protected Mark m_mOpponentMark;
    // The player number, either 1 or 2
    protected int m_iPlayerNumber;

    /**
     * Constructs a Player object.
     */
    public Player() {

    }

    /**
     * Constructs a Player object with the specified player number.
     * @param pn The integer for the player number.
     */
    public Player(int pn) {
        m_iPlayerNumber = pn;
        Human = true;
    }

    /**
     * Sets the Mark of the Player and determines the Mark of the opponent Player.
     * @param m The Mark.
     */
    public void setMark(Mark m) {
        m_mPlayerMark = m;

        // Set the opponent Player's Mark
        if (m == Mark.O)
            m_mOpponentMark = Mark.X;
        else
            m_mOpponentMark = Mark.O;
    }

    /**
     * Gets the Mark of the Player.
     * @return Returns the Player's Mark.
     */
    public Mark getMark() {
        return m_mPlayerMark;
    }

    /**
     * Gets the player number of the Player.
     * @return Returns an integer for the player number.
     */
    public int getPlayerNumber() {
        return m_iPlayerNumber;
    }

    /**
     * Gets the move selected by the Player.
     * @return Returns the ID of the Square selected by the Player.
     */
    public int GetPlayerMove() {
        return m_iMove;
    }

    /**
     * Sets the move selected by the Player.
     * @param id The ID of the Square selected.
     */
    public void setPlayerMove(int id) {
        m_iMove = id;
    }

    /**
     * Gets if the Player is human or AI.
     * @return Returns a Boolean for if the Player is human.
     */
    public boolean isHuman() {
        return Human;
    }
}
