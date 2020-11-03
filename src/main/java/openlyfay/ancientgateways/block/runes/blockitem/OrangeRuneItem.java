package openlyfay.ancientgateways.block.runes.blockitem;

import net.minecraft.block.Block;

public class OrangeRuneItem extends AbstractRuneItem {
    public OrangeRuneItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public char getRuneID() {
        return 'E';
    }
}
