package openlyfay.ancientgateways.block.runes.blockitem;

import net.minecraft.block.Block;

public class BlueRuneItem extends AbstractRuneItem {
    public BlueRuneItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public char getRuneID() {
        return '4';
    }
}
