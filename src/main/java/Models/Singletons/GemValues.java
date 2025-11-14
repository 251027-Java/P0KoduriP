package Models.Singletons;

public class GemValues {
    private GemValues(){}

    private static int time; //minutes per gem
    private static int resource;

    public static void GenerateValues(){

    }

    public static int GetTime(){
        return time;
    }
    public static int GetResource(){
        return resource;
    }
}
