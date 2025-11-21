package Models.Singletons;

import Application.Game;

import java.util.HashMap;
import java.util.Map;

public class GemShop {
    private GemShop(){}

    private static Map<Integer, Integer> shop; //USD -> gems

    public static void GenerateValues(){
        shop = Game.getInstance().GetRequirementService().GetGemShopOptions();
    }

    public static Map<Integer, Integer> GetAvailableOptions(int usd){
        Map<Integer, Integer> options = new HashMap<>();
        for (Map.Entry<Integer, Integer> e : shop.entrySet()){
            if (e.getValue() <= usd) options.put(e.getKey(), e.getValue());
        }
        return options;
    }
    public static Map<Integer, Integer> GetAllOptions(){
        return GetAvailableOptions(Integer.MAX_VALUE);
    }

    public static int GetGems(int usd){
        return shop.getOrDefault(usd, 0);
    }
}
