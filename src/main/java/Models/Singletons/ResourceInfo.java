package Models.Singletons;

import java.util.HashMap;
import java.util.Map;

public class ResourceInfo {
    private ResourceInfo(){}

    private static Map<Integer, String> resources = new HashMap<>();

    public static void GenerateValues(){

    }

    public static String GetResourceName(int resourceID){
        return resources.get(resourceID);
    }
}
