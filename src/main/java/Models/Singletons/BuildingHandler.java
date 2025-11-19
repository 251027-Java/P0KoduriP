package Models.Singletons;

import Application.Game;

import java.util.Map;

public class BuildingHandler {
    private final static BuildingHandler handler = new BuildingHandler();
    private BuildingHandler() {}
    public static BuildingHandler getInstance() {return handler;}

    private Map<Integer, String> buildingTypes; // building type ID -> building type name
    private int numBuildings = 0;

    public static void GenerateValues(){
        handler.buildingTypes = Game.getInstance().GetRequirementService().GetBuildingTypes();
    }
    public static void LoadBuildings(int profID){

    }

    public String GetBuildingTypeName(int buildingTypeID){
        return buildingTypes.get(buildingTypeID);
    }
}
