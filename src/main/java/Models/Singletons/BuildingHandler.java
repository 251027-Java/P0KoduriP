package Models.Singletons;

import Application.Game;
import Service.RequirementService;

import java.util.Map;

public class BuildingHandler {
    private final static BuildingHandler handler = new BuildingHandler();
    private BuildingHandler() {}
    public static BuildingHandler getInstance() {return handler;}

    private static Map<Integer, Integer> buildingTypes; // building ID -> building type ID
    private static Map<Integer, String> buildingTypeNames; // building type ID -> building type name
    private static Map<Integer, Integer> buildingPurchaseResources; // building ID -> resource to purchase building
    private static Map<Integer, String> buildingNames; // building ID -> building name

    private int numBuildings = 0;

    public static void GenerateValues(){
        RequirementService serv = Game.getInstance().GetRequirementService();
        buildingTypes = serv.GetBuildingTypes();
        buildingTypeNames = serv.GetBuildingTypeNames();
        buildingPurchaseResources = serv.GetBuildingPurchaseResources();
        buildingNames = serv.GetBuildingNames();
    }
    public static void LoadBuildings(int profID){

    }

    public static int GetBuildingType(int buildingID){
        return buildingTypes.get(buildingID);
    }
    public static String GetBuildingTypeName(int buildingTypeID){
        return buildingTypeNames.get(buildingTypeID);
    }
    public static int GetBuildingPurchaseResource(int buildingID){
        return buildingPurchaseResources.get(buildingID);
    }
    public static String GetBuildingName(int buildingID){
        return buildingNames.get(buildingID);
    }
}
