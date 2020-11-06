package openlyfay.ancientgateways.block.blockitem;

import net.minecraft.block.Block;

public class MagentaRuneItem extends AbstractRuneItem {
    public MagentaRuneItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public char getRuneID() {
        return 'D';
    }
}
