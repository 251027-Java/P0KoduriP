package Models;

public class Troop {
    private int troopid;
    private String name;
    private int cost;
    private int space;

    private int speed;
    private int range;
    private int hitspeed;
    private boolean ground;
    private boolean atkair;

    private int dmg;
    private int hp;

    public Troop(String tName, int tID, int tCost, int tSpace, int tSpeed, int tRange, int tHitSpeed, boolean groundTroop,
                 boolean canAttackAir, int damage, int health){
        troopid = tID;
        name = tName;
        cost = tCost;
        space = tSpace;

        speed = tSpeed;
        range = tRange;
        hitspeed = tHitSpeed;
        ground = groundTroop;
        atkair = canAttackAir;

        dmg = damage;
        hp = health;
    }

    public int Attack(){
        return dmg;
    }

    //returns true if troop died, false if still alive
    public boolean TakeDamage(int damage){
        hp -= damage;
        return hp <= 0;
    }
}
