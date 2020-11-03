package openlyfay.ancientgateways.block;


import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import openlyfay.ancientgateways.block.runes.AbstractRuneBlock;
import openlyfay.ancientgateways.blockentity.GatewayBlockEntity;

import static net.minecraft.util.math.Direction.NORTH;
import static net.minecraft.util.math.Direction.SOUTH;

public class GatewayBlock extends HorizontalFacingBlock implements BlockEntityProvider {

    public static final BooleanProperty ON = BooleanProperty.of("on");
    public static final BooleanProperty PAIRED = BooleanProperty.of("paired");
    public static final BooleanProperty ERROR = BooleanProperty.of("error");

    public GatewayBlock(Settings settings){
        super(settings);
        setDefaultState(this.stateManager.getDefaultState().with(Properties.HORIZONTAL_FACING, NORTH).with(ON,false).with(PAIRED, false).with(ERROR,false));
    }

    @Override
    public BlockEntity createBlockEntity(BlockView blockView){
        return new GatewayBlockEntity();
    }


    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(Properties.HORIZONTAL_FACING);
        stateManager.add(ON);
        stateManager.add(PAIRED);
        stateManager.add(ERROR);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state){
        return BlockRenderType.MODEL;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            Inventory blockEntity = (Inventory) world.getBlockEntity(pos);
            ItemStack itemStack = player.getStackInHand(hand);
            if (player.isSneaking()) {
                if (GatewayStructureIntact(pos, state, world)) {
                    if (world.getBlockEntity(pos) instanceof GatewayBlockEntity) {
                        ((GatewayBlockEntity) world.getBlockEntity(pos)).activationCheck(false,null);
                    }
                }
            } else {
                if (!itemStack.isEmpty()) {
                    for (int i = 0; i < blockEntity.size(); i++) {
                        if (blockEntity.isValid(i,itemStack.copy().split(1))) {
                            blockEntity.setStack(i, player.getStackInHand(hand).split(1));
                            return ActionResult.SUCCESS;
                        }
                    }
                } else {
                    for (int i = blockEntity.size() - 1; i >= 0; i--) {
                        if (!blockEntity.getStack(i).isEmpty()) {
                            ItemScatterer.spawn(world, pos.getX() + state.get(Properties.HORIZONTAL_FACING).getOffsetX(), pos.getY(), pos.getZ() + state.get(Properties.HORIZONTAL_FACING).getOffsetZ(), blockEntity.getStack(i));
                            blockEntity.removeStack(i);
                            return ActionResult.SUCCESS;

                        }
                    }
                }
            }
            return ActionResult.SUCCESS;
        }
        return ActionResult.CONSUME;
    }


    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved){
        if(state.getBlock() != newState.getBlock()){
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if(blockEntity instanceof GatewayBlockEntity){
                ItemScatterer.spawn(world, pos, (GatewayBlockEntity)blockEntity);
                ((GatewayBlockEntity) blockEntity).chunkLoaderManager(false);
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
                for (int y = 0; y < 7; y++) {
                    BlockPos blockPos = pos.add(xz * xFactor, -y, xz * zFactor);
                    if(y >= 1 && y <= 5) {
                        if (!world.getBlockState(blockPos).isAir()) {
                            return false;
                        }
                    }
                    else {
                        if(world.getBlockState(blockPos).isAir()){
                            return false;
                        }
                    }
                }
            }
            else {
                for (int y = 2; y < 5; y++) {
                    BlockPos blockPos = pos.add(xz * xFactor, -y, xz * zFactor);
                    if (xz == -3 || xz == 3) {
                        if (!(world.getBlockState(blockPos.offset(direction)).getBlock() instanceof AbstractRuneBlock)) {
                            return false;
                        }

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

    public String getGatewayRuneCode(BlockState state, BlockPos pos, World world){
        StringBuilder total = new StringBuilder();
        int xVal1 = 0;
        int xVal2 = 0;
        int zVal1 = 0;
        int zVal2 = 0;
        switch (state.get(Properties.HORIZONTAL_FACING)){
            case NORTH:
                xVal1 = -3;
                zVal1 = -1;
                xVal2 = 3;
                zVal2 = -1;
                break;
            case EAST:
                xVal1 = 1;
                zVal1 = -3;
                xVal2 = 1;
                zVal2 = 3;
                break;
            case SOUTH:
                xVal1 = 3;
                zVal1 = 1;
                xVal2 = -3;
                zVal2 = 1;
                break;
            case WEST:
                xVal1 = -1;
                zVal1 = 3;
                xVal2 = -1;
                zVal2 = -3;
                break;
        }

        for(int i = -2; i > -5; i--){
            if (!(world.getBlockState(pos.add(xVal1,i,zVal1)).getBlock() instanceof AbstractRuneBlock)){
                return null;
            }
            else {
                total.append(((AbstractRuneBlock) world.getBlockState(pos.add(xVal1, i, zVal1)).getBlock()).getRuneID());
            }
        }
        for(int i = -4; i < -1; i++){
            if (!(world.getBlockState(pos.add(xVal2,i,zVal2)).getBlock() instanceof AbstractRuneBlock)){
                return null;
            }
            else {
                total.append(((AbstractRuneBlock) world.getBlockState(pos.add(xVal2, i, zVal2)).getBlock()).getRuneID());
            }

        }
        return total.toString();

    }

}
