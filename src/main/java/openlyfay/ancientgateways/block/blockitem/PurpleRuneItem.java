package openlyfay.ancientgateways.block.blockitem;

import net.minecraft.block.Block;

public class PurpleRuneItem extends AbstractRuneItem {
    public PurpleRuneItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public char getRuneID() {
        return '5';
    }
}
