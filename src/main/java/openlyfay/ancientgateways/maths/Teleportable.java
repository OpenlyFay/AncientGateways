package openlyfay.ancientgateways.maths;

public interface Teleportable {



    default int getPortalCoolDown() {
        return 0;
    }


    default void setPortalCoolDown(int portalCD) {
    }
}
