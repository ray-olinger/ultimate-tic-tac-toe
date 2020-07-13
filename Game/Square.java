package Game;

import Implementation.ImageIconCreator;
import Implementation.Listener;

import javax.swing.*;

/**
 * A custom class of JButton that serves as a Square on a SmallBoard.
 */
public class Square extends JButton implements Space {

    // The Mark of the Square
    private Mark m_mMark;
    // The ID of the Square
    private int m_iID;

    /**
     * Constructs a Square object.
     */
    public Square() {
        // Set Mark to unmarked
        m_mMark = Mark.UNMARKED;

        // Sets the background image to a blank square
        this.setIcon(ImageIconCreator.getInstance().SQUARE_BLANK);
        this.setOpaque(false);
        this.setContentAreaFilled(false);
        this.setBorderPainted(false);
        // Adds the Listener object as the custom ActionListener for when the Square is clicked
        this.addActionListener(Listener.getInstance());
    }

    /**
     * @see Space
     * @param ID The ID of the Square.
     */
    public void setID(int ID) {
        m_iID = ID;
    }

    /**
     * @see Space
     * @return Returns the ID of the Square.
     */
    public int getID() {
        return m_iID;
    }

    /**
     * @see Space
     * @return Returns the Mark of the Square.
     */
    public Mark getMark() {
        return m_mMark;
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
     * @return Returns if the Square is valid.
     */
    public boolean isValid() {
        return m_mMark == Mark.UNMARKED;
    }

    /**
     * Determines the event based on if the Square is marked.
     * @param SQ The ID of the Square (not used).
     * @param playerMark The Mark of the current Player (not used).
     * @return Returns a winning move event if the Square is unmarked.
     */
    public Event SendUserMove(Mark playerMark, int SQ) {
        if(m_mMark == Mark.UNMARKED)
            return Event.WINNINGMOVE;
        return Event.INVALIDSPACE;
    }
}
