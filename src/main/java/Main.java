import java.sql.*;

public class Main {
    public static Game game;
    public static void main(String[] args){
        IO.println("Application starting...");

        game = new Game();
        game.Play();

        IO.println("Application closing.");
    }
}