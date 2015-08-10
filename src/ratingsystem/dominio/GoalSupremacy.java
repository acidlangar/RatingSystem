package ratingsystem.dominio;

public class GoalSupremacy {
    
    private int numHomeWins;
    
    private int numDraws;
    
    private int numAwayWins;
    
    public void setNumHomeWins(int numHomeWins) {
        this.numHomeWins = numHomeWins;
    }

    public int getNumHomeWins() {
        return numHomeWins;
    }

    public void setNumDraws(int numDraws) {
        this.numDraws = numDraws;
    }

    public int getNumDraws() {
        return numDraws;
    }

    public void setNumAwayWins(int numAwayWins) {
        this.numAwayWins = numAwayWins;
    }

    public int getNumAwayWins() {
        return numAwayWins;
    }


    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
            return "" + numHomeWins + "\t" + numDraws + "\t" + numAwayWins;
    }
}
