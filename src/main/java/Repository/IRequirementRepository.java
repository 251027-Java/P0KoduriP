package Repository;

import Models.TroopFactory;
import Util.Models.Upgrade;

import java.util.List;
import java.util.Map;

public interface IRequirementRepository extends IRepository {
    public Map<Integer, String> GetResourceInfo();
    public int GetValuePerGem(String value);
    public Map<Integer, Integer> GetGemShopOptions();
    public List<TroopFactory> GetTroopFactories();
    public int GetTroopDamage(int troopID, int troopLevel);
    public int GetTroopHP(int troopID, int troopLevel);
    public Upgrade GetUpgradeInfo(int troopID, int troopLevel);
}
