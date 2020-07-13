package Menus;

import Implementation.ImageIconCreator;
import Implementation.JBackgroundPanel;
import Main.GUI;
import Players.Difficulty;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Menu class that holds the JBackgroundPanel used for the Options menu of the application.
 */
public class Options implements Menu {

    // Singleton instance of the Options object
    private static Options instance = null;

    /**
     * The Singleton method to retrieve the instance of Options.
     * @return Returns the instance of Options.
     */
    public static Options getInstance() {
        if (instance == null)
            instance = new Options();
        return instance;
    }

    // The instance of the ImageIconCreator object
    private ImageIconCreator icon = ImageIconCreator.getInstance();

    // Graphics objects representing the Options menu
    private JBackgroundPanel optionsPanel;
    private JButton uploadButton;
    private JButton backButton;
    private JComboBox aiDiffComboBox;
    private JCheckBox soundCheckBox;
    private JCheckBox customCheckBox;
    private JLabel xImagePrev;
    private JLabel oImagePrev;
    private JRadioButton xRadioButton;
    private JRadioButton oRadioButton;
    private FileDialog openFileDialog;

    /**
     * Constructs an Options object.
     */
    private Options() {
        // Add the Options image as the background image
        optionsPanel = new JBackgroundPanel(icon.OPTIONS);
        // Positions the panel on the frame
        optionsPanel.setBounds(5,6,800,600);
        optionsPanel.setOpaque(false);
        optionsPanel.setLayout(null);

        // Create the Upload button
        uploadButton = new JButton(icon.UPLOAD_BUTTON);
        uploadButton.setOpaque(false);
        uploadButton.setContentAreaFilled(false);

        // Create the Back button
        backButton = new JButton(icon.BACK_BUTTON);
        backButton.setOpaque(false);
        backButton.setContentAreaFilled(false);

        // Create the AI Difficulty combo box
        aiDiffComboBox = new JComboBox();
        // Adds all the difficulties as options
        for (Difficulty n : Difficulty.values()){
            aiDiffComboBox.addItem(n.toString());
        }
        // Sets EASY as the default difficulty
        aiDiffComboBox.setSelectedItem(Difficulty.REACTIVE_I.toString());

        // Create the Sound On/Off and Custom Images On/Off check boxes
        soundCheckBox = new JCheckBox();
        customCheckBox = new JCheckBox();

        // Create the image labels to show the preview of the markers
        xImagePrev = new JLabel(icon.SMALL_BOARD_X);
        oImagePrev = new JLabel(icon.SMALL_BOARD_O);

        // Create the Radio buttons used for choosing which marker you want to change
        xRadioButton = new JRadioButton();
        oRadioButton = new JRadioButton();

        // Add all of the graphics objects to the panel
        optionsPanel.add(uploadButton);
        optionsPanel.add(backButton);
        optionsPanel.add(aiDiffComboBox);
        optionsPanel.add(soundCheckBox);
        optionsPanel.add(customCheckBox);
        optionsPanel.add(xImagePrev);
        optionsPanel.add(oImagePrev);
        optionsPanel.add(xRadioButton);
        optionsPanel.add(oRadioButton);

        // Position the buttons
        uploadButton.setBounds(605,485,120,40);
        backButton.setBounds(75,485,120,40);

        // Position the combo box
        aiDiffComboBox.setBounds(400,160,200,35);

        // Position the checkboxes and set sounds on
        soundCheckBox.setBounds(400,230,20,20);
        soundCheckBox.setOpaque(false);
        soundCheckBox.setSelected(true);
        customCheckBox.setBounds(400,290,20,20);
        customCheckBox.setOpaque(false);

        // Position the preview labels
        xImagePrev.setBounds(210,325,150,150);
        oImagePrev.setBounds(490,325,150,150);

        // Position the radio buttons and set X on
        xRadioButton.setBounds(125,390,20,20);
        xRadioButton.setOpaque(false);
        xRadioButton.setSelected(true);
        oRadioButton.setBounds(405,390,20,20);
        oRadioButton.setOpaque(false);

        // Allow user to upload an image when clicked
        uploadButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create a File Dialog box used to select an image to upload
                // You may only choose JPG or PNG files
                openFileDialog = new FileDialog(new JFrame(), "Choose an image", FileDialog.LOAD);
                openFileDialog.setLocationRelativeTo(null);
                openFileDialog.setFile("*.jpg;*.jpeg;*.png");

                // Open the File Dialog box
                openFileDialog.setVisible(true);

                try {
                    // Read in the selected image
                    BufferedImage image = ImageIO.read(new File(openFileDialog.getDirectory() + openFileDialog.getFile()));
                    BufferedImage smallBoardImage;
                    BufferedImage squareImage;

                    // If the image is not null
                    if (image != null) {
                        // Create 2 versions of the image for the scales
                        smallBoardImage = new BufferedImage(150,150,BufferedImage.TYPE_INT_ARGB);
                        squareImage = new BufferedImage(47,47,BufferedImage.TYPE_INT_ARGB);
                        Graphics2D sb_graphics = smallBoardImage.createGraphics();
                        Graphics2D sq_graphics = squareImage.createGraphics();

                        // Small board width and height factors
                        double sbWidthFactor = 150.0/image.getTileWidth();
                        double sbHeightFactor = 150.0/image.getTileHeight();

                        // Square width and height factors
                        double sqWidthFactor = 47.0/image.getTileWidth();
                        double sqHeightFactor = 47.0/image.getTileHeight();

                        // Scale the image with two transforms
                        AffineTransform sbTransform = AffineTransform.getScaleInstance(sbWidthFactor, sbHeightFactor);
                        AffineTransform sqTransform = AffineTransform.getScaleInstance(sqWidthFactor, sqHeightFactor);
                        sb_graphics.drawRenderedImage(image, sbTransform);
                        sq_graphics.drawRenderedImage(image, sqTransform);

                        // Make a folder to store custom images
                        boolean makeFolder = new File("Custom").mkdir();

                        // If X radio button is selected
                        if (xRadioButton.isSelected()) {
                            // Create two files for SmallBoard X and Square X images
                            File customFile1 = new File("Custom/Small Board X.png");
                            File customFile2 = new File("Custom/Square X.png");

                            if (!customFile1.exists())
                                customFile1.createNewFile();
                            ImageIO.write(smallBoardImage, "png", customFile1);

                            if (!customFile2.exists())
                                customFile2.createNewFile();
                            ImageIO.write(squareImage, "png", customFile2);

                            // If Custom Images is on change the images
                            if (customCheckBox.isSelected())
                                icon.setXImage();
                            xImagePrev.repaint();
                        }
                        // Else the O radio button is selected
                        else {
                            // Create two files for SmallBoard O and Square O images
                            File customFile1 = new File("Custom/Small Board O.png");
                            File customFile2 = new File("Custom/Square O.png");

                            if (!customFile1.exists())
                                customFile1.createNewFile();
                            ImageIO.write(smallBoardImage, "png", customFile1);

                            if (!customFile2.exists())
                                customFile2.createNewFile();
                            ImageIO.write(squareImage, "png", customFile2);

                            // If Custom Images is on change the images
                            if (customCheckBox.isSelected())
                                icon.setOImage();
                            oImagePrev.repaint();
                        }
                    }
                }
                catch(IOException ex) {
                    System.out.println("Error -- Custom images could not be saved.");
                }
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

        // Turn on custom images when clicked
        customCheckBox.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                icon.setXImage();
                icon.setOImage();
                xImagePrev.repaint();
                oImagePrev.repaint();
            }
        } );

        // Select the X button when clicked and un-select the O button
        xRadioButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                xRadioButton.setSelected(true);
                oRadioButton.setSelected(false);
            }
        } );

        // Select the O button when clicked and un-select the X button
        oRadioButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                oRadioButton.setSelected(true);
                xRadioButton.setSelected(false);
            }
        } );
    }

    /**
     * Gets if the Sound On/Off checkbox is selected.
     * @return Returns a Boolean for if the checkbox is selected.
     */
    public boolean isSoundOn() {
        return soundCheckBox.isSelected();
    }

    /**
     * Gets if the Custom Images On/Off checkbox is selected.
     * @return Returns a Boolean for if the checkbox is selected.
     */
    public boolean isUsingCustomImages() {
        return customCheckBox.isSelected();
    }

    /**
     * Gets the Difficulty selected in the combo box.
     * @return Returns the Difficulty selected.
     */
    public Difficulty getDifficulty() {
        String diff = aiDiffComboBox.getSelectedItem().toString();

        for (Difficulty n : Difficulty.values()){
            if (n.toString().equals(diff))
                return n;
        }

        return Difficulty.RANDOM;
    }

    /**
     * @see Menu
     */
    public void DisplayMenu() {
        GUI gui = GUI.getInstance();
        gui.frame.add(optionsPanel);
    }

    /**
     * @see Menu
     */
    public void ExitMenu() {
        GUI gui = GUI.getInstance();
        gui.frame.remove(optionsPanel);
        gui.frame.revalidate();
        gui.frame.repaint();
    }
}