package openlyfay.ancientgateways.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import openlyfay.ancientgateways.block.blockentity.AnchorBaseEntity;
import org.jetbrains.annotations.Nullable;

public class AnchorBase extends BlockWithEntity {


    public AnchorBase(Settings settings) {
        super(settings);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockView world) {
        return new AnchorBaseEntity();
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }




    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient){
            BlockEntity entity = world.getBlockEntity(pos);
            if (!(entity instanceof AnchorBaseEntity)){
                return ActionResult.FAIL;
            }
            boolean consumeItem = ((AnchorBaseEntity) entity).onInteract(0, player, hand);
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
