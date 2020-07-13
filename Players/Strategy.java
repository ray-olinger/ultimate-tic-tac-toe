package Players;

/**
 * An interface class used by all of the AI strategies.
 */
interface Strategy {

    /**
     * Calculates the ID of the Square selected by the AI.
     * @return Returns the ID of the Square selected by the AI.
     */
    int execute();
}
