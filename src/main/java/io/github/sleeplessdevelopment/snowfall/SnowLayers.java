package io.github.sleeplessdevelopment.snowfall;

import com.google.common.primitives.Ints;
import net.minecraft.block.SnowBlock;

public final class SnowLayers {
  private static final int[] LAYERS = Ints.toArray(SnowBlock.LAYERS.getValues());

  private SnowLayers() {
    throw new UnsupportedOperationException();
  }

  public static boolean contains(final int layer) {
    return Ints.contains(LAYERS, layer);
  }
}
