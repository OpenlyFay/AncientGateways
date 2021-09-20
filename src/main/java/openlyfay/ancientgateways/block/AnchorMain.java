package openlyfay.ancientgateways.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import openlyfay.ancientgateways.block.blockentity.AnchorBaseEntity;
import openlyfay.ancientgateways.util.SpiralHelper;

public class AnchorMain extends Block {

    public static final IntProperty TYPE = IntProperty.of("type",1,4);

    public AnchorMain(Settings settings) {
        super(settings);
        setDefaultState(this.stateManager.getDefaultState().with(TYPE,1));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(TYPE);
    }

    public String getTypeName (BlockState state){
        switch (state.get(TYPE)){
            case 1: return "water";
            case 2: return "grass";
            case 3: return "leaves";
            default: return "skybox";
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        BlockEntity anchorEntity = world.getBlockEntity(SpiralHelper.findNearestAnchor(new Vec3d(pos.getX(),pos.getY(),pos.getZ())));
        if (!world.isClient){
            if (!(anchorEntity instanceof AnchorBaseEntity)){
                return ActionResult.FAIL;
            }
            boolean consumeItem = ((AnchorBaseEntity) anchorEntity).onInteract(state.get(TYPE), player, hand);
            if (consumeItem){
                player.getStackInHand(hand).decrement(1);
            }
            return ActionResult.SUCCESS;
        }
        return ActionResult.CONSUME;
    }

    @Override
    public PistonBehavior getPistonBehavior(BlockState state) {
        return PistonBehavior.BLOCK;
    }
}
