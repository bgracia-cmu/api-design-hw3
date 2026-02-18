
/**
 * Enum representing the two types of checkers in the game: RED and BLACK.
 * Each checker has a string value for easy representation and a method to get the opponent checker.
 */
public enum Checker {
    /**
     * RED checker, represented by the string "RED".
     */
    RED("RED"),

    /**
     * BLACK checker, represented by the string "BLACK".
     */
    BLACK("BLACK");

    private final String value;

    Checker(String value){
        this.value = value;
    }

    /**
     * Returns the string representation of the checker.
     */
    @Override
    public String toString(){
        return value;
    }

    /**
     * Returns the opponent checker.
     *
     * @return the opposite checker
     */
    public Checker opponent(){
        // Checker knows how to return the opposite checker
        return (this == Checker.RED) ? Checker.BLACK : Checker.RED;
    }
}
