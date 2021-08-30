package openlyfay.ancientgateways.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.poi.PointOfInterest;
import net.minecraft.world.poi.PointOfInterestStorage;
import net.minecraft.world.poi.PointOfInterestType;
import openlyfay.ancientgateways.AncientGateways;

import java.util.Optional;

public class Barrier extends Block {
    public Barrier(Settings settings) {
        super(settings);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        super.onEntityCollision(state, world, pos, entity);
        Optional<BlockPos> shroedinger = world.getServer().getWorld(
                RegistryKey.of(Registry.DIMENSION, AncientGateways.DIM_ID)).getPointOfInterestStorage().getNearestPosition(AncientGateways.isAnchor,pos,2048, PointOfInterestStorage.OccupationStatus.ANY
        );
        if (shroedinger.isPresent()){
            BlockPos fulcrum = shroedinger.get();
            entity.teleport(fulcrum.getX(),fulcrum.getY(),fulcrum.getZ());
        }
    }
}
