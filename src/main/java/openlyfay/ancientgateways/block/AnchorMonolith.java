package openlyfay.ancientgateways.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import openlyfay.ancientgateways.block.blockentity.AnchorMonolithEntity;
import org.jetbrains.annotations.Nullable;

public class AnchorMonolith extends BlockWithEntity {

    public static final IntProperty TYPE = IntProperty.of("type",0,3);

    public AnchorMonolith(Settings settings) {
        super(settings);
        setDefaultState(this.stateManager.getDefaultState().with(TYPE,0));
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockView world) {
        return new AnchorMonolithEntity();
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(TYPE);
    }

    public String getTypeName (BlockState state){
        switch (state.get(TYPE)){
            case 0: return "settings";
            case 1: return "water";
            case 2: return "grass";
            case 3: return "skybox";
        }
        return "settings";
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient){
            BlockEntity entity = world.getBlockEntity(pos);
            if (!(entity instanceof AnchorMonolithEntity)){
                return ActionResult.FAIL;
            }
            boolean consumeItem = ((AnchorMonolithEntity) entity).onInteract(state.get(TYPE), player, hand);
            if (consumeItem){
                player.getStackInHand(hand).decrement(1);
            }

        }
        return ActionResult.CONSUME;
    }
}
