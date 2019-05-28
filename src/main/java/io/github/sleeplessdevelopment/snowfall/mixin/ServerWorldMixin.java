package io.github.sleeplessdevelopment.snowfall.mixin;

import io.github.sleeplessdevelopment.snowfall.SnowLayers;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SnowBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(ServerWorld.class)
abstract class ServerWorldMixin extends World {
  private ServerWorldMixin() {
    super(null, DimensionType.OVERWORLD, (lvl, dim) -> null, null, false);
    throw new AssertionError();
  }

  @ModifyArg(
    method = "tickChunk",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Z"),
    slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/Biome;canSetIce(Lnet/minecraft/world/ViewableWorld;Lnet/minecraft/util/math/BlockPos;)Z")))
  private BlockState snowfall$stackSnowLayers(final BlockPos pos, final BlockState state) {
    final BlockState existing = this.getBlockState(pos);
    if (Blocks.SNOW == existing.getBlock()) {
      final int nextLayer = existing.get(SnowBlock.LAYERS) + 1;
      if (SnowLayers.contains(nextLayer)) {
        return existing.with(SnowBlock.LAYERS, nextLayer);
      }
    }
    return state;
  }
}
