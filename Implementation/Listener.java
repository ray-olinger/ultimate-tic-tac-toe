package Implementation;

import Game.GameController;
import Game.Square;
import Players.Player;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Custom class of ActionListener that will be used for detecting clicks on Square objects.
 */
public class Listener implements ActionListener {

    // Singleton instance of the Listener object
    private static Listener instance = null;

    /**
     * The Singleton method to retrieve the instance of Listener.
     * @return Returns the instance of Listener.
     */
    public static Listener getInstance() {
        if (instance == null)
            instance = new Listener();
        return instance;
    }

    // Holds the current Player object
    private Player m_pCurrentPlayer;
    // Holds the current GameController object
    private GameController m_gcGameController;
    // Boolean for if the player is allowed to click on a Square object
    private boolean m_bPlayable = true;

    /**
     * Constructs an empty Listener object.
     */
    private Listener() {

    }

    /**
     * Determines the ID of the Square object that was clicked.
     * @param e Click event triggered by the Square object.
     */
    public void actionPerformed(ActionEvent e) {
        // If player is allowed to play
        if (m_bPlayable) {
            // Gets the ID of the Square object and sets that as the current player's move
            m_pCurrentPlayer.setPlayerMove(((Square) e.getSource()).getID());
            m_gcGameController.playGame();
        }
    }

    /**
     * Sets the current Player object.
     * @param p Current Player object.
     */
    public void setCurrentPlayer(Player p) {
        m_pCurrentPlayer = p;
    }

    /**
     * Sets the current GameController object.
     * @param gc Current GameController object.
     */
    public void setGameController(GameController gc) {
        m_gcGameController = gc;
    }

    /**
     * Sets the playable flag.
     * @param b Boolean flag for playable.
     */
    public void setPlayable(boolean b) {
        m_bPlayable = b;
    }
}
