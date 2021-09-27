package openlyfay.ancientgateways.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import openlyfay.ancientgateways.block.blockentity.AnchorBaseEntity;
import openlyfay.ancientgateways.util.SpiralHelper;
import openlyfay.ancientgateways.util.mixininterface.Teleportable;

public class Barrier extends Block {
    protected static final VoxelShape SHAPE = Block.createCuboidShape(1.0D, 1.0D, 1.0D, 15.0D, 15.0D, 15.0D);
    public Barrier(Settings settings) {
        super(settings);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!world.isClient && entity instanceof Teleportable && ((Teleportable) entity).getPortalCoolDown() == 0){
            BlockPos target = SpiralHelper.findNearestAnchor(entity.getPos());
            if (world.getBlockEntity(target) instanceof AnchorBaseEntity){
                int x = world.random.nextInt(2) - 1;
                int z = world.random.nextInt(2) - 1;
                entity.teleport(target.getX() + x,target.getY(),target.getZ() + z);
            }
            ((Teleportable) entity).setPortalCoolDown(50);
        }
        super.onEntityCollision(state, world, pos, entity);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }
}
