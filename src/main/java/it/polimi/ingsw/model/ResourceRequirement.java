package it.polimi.ingsw.model;

/**
 * Class Requirement represents the cost for a DevelopCard or a requirement for a LeaderCard
 */
public class ResourceRequirement implements Requirement{
    private ResourceMap resources;

    /**
     * Constructor ResourceRequirement takes a ResourceMap which has for each Resource the amount required
     * @param resources is a ResourceMap, which has for each Resource the amount required
     */
    public ResourceRequirement(ResourceMap resources) {
        this.resources = resources;
    }

    /**
     * Method getResources gets this Requirement resources
     * @return a ResourceMap, which has for each Resource the amount required
     */
    public ResourceMap getResources() {
        return resources;
    }

    /**
     * Method setResource sets this Requirement resources
     * @param resources is a ResourceMap, which has for each Resource the amount required
     */
    public void setResources(ResourceMap resources) {
        this.resources = resources;
    }

    /**
     * Method checkRequirement checks if the selected Player has the required resources
     * @param player is the Player to check
     * @return true if the Player meets the requirement, else false
     */
    @Override
    public boolean checkRequirement(Player player) {
        ResourceMap playerResources = player.getTotalResources();

        for (Resource value: Resource.values()){
            if (playerResources.getResource(value) < resources.getResource(value)){
                return false;
            }
        }
        return true;
    }

}