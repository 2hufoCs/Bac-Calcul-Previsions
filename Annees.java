public enum Annees {
    PREMIERE(0),
    TERMINALE(1),
    PREMIERE_ET_TERMINALE(2);

    private int value;

    private Annees(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}