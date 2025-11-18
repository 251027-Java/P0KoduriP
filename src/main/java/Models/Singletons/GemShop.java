package Models.Singletons;

import java.util.HashMap;
import java.util.Map;

public class GemShop {
    private GemShop(){}

    private static Map<Integer, Integer> shop = new HashMap<>(); //USD -> gems

    public static void GenerateValues(){

    }

    public void AddOption(int usd, int gems){
        shop.put(usd, gems);
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
        if (!shop.containsKey(usd)) return 0;
        return shop.get(usd);
    }
}
