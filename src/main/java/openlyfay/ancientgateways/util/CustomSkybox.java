package openlyfay.ancientgateways.util;

import net.minecraft.client.util.math.Vector3f;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Quaternion;

import java.awt.*;
import java.util.ArrayList;

public class CustomSkybox {
    private final Identifier ID;
    private final Identifier EAST;
    private final Identifier WEST;
    private final Identifier NORTH;
    private final Identifier SOUTH;
    private final Identifier UP;
    private final Identifier DOWN;
    private final Identifier EAST_N;
    private final Identifier WEST_N;
    private final Identifier NORTH_N;
    private final Identifier SOUTH_N;
    private final Identifier UP_N;
    private final Identifier DOWN_N;
    private final String direction;
    private final double startingRotation;
    private final double rotationPerTick;
    private final int cloudHeight;

    private final DefaultedList<DynamicSkyboxObject> DYN;

    public CustomSkybox(Identifier ID, DefaultedList<Identifier> textures, String direction, double startingRotation, double rotationPerTick, int cloudHeight, DefaultedList<DynamicSkyboxObject> dynamicSkyboxObjects){
        this.ID = ID;
        EAST = textures.get(0);
        WEST = textures.get(1);
        NORTH = textures.get(2);
        SOUTH = textures.get(3);
        UP = textures.get(4);
        DOWN = textures.get(5);

        EAST_N = textures.get(6);
        WEST_N = textures.get(7);
        NORTH_N = textures.get(8);
        SOUTH_N = textures.get(9);
        UP_N = textures.get(10);
        DOWN_N = textures.get(11);

        this.direction = direction;
        this.startingRotation = startingRotation;
        this.rotationPerTick = rotationPerTick;

        this.cloudHeight = cloudHeight;
        DYN = dynamicSkyboxObjects;
    }

    public double getCurrentRotation(float ticks){
        return startingRotation + rotationPerTick*ticks;
    }

    public Identifier getID() {
        return ID;
    }

    public Identifier getEast() {
        return EAST;
    }

    public Identifier getWest() {
        return WEST;
    }

    public Identifier getNorth() {
        return NORTH;
    }

    public Identifier getSouth() {
        return SOUTH;
    }

    public Identifier getUp() {
        return UP;
    }

    public Identifier getDown() {
        return DOWN;
    }

    public Identifier getEastNight() {
        return EAST_N;
    }

    public Identifier getWestNight() {
        return WEST_N;
    }

    public Identifier getNorthNight() {
        return NORTH_N;
    }

    public Identifier getSouthNight() {
        return SOUTH_N;
    }

    public Identifier getUpNight() {
        return UP_N;
    }

    public Identifier getDownNight() {
        return DOWN_N;
    }

    public DefaultedList<Identifier> getTextures(){
        DefaultedList<Identifier> textures = DefaultedList.of();
        textures.add(EAST);
        textures.add(WEST);
        textures.add(NORTH);
        textures.add(SOUTH);
        textures.add(UP);
        textures.add(DOWN);
        textures.add(EAST_N);
        textures.add(WEST_N);
        textures.add(NORTH_N);
        textures.add(SOUTH_N);
        textures.add(UP_N);
        textures.add(DOWN_N);
        return textures;
    }

    public String getDirection() {
        return direction;
    }

    public double getStartingRotation() {
        return startingRotation;
    }

    public double getRotationPerTick() {
        return rotationPerTick;
    }

    public int getCloudHeight() {
        return cloudHeight;
    }

    public boolean isDay(float ticks){
        for (DynamicSkyboxObject dynamicSkyboxObject : DYN) {
            if (dynamicSkyboxObject.lightEmitting) {
                double angle = dynamicSkyboxObject.getOrbitPosition(ticks);
                if (angle > 90 && angle < 255) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isDawn (float ticks){
        boolean dawn = false;
        for (DynamicSkyboxObject dynamicSkyboxObject : DYN) {
            if (dynamicSkyboxObject.lightEmitting) {
                double angle = dynamicSkyboxObject.getOrbitPosition(ticks);
                if (angle > 105 && angle < 255) {
                    return false;
                }
                else if (angle > 90 && angle < 105 && !dawn){
                    dawn = true;
                }
            }
        }
        return dawn;
    }

    public boolean isDusk (float ticks){
        boolean dusk = false;
        for (DynamicSkyboxObject dynamicSkyboxObject : DYN) {
            if (dynamicSkyboxObject.lightEmitting) {
                double angle = dynamicSkyboxObject.getOrbitPosition(ticks);
                if (angle > 105 && angle < 255) {
                    return false;
                }
                else if (angle > 255 && angle < 270 && !dusk){
                    dusk = true;
                }
            }
        }
        return dusk;
    }

    public DefaultedList<DynamicSkyboxObject> getHorizonObjects (float ticks){
        DefaultedList<DynamicSkyboxObject> horizonObjects = DefaultedList.of();
        for (DynamicSkyboxObject dynamicSkyboxObject : DYN){
            if (dynamicSkyboxObject.lightEmitting){
                double angle = dynamicSkyboxObject.getOrbitPosition(ticks);
                if ((angle > 90 && angle < 105) || (angle > 255 && angle < 270)){
                    horizonObjects.add(dynamicSkyboxObject);
                }
            }
        }
        return horizonObjects;
    }

    public float getDawnProgress (float ticks){
        DefaultedList<DynamicSkyboxObject> horizonObjects = getHorizonObjects(ticks);
        int i = 0;
        float f = 0.0f;
        for (DynamicSkyboxObject dynamicSkyboxObject : horizonObjects){
            i++;
            f += (1 - ((float) dynamicSkyboxObject.getOrbitPosition(ticks) - 90) / 15);
        }
        return f/i;
    }

    public float getDuskProgress (float ticks){
        DefaultedList<DynamicSkyboxObject> horizonObjects = getHorizonObjects(ticks);
        int i = 0;
        float f = 0.0f;
        for (DynamicSkyboxObject dynamicSkyboxObject : horizonObjects){
            i++;
            f += ((float) dynamicSkyboxObject.getOrbitPosition(ticks) - 270)/-15;
        }
        return f/i;
    }

    public ArrayList<Quaternion> getRotation(float ticks){
        //unused for now
        ArrayList<Quaternion> values = new ArrayList<>();
        float degrees = (float) getCurrentRotation(ticks);
        switch (direction){
            case "clockwise" :
                values.add(Vector3f.POSITIVE_Y.getDegreesQuaternion(degrees)); //down
                values.add(Vector3f.POSITIVE_X.getDegreesQuaternion(degrees)); //west
                values.add(Vector3f.POSITIVE_X.getDegreesQuaternion(degrees)); //east
                values.add(Vector3f.NEGATIVE_Y.getDegreesQuaternion(degrees)); //up
                values.add(Vector3f.POSITIVE_X.getDegreesQuaternion(degrees)); //north
                values.add(Vector3f.POSITIVE_X.getDegreesQuaternion(degrees)); //south
                break;
            case "counter-clockwise" :
                values.add(Vector3f.NEGATIVE_Y.getDegreesQuaternion(degrees)); //down
                values.add(Vector3f.NEGATIVE_X.getDegreesQuaternion(degrees)); //west
                values.add(Vector3f.NEGATIVE_X.getDegreesQuaternion(degrees)); //east
                values.add(Vector3f.POSITIVE_Y.getDegreesQuaternion(degrees)); //up
                values.add(Vector3f.NEGATIVE_X.getDegreesQuaternion(degrees)); //north
                values.add(Vector3f.NEGATIVE_X.getDegreesQuaternion(degrees)); //south
                break;
            case "north-south" :
                values.add(Vector3f.POSITIVE_Z.getDegreesQuaternion(degrees)); //down
                values.add(Vector3f.POSITIVE_Y.getDegreesQuaternion(degrees)); //west
                values.add(Vector3f.NEGATIVE_Y.getDegreesQuaternion(degrees)); //east
                values.add(Vector3f.POSITIVE_Z.getDegreesQuaternion(degrees)); //up
                values.add(Vector3f.POSITIVE_Z.getDegreesQuaternion(degrees)); //north
                values.add(Vector3f.POSITIVE_Z.getDegreesQuaternion(degrees)); //south
                break;
            case "south-north" :
                values.add(Vector3f.NEGATIVE_Z.getDegreesQuaternion(degrees)); //down
                values.add(Vector3f.NEGATIVE_Y.getDegreesQuaternion(degrees)); //west
                values.add(Vector3f.POSITIVE_Y.getDegreesQuaternion(degrees)); //east
                values.add(Vector3f.NEGATIVE_Z.getDegreesQuaternion(degrees)); //up
                values.add(Vector3f.NEGATIVE_Z.getDegreesQuaternion(degrees)); //north
                values.add(Vector3f.NEGATIVE_Z.getDegreesQuaternion(degrees)); //south
                break;
            case "west-east" :
                values.add(Vector3f.NEGATIVE_X.getDegreesQuaternion(degrees)); //down
                values.add(Vector3f.NEGATIVE_Z.getDegreesQuaternion(degrees)); //west
                values.add(Vector3f.NEGATIVE_Z.getDegreesQuaternion(degrees)); //east
                values.add(Vector3f.NEGATIVE_X.getDegreesQuaternion(degrees)); //up
                values.add(Vector3f.NEGATIVE_Y.getDegreesQuaternion(degrees)); //north
                values.add(Vector3f.POSITIVE_Y.getDegreesQuaternion(degrees)); //south
                break;
            default:
                values.add(Vector3f.POSITIVE_X.getDegreesQuaternion(degrees)); //down
                values.add(Vector3f.POSITIVE_Z.getDegreesQuaternion(degrees)); //west
                values.add(Vector3f.POSITIVE_Z.getDegreesQuaternion(degrees)); //east
                values.add(Vector3f.POSITIVE_X.getDegreesQuaternion(degrees)); //up
                values.add(Vector3f.POSITIVE_Y.getDegreesQuaternion(degrees)); //north
                values.add(Vector3f.NEGATIVE_Y.getDegreesQuaternion(degrees)); //south
                break;
        }
        return values;
    }

    public DefaultedList<DynamicSkyboxObject> getDYN() {
        return DYN;
    }

    public CompoundTag toNbt (CompoundTag tag){
        tag.putString("id",ID.toString());
        tag.putString("east",EAST.toString());
        tag.putString("west",WEST.toString());
        tag.putString("north",NORTH.toString());
        tag.putString("south",SOUTH.toString());
        tag.putString("up",UP.toString());
        tag.putString("down",DOWN.toString());

        tag.putString("eastNight",EAST_N.toString());
        tag.putString("westNight",WEST_N.toString());
        tag.putString("northNight",NORTH_N.toString());
        tag.putString("southNight",SOUTH_N.toString());
        tag.putString("upNight",UP_N.toString());
        tag.putString("downNight",DOWN_N.toString());

        tag.putString("direction",direction);
        tag.putDouble("startingVerticalRotation", startingRotation);
        tag.putDouble("rotationPerTick",rotationPerTick);

        tag.putInt("cloudHeight", cloudHeight);
        ListTag listTag = new ListTag();
        for (DynamicSkyboxObject object : DYN){
            CompoundTag compoundTag = new CompoundTag();
            listTag.add(object.toNbt(compoundTag));
        }
        if (!listTag.isEmpty()){
            tag.put("objects",listTag);
        }
        return tag;
    }

    public static CustomSkybox fromNBT(CompoundTag tag){
        DefaultedList<Identifier> textures = DefaultedList.of();
        textures.add(new Identifier(tag.getString("east")));
        textures.add(new Identifier(tag.getString("west")));
        textures.add(new Identifier(tag.getString("north")));
        textures.add(new Identifier(tag.getString("south")));
        textures.add(new Identifier(tag.getString("up")));
        textures.add(new Identifier(tag.getString("down")));

        textures.add(new Identifier(tag.getString("eastNight")));
        textures.add(new Identifier(tag.getString("westNight")));
        textures.add(new Identifier(tag.getString("northNight")));
        textures.add(new Identifier(tag.getString("southNight")));
        textures.add(new Identifier(tag.getString("upNight")));
        textures.add(new Identifier(tag.getString("downNight")));

        DefaultedList<DynamicSkyboxObject> objects = DefaultedList.of();
        ListTag listTag = tag.getList("objects", 10);
        for (int i = 0; i < listTag.size(); i++){
            CompoundTag compoundTag = listTag.getCompound(i);
            objects.add(DynamicSkyboxObject.fromNBT(compoundTag));
        }
        return new CustomSkybox(new Identifier(
                tag.getString("id")),
                textures,
                tag.getString("direction"),
                tag.getDouble("startingVerticalRotation"),
                tag.getDouble("rotationPerTick"),
                tag.getInt("cloudHeight"),
                objects);

    }

    public static class DynamicSkyboxObject{
        private final DefaultedList<Identifier> TEXTURES;
        private final int timePerPhase;
        private final int sizeX;
        private final int sizeY;
        private final double equatorAngle;
        private final double elevationAngle;
        private final double motionPerTick;
        private final boolean lightEmitting;
        private final Color sunsetColour;

        public DynamicSkyboxObject(DefaultedList<Identifier> TEXTURES, int timePerPhase, int sizeX, int sizeY, double equatorAngle, double elevationAngle, double motionPerTick, boolean lightEmitting, Color sunsetColour){
            this.TEXTURES = TEXTURES;
            this.timePerPhase = timePerPhase;
            this.sizeX = sizeX;
            this.sizeY = sizeY;
            this.equatorAngle = equatorAngle;
            this.elevationAngle = elevationAngle;
            this.motionPerTick = motionPerTick;
            this.lightEmitting = lightEmitting;
            this.sunsetColour = sunsetColour;
        }

        public double getOrbitPosition(float ticks){
             return (elevationAngle + motionPerTick*ticks) % 360;
        }

        public Identifier getCurrentTexture(float ticks){
            if (TEXTURES.size() == 1){
                return TEXTURES.get(0);
            }
            int phase = Math.floorDiv((int) ticks,timePerPhase) % TEXTURES.size();
            return TEXTURES.get(phase);
        }

        public DefaultedList<Identifier> getTEXTURES() {
            return TEXTURES;
        }

        public int getTimePerPhase() {
            return timePerPhase;
        }

        public int getSizeX() {
            return sizeX;
        }

        public int getSizeY() {
            return sizeY;
        }

        public double getElevationAngle() {
            return elevationAngle;
        }

        public double getEquatorAngle() {
            return equatorAngle;
        }

        public double getMotionPerTick() {
            return motionPerTick;
        }

        public boolean isLightEmitting() {
            return lightEmitting;
        }

        public Color getSunsetColour() {
            return sunsetColour;
        }

        public CompoundTag toNbt(CompoundTag tag){
            tag.putInt("textureNum", TEXTURES.size());
            for (int i = 0; i < TEXTURES.size();i++){
                tag.putString("texture" + i,TEXTURES.get(i).toString());
            }
            tag.putInt("timePerPhase",timePerPhase);
            tag.putInt("sizeX",sizeX);
            tag.putInt("sizeY",sizeY);
            tag.putDouble("equatorAngle",equatorAngle);
            tag.putDouble("elevationAngle",elevationAngle);
            tag.putDouble("motionPerTick",motionPerTick);
            tag.putBoolean("lightEmitting",lightEmitting);
            tag.putInt("sunsetColour",sunsetColour.getRGB());
            return tag;
        }

        public static DynamicSkyboxObject fromNBT(CompoundTag tag){
            DefaultedList<Identifier> textures = DefaultedList.of();
                int textureNum = tag.getInt("textureNum");
                for (int i = 0;i < textureNum;i++) {
                    textures.add(new Identifier(tag.getString("texture" + i)));
                }
            return new DynamicSkyboxObject(
                    textures,
                    tag.getInt("timePerPhase"),
                    tag.getInt("sizeX"),
                    tag.getInt("sizeY"),
                    tag.getDouble("equatorAngle"),
                    tag.getDouble("elevationAngle"),
                    tag.getDouble("motionPerTick"),
                    tag.getBoolean("lightEmitting"),
                    new Color(tag.getInt("sunsetColour")));
        }
    }
}
