package Util.Models;

import Util.Time;

public class Upgrade {
    private final String name;

    private final int upCost;
    private final int upDays;
    private final int upHours;
    private final int upMinutes;
    private final int upSeconds;
    private final boolean maxLevelRightNow;

    private final String upResource;
    private final int nextLevel;

    //upgrade numbers are meaningless if it's already at the max level right now
    public Upgrade(String nameOfUpgrade, int upgradeCost, int upgradeDays, int upgradeHours, int upgradeMinutes, int upgradeSeconds, boolean isMaxLevelRightNow,
                   String upgradingResource, int nextUpgradeLevel){
        name = nameOfUpgrade;

        upCost = upgradeCost;
        upDays = upgradeDays;
        upHours = upgradeHours;
        upMinutes = upgradeMinutes;
        upSeconds = upgradeSeconds;
        maxLevelRightNow = isMaxLevelRightNow;

        upResource = upgradingResource;
        nextLevel = nextUpgradeLevel;
    }

    public int GetUpgradeCost() { return upCost; }
    public int GetUpgradeDays() { return upDays; }
    public int GetUpgradeHours() { return upHours; }
    public int GetUpgradeMinutes() { return upMinutes; }
    public int GetUpgradeSeconds() { return upSeconds; }
    public boolean GetIsMaxLevelRightNow() { return maxLevelRightNow; }

    public String UpgradeInfo(){
        if (maxLevelRightNow) return String.format("%s is at max level!", name);

        String info = String.format("Upgrade %s to Level %d\n:%d %s\n", name, nextLevel, upCost, upResource);
        return info + Time.FormatTime(upDays, upHours, upMinutes, upSeconds) + "\n";
    }

    public boolean GetCanBeUpgraded(int resourceAmount){
        return resourceAmount >= upCost;
    }
}
