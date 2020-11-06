package openlyfay.ancientgateways.block.blockitem;

import net.minecraft.block.Block;

public class LimeRuneItem extends AbstractRuneItem {
    public LimeRuneItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public char getRuneID() {
        return 'A';
    }
}
