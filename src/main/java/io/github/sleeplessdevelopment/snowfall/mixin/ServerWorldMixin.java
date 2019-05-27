package io.github.sleeplessdevelopment.snowfall.mixin;

import io.github.sleeplessdevelopment.snowfall.SnowLayers;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SnowBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

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
      final int nextLayer = existing.get(SnowBlock.LAYERS) + 1;
      if (SnowLayers.contains(nextLayer)) {
        return world.setBlockState(pos, existing.with(SnowBlock.LAYERS, nextLayer));
      }
    }
    return world.setBlockState(pos, state);
  }
}
