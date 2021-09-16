package openlyfay.ancientgateways.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import static openlyfay.ancientgateways.world.RegisterWorld.POCKET_MAX_SIZE;

public class SpiralHelper {

    public static BlockPos findSpiral(int n){
        int c = POCKET_MAX_SIZE * 2;
        double r = Math.floor((Math.sqrt(n + 1) - 1) / 2) + 1;

        double p = (8 * r * (r - 1)) / 2;

        double en = r * 2;

        double a = (1 + n - p) % (r * 8);

        BlockPos pos;
        switch ((int)Math.floor(a/ (r * 2))){
            case 0: {
                pos = new BlockPos((a - r) * c,128,-r * c);
                return pos;
            }
            case 1: {
                pos = new BlockPos(r * c,128,((a % en) - r) * c);
                return pos;
            }
            case 2: {
                pos = new BlockPos((r - (a % en)) * c, 128, r * c);
                return pos;
            }
            case 3: {
                pos = new BlockPos(-r * c, 128, (r - (a % en)) * c);
                return pos;
            }
        }
        return new BlockPos(0,128,0);
    }

    public static BlockPos findNearestAnchor(Vec3d pos){
        return new BlockPos(
                POCKET_MAX_SIZE * (Math.round(pos.getX()/(POCKET_MAX_SIZE * 2)) * 2),
                128,
                POCKET_MAX_SIZE * (Math.round(pos.getZ()/(POCKET_MAX_SIZE * 2)) * 2)
        );
    }
}
