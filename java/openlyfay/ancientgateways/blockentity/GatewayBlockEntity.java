package openlyfay.ancientgateways.blockentity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import openlyfay.ancientgateways.AncientGateways;
import openlyfay.ancientgateways.block.GatewayBlock;
import openlyfay.ancientgateways.maths.MasterList;
import openlyfay.ancientgateways.maths.TeleportPatch;

import java.util.Iterator;
import java.util.List;

import static net.minecraft.util.math.Direction.NORTH;
import static net.minecraft.util.math.Direction.SOUTH;


public class GatewayBlockEntity extends BlockEntity implements InventoryOutsourcing, Tickable {
    private String runeIdentifier;
    private String runeTarget;
    //do concat magic with these to solve the whole "master list" problem
    private int countdown;
    private Box box;
    private boolean paired;
    private boolean unstable;

    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(6,ItemStack.EMPTY);

    public GatewayBlockEntity(){
        super(AncientGateways.GATEWAY_BLOCK_ENTITY);
        countdown = 0;

    }

    @Override
    public int getMaxCountPerStack() {
        return 1;
    }
    //Find out why hoppers disregard this and fix it

    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }

    @Override
    public void fromTag(BlockState blockState, CompoundTag tag){
        super.fromTag(blockState, tag);
        Inventories.fromTag(tag, this.items);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag){
        super.toTag(tag);
        Inventories.toTag(tag, this.items);
        return tag;
    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
        return stack.isEmpty() || stack.getCount() == 1;
    }

    public void activationCheck(Direction facing){
        if(facing == null){
            facing = getCachedState().get(Properties.FACING);
        }

        if (countdown <= 0){
            countdown = 400;
            if(facing == NORTH || facing == SOUTH){
                box = new Box(pos.add(2,-1,1), pos.add(-2,-5,0));
            }
            else {
                box = new Box(pos.add(1,-1,2), pos.add(0,-5,-2));
            }
        }
    }

    public void tick(){
        if (countdown > 0){
            List<Entity> payloadList = world.getNonSpectatingEntities(Entity.class,box);
            Iterator payloadIterator = payloadList.iterator();
            Entity payloadEntity;
            while (payloadIterator.hasNext()){
                payloadEntity = (Entity) payloadIterator.next();
                TeleportPatch tpHack = TeleportPatch.getInstance();
                RegistryKey<World> registryKey = ServerWorld.OVERWORLD;
                ServerWorld targetWorld = ((ServerWorld) world).getServer().getWorld(registryKey);
                if (targetWorld != null) {

                    Vec3d oldVelocity = payloadEntity.getVelocity();
                    tpHack.interdimensionalTeleport(payloadEntity, targetWorld, 0, 128, 0);
                    payloadEntity.setVelocity(oldVelocity);
                }
            }
            if (countdown%80 == 0){
                Block block = this.getCachedState().getBlock();
                if (block instanceof GatewayBlock){
                    if(!((GatewayBlock) block).GatewayStructureIntact(pos, world.getBlockState(pos),world)){
                        countdown = 0;
                    }
                }
            }
            countdown--;
        }
    }

    public int getCountdown() {
        return countdown;
    }
}
