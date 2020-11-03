package openlyfay.ancientgateways.block.runes.blockitem;

import net.minecraft.block.Block;

public class RedRuneItem extends AbstractRuneItem {
    public RedRuneItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public char getRuneID() {
        return '1';
    }
}
