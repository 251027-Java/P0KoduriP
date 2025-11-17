package Models.Singletons;

import Models.Buildings.ArmyCamp;
import Models.Troop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Army {
    private final static Army army = new Army();
    private Army() {}
    public static Army getInstance() {return army;}

    private List<ArmyCamp> camps = new ArrayList<>();
    private int maxSpace = 0;
    private int currSpace = 0;

    private Map<String, Integer> troopCounts = new HashMap<>();
    private Map<String, List<Troop>> troops = new HashMap<>();

    public void AddArmyCamp(ArmyCamp camp){
        camps.add(camp);
        UpdateSpace(camp.GetSpace());
    }
    public void UpdateSpace(int oldSpace, int newSpace){
        UpdateSpace(newSpace - oldSpace);
    }
    public void UpdateSpace(int changeSpace){
        maxSpace += changeSpace;
        if (maxSpace < 0) maxSpace = 0;
    }

    public int GetSpace(){
        return maxSpace;
    }

    public void Reset(){
        camps = new ArrayList<>();
        maxSpace = 0;
        currSpace = 0;

        troopCounts.replaceAll((k, v) -> 0);
        troops.replaceAll((k, v) -> new ArrayList<>());
    }
}
