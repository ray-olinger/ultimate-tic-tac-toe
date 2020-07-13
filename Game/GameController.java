package Game;

import Implementation.ImageIconCreator;
import Implementation.JBackgroundPanel;
import Implementation.Listener;
import Main.GUI;
import Menus.MainMenu;
import Players.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The class that controls the overall function of the game.
 */
public class GameController {

    // The instance of the Listener object
    private Listener listener = Listener.getInstance();
    // The instance of the ImageIconCreator object
    private ImageIconCreator icon = ImageIconCreator.getInstance();
    // The instance of the GUI object
    private GUI gui = GUI.getInstance();

    // Holds the MainBoard object
    private MainBoard m_mbMainBoard;
    // Array of two Player objects representing the ones playing
    private Player[] m_pPlayers = new Player[2];
    // Holds the current Player object
    private Player m_pCurrentPlayer;
    // Holds the Event that occurred on the board
    private Event m_eGameEvent = Event.INVALIDSPACE;
    // A timer object for allowing the AI to make a move
    private Timer m_tTimer;

    // The panel that will be displayed upon a win
    private JBackgroundPanel winPanel = new JBackgroundPanel();

    /**
     * Constructs a GameController object and creates two Player objects.
     */
    public GameController() {
        m_pPlayers[0] = new Player(1);
        m_pPlayers[1] = new Player(2);
        m_pCurrentPlayer = m_pPlayers[0];
        listener.setCurrentPlayer(m_pCurrentPlayer);

        listener.setGameController(this);
        listener.setCurrentPlayer(m_pCurrentPlayer);
        listener.setPlayable(true);
    }

    /**
     * Creates the MainBoard object, sets up the Player objects and the GUI.
     * @param PvP A Boolean that determines whether to set up the AI or not.
     * @param AIDiff The difficulty selected for the AI by the user.
     */
    public void setupGame(boolean PvP, Difficulty AIDiff){
        m_mbMainBoard = new MainBoard();

        // If Player vs Player, ie. no AI
        if (PvP)
            // Creates the board
            m_mbMainBoard.BuildBoard();
        else {
            // Creates the board and stores it for AI use
            MainBoard tempSmallBoards[] = m_mbMainBoard.BuildBoard();
            m_pPlayers[1] = new AI(AIDiff, m_mbMainBoard, tempSmallBoards, Mark.O);

            // For developer use only. Allows AI vs AI game when true
            if (false) {
                m_pPlayers[0] = new AI(AIDiff, m_mbMainBoard, tempSmallBoards, Mark.X, 1);
                m_pPlayers[1] = new AI(AIDiff, m_mbMainBoard, tempSmallBoards, Mark.O, 2);
                m_pCurrentPlayer = m_pPlayers[0];
            }
        }

        // Set the mark for each Player
        m_pPlayers[0].setMark(Mark.X);
        m_pPlayers[1].setMark(Mark.O);

        // Add the MainBoard object to the GUI
        gui.frame.add(m_mbMainBoard);
        m_mbMainBoard.setBounds(5, 6, 800, 600);

        // Create a timer that occurs every 1 second in order to delay AI response
        m_tTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // If the current Player object is an AI, then allow AI to play
                if (!m_pCurrentPlayer.isHuman())
                    playGame();
            }
        });
    }

    /**
     * Gets the move selected by the Player and translates it to game input.
     */
    public void playGame() {
        // Integer between 0 and 80 corresponding to the Square object clicked
        int playSpace;
        // Integers to determine the SmallBoard and the Square on that SmallBoard
        int SB, SQ;

        // Gets the player move
        playSpace = m_pCurrentPlayer.GetPlayerMove();

        // Translates the move
        SB = (playSpace/9);
        SQ = (playSpace + SB)%10;

        // Sends the move to the board
        m_eGameEvent = m_mbMainBoard.SendUserInput(SB, SQ, m_pCurrentPlayer.getMark());

        // If the last move won the game or ended it in a draw
        if ((m_eGameEvent == Event.WINNINGMOVE || m_eGameEvent == Event.CATS)) {
            // Create a button to add to the winPanel
            JButton returnButton = new JButton();
            returnButton.setOpaque(false);
            returnButton.setContentAreaFilled(false);
            returnButton.setBorderPainted(false);
            returnButton.setBounds(0, 0, 800, 600);

            // When the button is clicked, return to the main menu
            returnButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    exitToMenu();
                    MainMenu.getInstance().DisplayMenu();
                }
            });

            winPanel.add(returnButton);

            // If the last move ended in a win
            if (m_eGameEvent == Event.WINNINGMOVE) {
                // If the current Player object is human
                if (m_pCurrentPlayer.isHuman()) {
                    // If the current Player is 1, display Player 1 win
                    if (m_pCurrentPlayer.getPlayerNumber() == 1)
                        winPanel.setIcon(icon.P1_WIN);
                    // Else display Player 2 win
                    else
                        winPanel.setIcon(icon.P2_WIN);
                }
                // Else current Player is AI, display AI win
                else
                    winPanel.setIcon(icon.AI_WIN);
            }
            // Else last move was draw, display draw
            else
                winPanel.setIcon(icon.DRAW);

            // Re-configures the timer object to now occur every 2 seoncds
            m_tTimer.stop();
            // Occurs only once. After 2 seconds, display the winPanel
            m_tTimer = new Timer(2000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    gui.frame.remove(m_mbMainBoard);
                    gui.frame.add(winPanel);
                    winPanel.setBounds(5, 6, 800, 600);
                    m_tTimer.stop();
                }
            });

            m_tTimer.start();
        }
        // Else last move was not a win or a draw
        else {
            // If the last move was valid, switch players
            if (m_eGameEvent == Event.VALIDMOVE)
                SwitchPlayer();

            // If the current Player is an AI, start the timer and set Listener playable to false
            if (!m_pCurrentPlayer.isHuman()) {
                m_tTimer.start();
                listener.setPlayable(false);
            }
            // Else current Player is human, stop timer and set Listener playable to true
            else {
                m_tTimer.stop();
                listener.setPlayable(true);
            }
        }
    }

    /**
     * Switches the Player objects and updates Listener and display.
     */
    private void SwitchPlayer(){
        m_pCurrentPlayer = m_pPlayers[(m_pCurrentPlayer.getPlayerNumber())%2];
        listener.setCurrentPlayer(m_pCurrentPlayer);
        m_mbMainBoard.changePlayerIcon();
    }

    /**
     * Removes the winPanel for exiting to main menu.
     */
    private void exitToMenu(){
        gui.frame.remove(winPanel);
        gui.frame.revalidate();
        gui.frame.repaint();
    }
}
