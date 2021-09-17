package openlyfay.ancientgateways.block.blockentity;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Hand;

import java.awt.*;

import static openlyfay.ancientgateways.block.RegisterBlocks.ANCHOR_BLOCK_ENTITY;

public class AnchorBaseEntity extends BlockEntity implements BlockEntityClientSerializable {
    private Color grassColour = new Color(11272039);
    private Color waterColour = new Color(3625215);
    private Color leafColour = new Color(5242667);

    public AnchorBaseEntity() {
        super(ANCHOR_BLOCK_ENTITY);
    }

    public boolean onInteract(int type, PlayerEntity player, Hand hand){
        ItemStack stack = player.getStackInHand(hand);
        boolean consumeItem = false;
        switch (type){
            case 0:
                consumeItem = alterRules(stack);
                break;
            case 1:
                if (stack.getItem() instanceof DyeItem){
                    consumeItem = true;
                    alterWater((DyeItem) stack.getItem(), player, hand);
                }
                else {
                    consumeItem = false;
                }
                break;
            case 2:
                if (stack.getItem() instanceof DyeItem){
                    consumeItem = true;
                    alterGrass((DyeItem) stack.getItem(), player, hand);
                }
                else {
                    consumeItem = false;
                }
                break;
            case 3:
                if (stack.getItem() instanceof DyeItem){
                    consumeItem = true;
                    alterLeaves((DyeItem) stack.getItem(), player, hand);
                }
                else {
                    consumeItem = false;
                }
                break;
            case 4:
                consumeItem = alterSky(stack);
                break;
        }
        markDirty();

        return consumeItem;
    }

    private boolean alterRules(ItemStack itemStack){
        return false;
    }

    private void alterWater(DyeItem item, PlayerEntity player, Hand hand){
        float[] colour = item.getColor().getColorComponents();
        Hand hand1;
        if (hand == Hand.MAIN_HAND){
            hand1 = Hand.OFF_HAND;
        }
        else {
            hand1 = Hand.MAIN_HAND;
        }
        if (player.getStackInHand(hand1).getItem() == Items.MILK_BUCKET){
            waterColour = new Color(colour[0],colour[1],colour[2]);
            player.setStackInHand(hand1,new ItemStack(Items.BUCKET,1));
        }
        else {
            waterColour = new Color((colour[0] + waterColour.getRed()) / 2,(colour[1] + waterColour.getGreen()) / 2,(colour[2] + waterColour.getBlue()) / 2);
        }
    }

    private void alterGrass(DyeItem item, PlayerEntity player, Hand hand){
        float[] colour = item.getColor().getColorComponents();
        Hand hand1;
        if (hand == Hand.MAIN_HAND){
            hand1 = Hand.OFF_HAND;
        }
        else {
            hand1 = Hand.MAIN_HAND;
        }
        if (player.getStackInHand(hand1).getItem() == Items.MILK_BUCKET){
            grassColour = new Color(colour[0],colour[1],colour[2]);
            player.setStackInHand(hand1,new ItemStack(Items.BUCKET,1));
        }
        else {
            grassColour = new Color((colour[0] + grassColour.getRed()) / 2,(colour[1] + grassColour.getGreen()) / 2,(colour[2] + grassColour.getBlue()) / 2);
        }
    }

    private void alterLeaves(DyeItem item, PlayerEntity player, Hand hand){
        float[] colour = item.getColor().getColorComponents();
        Hand hand1;
        if (hand == Hand.MAIN_HAND){
            hand1 = Hand.OFF_HAND;
        }
        else {
            hand1 = Hand.MAIN_HAND;
        }
        if (player.getStackInHand(hand1).getItem() == Items.MILK_BUCKET){
            grassColour = new Color(colour[0],colour[1],colour[2]);
            player.setStackInHand(hand1,new ItemStack(Items.BUCKET,1));
        }
        else {
            grassColour = new Color((colour[0] + grassColour.getRed()) / 2,(colour[1] + grassColour.getGreen()) / 2,(colour[2] + grassColour.getBlue()) / 2);
        }
    }

    private boolean alterSky(ItemStack itemStack){
        return false;
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        tag.putInt("water",waterColour.getRGB());
        tag.putInt("grass",grassColour.getRGB());
        tag.putInt("leaves",leafColour.getRGB());
        return super.toTag(tag);
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        waterColour = new Color(tag.getInt("water"));
        grassColour = new Color(tag.getInt("grass"));
        leafColour = new Color(tag.getInt("leaves"));
        super.fromTag(state, tag);
    }


    @Override
    public void fromClientTag(CompoundTag tag) {
        fromTag(getCachedState(),tag);
    }

    @Override
    public CompoundTag toClientTag(CompoundTag tag) {
        return toTag(tag);
    }

    @Override
    public void markDirty() {
        sync();
        super.markDirty();
    }


}
