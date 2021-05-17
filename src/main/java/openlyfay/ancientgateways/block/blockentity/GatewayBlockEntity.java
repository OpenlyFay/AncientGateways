package openlyfay.ancientgateways.block.blockentity;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.*;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import openlyfay.ancientgateways.AncientGateways;
import openlyfay.ancientgateways.block.GatewayBlock;
import openlyfay.ancientgateways.block.blockitem.AbstractRuneItem;
import openlyfay.ancientgateways.util.MasterList;
import openlyfay.ancientgateways.util.TeleportPatch;
import openlyfay.ancientgateways.util.Teleportable;

import java.util.Iterator;
import java.util.List;

import static net.minecraft.util.math.Direction.NORTH;
import static net.minecraft.util.math.Direction.SOUTH;


public class GatewayBlockEntity extends BlockEntity implements Inventory, Tickable, BlockEntityClientSerializable {
    private String runeIdentifier = "";
    private String runeTarget = "";
    private BlockPos targetPos;
    private RegistryKey<World> targetWorld;
    private int countdown;
    private Box box = new Box(pos.add(1,-1,2), pos.add(0,-5,-2));
    private DefaultedList<ItemStack> inventory;
    private static MasterList masterlist;
    private boolean fresh;
    private final static int cooldownDuration = AncientGateways.agConfig.gatewayCooldown;
    private final static int gatewayDuration = AncientGateways.agConfig.gatewayActivationTime;

    public GatewayBlockEntity(){
        super(AncientGateways.GATEWAY_BLOCK_ENTITY);
        countdown = 0;
        inventory = DefaultedList.ofSize(6,ItemStack.EMPTY);
        if(world != null && !world.isClient){
            masterlist = MasterList.get(((ServerWorld) world).getServer().getWorld(ServerWorld.OVERWORLD));
        }
        targetWorld = ServerWorld.OVERWORLD;
        targetPos = new BlockPos(0,0,0);
        fresh = true;
    }

    @Override
    public int getMaxCountPerStack() {
        return 1;
    }


    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public int size() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < size(); i++) {
            ItemStack stack = getStack(i);
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getStack(int slot) {
        return inventory.get(slot);

    }

    @Override
    public ItemStack removeStack(int slot, int count) {
        ItemStack result = Inventories.splitStack(inventory, slot, count);
        markDirty();
        return result;
    }

    @Override
    public ItemStack removeStack(int slot) {
        markDirty();
        return Inventories.removeStack(inventory, slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        inventory.set(slot, stack);
        markDirty();
    }

    @Override
    public void clear() {
        inventory.clear();
        markDirty();
    }

    @Override
    public void markDirty() {
        sync();
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return false;
    }

    @Override
    public void fromTag(BlockState blockState, CompoundTag tag){
        super.fromTag(blockState, tag);
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        countdown = tag.getInt("countdown");
        runeIdentifier = tag.getString("runeID");
        runeTarget = tag.getString("targetID");
        targetWorld = RegistryKey.of(Registry.DIMENSION, new Identifier(tag.getString("targetWorld")));
        int[] targetPos2 = tag.getIntArray("targetPos");
        targetPos = new BlockPos(targetPos2[0],targetPos2[1],targetPos2[2]);
        Inventories.fromTag(tag, this.inventory);
        if (tag.getBoolean("facingNorth")){
            box = new Box(pos.add(2, -1, 1), pos.add(-2, -5, 0));
        } else {
            box = new Box(pos.add(1, -1, 2), pos.add(0, -5, -2));

        }
        fresh = true;
    }

    @Override
    public CompoundTag toTag(CompoundTag tag){
        Inventories.toTag(tag, this.inventory);
        tag.putInt("countdown", countdown);
        tag.putString("runeID",runeIdentifier);
        tag.putString("targetID",runeTarget);
        tag.putString("targetWorld", targetWorld.getValue().toString());
        int[] targetPos2 = {targetPos.getX(), targetPos.getY(), targetPos.getZ()};
        tag.putIntArray("targetPos", targetPos2);
        tag.putBoolean("facingNorth",getCachedState().get(Properties.HORIZONTAL_FACING).getAxis() == Direction.Axis.Z);
        return super.toTag(tag);
    }

    @Override
    public void fromClientTag(CompoundTag compoundTag) {
        fromTag(null, compoundTag);
    }

    @Override
    public CompoundTag toClientTag(CompoundTag compoundTag) {
        return toTag(compoundTag);
    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
        return getStack(slot).isEmpty() && stack.getCount() == 1 && stack.getItem() instanceof AbstractRuneItem;
    }

    public boolean isActive(){
        return countdown > 0;
    }

    public void activationCheck(){
        activationCheck(false,null,0);
    }

    public void activationCheck(boolean remote, String remoteRuneTarget, int index){
        Direction facing = getCachedState().get(Properties.HORIZONTAL_FACING);
        String runeDelta = ((GatewayBlock) getCachedState().getBlock()).getGatewayRuneCode(world.getBlockState(pos),pos,world);
        if (!world.isClient) {
            masterListSetAddress();
            int localIndex = getIndex();
            if (countdown <= 0 && !runeDelta.isEmpty()) {
                if (!runeDelta.equals(runeIdentifier)) {
                    masterListChangeAddress();
                }
                if (remote){
                    runeTarget = remoteRuneTarget;
                }
                else {
                    runeTarget = getTargetRuneCode();}
                if (runeTarget.equals("")){
                    runeTarget = runeIdentifier;
                }
                if (MasterList.doesElementExist(runeTarget)) {
                    if (runeTarget.equals(runeIdentifier) && masterlist.getAddressLength(runeIdentifier) > 1 && !remote){
                        if (localIndex == masterlist.getAddressLength(runeIdentifier) - 1){
                            index = 0;
                        }
                        else {
                            index = localIndex + 1;
                        }
                        world.setBlockState(pos,getCachedState().cycle(GatewayBlock.PAIRED));
                    }
                    if (remote && runeTarget.equals(runeIdentifier)){
                        world.setBlockState(pos,getCachedState().cycle(GatewayBlock.PAIRED));
                    }
                    countdown = gatewayDuration;
                    if (facing == NORTH || facing == SOUTH) {
                        box = new Box(pos.add(2, -1, 1), pos.add(-2, -5, 0));
                    } else {
                        box = new Box(pos.add(1, -1, 2), pos.add(0, -5, -2));
                    }
                    world.setBlockState(pos, getCachedState().cycle(GatewayBlock.ON));
                    targetPos = new BlockPos(MasterList.getPosition(runeTarget, index));
                    targetWorld = MasterList.getWorld(runeTarget, index);
                    ServerWorld targetWorld2 = ((ServerWorld) world).getServer().getWorld(targetWorld);
                    if (targetWorld2 == null || !(targetWorld2.getBlockEntity(targetPos) instanceof GatewayBlockEntity)) {
                        countdown = 0;
                        masterlist.removeElement(runeTarget,new Vec3d(targetPos.getX(),targetPos.getY(),targetPos.getZ()),targetWorld);
                    }
                    if (!remote) {
                        ((GatewayBlockEntity) targetWorld2.getBlockEntity(targetPos)).activationCheck(true,runeIdentifier,localIndex);
                    }
                    chunkLoaderManager(true);
                    world.playSound(null,pos,SoundEvents.BLOCK_BEACON_ACTIVATE,SoundCategory.AMBIENT,1.0f,0.5f);
                }
                markDirty();
            }
            else if (runeDelta.isEmpty()){
                masterlist.removeElement(runeIdentifier);
            }
        }

    }

    @Override
    public void tick(){
        if (countdown > 0){
            if (world != null && !world.isClient) {
                if (fresh){
                    chunkLoaderManager(true);
                    fresh = false;
                }
                List<Entity> payloadList = world.getNonSpectatingEntities(Entity.class, box);
                Iterator payloadIterator = payloadList.iterator();
                Entity payloadEntity;
                while (payloadIterator.hasNext()) {
                    List<Entity> riders = null;
                    payloadEntity = (Entity) payloadIterator.next();
                    if (payloadEntity instanceof Teleportable && ((Teleportable) payloadEntity).getPortalCoolDown() < 1){
                        ((Teleportable) payloadEntity).setPortalCoolDown(cooldownDuration);
                        TeleportPatch tpHack = TeleportPatch.getInstance();
                        ServerWorld targetWorld2 = ((ServerWorld) world).getServer().getWorld(targetWorld);
                        double oldVelocityFlat = Math.sqrt(payloadEntity.getVelocity().x * payloadEntity.getVelocity().x + payloadEntity.getVelocity().z*payloadEntity.getVelocity().z);
                        double oldVelocityY = payloadEntity.getVelocity().y;
                        BlockState otherSide = targetWorld2.getBlockState(targetPos);
                        Direction otherSideDirection = otherSide.get(Properties.HORIZONTAL_FACING);
                        Vec3d posDelta = new Vec3d(0,pos.getY(),0)
                                .subtract(0,payloadEntity.getPos().y,0)
                                .subtract(otherSideDirection.getOffsetX()*1.5+0.5, 0 ,otherSideDirection.getOffsetZ()*1.5+0.5);
                        if (targetWorld != world.getRegistryKey()) {
                            tpHack.interdimensionalTeleport(payloadEntity, targetWorld2, targetPos.getX() - posDelta.getX(),targetPos.getY() - posDelta.getY(),targetPos.getZ() - posDelta.getZ());
                        }
                        else {
                            if (payloadEntity.hasPlayerRider()){
                                riders = payloadEntity.getPassengerList();
                                for (Entity rider : riders){
                                    rider.method_29239();
                                    if (rider instanceof PlayerEntity){
                                        rider.setSneaking(true);
                                    }
                                }
                            }
                            payloadEntity.teleport(targetPos.getX() - posDelta.getX(), targetPos.getY() - posDelta.getY(), targetPos.getZ() - posDelta.getZ());
                            if (riders != null){
                                for (Entity rider : riders){
                                    rider.teleport(payloadEntity.getX(),payloadEntity.getY(),payloadEntity.getZ());
                                    rider.startRiding(payloadEntity, false);
                                }
                            }
                        }
                        payloadEntity.setVelocity(oldVelocityFlat*otherSideDirection.getOffsetX(),oldVelocityY,oldVelocityFlat*otherSideDirection.getOffsetZ());
                        if (payloadEntity instanceof LivingEntity){
                            payloadEntity.lookAt(EntityAnchorArgumentType.EntityAnchor.FEET,payloadEntity.getPos().add(otherSideDirection.getOffsetX(),0,otherSideDirection.getOffsetZ()));
                        }
                        targetWorld2.playSound(null, targetPos,SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, SoundCategory.AMBIENT,1.0f,1.0f);
                        payloadEntity.playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT,1.0f,1.0f);
                    }
                }
                if (countdown % 80 == 0) {
                    Block block = this.getCachedState().getBlock();
                    if (block instanceof GatewayBlock) {
                        if (!((GatewayBlock) block).GatewayStructureIntact(pos, world.getBlockState(pos), world,null)) {
                            countdown = 1;
                        }
                    }
                    if (!MasterList.doesElementExist(runeTarget)){
                        countdown = 1;
                    }
                    else{
                        ServerWorld targetWorld2 = ((ServerWorld) world).getServer().getWorld(targetWorld);
                        if (!(targetWorld2.getBlockEntity(targetPos) instanceof GatewayBlockEntity)){
                            countdown = 1;
                        }
                    }

                }
            }
            countdown--;
            if(countdown == 0){
                if (getCachedState().get(GatewayBlock.ON)){
                    world.setBlockState(pos, getCachedState().cycle(GatewayBlock.ON));
                }
                if (getCachedState().get(GatewayBlock.ERROR)){
                    world.setBlockState(pos, getCachedState().cycle(GatewayBlock.ERROR));
                }
                if (getCachedState().get(GatewayBlock.PAIRED)){
                    world.setBlockState(pos, getCachedState().cycle(GatewayBlock.PAIRED));
                }
                runeTarget = "";
                chunkLoaderManager(false);
                world.playSound(null,pos,SoundEvents.BLOCK_BEACON_DEACTIVATE,SoundCategory.AMBIENT,1.0f,0.5f);
            }
            if(!world.isClient){
                markDirty();
            }
        }

        if ((world.getTime() % 1200 == 0 || world.getTime() == 0) && !world.isClient){
            String runeDelta = ((GatewayBlock) getCachedState().getBlock()).getGatewayRuneCode(world.getBlockState(pos),pos,world);
            if (runeDelta != null){
                if (!runeDelta.equals(runeIdentifier)){
                    if (masterlist == null){
                        masterlist = MasterList.get(((ServerWorld) world).getServer().getWorld(ServerWorld.OVERWORLD));
                    }
                    if(runeDelta.isEmpty()){
                        masterlist.removeElement(runeIdentifier);
                    }
                    else {
                        masterListChangeAddress();
                    }
                }
            }
            else {
                masterListRemoveAddress();
            }
        }
    }

    private int getIndex(){
        if (masterlist == null){
            masterlist = MasterList.get(((ServerWorld) world).getServer().getWorld(ServerWorld.OVERWORLD));
        }
        return masterlist.getIndex(runeIdentifier,new Vec3d(pos.getX(),pos.getY(),pos.getZ()),world.getRegistryKey());
    }

    private String getTargetRuneCode(){
        StringBuilder total = new StringBuilder();
        for (ItemStack itemStack : inventory) {
            if(!(itemStack.getItem() instanceof AbstractRuneItem)){
                return "";
            }
            total.append(((AbstractRuneItem) itemStack.getItem()).getRuneID());
        }
        return total.toString();
    }


    private void masterListSetAddress(){
        if (masterlist == null){
            masterlist = MasterList.get(((ServerWorld) world).getServer().getWorld(ServerWorld.OVERWORLD));
        }
        runeIdentifier = ((GatewayBlock) getCachedState().getBlock()).getGatewayRuneCode(world.getBlockState(pos),pos,world);
        if(!runeIdentifier.isEmpty()){
            masterlist.addElement(runeIdentifier,new Vec3d(pos.getX(),pos.getY(),pos.getZ()),world.getRegistryKey());
        }
    }

    private void masterListChangeAddress(){
        masterListRemoveAddress();
        masterListSetAddress();
    }

    public void masterListRemoveAddress(){
        if (masterlist == null){
            masterlist = MasterList.get(((ServerWorld) world).getServer().getWorld(ServerWorld.OVERWORLD));
        }
        masterlist.removeElement(runeIdentifier,new Vec3d(pos.getX(),pos.getY(),pos.getZ()),world.getRegistryKey());
    }

    public void chunkLoaderManager(boolean on){
        if (!world.isClient){
            int x = MathHelper.abs(getCachedState().get(Properties.HORIZONTAL_FACING).getOffsetZ());
            int z = MathHelper.abs(getCachedState().get(Properties.HORIZONTAL_FACING).getOffsetX());
            for (int i = -1; i < 2; i++){
                Chunk loadedChunk = world.getChunk((MathHelper.floor(pos.getX()) >> 4)+i*x, (MathHelper.floor(pos.getZ()) >> 4)+i*z);
                ((ServerWorld) world).getChunkManager().setChunkForced(loadedChunk.getPos(),on);
            }
        }
    }
}
