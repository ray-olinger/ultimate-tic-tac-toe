package Implementation;

import Menus.Options;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * A class that creates all of the necessary ImageIcons for the application.
 */
public class ImageIconCreator {

    // Singleton instance of the ImageIconCreator object
    private static ImageIconCreator instance = null;

    /**
     * The Singleton method to retrieve the instance of ImageIconCreator.
     * @return Returns the instance of ImageIconCreator.
     */
    public static ImageIconCreator getInstance() {
        if (instance == null)
            instance = new ImageIconCreator();
        return instance;
    }

    // ImageIcons for the button images
    public ImageIcon BACK_BUTTON = new ImageIcon(getClass().getResource("/Resources/Images/Buttons/Button Back.png"));
    public ImageIcon START_BUTTON = new ImageIcon(getClass().getResource("/Resources/Images/Buttons/Button Start.png"));
    public ImageIcon OPTIONS_BUTTON = new ImageIcon(getClass().getResource("/Resources/Images/Buttons/Button Options.png"));
    public ImageIcon RULES_BUTTON = new ImageIcon(getClass().getResource("/Resources/Images/Buttons/Button Rules.png"));
    public ImageIcon QUIT_BUTTON = new ImageIcon(getClass().getResource("/Resources/Images/Buttons/Button Quit.png"));
    public ImageIcon PVP_BUTTON = new ImageIcon(getClass().getResource("/Resources/Images/Buttons/Button PvP.png"));
    public ImageIcon PVAI_BUTTON = new ImageIcon(getClass().getResource("/Resources/Images/Buttons/Button PvAI.png"));
    public ImageIcon UPLOAD_BUTTON = new ImageIcon(getClass().getResource("/Resources/Images/Buttons/Button Upload.png"));

    // ImageIcons for the menu images
    public ImageIcon BACKGROUND = new ImageIcon(getClass().getResource("/Resources/Images/Main/Background.png"));
    public ImageIcon LOGO = new ImageIcon(getClass().getResource("/Resources/Images/Main/Logo.png"));
    public ImageIcon GAMETYPE = new ImageIcon(getClass().getResource("/Resources/Images/Main/Game Type.png"));
    public ImageIcon OPTIONS = new ImageIcon(getClass().getResource("/Resources/Images/Main/Options.png"));
    public ImageIcon RULES = new ImageIcon(getClass().getResource("/Resources/Images/Main/Rules.png"));

    // ImageIcons for the win message images
    public ImageIcon P1_WIN = new ImageIcon(getClass().getResource("/Resources/Images/Main/P1 Win Screen.png"));
    public ImageIcon P2_WIN = new ImageIcon(getClass().getResource("/Resources/Images/Main/P2 Win Screen.png"));
    public ImageIcon AI_WIN = new ImageIcon(getClass().getResource("/Resources/Images/Main/AI Win Screen.png"));
    public ImageIcon DRAW = new ImageIcon(getClass().getResource("/Resources/Images/Main/Draw Screen.png"));

    // ImageIcons for the main board images
    public ImageIcon MAIN_BOARD_P1 = new ImageIcon(getClass().getResource("/Resources/Images/Main/Main Board P1.png"));
    public ImageIcon MAIN_BOARD_P2 = new ImageIcon(getClass().getResource("/Resources/Images/Main/Main Board P2.png"));

    // ImageIcons for the small board images
    public ImageIcon SMALL_BOARD_BLANK = new ImageIcon(getClass().getResource("/Resources/Images/Small_Board/Small Board Blank.png"));
    public ImageIcon SMALL_BOARD = new ImageIcon(getClass().getResource("/Resources/Images/Small_Board/Small Board.png"));
    public ImageIcon SMALL_BOARD_VALID = new ImageIcon(getClass().getResource("/Resources/Images/Small_Board/Small Board Valid.png"));
    public ImageIcon SMALL_BOARD_O = new ImageIcon(getClass().getResource("/Resources/Images/Small_Board/Small Board O.png"));
    private ImageIcon DEFAULT_SB_O = new ImageIcon(getClass().getResource("/Resources/Images/Small_Board/Small Board O.png"));
    public ImageIcon SMALL_BOARD_X = new ImageIcon(getClass().getResource("/Resources/Images/Small_Board/Small Board X.png"));
    private ImageIcon DEFAULT_SB_X = new ImageIcon(getClass().getResource("/Resources/Images/Small_Board/Small Board X.png"));
    public ImageIcon SMALL_BOARD_C = new ImageIcon(getClass().getResource("/Resources/Images/Small_Board/Small Board Draw.png"));

    // ImageIcons for winstrike images
    public ImageIcon WINSTRIKE_TRD = new ImageIcon(getClass().getResource("/Resources/Images/Small_Board/Winstrike TRD.png"));
    public ImageIcon WINSTRIKE_TLD = new ImageIcon(getClass().getResource("/Resources/Images/Small_Board/Winstrike TLD.png"));
    public ImageIcon WINSTRIKE_TH = new ImageIcon(getClass().getResource("/Resources/Images/Small_Board/Winstrike TH.png"));
    public ImageIcon WINSTRIKE_MH = new ImageIcon(getClass().getResource("/Resources/Images/Small_Board/Winstrike MH.png"));
    public ImageIcon WINSTRIKE_BH = new ImageIcon(getClass().getResource("/Resources/Images/Small_Board/Winstrike BH.png"));
    public ImageIcon WINSTRIKE_LV = new ImageIcon(getClass().getResource("/Resources/Images/Small_Board/Winstrike LV.png"));
    public ImageIcon WINSTRIKE_MV = new ImageIcon(getClass().getResource("/Resources/Images/Small_Board/Winstrike MV.png"));
    public ImageIcon WINSTRIKE_RV = new ImageIcon(getClass().getResource("/Resources/Images/Small_Board/Winstrike RV.png"));

    // ImageIcons for the square images
    public ImageIcon SQUARE_BLANK = new ImageIcon(getClass().getResource("/Resources/Images/Square/Square Blank.png"));
    public ImageIcon SQUARE_O = new ImageIcon(getClass().getResource("/Resources/Images/Square/Square O.png"));
    private ImageIcon DEFAULT_SQ_O = new ImageIcon(getClass().getResource("/Resources/Images/Square/Square O.png"));
    public ImageIcon SQUARE_X = new ImageIcon(getClass().getResource("/Resources/Images/Square/Square X.png"));
    private ImageIcon DEFAULT_SQ_X = new ImageIcon(getClass().getResource("/Resources/Images/Square/Square X.png"));



    /**
     * Constructs an ImageIconCreator object.
     */
    private ImageIconCreator() {

    }

    /**
     * Sets the custom images for the X marker if custom images are turned on.
     */
    public void setXImage() {
        try {
            // If custom images are turned on
            if (Options.getInstance().isUsingCustomImages()) {
                SMALL_BOARD_X.setImage(ImageIO.read(new File("Custom/Small Board X.png")));
                SQUARE_X.setImage(ImageIO.read(new File("Custom/Square X.png")));
            }
            else {
                SMALL_BOARD_X.setImage(DEFAULT_SB_X.getImage());
                SQUARE_X.setImage(DEFAULT_SQ_X.getImage());
            }
        }
        catch (IOException ex) {
            System.out.println("Error -- Custom X images could not be opened.");
        }
    }

    /**
     * Sets the custom images for the O marker if custom images are turned on.
     */
    public void setOImage() {
        try {
            // If custom images are turned on
            if (Options.getInstance().isUsingCustomImages()) {
                SMALL_BOARD_O.setImage(ImageIO.read(new File("Custom/Small Board O.png")));
                SQUARE_O.setImage(ImageIO.read(new File("Custom/Square O.png")));
            }
            else {
                SMALL_BOARD_O.setImage(DEFAULT_SB_O.getImage());
                SQUARE_O.setImage(DEFAULT_SQ_O.getImage());
            }
        }
        catch (IOException ex) {
            System.out.println("Error -- Custom O images could not be opened.");
        }
    }
}
