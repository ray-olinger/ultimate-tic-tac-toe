package Game;

/**
 * Enumerated type of all possible strikes on a board.
 */
public enum WinStrike {
    NONE,
    TOPRIGHTDIAGONAL, TOPLEFTDIAGONAL,
    TOPHORIZONTAL, MIDDLEHORIZONTAL, BOTTOMHORIZONTAL,
    LEFTVERTICAL, MIDDLEVERTICAL, RIGHTVERTICAL
}
