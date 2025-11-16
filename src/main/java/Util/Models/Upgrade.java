package Util.Models;

public class Upgrade {
    private final int upCost;
    private final int upDays;
    private final int upHours;
    private final int upMinutes;
    private final int upSeconds;
    private final boolean maxLevel;

    private final String upResource;
    private final int nextLevel;

    public Upgrade(int upgradeCost, int upgradeDays, int upgradeHours, int upgradeMinutes, int upgradeSeconds, boolean isMaxLevel,
                   String upgradingResource, int nextUpgradeLevel){
        upCost = upgradeCost;
        upDays = upgradeDays;
        upHours = upgradeHours;
        upMinutes = upgradeMinutes;
        upSeconds = upgradeSeconds;
        maxLevel = isMaxLevel;

        upResource = upgradingResource;
        nextLevel = nextUpgradeLevel;
    }

    public int UpgradeCost() { return upCost; }
    public int UpgradeDays() { return upDays; }
    public int UpgradeHours() { return upHours; }
    public int UpgradeMinutes() { return upMinutes; }
    public int UpgradeSeconds() { return upSeconds; }
    public boolean IsMaxLevel() { return maxLevel; }

    public String UpgradeInfo(){
        if (maxLevel) return "Already at max level!";

        String info = String.format("Upgrade to Level %d\n:%d %s\n", nextLevel, upCost, upResource);
        if (upDays > 0) info += upDays + " Days, ";
        if (upHours > 0) info += upHours + " Hours, ";
        if (upMinutes > 0) info += upMinutes + " Minutes, ";
        if (upSeconds > 0) info += upSeconds + " Seconds, ";
        return info.substring(0, info.length()-2) + "\n";
    }

    public boolean CanBeUpgraded(int resourceAmount){
        return resourceAmount >= upCost;
    }
}
