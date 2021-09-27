package openlyfay.ancientgateways.recipe;

import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static openlyfay.ancientgateways.AncientGateways.MOD_ID;

public class RegisterRecipes {

    public static final RecipeType<BoolRuleRecipe> BOOL_RULE_RECIPE_TYPE = new RecipeType<BoolRuleRecipe>() {
        @Override
        public String toString() {return "boolean_rule_recipe";}
    };
    public static final RecipeSerializer<BoolRuleRecipe> BOOL_RULE_RECIPE_SERIALIZER = new BoolRuleRecipe.Serializer();

    public static final RecipeType<IntRuleRecipe> INT_RULE_RECIPE_TYPE = new RecipeType<IntRuleRecipe>() {
        @Override
        public String toString() {return "int_rule_recipe";}};
    public static final RecipeSerializer<IntRuleRecipe> INT_RULE_RECIPE_SERIALIZER = new IntRuleRecipe.Serializer();

    public static void register(){
        Registry.register(Registry.RECIPE_SERIALIZER,new Identifier(MOD_ID,"boolean_rule_recipe"),BOOL_RULE_RECIPE_SERIALIZER);
        Registry.register(Registry.RECIPE_SERIALIZER,new Identifier(MOD_ID,"int_rule_recipe"),INT_RULE_RECIPE_SERIALIZER);

        Registry.register(Registry.RECIPE_TYPE,new Identifier(MOD_ID,"boolean_rule_recipe"),BOOL_RULE_RECIPE_TYPE);
        Registry.register(Registry.RECIPE_TYPE,new Identifier(MOD_ID,"int_rule_recipe"),INT_RULE_RECIPE_TYPE);
    }
}
