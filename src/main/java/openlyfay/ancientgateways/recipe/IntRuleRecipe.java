package openlyfay.ancientgateways.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public class IntRuleRecipe implements Recipe<Inventory> {
    private final Identifier id;
    private final Ingredient input;
    private final GameRules.IntRule rule;
    private final GameRules.Key<GameRules.IntRule> key;
    private final int modifier;

    public IntRuleRecipe(Identifier id, Ingredient input, GameRules.IntRule rule, GameRules.Key<GameRules.IntRule> key, int modifier){
        this.id = id;
        this.input = input;
        this.rule = rule;
        this.key = key;
        this.modifier = modifier;
    }

    public GameRules.IntRule getRule() {
        return rule;
    }

    public GameRules.Key<GameRules.IntRule> getKey() {
        return key;
    }

    public int getModifier() {
        return modifier;
    }

    public boolean matches(ItemStack itemStack){
        return input.test(itemStack);
    }

    @Override
    public boolean matches(Inventory inv, World world) {
        for (int i = 0; i < inv.size(); i++){
            if (input.test(inv.getStack(i))){
                return true;
            }
        }
        return false;
    }

    @Override
    public ItemStack craft(Inventory inv) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput() {
        return ItemStack.EMPTY;
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RegisterRecipes.INT_RULE_RECIPE_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return RegisterRecipes.INT_RULE_RECIPE_TYPE;
    }

    public static class Serializer implements RecipeSerializer<IntRuleRecipe>{

        @Override
        public IntRuleRecipe read(Identifier id, JsonObject json) {
            Ingredient ingredient = Ingredient.fromJson(json.get("ingredient"));
            String ruleName = JsonHelper.getString(json,"rule_name");
            GameRules.Category category;
            switch (JsonHelper.getString(json,"category")){
                case "player": category = GameRules.Category.PLAYER;
                    break;
                case "mobs": category = GameRules.Category.MOBS;
                    break;
                case "spawning": category = GameRules.Category.SPAWNING;
                    break;
                case "drops": category = GameRules.Category.DROPS;
                    break;
                case "updates": category = GameRules.Category.UPDATES;
                    break;
                case "chat": category = GameRules.Category.CHAT;
                    break;
                case "misc": category = GameRules.Category.MISC;
                    break;
                default: throw new JsonParseException("integer rule category must be player, mobs, drops, updates, chat or misc");
            }
            GameRules.Key<GameRules.IntRule> newKey = new GameRules.Key<>(ruleName, category);
            GameRules.IntRule newRule = new GameRules().get(newKey);
            if (newRule == null){
                throw new JsonParseException("integer rule name must match an existing, valid integer rule");
            }
            int mod = JsonHelper.getInt(json,"modifier");
            return new IntRuleRecipe(id,ingredient,newRule, newKey,mod);
        }

        @Override
        public IntRuleRecipe read(Identifier id, PacketByteBuf buf) {
            Ingredient ingredient = Ingredient.fromPacket(buf);
            String ruleName = buf.readString(0);
            GameRules.Category category;
            switch (buf.readString(1)) {
                case "player":
                    category = GameRules.Category.PLAYER;
                    break;
                case "mobs":
                    category = GameRules.Category.MOBS;
                    break;
                case "spawning":
                    category = GameRules.Category.SPAWNING;
                    break;
                case "drops":
                    category = GameRules.Category.DROPS;
                    break;
                case "updates":
                    category = GameRules.Category.UPDATES;
                    break;
                case "chat":
                    category = GameRules.Category.CHAT;
                    break;
                default:
                    category = GameRules.Category.MISC;
                    break;
            }
            GameRules.Key<GameRules.IntRule> newKey = new GameRules.Key<>(ruleName, category);
            GameRules.IntRule newRule = new GameRules().get(newKey);
            return new IntRuleRecipe(id,ingredient,newRule,newKey,buf.readInt());
        }

        @Override
        public void write(PacketByteBuf buf, IntRuleRecipe recipe) {
            recipe.input.write(buf);
            buf.writeString(recipe.key.getName(),0);
            buf.writeString(recipe.key.getCategory().toString(),1);
            buf.writeVarInt(recipe.modifier);
        }
    }
}
