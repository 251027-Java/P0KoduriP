package Application;

public class Main {
    public static void main(String[] args){
        IO.println("Application starting...");

        boolean resetRequirements = false;

        boolean resetData = true;
        boolean confirmResetData = true;

        Game.getInstance().Play(resetRequirements, resetData && confirmResetData);

        IO.println("Application closing.");
    }
}