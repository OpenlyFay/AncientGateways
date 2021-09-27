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


public class BoolRuleRecipe implements Recipe<Inventory> {
    private final Identifier id;
    private final Ingredient input;
    private final GameRules.BooleanRule rule;
    private final GameRules.Key<GameRules.BooleanRule> key;

    public BoolRuleRecipe(Identifier identifier, Ingredient input, GameRules.BooleanRule rule, GameRules.Key<GameRules.BooleanRule> key){
        this.id = identifier;
        this.input = input;
        this.rule = rule;
        this.key = key;
    }

    public GameRules.BooleanRule getRule() {
        return rule;
    }

    public GameRules.Key<GameRules.BooleanRule> getKey(){
        return key;
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
        return RegisterRecipes.BOOL_RULE_RECIPE_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return RegisterRecipes.BOOL_RULE_RECIPE_TYPE;
    }

    public static class Serializer implements RecipeSerializer<BoolRuleRecipe>{

        @Override
        public BoolRuleRecipe read(Identifier id, JsonObject json) {
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
                default: throw new JsonParseException("boolean rule category must be player, mobs, drops, updates, chat or misc");
            }
            GameRules.Key<GameRules.BooleanRule> newKey = new GameRules.Key<>(ruleName, category);
            GameRules.BooleanRule newRule = new GameRules().get(newKey);
            if (newRule == null){
                throw new JsonParseException("boolean rule name must match an existing, valid boolean rule");
            }
            return new BoolRuleRecipe(id,ingredient,newRule,newKey);
        }

        @Override
        public BoolRuleRecipe read(Identifier id, PacketByteBuf buf) {
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
            GameRules.Key<GameRules.BooleanRule> newKey = new GameRules.Key<>(ruleName, category);
            GameRules.BooleanRule newRule = new GameRules().get(newKey);
            return new BoolRuleRecipe(id,ingredient,newRule,newKey);
        }

        @Override
        public void write(PacketByteBuf buf, BoolRuleRecipe recipe) {
            recipe.input.write(buf);
            buf.writeString(recipe.key.getName(),0);
            buf.writeString(recipe.key.getCategory().toString(),1);
        }
    }
}
