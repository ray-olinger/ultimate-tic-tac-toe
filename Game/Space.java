package Game;

/**
 * An interface class used by SmallBoard and Square.
 */
interface Space {

    /**
     * Sets the ID of the Space.
     * @param ID The ID.
     */
    void setID(int ID);

    /**
     * Gets the ID of the Space.
     * @return Returns the ID of the Space.
     */
    int getID();

    /**
     * Gets if the Space is valid.
     * @return Returns a Boolean for if the Space is valid.
     */
    boolean isValid();

    /**
     * Gets the Mark of the Space.
     * @return Returns the Mark of the Space.
     */
    Mark getMark();

    /**
     * Sets the Mark of the Space.
     * @param m A Mark.
     */
    void setMark(Mark m);

    /**
     * Sets the Space as valid.
     * Defaulted for Squares.
     * @see SmallBoard
     */
    default void setAsValid() {
        throw new IllegalStateException("Illegal Game State: Square setAsValid");
    }

    /**
     * Sets the Space as invalid.
     * Defaulted for Squares.
     * @see SmallBoard
     */
    default void setAsInValid() {
        throw new IllegalStateException("Illegal Game State: Square setAsInValid");
    }

    /**
     * Sends user move down to the Space.
     * @see SmallBoard
     */
    Event SendUserMove(Mark playerMark, int SP);
}
