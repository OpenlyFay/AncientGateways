package openlyfay.ancientgateways.block.blockitem;

import net.minecraft.block.Block;

public class WhiteRuneItem extends AbstractRuneItem {
    public WhiteRuneItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public char getRuneID() {
        return 'F';
    }
}
