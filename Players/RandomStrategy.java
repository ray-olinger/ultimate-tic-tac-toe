package Players;

import Game.MainBoard;

import java.util.concurrent.ThreadLocalRandom;//for random int

public class RandomStrategy extends AI implements Strategy {

    public RandomStrategy(Difficulty d, MainBoard mb, MainBoard[] SB) {
        this.m_iValidSpaces = new int [82];
        this.m_mbBoard = mb;
        this.m_sbSmallboards = SB;
    }

    public int execute() {
        final int min = 0;
        fillValidArrays();
        return m_iValidSpaces[ThreadLocalRandom.current().nextInt(min, m_iNumValidSP)];
    }
}
