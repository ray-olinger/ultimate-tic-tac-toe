package Players;

/**
 * Enumerated type of the AI difficulties.
 */
public enum Difficulty {
    RANDOM("RANDOM") , REACTIVE_I("EASY"), REACTIVE_II ("MEDIUM"), PROACTIVE ("HARD");

    private final String text;

    /**
     * Adds the String associated with the Difficulty.
     * @param text The String.
     */
    Difficulty(final String text) {
        this.text = text;
    }

    /**
     * Converts the Difficulty to the String
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return text;
    }
}