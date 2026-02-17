
public enum Checker {
    RED("RED"),
    BLACK("BLACK");

    private final String value;

    Checker(String value){
        this.value = value;
    }

    @Override
    public String toString(){
        return value;
    }

    public Checker opponent(){
        // Checker knows how to return the opposite checker
        return (this == Checker.RED) ? Checker.BLACK : Checker.RED;
    }
}
