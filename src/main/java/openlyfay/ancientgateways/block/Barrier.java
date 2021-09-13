package openlyfay.ancientgateways.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.poi.PointOfInterest;
import net.minecraft.world.poi.PointOfInterestStorage;
import net.minecraft.world.poi.PointOfInterestType;
import openlyfay.ancientgateways.AncientGateways;

import java.util.Optional;

import static openlyfay.ancientgateways.world.RegisterWorld.getPocketMaxSize;
import static openlyfay.ancientgateways.world.RegisterWorld.isAnchor;

public class Barrier extends Block {
    protected static final VoxelShape SHAPE = Block.createCuboidShape(1.0D, 1.0D, 1.0D, 15.0D, 15.0D, 15.0D);
    public Barrier(Settings settings) {
        super(settings);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!world.isClient){
            Optional<BlockPos> schrodinger = world.getServer().getWorld(
                    RegistryKey.of(Registry.DIMENSION, AncientGateways.DIM_ID)).getPointOfInterestStorage().getNearestPosition(isAnchor,pos, getPocketMaxSize() * 2, PointOfInterestStorage.OccupationStatus.ANY
            );
            if (schrodinger.isPresent()){
                BlockPos fulcrum = schrodinger.get();
                int x = world.random.nextInt(2) - 1;
                int z = world.random.nextInt(2) - 1;
                entity.teleport(fulcrum.getX() + x,fulcrum.getY(),fulcrum.getZ() + z);
            }
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
