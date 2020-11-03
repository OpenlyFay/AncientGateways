package openlyfay.ancientgateways.block.runes.blockitem;

import net.minecraft.block.Block;

public class YellowRuneItem extends AbstractRuneItem {
    public YellowRuneItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public char getRuneID() {
        return 'B';
    }
}
