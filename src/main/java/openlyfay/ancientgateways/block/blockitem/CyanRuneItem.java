package openlyfay.ancientgateways.block.blockitem;

import net.minecraft.block.Block;

public class CyanRuneItem extends AbstractRuneItem {
    public CyanRuneItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public char getRuneID() {
        return '6';
    }
}
