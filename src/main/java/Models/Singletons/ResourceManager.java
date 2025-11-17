package Models.Singletons;

public class ResourceManager {
    private final static ResourceManager resourceManager = new ResourceManager();
    private ResourceManager() {}
    public static ResourceManager getInstance() {return resourceManager;}
}
