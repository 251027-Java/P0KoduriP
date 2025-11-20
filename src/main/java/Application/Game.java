package Application;

import GamePackage.HomeScreen;
import Models.Singletons.*;
import Repository.PostgreDataRepo;
import Repository.PostgreReqRepo;
import Service.DataService;
import Service.RequirementService;
import Util.ConnectionUtil;
import Util.UserInput;

import java.sql.Connection;

public class Game {
    public final static int MaxDaysTracked = 14;
    public static int NumProfiles = -1;

    private final static Game game = new Game();
    private Game() {}
    public static Game getInstance() {return game;}

    private boolean gameStarted = false;

    private DataService dataServ;
    private RequirementService reqServ;

    public void Play(boolean resetReq, boolean resetData){
        if (gameStarted) return;
        gameStarted = true;
        IO.println("Game starting...");

        try {
            Connection conn = ConnectionUtil.GetConnection();

            //reqRepo instantiation needs to come before dataRepo instantiation since some data tables reference req tables
            PostgreReqRepo reqRepo = new PostgreReqRepo(conn, resetReq);
            PostgreDataRepo dataRepo = new PostgreDataRepo(conn, resetData);

            dataServ = new DataService(dataRepo, reqRepo);
            reqServ = new RequirementService(reqRepo);

            Setup();
            PlayGame();
        } finally {
            ConnectionUtil.CloseConnection();
            UserInput.CloseScanner();
        }

        IO.println("Game closing.");
    }
    private void Setup(){
        ResourceManager.GenerateValues();
        GemShop.GenerateValues();
        GemValues.GenerateValues();
        TroopFactoryHandler.GenerateFactories(); //ResourceManager values need to be generated first
        BuildingHandler.GenerateValues();
        NumProfiles = dataServ.GetNumberOfProfiles();
    }
    private void PlayGame(){
        HomeScreen.StartHomeScreen();
    }

    public RequirementService GetRequirementService(){
        return reqServ;
    }
    public DataService GetDataService(){
        return dataServ;
    }
}

