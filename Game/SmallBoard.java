package Game;

import Implementation.ImageIconCreator;
import Menus.Options;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

/**
 * Custom class of MainBoard that serves as the smaller boards within the main board.
 */
public class SmallBoard extends MainBoard implements Space {

    // The instance of the ImageIconCreator object
    private ImageIconCreator icon = ImageIconCreator.getInstance();

    // The Mark of the SmallBoard
    private Mark m_mMark;
    // Boolean for the validity of SmallBoard
    private boolean m_bValid;
    // ID of the SmallBoard
    private int m_iID;

    // Label to overlay the strike through the win
    private JLabel winStrikeLabel = new JLabel(icon.SMALL_BOARD_BLANK);
    // Label to overlay the Mark of the win
    private JLabel boardWinLabel = new JLabel(icon.SMALL_BOARD_BLANK);

    /**
     * Constructs a SmallBoard object.
     */
    public SmallBoard() {
        // Creates an array of 9 Square objects
        Square[] squares = new Square[9];

        // For each Square, add to the SmallBoard and set the position
        for (int i = 0; i < 9; i++) {
            squares[i] = new Square();
            this.add(squares[i]);
            squares[i].setBounds(((i%3*52) - (i%3*52)/100), (52*(i/3)) - (52*(i/3))/100, 47, 47);
        }

        // Set the array of Spaces to the array of Squares
        this.m_sSpaces = squares;
        // Set valid to true since first move is a free move
        m_bValid = true;
        // Set Mark to unmarked
        m_mMark = Mark.UNMARKED;

        // Sets the background image to a normal SmallBoard
        this.setIcon(icon.SMALL_BOARD);
        this.setOpaque(false);
        this.setLayout(null);
    }

    /**
     * Assigns IDs to the Squares in SmallBoard.
     * @param ID The ID of the SmallBoard.
     */
     public void setID(int ID) {
         m_iID = ID;
         for (int i = 0; i < 9; i++)
             m_sSpaces[i].setID(i + ID*9);
    }

    /**
     * @see Space
     * @return Returns the ID of the SmallBoard.
     */
    public int getID() {
        return m_iID;
    }

    /**
     * @see Space
     * @return Returns the Mark of the SmallBoard.
     */
    public Mark getMark() {
      return this.m_mMark;
    }

    /**
     * @see Space
     * @param m A Mark.
     */
    public void setMark(Mark m) {
        m_mMark = m;
    }

    /**
     * @see Space
     * @return Returns if the SmallBoard is valid.
     */
    public boolean isValid() {
        return m_bValid;
    }

    /**
     * Sets the SmallBoard to valid.
     */
    public void setAsValid() {
        m_bValid = true;
        this.setIcon(icon.SMALL_BOARD_VALID);
    }

    /**
     * Sets the SmallBoard to invalid.
     */
    public void setAsInValid() {
        m_bValid = false;
        this.setIcon(icon.SMALL_BOARD);
    }

    /**
     * A drawing function that draws a mark on the Square.
     * @param SP The ID of the Square.
     * @param playerMark The Mark of the current Player.
     */
    @Override
    protected void DrawOnSpace(int SP, Mark playerMark) {
        // Draws the mark
        if (playerMark == Mark.X)
            ((Square)m_sSpaces[SP]).setIcon(icon.SQUARE_X);
        else
            ((Square)m_sSpaces[SP]).setIcon(icon.SQUARE_O);

        // Plays a placement sound
        try {
            Clip place = AudioSystem.getClip();
            AudioInputStream ais = AudioSystem.getAudioInputStream(getClass().getResource("/Resources/Sounds/Place Sound.wav"));
            place.open(ais);
            // If sound option is turned on, play the sound
            if (Options.getInstance().isSoundOn())
                place.start();
        }
        catch(LineUnavailableException | IOException | UnsupportedAudioFileException ex) {
            System.out.println("Error -- Audio clip for marker placement could not be found.");
        }
    }

    /**
     * Draws a strike through the 3 adjacent Square objects.
     * @param Strike The certain strike that needs to be drawn.
     */
    @Override
    protected void DrawStrike(WinStrike Strike) {
        switch (Strike) {
            case TOPRIGHTDIAGONAL:
                winStrikeLabel.setIcon(icon.WINSTRIKE_TRD);
                break;
            case TOPLEFTDIAGONAL:
                winStrikeLabel.setIcon(icon.WINSTRIKE_TLD);
                break;
            case TOPHORIZONTAL:
                winStrikeLabel.setIcon(icon.WINSTRIKE_TH);
                break;
            case MIDDLEHORIZONTAL:
                winStrikeLabel.setIcon(icon.WINSTRIKE_MH);
                break;
            case BOTTOMHORIZONTAL:
                winStrikeLabel.setIcon(icon.WINSTRIKE_BH);
                break;
            case LEFTVERTICAL:
                winStrikeLabel.setIcon(icon.WINSTRIKE_LV);
                break;
            case MIDDLEVERTICAL:
                winStrikeLabel.setIcon(icon.WINSTRIKE_MV);
                break;
            case RIGHTVERTICAL:
                winStrikeLabel.setIcon(icon.WINSTRIKE_RV);
                break;
        }
        reDraw();
    }

    /**
     * Does not serve a purpose in SmallBoard.
     */
    @Override
    protected void DetermineNextMove(int SP) {
    }

    /**
     * Sends the move down to the space of the current board.
     * @param playerMark The Mark of the current Player.
     * @param SP The ID of the Square.
     * @return Returns the Event that occurred.
     */
     public Event SendUserMove(Mark playerMark, int SP) {
         // If the SmallBoard is not valid
        if(!m_bValid)
            return Event.INVALIDSECTOR;

        Event returnEvent;
        returnEvent = SendUserInput(SP, -1, playerMark);

         // Sets the SmallBoard as invalid on the way out of a valid move
        if (returnEvent == Event.VALIDMOVE || returnEvent == Event.WINNINGMOVE || returnEvent == Event.CATS)
            setAsInValid();

        return returnEvent;
    }

    /**
     * Sets the label with the correct strike.
     * @param l The label with a strike image.
     */
    public void setWinLabel(JLabel l) {
        boardWinLabel = l;
    }

    /**
     * Redraws the SmallBoard to properly display all graphic objects.
     */
    public void reDraw() {
        this.removeAll();
        this.add(boardWinLabel);
        this.add(winStrikeLabel);
        boardWinLabel.setBounds(0, 0, 150, 150);
        winStrikeLabel.setBounds(0, 0, 150, 150);

        for (int i = 0; i < 9; i++) {
            this.add((Square)m_sSpaces[i]);
        }
    }
}
