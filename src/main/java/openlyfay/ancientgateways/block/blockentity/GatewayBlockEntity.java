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
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.*;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.*;
import net.minecraft.world.chunk.Chunk;
import openlyfay.ancientgateways.AncientGateways;
import openlyfay.ancientgateways.block.GatewayBlock;
import openlyfay.ancientgateways.block.blockitem.AbstractRuneItem;
import openlyfay.ancientgateways.util.AGComponents;
import openlyfay.ancientgateways.util.MasterList;
import openlyfay.ancientgateways.util.MasterListComponent;
import openlyfay.ancientgateways.util.TeleportPatch;
import openlyfay.ancientgateways.util.mixininterface.Teleportable;

import java.util.*;

import static net.minecraft.util.math.Direction.NORTH;
import static net.minecraft.util.math.Direction.SOUTH;
import static openlyfay.ancientgateways.block.RegisterBlocks.*;


public class GatewayBlockEntity extends BlockEntity implements Inventory, BlockEntityClientSerializable {
    private String runeIdentifier = "";
    private String runeTarget = "";
    private BlockPos targetPos;
    private RegistryKey<World> targetWorld;
    private int countdown;
    private Box box = new Box(pos.add(1,-1,2), pos.add(0,-5,-2));
    private DefaultedList<ItemStack> inventory;
    private boolean fresh;
    private final static int cooldownDuration = AncientGateways.agConfig.gatewayCooldown;
    private final static int gatewayDuration = AncientGateways.agConfig.gatewayActivationTime;

    public GatewayBlockEntity(BlockPos pos, BlockState state){
        super(GATEWAY_BLOCK_ENTITY, pos, state);
        countdown = 0;
        inventory = DefaultedList.ofSize(6,ItemStack.EMPTY);
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
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        countdown = nbt.getInt("countdown");
        runeIdentifier = nbt.getString("runeID");
        runeTarget = nbt.getString("targetID");
        targetWorld = RegistryKey.of(Registry.WORLD_KEY, new Identifier(nbt.getString("targetWorld")));
        targetPos = new BlockPos(nbt.getDouble("targetx"),nbt.getDouble("targety"),nbt.getDouble("targetz"));
        Inventories.readNbt(nbt, this.inventory);
        fresh = true;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, this.inventory);
        nbt.putInt("countdown", countdown);
        nbt.putString("runeID",runeIdentifier);
        nbt.putString("targetID",runeTarget);
        nbt.putString("targetWorld", targetWorld.getValue().toString());
        nbt.putDouble("targetx",targetPos.getX());
        nbt.putDouble("targety",targetPos.getY());
        nbt.putDouble("targetz",targetPos.getZ());
        return super.writeNbt(nbt);
    }

    @Override
    public void fromClientTag(NbtCompound tag) {
        readNbt(tag);
    }

    @Override
    public NbtCompound toClientTag(NbtCompound tag) {
        return writeNbt(tag);
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
                if (AGComponents.doesElementExist(runeTarget,world)) {
                    if (runeTarget.equals(runeIdentifier) && !remote){
                        if (AGComponents.getAddressLength(runeIdentifier,world) > 1){
                            if (localIndex == AGComponents.getAddressLength(runeIdentifier,world) - 1){
                                index = 0;
                            }
                            else {
                                index = localIndex + 1;
                            }
                            world.setBlockState(pos,getCachedState().cycle(GatewayBlock.PAIRED));
                        }
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
                    targetPos = new BlockPos(AGComponents.getPosition(runeTarget, index,world));
                    targetWorld = AGComponents.getWorld(runeTarget, index,world);
                    ServerWorld targetWorld2 = ((ServerWorld) world).getServer().getWorld(targetWorld);
                    if (targetWorld2 == null ||
                            !(targetWorld2.getBlockEntity(targetPos) instanceof GatewayBlockEntity)
                            || !((GatewayBlock) targetWorld2.getBlockState(targetPos).getBlock()).GatewayStructureIntact(targetPos,targetWorld2.getBlockState(targetPos),targetWorld2,null)
                            || !((GatewayBlock) targetWorld2.getBlockState(targetPos).getBlock()).getGatewayRuneCode(targetWorld2.getBlockState(targetPos), targetPos, targetWorld2).equals(runeTarget)) {
                        countdown = 0;
                        AGComponents.removeElement(runeTarget,new Vec3d(targetPos.getX(),targetPos.getY(),targetPos.getZ()),targetWorld,world);
                        gatewayShutdown();
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
                AGComponents.removeElement(runeIdentifier,world);
            }
        }

    }

    public static void tick(World world, BlockPos pos, BlockState state, GatewayBlockEntity entity){
        if (entity.countdown > 0){
            if (world != null && !world.isClient) {
                if (entity.fresh){
                    entity.chunkLoaderManager(true);
                    Direction facing = entity.getCachedState().get(Properties.HORIZONTAL_FACING);
                    if (facing == NORTH || facing == SOUTH) {
                        entity.box = new Box(pos.add(2, -1, 1), pos.add(-2, -5, 0));
                    } else {
                        entity.box = new Box(pos.add(1, -1, 2), pos.add(0, -5, -2));
                    }
                    entity.fresh = false;
                }
                List<Entity> payloadList = world.getNonSpectatingEntities(Entity.class, entity.box);
                Iterator payloadIterator = payloadList.iterator();
                Entity payloadEntity;
                while (payloadIterator.hasNext()) {
                    List<Entity> riders = null;
                    payloadEntity = (Entity) payloadIterator.next();
                    if (payloadEntity instanceof Teleportable && ((Teleportable) payloadEntity).getPortalCoolDown() < 1){
                        ((Teleportable) payloadEntity).setPortalCoolDown(cooldownDuration);
                        TeleportPatch tpHack = TeleportPatch.getInstance();
                        ServerWorld targetWorld2 = ((ServerWorld) world).getServer().getWorld(entity.targetWorld);
                        double oldVelocityFlat = Math.sqrt(payloadEntity.getVelocity().x * payloadEntity.getVelocity().x + payloadEntity.getVelocity().z*payloadEntity.getVelocity().z);
                        double oldVelocityY = payloadEntity.getVelocity().y;
                        BlockState otherSide = targetWorld2.getBlockState(entity.targetPos);
                        Direction otherSideDirection = otherSide.get(Properties.HORIZONTAL_FACING);
                        Vec3d posDelta = new Vec3d(0,pos.getY(),0)
                                .subtract(0,payloadEntity.getPos().y,0)
                                .subtract(otherSideDirection.getOffsetX()*1.5+0.5, 0 ,otherSideDirection.getOffsetZ()*1.5+0.5);
                        if (entity.targetWorld != world.getRegistryKey()) {
                            tpHack.interdimensionalTeleport(payloadEntity, targetWorld2,  entity.targetPos.getX() - posDelta.getX(),entity.targetPos.getY() - posDelta.getY(),entity.targetPos.getZ() - posDelta.getZ());
                        }
                        else {
                            riders = payloadEntity.getPassengerList();
                            for (Entity rider : riders){
                                rider.stopRiding();
                                if (rider instanceof PlayerEntity){
                                    rider.setSneaking(true);
                                }
                            }
                            payloadEntity.teleport(entity.targetPos.getX() - posDelta.x,entity.targetPos.getY() - posDelta.y,entity.targetPos.getZ() - posDelta.z);
                            for (Entity rider : riders){
                                rider.teleport(payloadEntity.getX(),payloadEntity.getY(),payloadEntity.getZ());
                                rider.startRiding(payloadEntity, false);
                            }
                        }
                        payloadEntity.setVelocity(oldVelocityFlat*otherSideDirection.getOffsetX(),oldVelocityY,oldVelocityFlat*otherSideDirection.getOffsetZ());
                        if (payloadEntity instanceof LivingEntity){
                            payloadEntity.lookAt(EntityAnchorArgumentType.EntityAnchor.FEET,payloadEntity.getPos().add(otherSideDirection.getOffsetX(),0,otherSideDirection.getOffsetZ()));
                        }
                        targetWorld2.playSound(null, entity.targetPos,SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, SoundCategory.AMBIENT,1.0f,1.0f);
                        payloadEntity.playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT,1.0f,1.0f);
                    }
                }
                if (entity.countdown % 80 == 0) {
                    Block block = entity.getCachedState().getBlock();
                    if (block instanceof GatewayBlock) {
                        if (!((GatewayBlock) block).GatewayStructureIntact(pos, world.getBlockState(pos), world,null)) {
                            entity.countdown = 1;
                        }
                    }
                    if (!AGComponents.doesElementExist(entity.runeTarget,world)){
                        entity.countdown = 1;
                    }
                    else{
                        ServerWorld targetWorld2 = ((ServerWorld) world).getServer().getWorld(entity.targetWorld);
                        if (!(targetWorld2.getBlockEntity(entity.targetPos) instanceof GatewayBlockEntity)){
                            entity.countdown = 1;
                        }
                    }

                }
            }
            entity.countdown--;
            if(entity.countdown == 0){
                entity.gatewayShutdown();
            }
            if(!world.isClient){
                entity.markDirty();
            }
        }

        if ((world.getTime() % 1200 == 0 || world.getTime() == 0) && !world.isClient){
            String runeDelta = ((GatewayBlock) entity.getCachedState().getBlock()).getGatewayRuneCode(world.getBlockState(pos),pos,world);
            if (runeDelta != null){
                if (!runeDelta.equals(entity.runeIdentifier)){
                    if(runeDelta.isEmpty()){
                        AGComponents.removeElement(entity.runeIdentifier,world);
                    }
                    else {
                        entity.masterListChangeAddress();
                    }
                }
            }
            else {
                entity.masterListRemoveAddress();
            }
        }
    }

    private int getIndex(){
        return AGComponents.getIndex(runeIdentifier,new Vec3d(pos.getX(),pos.getY(),pos.getZ()),world.getRegistryKey(),world);
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
        runeIdentifier = ((GatewayBlock) getCachedState().getBlock()).getGatewayRuneCode(world.getBlockState(pos),pos,world);
        if(!runeIdentifier.isEmpty()){
            AGComponents.addElement(runeIdentifier,new Vec3d(pos.getX(),pos.getY(),pos.getZ()),world.getRegistryKey(),world);
        }
    }

    private void masterListChangeAddress(){
        masterListRemoveAddress();
        masterListSetAddress();
    }

    public void masterListRemoveAddress(){
        AGComponents.removeElement(runeIdentifier,new Vec3d(pos.getX(),pos.getY(),pos.getZ()),world.getRegistryKey(),world);
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

    private void gatewayShutdown(){
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

}
