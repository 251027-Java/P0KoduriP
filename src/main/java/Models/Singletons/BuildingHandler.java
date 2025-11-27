package Models.Singletons;

import Application.Game;
import Models.Abstracts.Building;
import Models.Buildings.ArmyCamp;
import Models.Buildings.ResourceCollector;
import Models.Buildings.ResourceStorage;
import Models.Buildings.TownHall;
import Service.DataService;
import Service.RequirementService;

import java.util.*;

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
    public static void LoadBuildings(){
        handler.buildings = new HashMap<>();
        for (Building b : Game.getInstance().GetDataService().GetPlayerBuildings(Profile.GetID())){
            handler.buildings.put(b.GetBuildID(), b);

            switch (GetBuildingTypeName(b.GetBuildingTypeID())){
                case "camp":
                    Army.getInstance().AddArmyCamp((ArmyCamp) b);
                    break;
                case "collector":
                    ResourceManager.getInstance().AddCollector((ResourceCollector) b);
                    break;
                case "storage":
                    ResourceManager.getInstance().AddStorage((ResourceStorage) b);
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
    public Set<Building> GetBuildings(){
        return new HashSet<>(buildings.values());
    }

    public int GetHighestBarracksLevel(){
        int high = 0;
        for (Building b : buildings.values()){
            if (b.GetBuildingID() == 2) high = Math.max(high, b.GetLevel()); // Barracks Building ID = 2
        }
        return high;
    }

    public void UpdateUpgrades(){
        DataService dServ = Game.getInstance().GetDataService();
        RequirementService rServ = Game.getInstance().GetRequirementService();

        ResourceManager.getInstance().UpdateCollectorResourceAmounts();

        int profID = Profile.GetID();

        for (Map.Entry<Integer, Building> e : buildings.entrySet()){
            int buildID = e.getKey();
            Building b = e.getValue();

            long rem = dServ.GetBuildingUpgradeTimeRemainingSeconds(profID, buildID);
            if (rem <= 0 && b.GetUpgrading()){
                b.FinishUpgrade();
                b.SetUpgradingInfo(rServ.GetBuildingUpgradeInfo(b.GetBuildingID(), b.GetLevel() + 1));
                UpgradeBuilding(profID, buildID, b.GetBuildingTypeID(), b, Game.getInstance().GetRequirementService());
            }
        }
    }
    private void UpgradeBuilding(int profID, int buildID, int btid, Building b, RequirementService rServ){
        int level = b.GetLevel();
        int buildingID = b.GetBuildingID();

        switch (buildingTypeNames.get(btid)){
            case "TH":
                buildings.put(buildID, rServ.GetTownHall(buildID, level, buildingID, false));
                break;
            case "camp":
                buildings.put(buildID, rServ.GetArmyCamp(buildID, level, buildingID, false));
                break;
            case "rax":
                buildings.put(buildID, rServ.GetBarracks(buildID, level, buildingID, false));
                break;
            case "lab":
                buildings.put(buildID, rServ.GetLab(buildID, level, buildingID, false));
                break;
            case "collector":
                buildings.put(buildID, rServ.GetCollector(buildID, level, buildingID, false, 0));
                break;
            case "storage":
                buildings.put(buildID, rServ.GetStorage(buildID, level, buildingID, false));
                break;
            case "defense":
                buildings.put(buildID, rServ.GetDefense(buildID, level, buildingID, false));
                break;
            default:
                IO.println(String.format("ERROR: incorrect build type ID (%d) with profID=%d, buildID=%d", btid, profID, buildID));
        }
    }
}
