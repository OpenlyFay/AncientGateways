package openlyfay.ancientgateways.block.runes;

import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import static net.minecraft.util.math.Direction.NORTH;

public abstract class AbstractRuneBlock extends HorizontalFacingBlock implements Waterloggable {
    protected static final VoxelShape BOUNDING_SHAPE_N = Block.createCuboidShape(0.0D, 0.0D, 15.75D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape BOUNDING_SHAPE_S = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 0.25D);
    protected static final VoxelShape BOUNDING_SHAPE_E = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 0.25D, 16.0D, 16.0D);
    protected static final VoxelShape BOUNDING_SHAPE_W = Block.createCuboidShape(15.75D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    public AbstractRuneBlock(Settings settings){
        super(settings);
        setDefaultState(this.stateManager.getDefaultState().with(Properties.HORIZONTAL_FACING, NORTH).with(WATERLOGGED,false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(Properties.HORIZONTAL_FACING).add(Properties.WATERLOGGED);
    }

    public char getRuneID() {
        return '|';
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context){
        switch (state.get(Properties.HORIZONTAL_FACING)){
            case NORTH: return BOUNDING_SHAPE_N;
            case EAST: return BOUNDING_SHAPE_E;
            case WEST: return BOUNDING_SHAPE_W;
            case SOUTH: return BOUNDING_SHAPE_S;
        }
        return BOUNDING_SHAPE_N;
    }

    public FluidState getFluidState(BlockState state) {
        return (Boolean)state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        boolean fl = fluidState.getFluid() == Fluids.WATER;
        return (BlockState)this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite()).with(WATERLOGGED,fl);
    }

    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return world.getBlockState(pos.offset(((Direction)state.get(FACING)).getOpposite())).getMaterial().isSolid();
    }

    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        return direction.getOpposite() == state.get(FACING) && !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
    }


}
