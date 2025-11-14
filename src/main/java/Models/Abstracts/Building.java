package Models.Abstracts;

public abstract class Building {
    protected int buildingid;
    protected int btid;
    protected String name;

    protected int rid;
    protected String resname;

    protected int hp;
    protected int maxHP;

    public Building(int buildingID, int buildingTypeID, String buildingName, int resourceID, String resourceName, int maxHealth){
        buildingid = buildingID;
        btid = buildingTypeID;
        name = buildingName;
        rid = resourceID;
        resname = resourceName;
        hp = maxHealth;
        maxHP = maxHealth;
    }

    public int hp(){
        return hp;
    }
    public int maxHP(){
        return maxHP;
    }

    // returns true if still not broken after damage taken, false if destroyed
    public boolean takeDamage(int dmg){
        hp -= dmg;
        if (hp < 0) hp = 0;
        return hp <= 0;
    }

    public void rebuild(){
        hp = maxHP;
    }
}
