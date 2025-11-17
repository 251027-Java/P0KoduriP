package Models.Singletons;

import Models.Abstracts.Building;

public class BuildingLineup {
    private final static BuildingLineup buildingLineup = new BuildingLineup();
    private BuildingLineup() {}
    public static BuildingLineup getInstance() {return buildingLineup;}

    private static final int maxBuildings = 12;
    private Building[] lineup = new Building[maxBuildings];

    public void Reset(){
        lineup = new Building[maxBuildings];
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
