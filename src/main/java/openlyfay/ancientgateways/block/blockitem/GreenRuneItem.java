package openlyfay.ancientgateways.block.blockitem;

import net.minecraft.block.Block;

public class GreenRuneItem extends AbstractRuneItem {
    public GreenRuneItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public char getRuneID() {
        return '2';
    }
}
