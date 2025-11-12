import Repository.PostgreDataRepo;
import Repository.PostgreReqRepo;
import Service.DataService;
import Service.RequirementService;
import Util.ConnectionUtil;
import Util.UserInput;

import java.sql.Connection;

public class Game {
    private static boolean gameStarted = false;

    private DataService dataServ;
    private RequirementService reqServ;

    public void Play(boolean resetSchema){
        if (gameStarted) return;
        gameStarted = true;
        IO.println("Game starting...");

        try {
            Connection conn = ConnectionUtil.GetConnection();

            dataServ = new DataService(new PostgreDataRepo(conn));
            reqServ = new RequirementService(new PostgreReqRepo(conn, resetSchema));

            PlayGame();
        } finally {
            ConnectionUtil.CloseConnection();
            UserInput.CloseScanner();
        }

        IO.println("Game closing.");
    }
    private void PlayGame(){

    }
}
