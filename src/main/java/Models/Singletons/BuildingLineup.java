package Models.Singletons;

import Application.Game;
import Models.Abstracts.Building;
import Service.DataService;
import Service.RequirementService;

import java.util.Map;

public class BuildingLineup {
    private final static BuildingLineup buildingLineup = new BuildingLineup();
    private BuildingLineup() {}
    public static BuildingLineup getInstance() {return buildingLineup;}

    private static final int maxBuildings = 12;
    private Building[] lineup = new Building[maxBuildings];

    public static void LoadBuildingLineup(int profID){
        buildingLineup.lineup = new Building[maxBuildings];

        DataService dServ = Game.getInstance().GetDataService();
        RequirementService rServ = Game.getInstance().GetRequirementService();

        for (Map.Entry<Integer, Integer> e : dServ.GetPlayerBuildingLineup(profID).entrySet()){
            buildingLineup.lineup[e.getValue()] = BuildingHandler.getInstance().GetBuilding(e.getKey());
        }
    }
    public void AddBuilding(Building building, int pos){
        lineup[pos] = building;
    }
    public Building GetBuilding(int pos){
        return lineup[pos];
    }
    public Building[] GetLineup(){
        return lineup.clone();
    }
}
