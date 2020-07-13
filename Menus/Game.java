package Menus;

import Game.GameController;
import Implementation.ImageIconCreator;
import Implementation.JBackgroundPanel;
import Main.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * Menu class that holds the JBackgroundPanel used for the Game Type menu of the application.
 */
public class Game implements Menu {

    // Singleton instance of the Game object
    private static Game instance = null;

    /**
     * The Singleton method to retrieve the instance of Game.
     * @return Returns the instance of Game.
     */
    public static Game getInstance() {
        if (instance == null)
            instance = new Game();
        return instance;
    }

    // The instance of the ImageIconCreator
    private ImageIconCreator icon = ImageIconCreator.getInstance();

    // Graphics objects representing the Game Type menu
    private JBackgroundPanel gameTypePanel;
    private JButton PvAIButton;
    private JButton PvPButton;
    private JButton backButton;

    /**
     * Constructs a Game object.
     */
    private Game() {
        // Add the Select Game Type image as the background image
        gameTypePanel = new JBackgroundPanel(icon.GAMETYPE);
        // Position the panel on the frame
        gameTypePanel.setBounds(5,6,800,600);
        gameTypePanel.setOpaque(false);
        gameTypePanel.setLayout(null);

        // Create the Player vs AI button
        PvAIButton = new JButton(icon.PVAI_BUTTON);
        PvAIButton.setOpaque(false);
        PvAIButton.setContentAreaFilled(false);

        // Create the Player vs Player button
        PvPButton = new JButton(icon.PVP_BUTTON);
        PvPButton.setOpaque(false);
        PvPButton.setContentAreaFilled(false);

        // Create the Back button
        backButton = new JButton(icon.BACK_BUTTON);
        backButton.setOpaque(false);
        backButton.setContentAreaFilled(false);

        // Add the buttons to the panel
        gameTypePanel.add(PvAIButton);
        gameTypePanel.add(PvPButton);
        gameTypePanel.add(backButton);

        // Position the buttons
        PvAIButton.setBounds(250,225,300,50);
        PvPButton.setBounds(250,325,300,50);
        backButton.setBounds(75,485,120,40);

        // Create a Player vs AI game when clicked
        PvAIButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ExitMenu();
                GameController gc = new GameController();
                gc.setupGame(false, Options.getInstance().getDifficulty());
            }
        } );

        // Create a Player vs Player game when clicked
        PvPButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ExitMenu();
                GameController gc = new GameController();
                gc.setupGame(true, null);
            }
        } );

        // Display the Main menu when clicked
        backButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ExitMenu();
                MainMenu.getInstance().DisplayMenu();
            }
        } );
    }

    /**
     * @see Menu
     */
    public void DisplayMenu() {
        GUI gui = GUI.getInstance();
        gui.frame.add(gameTypePanel);
    }

    /**
     * @see Menu
     */
    public void ExitMenu() {
        GUI gui = GUI.getInstance();
        gui.frame.remove(gameTypePanel);
        gui.frame.revalidate();
        gui.frame.repaint();
    }
}
