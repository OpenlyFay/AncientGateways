package openlyfay.ancientgateways.block.blockitem;

import net.minecraft.block.Block;

public class LightBlueRuneItem extends AbstractRuneItem {
    public LightBlueRuneItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public char getRuneID() {
        return 'C';
    }
}
