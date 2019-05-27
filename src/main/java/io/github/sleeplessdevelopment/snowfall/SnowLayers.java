package io.github.sleeplessdevelopment.snowfall;

import net.minecraft.block.SnowBlock;

import java.util.Collections;

public final class SnowLayers {
  private static final int MIN_LAYER = Collections.min(SnowBlock.LAYERS.getValues());
  private static final int MAX_LAYER = Collections.max(SnowBlock.LAYERS.getValues());

  private SnowLayers() {
    throw new UnsupportedOperationException();
  }

  public static boolean contains(final int layer) {
    return MIN_LAYER <= layer && layer <= MAX_LAYER;
  }
}
