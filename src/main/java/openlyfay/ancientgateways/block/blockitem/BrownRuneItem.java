package openlyfay.ancientgateways.block.blockitem;

import net.minecraft.block.Block;

public class BrownRuneItem extends AbstractRuneItem {
    public BrownRuneItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public char getRuneID() {
        return '3';
    }
}
