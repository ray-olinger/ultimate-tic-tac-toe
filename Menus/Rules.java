package Menus;

import Implementation.ImageIconCreator;
import Implementation.JBackgroundPanel;
import Main.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/**
 * Menu class that holds the JBackgroundPanel used for the Rules menu of the application.
 */
public class Rules implements Menu {

    // Singleton instance of the Rules object
    private static Rules instance = null;

    /**
     * The Singleton method to retrieve the instance of Rules.
     * @return Returns the instance of Rules.
     */
    public static Rules getInstance() {
        if (instance == null)
            instance = new Rules();
        return instance;
    }

    // The instance of the ImageIconCreator object
    private ImageIconCreator icon = ImageIconCreator.getInstance();

    // Graphics objects representing the Rules menu
    private JBackgroundPanel rulesPanel;
    private JButton backButton;

    /**
     * Constructs a Rules object.
     */
    private Rules() {
        // Add the Rules image as the background image
        rulesPanel = new JBackgroundPanel(icon.RULES);
        // Positions the panel on the frame
        rulesPanel.setBounds(5,6,800,600);
        rulesPanel.setOpaque(false);
        rulesPanel.setLayout(null);

        // Create the Back button
        backButton = new JButton(icon.BACK_BUTTON);
        backButton.setOpaque(false);
        backButton.setContentAreaFilled(false);

        // Add the button to the panel
        rulesPanel.add(backButton);

        // Position the button
        backButton.setBounds(75,485,120,40);

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
        gui.frame.add(rulesPanel);
    }

    /**
     * @see Menu
     */
    public void ExitMenu() {
        GUI gui = GUI.getInstance();
        gui.frame.remove(rulesPanel);
        gui.frame.revalidate();
        gui.frame.repaint();
    }
}
