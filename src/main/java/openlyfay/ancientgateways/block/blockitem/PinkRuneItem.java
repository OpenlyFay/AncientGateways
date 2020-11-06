package openlyfay.ancientgateways.block.blockitem;

import net.minecraft.block.Block;

public class PinkRuneItem extends AbstractRuneItem {
    public PinkRuneItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public char getRuneID() {
        return '9';
    }
}
