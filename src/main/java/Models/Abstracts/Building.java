package Models.Abstracts;

public abstract class Building {
    protected int buildingid;
    protected int btid;
    protected String name;

    protected int rid;
    protected String resname;

    public Building(int buildingID, int buildingTypeID, String buildingName, int resourceID, String resourceName){
        buildingid = buildingID;
        btid = buildingTypeID;
        name = buildingName;
        rid = resourceID;
        resname = resourceName;
    }
}
