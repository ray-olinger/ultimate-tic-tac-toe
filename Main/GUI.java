package Main;

import Implementation.ImageIconCreator;
import Menus.MainMenu;

import javax.swing.*;

/**
 * A class that holds a JFrame object that forms the base GUI of the application.
 */
public class GUI {

    // Singleton instance of the GUI object
    private static GUI instance = null;

    /**
     * The Singleton method to retrieve the instance of GUI.
     * @return Returns the instance of GUI.
     */
    public static GUI getInstance() {
        if (instance == null)
            instance = new GUI();
        return instance;
    }

    // The instance of the MainMenu class
    private MainMenu mainMenu = MainMenu.getInstance();

    // The JFrame object that will be the backbone of the application
    public JFrame frame = new JFrame();

    /**
     * Constructs an empty GUI object.
     */
    private GUI() {

    }

    /**
     * Sets up the JFrame object and displays the main menu.
     */
    private void setupFrame() {
        frame.setContentPane(new JLabel(ImageIconCreator.getInstance().BACKGROUND));
        frame.pack();
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);

        mainMenu.DisplayMenu();
    }

    /**
     * This main method creates the GUI.
     * @param args Command Line arguments.
     */
    public static void main(String[] args) {
        GUI gui = GUI.getInstance();
        gui.setupFrame();
    }
}
