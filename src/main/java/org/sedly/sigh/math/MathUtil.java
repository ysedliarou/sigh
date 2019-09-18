package org.sedly.sigh.math;

import java.text.DecimalFormat;

public final class MathUtil {

  public static final ThreadLocal<DecimalFormat> NUMBER_FORMAT
      = ThreadLocal.withInitial(() -> new DecimalFormat("#.####"));

  private MathUtil() {
    super();
  }

  public static float clamp(final float val, final float min, final float max) {
    return Math.max(min, Math.min(max, val));
  }

}
