package openlyfay.ancientgateways.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import openlyfay.ancientgateways.util.CustomSkybox;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

import static openlyfay.ancientgateways.recipe.RegisterRecipes.SKYBOX_RECIPE_SERIALIZER;
import static openlyfay.ancientgateways.recipe.RegisterRecipes.SKYBOX_RECIPE_TYPE;

public class CustomSkyboxRecipe implements Recipe<Inventory> {
    private final Identifier id;
    private final Ingredient input;
    private final CustomSkybox skybox;

    public CustomSkyboxRecipe(Identifier id, Ingredient input, CustomSkybox skybox){
        this.id = id;
        this.input = input;
        this.skybox = skybox;
    }

    public CustomSkybox getSkybox() {
        return skybox;
    }

    public boolean matches(ItemStack itemStack){
        return input.test(itemStack);
    }

    @Override
    public boolean matches(Inventory inv, World world) {
        for (int i = 0; i < inv.size();i++){
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
        return SKYBOX_RECIPE_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return SKYBOX_RECIPE_TYPE;
    }

    public static class Serializer implements RecipeSerializer<CustomSkyboxRecipe>{

        @Override
        public CustomSkyboxRecipe read(Identifier id, JsonObject json) {
            Ingredient ingredient = Ingredient.fromJson(json.get("ingredient"));
            CustomSkybox skybox = skyboxFromJson(JsonHelper.getObject(json,"skybox"),id);
            return new CustomSkyboxRecipe(id,ingredient,skybox);
        }

        @Override
        public CustomSkyboxRecipe read(Identifier id, PacketByteBuf buf) {
            Ingredient ingredient = Ingredient.fromPacket(buf);
            DefaultedList<Identifier> textures = DefaultedList.of();
            for (int i = 0; i < 12; i++){
                textures.add(buf.readIdentifier());
            }
            String direction = buf.readString();
            double startingRotation = buf.readDouble();
            double rotationPerTick = buf.readDouble();
            int cloudHeight = buf.readInt();
            DefaultedList<CustomSkybox.DynamicSkyboxObject> dynamicObjects = DefaultedList.of();
            int objects = buf.readInt();
            for (int i = 0;i < objects; i++){
                DefaultedList<Identifier> dynTextures = DefaultedList.of();
                int phases = buf.readInt();
                for (int j = 0;j < phases;j++){
                    dynTextures.add(buf.readIdentifier());
                }
                dynamicObjects.add(new CustomSkybox.DynamicSkyboxObject(dynTextures,buf.readInt(), buf.readInt(), buf.readInt(), buf.readDouble(),buf.readDouble(),buf.readDouble(), buf.readBoolean(), new Color(buf.readInt())));
            }
            CustomSkybox customSkybox = new CustomSkybox(id,textures,direction,startingRotation,rotationPerTick,cloudHeight,dynamicObjects);
            return new CustomSkyboxRecipe(id,ingredient,customSkybox);
        }

        @Override
        public void write(PacketByteBuf buf, CustomSkyboxRecipe recipe) {
            recipe.input.write(buf);
            CustomSkybox skybox = recipe.skybox;
            DefaultedList<Identifier> textures = skybox.getTextures();
            for (Identifier texture : textures) {
                buf.writeIdentifier(texture);
            }
            buf.writeString(skybox.getDirection());
            buf.writeDouble(skybox.getStartingRotation());
            buf.writeDouble(skybox.getRotationPerTick());

            buf.writeInt(skybox.getCloudHeight());

            DefaultedList<CustomSkybox.DynamicSkyboxObject> dynamicObjects = skybox.getDYN();

            buf.writeInt(dynamicObjects.size());
            for (CustomSkybox.DynamicSkyboxObject dynamicObject : dynamicObjects){
                DefaultedList<Identifier> dynTextures = dynamicObject.getTEXTURES();
                buf.writeInt(dynTextures.size());
                for (Identifier identifier : dynTextures){
                    buf.writeIdentifier(identifier);
                }
                buf.writeInt(dynamicObject.getTimePerPhase());
                buf.writeInt(dynamicObject.getSizeX());
                buf.writeInt(dynamicObject.getSizeY());
                buf.writeDouble(dynamicObject.getEquatorAngle());
                buf.writeDouble(dynamicObject.getElevationAngle());
                buf.writeDouble(dynamicObject.getMotionPerTick());
                buf.writeBoolean(dynamicObject.isLightEmitting());
                buf.writeInt(dynamicObject.getSunsetColour().getRGB());
            }
        }

        public static CustomSkybox skyboxFromJson(JsonObject json, Identifier id){
            DefaultedList<Identifier> textures = DefaultedList.of();
            textures.add(new Identifier(JsonHelper.getString(json,"east")));
            textures.add(new Identifier(JsonHelper.getString(json,"west")));
            textures.add(new Identifier(JsonHelper.getString(json,"north")));
            textures.add(new Identifier(JsonHelper.getString(json,"south")));
            textures.add(new Identifier(JsonHelper.getString(json,"up")));
            textures.add(new Identifier(JsonHelper.getString(json,"down")));
            textures.add(new Identifier(JsonHelper.getString(json,"east_night")));
            textures.add(new Identifier(JsonHelper.getString(json,"west_night")));
            textures.add(new Identifier(JsonHelper.getString(json,"north_night")));
            textures.add(new Identifier(JsonHelper.getString(json,"south_night")));
            textures.add(new Identifier(JsonHelper.getString(json,"up_night")));
            textures.add(new Identifier(JsonHelper.getString(json,"down_night")));

            ArrayList<String> validDirections = new ArrayList<String>(Arrays.asList("north-south","south-north","east-west","west-east","clockwise","counter-clockwise"));
            String direction = /* validDirections.contains(JsonHelper.getString(json, "rotation_direction")) ? JsonHelper.getString(json, "rotation_direction") : */ "east-west";
            double startingElevation = 0 /* JsonHelper.getFloat(json,"starting_vertical_rotation") */;
            double rotationPerTick = 0 /* (JsonHelper.getFloat(json, "day_scale")/480000) */;

            int cloudHeight = JsonHelper.getInt(json,"cloud_height");
            DefaultedList<CustomSkybox.DynamicSkyboxObject> dynamicObjects = DefaultedList.of();
            JsonArray jsonArray = JsonHelper.getArray(json,"dynamic_objects");
            for (int i = 0;i < jsonArray.size(); i++){
                dynamicObjects.add(dynamicSkyboxObjectFromJson(jsonArray.get(i).getAsJsonObject()));
            }
            return new CustomSkybox(id,textures,direction,startingElevation,rotationPerTick,cloudHeight,dynamicObjects);
        }

        public static CustomSkybox.DynamicSkyboxObject dynamicSkyboxObjectFromJson(JsonObject json){
            DefaultedList<Identifier> textures = DefaultedList.of();
            JsonArray jsonArray = JsonHelper.getArray(json,"textures");
            for (int i = 0;i < jsonArray.size(); i++){
                textures.add(new Identifier(jsonArray.get(i).getAsString()));
            }
            int timePerPhase = JsonHelper.getInt(json,"ticks_per_phase");
            int sizeX = JsonHelper.getInt(json,"size_x");
            int sizeY = JsonHelper.getInt(json,"size_y");
            double horizontalRotation = JsonHelper.getFloat(json, "horizontal_rotation");
            double startingRotation = JsonHelper.getFloat(json,"starting_vertical_rotation");
            double rotationPerTick = (360/JsonHelper.getFloat(json, "days_per_orbit"))/24000;
            boolean lightEmitting = JsonHelper.getBoolean(json,"emits_light");
            jsonArray = JsonHelper.getArray(json, "sunset_colour");
            Color sunsetColour = new Color(jsonArray.get(0).getAsInt(),jsonArray.get(1).getAsInt(),jsonArray.get(2).getAsInt());
            return new CustomSkybox.DynamicSkyboxObject(textures,timePerPhase,sizeX,sizeY,horizontalRotation,startingRotation,rotationPerTick,lightEmitting,sunsetColour);
        }
    }
}
