package openlyfay.ancientgateways.block.runes.blockitem;
import net.minecraft.block.Block;

public class BlackRuneItem extends AbstractRuneItem {
    public BlackRuneItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public char getRuneID() {
        return '0';
    }
}
