package Models.Singletons;

import Application.Game;
import GamePackage.BaseScreen;
import GamePackage.HomeGameScreen;
import Models.Buildings.ResourceCollector;
import Models.Buildings.ResourceStorage;
import Service.DataService;
import Service.RequirementService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourceManager {
    public final static int MaxCollectionHours = 12;

    private final static ResourceManager resourceManager = new ResourceManager();
    private ResourceManager() {}
    public static ResourceManager getInstance() {return resourceManager;}

    private static final Map<Integer, String> resourceNames = new HashMap<>(); //resourceID -> resource name
    private static Map<Integer, Integer> resourceHeld; //buildingID -> resource ID

    private final Map<Integer, Integer> resources = new HashMap<>(); //resourceID -> amount stored

    private final Map<Integer, Integer> maxResources = new HashMap<>(); //resourceID -> max amount stored
    private final Map<Integer, List<ResourceCollector>> collectors = new HashMap<>(); //resourceID -> collectors
    private final Map<Integer, List<ResourceStorage>> storages = new HashMap<>(); //resourceID -> storages
    private final Map<Integer, Integer> collectorsHP = new HashMap<>(); //resourceID -> total collector HP
    private final Map<Integer, Integer> storagesHP = new HashMap<>(); //resourceID -> total storage HP

    public static void GenerateValues(){
        RequirementService serv = Game.getInstance().GetRequirementService();

        for (Map.Entry<Integer, String> resources : serv.GetResourceInfo().entrySet()){
            resourceNames.put(resources.getKey(), resources.getValue());
            resourceManager.AddResource(resources.getKey());
        }

        resourceHeld = serv.GetBuildingResourcesHeld();
    }
    private void AddResource(int resourceID){
        resources.put(resourceID, 0);
        maxResources.put(resourceID, 0);
        collectors.put(resourceID, new ArrayList<>());
        storages.put(resourceID, new ArrayList<>());
        collectorsHP.put(resourceID, 0);
        storagesHP.put(resourceID, 0);
    }

    public void AddCollector(ResourceCollector rc){
        int id = rc.GetResourceGeneratedID();
        collectors.get(id).add(rc);
        UpdateCollector(id, rc.GetMaxHP());
    }
    public void AddStorage(ResourceStorage rs){
        int id = rs.GetResourceStoredID();
        storages.get(id).add(rs);
        UpdateStorage(id, rs.GetMaxCapacity(), rs.GetMaxHP());
        resources.compute(id, (k, v) -> v + Game.getInstance().GetDataService().GetResourceAmount(Profile.GetID(), rs.GetBuildID()));
    }
    public void UpdateCollector(int resourceID, int oldHP, int newHP){
        UpdateCollector(resourceID, newHP - oldHP);
    }
    public void UpdateCollector(int resourceID, int changeHP){
        collectorsHP.compute(resourceID, (k, hp) -> Math.max(0, hp + changeHP));
    }
    public void UpdateStorage(int resourceID, int oldCapacity, int newCapacity, int oldHP, int newHP){
        UpdateStorage(resourceID,newCapacity - oldCapacity, newHP - oldHP);
    }
    public void UpdateStorage(int resourceID, int changeCapacity, int changeHP){
        maxResources.compute(resourceID, (k, cap) -> Math.max(0, cap + changeCapacity));
        storagesHP.compute(resourceID, (k, hp) -> Math.max(0, hp + changeHP));
    }

    public int AddResources(int resourceID, int addedResources){
        DataService serv = Game.getInstance().GetDataService();

        int maxCapacity = maxResources.get(resourceID);
        int newAmount = resources.get(resourceID) + addedResources;

        int refund = 0;
        if (newAmount > maxCapacity) {
            refund = newAmount - maxCapacity;
            newAmount = maxCapacity;
        }

        resources.put(resourceID, newAmount);

        for (ResourceStorage rs : storages.get(resourceID)){
            int cap = rs.GetMaxCapacity();
            int amount = Math.min(cap, newAmount);
            newAmount -= amount;
            serv.SetResourceAmount(Profile.GetID(), rs.GetBuildID(), amount);
        }

        return refund;
    }
    public void CollectResources(int resourceID){
        DataService serv = Game.getInstance().GetDataService();

        for (ResourceCollector rc : collectors.get(resourceID)){
            rc.RestoreResources(AddResources(resourceID, rc.CollectResources(serv.GetHoursSinceLastCollectedResources(Profile.GetID()))));
        }

        serv.UpdateCollectedResourcesTime(Profile.GetID());
    }
    public boolean SpendResources(int resourceID, int spendAmount){ //returns true if successfully spent, false if not (no change)
        int currAmount = resources.get(resourceID);
        if (currAmount < spendAmount) return false;

        resources.put(resourceID, currAmount - spendAmount);
        return true;
    }

    public static String GetResourceName(int resourceID){
        return resourceNames.get(resourceID);
    }
    public static int GetBuildingResourceHeld(int resourceID){
        return resourceHeld.get(resourceID);
    }

    public int GetResources(int resourceID){
        return resources.get(resourceID);
    }
    public void Reset(){
        resources.replaceAll((k, v) -> 0);
        maxResources.replaceAll((k, v) -> 0);
        collectors.replaceAll((k, v) -> new ArrayList<>());
        storages.replaceAll((k, v) -> new ArrayList<>());
        collectorsHP.replaceAll((k, v) -> 0);
        storagesHP.replaceAll((k, v) -> 0);
    }
}
