package Menus;

import Implementation.ImageIconCreator;
import Implementation.JBackgroundPanel;
import Main.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;

/**
 * Menu class that holds the JBackgroundPanel used for the Main menu of the application.
 */
public class MainMenu implements Menu {

    // Singleton instance of the MainMenu object
    private static MainMenu instance = null;

    /**
     * The Singleton method to retrieve the instance of MainMenu.
     * @return Returns the instance of MainMenu.
     */
    public static MainMenu getInstance() {
        if (instance == null)
            instance = new MainMenu();
        return instance;
    }

    // The instance of the ImageIconCreator object
    private ImageIconCreator icon = ImageIconCreator.getInstance();
    // The instance of the Game object
    private Game gameMenu = Game.getInstance();
    // The instance of the Rules object
    private Rules rulesMenu = Rules.getInstance();
    // The instance of the Options object
    private Options optionsMenu = Options.getInstance();

    // Graphics objects representing the Main menu
    private JBackgroundPanel mainPanel;
    private JButton startButton;
    private JButton optionsButton;
    private JButton rulesButton;
    private JButton quitButton;
    private JButton blooperButton;
    private JLabel blooperLabel;

    /**
     * Constructs a MainMenu object.
     */
    private MainMenu() {
        // Add the logo as the background image
        mainPanel = new JBackgroundPanel(icon.LOGO);
        // Positions the panel on the frame
        mainPanel.setBounds(5, 6, 800, 600);
        mainPanel.setOpaque(false);
        mainPanel.setLayout(null);

        // Create the Start Game button
        startButton = new JButton(icon.START_BUTTON);
        startButton.setOpaque(false);
        startButton.setContentAreaFilled(false);

        // Create the Options button
        optionsButton = new JButton(icon.OPTIONS_BUTTON);
        optionsButton.setOpaque(false);
        optionsButton.setContentAreaFilled(false);

        // Create the Rules button
        rulesButton = new JButton(icon.RULES_BUTTON);
        rulesButton.setOpaque(false);
        rulesButton.setContentAreaFilled(false);

        // Create the Quit button
        quitButton = new JButton(icon.QUIT_BUTTON);
        quitButton.setOpaque(false);
        quitButton.setContentAreaFilled(false);

        // Create the blooper button
        blooperButton = new JButton();
        blooperButton.setOpaque(false);
        blooperButton.setContentAreaFilled(false);
        blooperButton.setBorderPainted(false);

        // Add the buttons to the panel
        mainPanel.add(startButton);
        mainPanel.add(optionsButton);
        mainPanel.add(rulesButton);
        mainPanel.add(quitButton);
        mainPanel.add(blooperButton);

        // Position the buttons
        startButton.setBounds(250, 225, 300, 50);
        optionsButton.setBounds(250, 300, 300, 50);
        rulesButton.setBounds(250, 375, 300, 50);
        quitButton.setBounds(250, 450, 300, 50);
        blooperButton.setBounds(740, 49, 10, 10);

        // Display the Game menu when clicked
        startButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ExitMenu();
                gameMenu.DisplayMenu();
            }
        } );

        // Display the Options menu when clicked
        optionsButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ExitMenu();
                optionsMenu.DisplayMenu();
            }
        } );

        // Display the Rules menu when clicked
        rulesButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ExitMenu();
                rulesMenu.DisplayMenu();
            }
        } );

        // Close the application when clicked
        quitButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUI gui = GUI.getInstance();
                gui.frame.dispatchEvent(new WindowEvent(
                        gui.frame, WindowEvent.WINDOW_CLOSING));
            }
        } );

        // Activate the blooper when clicked
        blooperButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ExitMenu();
                StartBlooper();
                /*clicks += 1;
                if (clicks == 2)
                {
                    GUI.getInstance().frame.remove(blooperLabel);
                    GUI.getInstance().frame.revalidate();
                    GUI.getInstance().frame.repaint();
                }
                if(clicks > 1)
                {
                   
                    int randomX = (int)(Math.random()*800);
                    int randomY = (int)(Math.random()*600);
                    try{
                        
                        JLabel wanWan = new JLabel(new ImageIcon(ImageIO.read(new File("C:/Users/Branden/Pictures/wan wan intensifies.png"))));
                        wanWan.setOpaque(false);
                        
                        GUI.getInstance().frame.add(wanWan);
                        wanWan.setBounds(randomX, randomY, 200, 200);

                    }
                    catch (Exception ex)
                    {
                    
                    }
                }*/
                blooperButton.setEnabled(false);
            }
        });
    }
    //private int clicks = 0;
    /**
     * @see Menu
     */
    public void DisplayMenu() {
        GUI gui = GUI.getInstance();
        gui.frame.add(mainPanel);
    }

    /**
     * @see Menu
     */
    public void ExitMenu() {
        GUI gui = GUI.getInstance();
        gui.frame.remove(mainPanel);
        gui.frame.revalidate();
        gui.frame.repaint();
    }

    /**
     * Activates the blooper.
     */
    private void StartBlooper() {
        // Plays the blooper audio clip
        try {
            Clip blooper = AudioSystem.getClip();
            AudioInputStream ais = AudioSystem.getAudioInputStream(getClass().getResource("/Resources/Blooper/Blooper.wav"));
            blooper.open(ais);
            // When the clip finishes this LineListener will catch that event
            blooper.addLineListener(new LineListener() {
                @Override
                public void update(LineEvent event) {
                    StopBlooper();
                }
            });
            blooper.start();
        } catch(LineUnavailableException | IOException | UnsupportedAudioFileException ex) {
            System.out.println("Error -- Audio clip for blooper could not be found.");
        }

        // Add the GIF to the blooper label
        blooperLabel = new JLabel(new ImageIcon(getClass().getResource("/Resources/Blooper/Snoop.gif")));

        // Add the label to the screen
        GUI gui = GUI.getInstance();
        gui.frame.add(blooperLabel);
        gui.frame.add(mainPanel);
        blooperLabel.setBounds(255, 0, 290, 595);
    }

    /**
     * Deactivates the blooper.
     */
    private void StopBlooper() {
        GUI gui = GUI.getInstance();
        gui.frame.remove(blooperLabel);
        gui.frame.revalidate();
        gui.frame.repaint();
        blooperButton.setEnabled(true);
    }
}
