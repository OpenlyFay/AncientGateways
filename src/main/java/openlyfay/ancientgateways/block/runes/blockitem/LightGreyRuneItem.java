package openlyfay.ancientgateways.block.runes.blockitem;

import net.minecraft.block.Block;

public class LightGreyRuneItem extends AbstractRuneItem {
    public LightGreyRuneItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public char getRuneID() {
        return '7';
    }
}
