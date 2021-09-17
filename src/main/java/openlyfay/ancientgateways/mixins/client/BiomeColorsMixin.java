package openlyfay.ancientgateways.mixins.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.render.chunk.ChunkRendererRegion;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.World;
import openlyfay.ancientgateways.block.blockentity.AnchorBaseEntity;
import openlyfay.ancientgateways.util.SpiralHelper;
import openlyfay.ancientgateways.util.WorldAccessHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static openlyfay.ancientgateways.AncientGateways.DIM_ID;
import static openlyfay.ancientgateways.block.RegisterBlocks.ANCHOR_BLOCK;

@Environment(EnvType.CLIENT)
@Mixin(BiomeColors.class)
public class BiomeColorsMixin {

    @Inject(method = "getGrassColor",at = @At("HEAD"), cancellable = true)
    private static void onGetGrassColor(BlockRenderView world, BlockPos pos, CallbackInfoReturnable<Integer> cir){
        BlockPos pos1 = SpiralHelper.findNearestAnchor(new Vec3d(pos.getX(),pos.getY(), pos.getZ()));
        BlockEntity anchor = ((WorldAccessHelper) ((ChunkRendererRegion) world)).getWorld().getBlockEntity(pos1);;
        if (anchor instanceof AnchorBaseEntity){
            cir.setReturnValue(((AnchorBaseEntity) anchor).getGrassColour().getRGB());
        }
    }

    @Inject(method = "getWaterColor",at = @At("HEAD"), cancellable = true)
    private static void onGetWaterColor(BlockRenderView world, BlockPos pos, CallbackInfoReturnable<Integer> cir){
        BlockPos pos1 = SpiralHelper.findNearestAnchor(new Vec3d(pos.getX(),pos.getY(), pos.getZ()));
        BlockEntity anchor = ((WorldAccessHelper) ((ChunkRendererRegion) world)).getWorld().getBlockEntity(pos1);;
        if (anchor instanceof AnchorBaseEntity){
            cir.setReturnValue(((AnchorBaseEntity) anchor).getWaterColour().getRGB());
        }
    }

    @Inject(method = "getFoliageColor",at = @At("HEAD"), cancellable = true)
    private static void onGetFoliageColor(BlockRenderView world, BlockPos pos, CallbackInfoReturnable<Integer> cir){
        BlockPos pos1 = SpiralHelper.findNearestAnchor(new Vec3d(pos.getX(),pos.getY(), pos.getZ()));
        BlockEntity anchor = ((WorldAccessHelper) ((ChunkRendererRegion) world)).getWorld().getBlockEntity(pos1);;
        if (anchor instanceof AnchorBaseEntity){
            cir.setReturnValue(((AnchorBaseEntity) anchor).getLeafColour().getRGB());
        }
    }

}
