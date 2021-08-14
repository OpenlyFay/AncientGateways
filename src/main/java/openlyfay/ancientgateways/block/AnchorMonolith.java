package openlyfay.ancientgateways.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import openlyfay.ancientgateways.block.blockentity.AnchorMonolithEntity;
import org.jetbrains.annotations.Nullable;

public class AnchorMonolith extends BlockWithEntity {

    public final static BooleanProperty WATER = BooleanProperty.of("water");
    public final static BooleanProperty GRASS = BooleanProperty.of("grass");
    public final static BooleanProperty SKY = BooleanProperty.of("sky");
    public final static BooleanProperty RULES = BooleanProperty.of("rules");

    public AnchorMonolith(Settings settings) {
        super(settings);
        setDefaultState(this.stateManager.getDefaultState().with(WATER,false).with(GRASS,false).with(SKY, false).with(RULES,false));
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockView world) {
        return new AnchorMonolithEntity();
    }


    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(WATER);
        stateManager.add(GRASS);
        stateManager.add(SKY);
        stateManager.add(RULES);
    }

}
