package openlyfay.ancientgateways.block;

import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import static net.minecraft.util.math.Direction.NORTH;

public abstract class AbstractRuneBlock extends HorizontalFacingBlock {
    protected static final VoxelShape BOUNDING_SHAPE_N = Block.createCuboidShape(0.0D, 0.0D, 16.0D, 16.0D, 16.0D, 15.75D);
    protected static final VoxelShape BOUNDING_SHAPE_S = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 0.25D);
    protected static final VoxelShape BOUNDING_SHAPE_E = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 0.25D, 16.0D, 16.0D);
    protected static final VoxelShape BOUNDING_SHAPE_W = Block.createCuboidShape(16.0D, 0.0D, 0.0D, 15.75D, 16.0D, 16.0D);

    public AbstractRuneBlock(Settings settings){
        super(settings);
        setDefaultState(this.stateManager.getDefaultState().with(Properties.HORIZONTAL_FACING, NORTH));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(Properties.HORIZONTAL_FACING);
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        switch ((Direction) state.get(FACING)) {
            case NORTH:
            default:
                return BOUNDING_SHAPE_N;
            case SOUTH:
                return BOUNDING_SHAPE_S;
            case EAST:
                return BOUNDING_SHAPE_E;
            case WEST:
                return BOUNDING_SHAPE_W;
        }
    }

    public String getRuneID() {
        return "null";
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState)this.getDefaultState().with(FACING, ctx.getPlayerFacing());
    }

    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        return direction == Direction.DOWN && !this.canPlaceAt(state, world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
    }

    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos){
        return sideCoversSmallSquare(world, pos.down(), Direction.UP);
    }

}
