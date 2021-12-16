package openlyfay.ancientgateways.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import openlyfay.ancientgateways.AncientGateways;
import openlyfay.ancientgateways.util.TeleportPatch;

import java.util.List;

public class WiseTablet extends Item {
    public static final int chargeTime = AncientGateways.agConfig.tabletChargeTime;

    public WiseTablet(Settings settings){super(settings);}

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (!world.isClient){
            NbtCompound compoundTag = stack.getOrCreateNbt();
            if (!compoundTag.contains("HasHome")){
                compoundTag.putDouble("CoordinateX",user.getX());
                compoundTag.putDouble("CoordinateY",user.getY());
                compoundTag.putDouble("CoordinateZ",user.getZ());
                compoundTag.putString("World",world.getRegistryKey().getValue().toString());
                compoundTag.putBoolean("HasHome", true);
            }
        }
        return  ItemUsage.consumeHeldItem(world,user,hand);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        int time = getMaxUseTime(stack) - remainingUseTicks;
        if (!world.isClient && time >= chargeTime){
            NbtCompound compoundTag = stack.getOrCreateNbt();
            if (compoundTag.contains("HasHome")){
                Vec3d targetPos;
                String worldID;
                if (compoundTag.getBoolean("HasReturn") && !user.isSneaking()){
                    targetPos = new Vec3d(compoundTag.getDouble("ReturnX"), compoundTag.getDouble("ReturnY"), compoundTag.getDouble("ReturnZ"));
                    worldID = compoundTag.getString("ReturnWorld");
                    compoundTag.putBoolean("HasReturn", false);
                }
                else{
                    targetPos = new Vec3d(compoundTag.getDouble("CoordinateX"),compoundTag.getDouble("CoordinateY"),compoundTag.getDouble("CoordinateZ"));
                    worldID = compoundTag.getString("World");

                    compoundTag.putDouble("ReturnX",user.getX());
                    compoundTag.putDouble("ReturnY",user.getY());
                    compoundTag.putDouble("ReturnZ",user.getZ());
                    compoundTag.putString("ReturnWorld",world.getRegistryKey().getValue().toString());
                    compoundTag.putBoolean("HasReturn", true);
                }

                if(worldID.equals(world.getRegistryKey().getValue().toString())){
                    user.teleport(targetPos.x,targetPos.y,targetPos.z);
                }
                else {
                    TeleportPatch tpHack = TeleportPatch.getInstance();
                    ServerWorld targetWorld;
                    targetWorld = world.getServer().getWorld(RegistryKey.of(Registry.WORLD_KEY, new Identifier(worldID)));
                    tpHack.interdimensionalTeleport(user,targetWorld,targetPos.x,targetPos.y,targetPos.z);
                }
            }
        }
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        NbtCompound compoundTag = stack.getOrCreateNbt();
        if (compoundTag.contains("HasHome")){
            tooltip.add(new TranslatableText("item.ancientgateways.wise_tablet.tooltip0", (int) compoundTag.getDouble("CoordinateX"), (int) compoundTag.getDouble("CoordinateY"), (int) compoundTag.getDouble("CoordinateZ"),compoundTag.getString("World")));
        }
        if (compoundTag.getBoolean("HasReturn")){
            tooltip.add(new TranslatableText("item.ancientgateways.wise_tablet.tooltip1", (int) compoundTag.getDouble("ReturnX"), (int) compoundTag.getDouble("ReturnY"), (int) compoundTag.getDouble("ReturnZ"),compoundTag.getString("ReturnWorld")));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }
}
