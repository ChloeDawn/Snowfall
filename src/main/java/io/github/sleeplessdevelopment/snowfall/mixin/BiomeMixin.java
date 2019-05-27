package io.github.sleeplessdevelopment.snowfall.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ViewableWorld;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Biome.class)
abstract class BiomeMixin {
  private BiomeMixin() {
    throw new AssertionError();
  }

  @Inject(
    method = "canSetIce",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isAir()Z"),
    locals = LocalCapture.CAPTURE_FAILHARD,
    cancellable = true)
  private void snowfall$permitSnowLayers(final ViewableWorld world, final BlockPos pos, final CallbackInfoReturnable<Boolean> cir, final BlockState state) {
    if (Blocks.SNOW == state.getBlock()) {
      cir.setReturnValue(true);
    }
  }
}
