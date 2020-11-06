package openlyfay.ancientgateways.block.blockitem;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;

public abstract class AbstractRuneItem extends BlockItem {

    public AbstractRuneItem(Block block, Settings settings) {
        super(block,settings);
    }

    public char getRuneID(){
        return '|';
    }

}