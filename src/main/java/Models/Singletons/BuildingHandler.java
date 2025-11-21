package Models.Singletons;

import Application.Game;
import Models.Abstracts.Building;
import Models.Buildings.ArmyCamp;
import Service.RequirementService;

import java.util.HashMap;
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

    private Map<Integer, Building> buildings; // build ID -> building

    public static void GenerateValues(){
        RequirementService serv = Game.getInstance().GetRequirementService();
        buildingTypes = serv.GetBuildingTypes();
        buildingTypeNames = serv.GetBuildingTypeNames();
        buildingPurchaseResources = serv.GetBuildingPurchaseResources();
        buildingNames = serv.GetBuildingNames();
    }
    public static void LoadBuildings(int profID){
        handler.buildings = new HashMap<>();
        for (Building b : Game.getInstance().GetDataService().GetPlayerBuildings(profID)){
            handler.buildings.put(b.GetBuildID(), b);

            switch (GetBuildingName(b.GetBuildingID())){
                case "Army Camp":
                    Army.getInstance().AddArmyCamp((ArmyCamp) b);
                    break;
            }
        }
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

    public Building GetBuilding(int buildID){
        return buildings.get(buildID);
    }
}
