/**
 * Mostly from Applied Energistics 2, used with permission
 * */

package openlyfay.ancientgateways.util;

import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkStatus;

import java.util.ArrayList;
import java.util.List;

public class TeleportPatch {


    private static TeleportPatch instance;

    public static TeleportPatch getInstance() {
        if (instance == null) {
            instance = new TeleportPatch();
        }
        return instance;
    }

    public void interdimensionalTeleport(Entity entity, ServerWorld dim, double x, double y, double z){
        TelDestination target = new TelDestination(dim, x, y, z);
        this.teleportEntity(entity, target);
    }

    private final ThreadLocal<TeleportTarget> teleportTarget = new ThreadLocal<>();

    /**
     * If an entity is currently being teleported, this will return the target
     * within the target dimension.
     */
    public TeleportTarget getTeleportTarget() {
        return teleportTarget.get();
    }

    /**
     * Mostly from dimensional doors.. which mostly got it form X-Comp.
     *
     * @param entity to be teleported entity
     * @param link   destination
     * @return teleported entity
     */
    private Entity teleportEntity(Entity entity, final TelDestination link) {
        final ServerWorld oldWorld;
        final ServerWorld newWorld;

        try {
            oldWorld = (ServerWorld) entity.world;
            newWorld = link.dim;
        } catch (final Throwable e) {
            return entity;
        }

        if (oldWorld == null) {
            return entity;
        }
        if (newWorld == null) {
            return entity;
        }

        if (newWorld == oldWorld) {
            return entity;
        }

        // Are we riding something? Teleport it instead.
        if (entity.hasVehicle()) {
            return this.teleportEntity(entity.getVehicle(), link);
        }

        // Is something riding us? Handle it first.
        final List<Entity> passengers = entity.getPassengerList();
        final List<Entity> passengersOnOtherSide = new ArrayList<>(passengers.size());
        for (Entity passenger : passengers) {
            passenger.stopRiding();
            passengersOnOtherSide.add(this.teleportEntity(passenger, link));
        }
        // We keep track of all so we can remount them on the other side.

        // load the chunk!
        newWorld.getChunkManager().getChunk(MathHelper.floor(link.x) >> 4, MathHelper.floor(link.z) >> 4,
                ChunkStatus.FULL, true);

        //had to use an alternate method to prevent players from landing at their spawn when returning from the End
        if (entity instanceof ServerPlayerEntity && oldWorld.getRegistryKey() == World.END && newWorld.getRegistryKey() == World.OVERWORLD){
            ((ServerPlayerEntity) entity).teleport(newWorld,link.x,link.y,link.z,entity.yaw,entity.pitch);
        }

        // Store in a threadlocal so that EntityMixin can return it for the Vanilla
        // logic to use
        else {
            teleportTarget.set(new TeleportTarget(new Vec3d(link.x, link.y, link.z), Vec3d.ZERO, entity.yaw, entity.pitch));
            try {
                entity = entity.moveToWorld(link.dim);
            } finally {
                teleportTarget.remove();
            }

        }

        if (!passengersOnOtherSide.isEmpty()) {
            for (Entity passanger : passengersOnOtherSide) {
                passanger.startRiding(entity, true);
            }
        }

        return entity;
    }

    private static class TelDestination {
        private final ServerWorld dim;
        private final double x;
        private final double y;
        private final double z;

        TelDestination(final ServerWorld dimension, final double x, final double y, final double z) {
            this.dim = dimension;
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

}

