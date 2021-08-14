package openlyfay.ancientgateways.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import openlyfay.ancientgateways.AncientGateways;

public class Barrier extends Block {
    public Barrier(Settings settings) {
        super(settings);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        super.onEntityCollision(state, world, pos, entity);
        BlockPos fulcrum = world.getServer().getWorld(RegistryKey.of(Registry.DIMENSION, AncientGateways.DIM_ID)).getPointOfInterestStorage().getNearestPosition();
        int cellX = (pos.getX()+1024)/2048;
        int cellZ = (pos.getX()+1024)/2048;
        if (pos.getX() < 0){
            cellX = (pos.getX()-1024)/2048;
        }
        if (pos.getZ() < 0){
            cellZ = (pos.getZ()-1024)/2048;
        }
        fulcrum = new BlockPos(cellX*2048,128,cellZ*2048);
        entity.teleport(fulcrum.getX(),fulcrum.getY(),fulcrum.getZ());
    }
}
