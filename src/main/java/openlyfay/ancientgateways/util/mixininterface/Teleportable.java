package openlyfay.ancientgateways.util.mixininterface;

public interface Teleportable {



    default int getPortalCoolDown() {
        return 0;
    }


    default void setPortalCoolDown(int portalCD) {
    }
}
