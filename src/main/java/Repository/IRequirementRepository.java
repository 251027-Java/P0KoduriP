package Repository;

import java.util.Map;

public interface IRequirementRepository extends IRepository {
    public Map<Integer, String> GetResourceInfo();
    public int GetValuePerGem(String value);
    public Map<Integer, Integer> GetGemShopOptions();
}
