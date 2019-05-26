package io.github.sleeplessdevelopment.snowfall;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SnowBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ViewableWorld;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
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

@Mixin(ServerWorld.class)
abstract class ServerWorldMixin {
  private ServerWorldMixin() {
    throw new AssertionError();
  }

  @Redirect(
    method = "tickChunk",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Z"),
    slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/Biome;canSetIce(Lnet/minecraft/world/ViewableWorld;Lnet/minecraft/util/math/BlockPos;)Z")))
  private boolean snowfall$stackSnowLayers(final ServerWorld world, final BlockPos pos, final BlockState state) {
    final BlockState existing = world.getBlockState(pos);
    if (Blocks.SNOW == existing.getBlock()) {
      final int layers = existing.get(SnowBlock.LAYERS) + 1;
      if (layers <= 8) {
        return world.setBlockState(pos, existing.with(SnowBlock.LAYERS, layers));
      }
    }
    return world.setBlockState(pos, state);
  }
}
