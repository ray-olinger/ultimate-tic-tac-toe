package Implementation;

import javax.swing.*;
import java.awt.*;

/**
 * Custom class of JPanel that includes a background image.
 */
public class JBackgroundPanel extends JPanel {

    // Stores the background image
    private Image m_Img;

    /**
     * Constructs an empty JBackgroundPanel object.
     */
    public JBackgroundPanel() {

    }

    /**
     * Constructs a JBackgroundPanel object with a background image.
     * @param p ImageIcon to be used as the background image.
     */
    public JBackgroundPanel(ImageIcon p) {
        // Converts the ImageIcon to an Image
        m_Img = p.getImage();
    }

    /**
     * Sets the background image of the JBackgroundPanel object.
     * @param p ImageIcon to be used as the background image.
     */
    public void setIcon(ImageIcon p) {
        // Converts the ImageIcon to an Image
        m_Img = p.getImage();
        // Updates the JBackgroundPanel to display the new image.
        this.repaint();
    }

    /**
     * Paints an image onto the background of the JBackgroundPanel object.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(m_Img, 0, 0, getWidth(), getHeight(), this);
    }
}