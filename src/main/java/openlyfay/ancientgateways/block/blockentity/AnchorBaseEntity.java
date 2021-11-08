package openlyfay.ancientgateways.block.blockentity;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameRules;
import openlyfay.ancientgateways.item.RegisterItem;
import openlyfay.ancientgateways.recipe.BoolRuleRecipe;
import openlyfay.ancientgateways.recipe.CustomSkyboxRecipe;
import openlyfay.ancientgateways.recipe.IntRuleRecipe;
import openlyfay.ancientgateways.recipe.RegisterRecipes;
import openlyfay.ancientgateways.util.CustomSkybox;
import openlyfay.ancientgateways.util.RegHandler;
import openlyfay.ancientgateways.util.mixininterface.GameRulesNBT;
import openlyfay.ancientgateways.util.mixininterface.RuleAccessHelper;
import openlyfay.ancientgateways.util.mixininterface.WorldRendererAccessHelper;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static openlyfay.ancientgateways.block.RegisterBlocks.ANCHOR_BLOCK_ENTITY;

public class AnchorBaseEntity extends BlockEntity implements BlockEntityClientSerializable {
    private Color grassColour = new Color(11272039);
    private Color waterColour = new Color(3625215);
    private Color leafColour = new Color(5242667);
    private final GameRules rules = new GameRules();
    private int expansions = 0;
    private boolean resetRender = false;
    private CustomSkybox skybox;

    public AnchorBaseEntity() {
        super(ANCHOR_BLOCK_ENTITY);
    }

    public Color getGrassColour() {
        return grassColour;
    }

    public Color getWaterColour() {
        return waterColour;
    }

    public Color getLeafColour() {
        return leafColour;
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
                    resetRender = true;
                }
                break;
            case 2:
                if (stack.getItem() instanceof DyeItem){
                    consumeItem = true;
                    alterGrass((DyeItem) stack.getItem(), player, hand);
                    resetRender = true;
                }
                break;
            case 3:
                if (stack.getItem() instanceof DyeItem){
                    consumeItem = true;
                    alterLeaves((DyeItem) stack.getItem(), player, hand);
                    resetRender = true;
                }
                break;
            case 4:
                consumeItem = alterSky(stack, player, hand);
                break;
        }
        markDirty();

        return consumeItem;
    }

    public CustomSkybox getSkybox() {
        return skybox;
    }

    private boolean alterRules(ItemStack itemStack){
        if (itemStack.getItem().equals(RegisterItem.WORLD_EGG)){

        }
        /*
        BoolRuleRecipe recipe = world.getRecipeManager().listAllOfType(RegisterRecipes.BOOL_RULE_RECIPE_TYPE).stream().filter(boolRuleRecipe -> boolRuleRecipe.matches(itemStack)).findFirst().orElse(null);
        if (recipe != null){
            ((RuleAccessHelper) rules.get(recipe.getKey())).flipValue();
            return true;
        }
        IntRuleRecipe recipe1 = world.getRecipeManager().listAllOfType(RegisterRecipes.INT_RULE_RECIPE_TYPE).stream().filter(intRuleRecipe -> intRuleRecipe.matches(itemStack)).findFirst().orElse(null);
        if (recipe1 != null){
            if (recipe1.getModifier() == 0){
                ((RuleAccessHelper) rules.get(recipe1.getKey())).setValue(Integer.toString(world.getGameRules().getInt(recipe1.getKey())));
            }
            else{
                ((RuleAccessHelper) rules.get(recipe1.getKey())).setValue(Integer.toString(recipe1.getModifier()));
            }
            return true;
        }
        //We were on the verge of greatness, we were this close.
         */

        return false;
    }

    private void alterWater(DyeItem item, PlayerEntity player, Hand hand){
        waterColour = modifyColour(item,player,hand,waterColour);
    }

    private void alterGrass(DyeItem item, PlayerEntity player, Hand hand){
        grassColour = modifyColour(item,player,hand,grassColour);
    }

    private void alterLeaves(DyeItem item, PlayerEntity player, Hand hand){
        leafColour = modifyColour(item,player,hand,leafColour);
    }

    private boolean alterSky(ItemStack itemStack, PlayerEntity player, Hand hand){
        CustomSkyboxRecipe skyboxRecipe = world.getRecipeManager().listAllOfType(RegisterRecipes.SKYBOX_RECIPE_TYPE).stream().filter(customSkyboxRecipe -> customSkyboxRecipe.matches(itemStack)).findFirst().orElse(null);
        if (skyboxRecipe != null){
            skybox = skyboxRecipe.getSkybox();
            return true;
        }
        if (itemStack.getItem() == Items.MILK_BUCKET){
            Hand hand1;
            if (hand == Hand.MAIN_HAND){
                hand1 = Hand.OFF_HAND;
            }
            else {
                hand1 = Hand.MAIN_HAND;
            }
            if (player.getStackInHand(hand1).getItem() == Items.MILK_BUCKET) {
                player.setStackInHand(hand1, new ItemStack(Items.BUCKET, 1));
            }
            skybox = null;
        }
        resetRender = true;
        return false;
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        tag.putInt("water",waterColour.getRGB());
        tag.putInt("grass",grassColour.getRGB());
        tag.putInt("leaves",leafColour.getRGB());
        tag.putInt("expansions",expansions);
        tag.put("rules", rules.toNbt());
        if (skybox != null) {
            CompoundTag compoundTag = new CompoundTag();
            tag.put("skybox", skybox.toNbt(compoundTag));
        }
        tag.putInt("expansions",expansions);
        return super.toTag(tag);
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        waterColour = new Color(tag.getInt("water"));
        grassColour = new Color(tag.getInt("grass"));
        leafColour = new Color(tag.getInt("leaves"));
        expansions = tag.getInt("expansions");
        ((GameRulesNBT) rules).fromNBT(tag.getCompound("rules"));
        if (tag.contains("skybox")) {
            skybox = CustomSkybox.fromNBT(tag.getCompound("skybox"));
        }
        expansions = tag.getInt("expansions");
        super.fromTag(state, tag);
    }


    @Override
    public void fromClientTag(CompoundTag tag) {
        fromTag(getCachedState(),tag);
        resetRender = tag.getBoolean("reset");
        if (world.isClient && resetRender){
            ((WorldRendererAccessHelper)world).getWorldRenderer().reload();
            resetRender = false;
        }
    }

    @Override
    public CompoundTag toClientTag(CompoundTag tag) {
        tag.putBoolean("reset",resetRender);
        resetRender = false;
        return toTag(tag);
    }

    @Override
    public void markDirty() {
        sync();
        super.markDirty();
    }

    private Color modifyColour(DyeItem item, PlayerEntity player, Hand hand, Color colour){
        float[] colour1 = item.getColor().getColorComponents();
        Hand hand1;
        if (hand == Hand.MAIN_HAND){
            hand1 = Hand.OFF_HAND;
        }
        else {
            hand1 = Hand.MAIN_HAND;
        }
        if (player.getStackInHand(hand1).getItem() == Items.MILK_BUCKET){
            player.setStackInHand(hand1,new ItemStack(Items.BUCKET,1));
            return new Color(colour1[0],colour1[1],colour1[2]);
        }
        else {
            double r = Math.sqrt((((colour1[0] * 255)*(colour1[0] * 255)) + (colour.getRed() * colour.getRed()))/2);
            double g = Math.sqrt((((colour1[1] * 255)*(colour1[1] * 255)) + (colour.getGreen() * colour.getGreen()))/2);
            double b = Math.sqrt((((colour1[2] * 255)*(colour1[2] * 255)) + (colour.getBlue() * colour.getBlue()))/2);
            return new Color((int) r, (int) g, (int) b);
        }
    }

}