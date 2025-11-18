package Application;

public class Main {
    public static void main(String[] args){
        IO.println("Application starting...");

        boolean resetRequirements = true;
        boolean resetData = true;
        Game.getInstance().Play(resetRequirements, resetData);

        IO.println("Application closing.");
    }
}