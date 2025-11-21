package Application;

import Util.Screen;

public class Main {
    public static void main(String[] args){
        Screen.Clear();

        IO.println("Application starting...");

        boolean resetRequirements = false;

        boolean resetData = false;
        boolean confirmResetData = false;

        if (args.length >= 1){
            switch (args[0]){
                case "req":
                    resetRequirements = true;
                    break;
                case "data":
                    resetData = true;
                    confirmResetData = true;
                    break;
            }
        }
        if (args.length >= 2){
            switch (args[1]){
                case "req":
                    resetRequirements = true;
                    break;
                case "data":
                    resetData = true;
                    confirmResetData = true;
                    break;
            }
        }

        Game.getInstance().Play(resetRequirements, resetData && confirmResetData);

        IO.println("Application closing.");
    }
}