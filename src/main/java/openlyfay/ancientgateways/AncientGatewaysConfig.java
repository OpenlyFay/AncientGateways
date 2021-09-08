package openlyfay.ancientgateways;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = AncientGateways.MOD_ID)
public class AncientGatewaysConfig implements ConfigData {
    public int chorusInkRadius = 24;
    public boolean chorusInkActivatesGateways = true;

    public int chorusPearlBreakChance = 20;

    public int gatewayActivationTime = 400;
    public int gatewayCooldown = 100;

    public boolean pocketDimensionsEnabled = true;
    public int pocketDimensionInitRadius = 64;
    public int maxPocketExpansions = 16;
}
