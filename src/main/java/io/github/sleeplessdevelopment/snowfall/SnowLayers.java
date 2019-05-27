package io.github.sleeplessdevelopment.snowfall;

import net.minecraft.block.SnowBlock;

import java.util.Collections;

public final class SnowLayers {
  private static final int MAX_LAYER = Collections.max(SnowBlock.LAYERS.getValues());

  private SnowLayers() {
    throw new UnsupportedOperationException();
  }

  public static boolean contains(final int layer) {
    return 0 <= layer && layer <= MAX_LAYER;
  }
}
