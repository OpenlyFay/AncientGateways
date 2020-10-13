package openlyfay.ancientgateways.block;


import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import openlyfay.ancientgateways.blockentity.GatewayBlockEntity;

import static net.minecraft.util.math.Direction.NORTH;
import static net.minecraft.util.math.Direction.SOUTH;

public class GatewayBlock extends HorizontalFacingBlock implements BlockEntityProvider {

    public GatewayBlock(Settings settings){
        super(settings);
        setDefaultState(this.stateManager.getDefaultState().with(Properties.HORIZONTAL_FACING, NORTH));
    }

    @Override
    public BlockEntity createBlockEntity(BlockView blockView){
        return new GatewayBlockEntity();
    }


    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(Properties.HORIZONTAL_FACING);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state){
        return BlockRenderType.MODEL;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        Inventory blockEntity = (Inventory) world.getBlockEntity(pos);
        if (player.isSneaking()){
            if(world instanceof ServerWorld && this.GatewayStructureIntact(pos, state, world)) {
                if (world.getBlockEntity(pos) instanceof GatewayBlockEntity) {
                    ((GatewayBlockEntity) world.getBlockEntity(pos)).activationCheck(state.get(Properties.HORIZONTAL_FACING));
                }
            }
        }
        else{
            if(!player.getStackInHand(hand).isEmpty()){
                for (int i = 0; i < blockEntity.size(); i++) {
                    if (blockEntity.getStack(i).isEmpty()) {
                        blockEntity.setStack(i, player.getStackInHand(hand).copy());
                        player.getStackInHand(hand).setCount(player.getStackInHand(hand).getCount() - blockEntity.getStack(i).getCount());
                        return ActionResult.SUCCESS;
                    }
                }
            }
            else {
                for (int i = blockEntity.size()-1; i >= 0; i--) {
                    if (!blockEntity.getStack(i).isEmpty()) {
                        ItemScatterer.spawn(world,pos.getX() + state.get(Properties.HORIZONTAL_FACING).getOffsetX(),pos.getY(),pos.getZ() + state.get(Properties.HORIZONTAL_FACING).getOffsetZ(),blockEntity.getStack(i));
                        blockEntity.setStack(i, ItemStack.EMPTY);
                        return ActionResult.SUCCESS;

                    }
                }
            }
        }
        return ActionResult.SUCCESS;
    }


    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState)this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved){
        if(state.getBlock() != newState.getBlock()){
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if(blockEntity instanceof GatewayBlockEntity){
                ItemScatterer.spawn(world, pos, (GatewayBlockEntity)blockEntity);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    public boolean GatewayStructureIntact(BlockPos pos, BlockState state, World world){
        Direction direction = state.get(FACING);
        byte xFactor = 1;
        byte zFactor = 1;

        if(direction == NORTH || direction == SOUTH){
            zFactor = 0;
        }
        else {
            xFactor = 0;
        }
        for (int xz = -3; xz < 4; xz++) {
            if (xz >= -1 && xz <= 1) {
                for (int y = 1; y < 6; y++) {
                    BlockPos blockPos = pos.add(xz * xFactor, -y, xz * zFactor);
                        if (!world.getBlockState(blockPos).isAir()) {
                            return false;
                        }
                }
            }
            else {
                for (int y = 2; y < 5; y++) {
                    BlockPos blockPos = pos.add(xz * xFactor, -y, xz * zFactor);
                    if (xz == -3 || xz == 3) {
                        if (!world.getBlockState(blockPos).isOf(Blocks.CHISELED_STONE_BRICKS)) {
                            return false;
                        }
                        /**
                        if (!world.getBlockState(blockPos.offset(direction)).isOf(Blocks.CHISELED_STONE_BRICKS)) {
                            return false;
                        }
                         */
                    } else {
                        if (!world.getBlockState(blockPos).isAir()) {
                            return false;
                        }
                    }
                }
        }
        }


        return true;
    }

}
