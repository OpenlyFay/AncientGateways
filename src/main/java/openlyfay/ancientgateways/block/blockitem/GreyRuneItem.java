package openlyfay.ancientgateways.block.blockitem;

import net.minecraft.block.Block;

public class GreyRuneItem extends AbstractRuneItem {
    public GreyRuneItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public char getRuneID() {
        return '8';
    }
}
