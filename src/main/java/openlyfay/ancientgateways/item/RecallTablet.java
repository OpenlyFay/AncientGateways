package openlyfay.ancientgateways.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import openlyfay.ancientgateways.util.TeleportPatch;

public class RecallTablet extends Item {
    public RecallTablet(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient){
            ItemStack stack = user.getStackInHand(hand);
            NbtCompound compoundTag = stack.getOrCreateNbt();
            if (compoundTag.contains("HasTarget")){
                Vec3d targetPos = new Vec3d(compoundTag.getDouble("CoordinateX"),compoundTag.getDouble("CoordinateY"),compoundTag.getDouble("CoordinateZ"));
                if(compoundTag.getString("World").equals(world.getRegistryKey().getValue().toString())){
                    user.teleport(targetPos.x,targetPos.y,targetPos.z);
                }
                else {
                    TeleportPatch tpHack = TeleportPatch.getInstance();
                    ServerWorld targetWorld;
                    targetWorld = world.getServer().getWorld(RegistryKey.of(Registry.WORLD_KEY, new Identifier(compoundTag.getString("World"))));
                    tpHack.interdimensionalTeleport(user,targetWorld,targetPos.x,targetPos.y,targetPos.z);
                }
            }
            else {
                compoundTag.putDouble("CoordinateX",user.getX());
                compoundTag.putDouble("CoordinateY",user.getY());
                compoundTag.putDouble("CoordinateZ",user.getZ());
                compoundTag.putString("World",world.getRegistryKey().getValue().toString());
                compoundTag.putBoolean("HasTarget", true);
            }
        }


        return super.use(world, user, hand);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
    }

    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }
}
