package org.sedly.sigh.math;

import org.sedly.sigh.util.MathUtil;

public class Color {

  public static final float MIN = 0.0f;
  public static final float MAX_FLOAT = 1.0f;
  public static final int MAX_INT = 255;


  public static final float TRANSPARENT = 0.0f;
  public static final float OPAQUE = 1.0f;

  public final static Color BLACK = new Color(0.0f, 0.0f, 0.0f);
  public final static Color WHITE = new Color(1.0f, 1.0f, 1.0f);

  public final static Color RED = new Color(1.0f, 0.0f, 0.0f);
  public final static Color GREEN = new Color(0.0f, 1.0f, 0.0f);
  public final static Color BLUE = new Color(0.0f, 0.0f, 1.0f);

  private float r, g, b, a;

  public Color(float r, float g, float b, float a) {
    this.r = MathUtil.clamp(r, MIN, MAX_FLOAT);
    this.g = MathUtil.clamp(g, MIN, MAX_FLOAT);
    this.b = MathUtil.clamp(b, MIN, MAX_FLOAT);
    this.a = MathUtil.clamp(a, MIN, MAX_FLOAT);
  }

  public Color(float r, float g, float b) {
    this(r, g, b, OPAQUE);
  }

/*  public Color(int r, int g, int b, float a) {
    this.r = MathUtil.clamp(r / MAX_INT, MIN, MAX_FLOAT);
    this.g = MathUtil.clamp(g / MAX_INT, MIN, MAX_FLOAT);
    this.b = MathUtil.clamp(b / MAX_INT, MIN, MAX_FLOAT);
    this.a = MathUtil.clamp(a, MIN, MAX_FLOAT);
  }

  public Color(int r, int g, int b) {
    this(r, g, b, OPAQUE);
  }*/

  public float getR() {
    return r;
  }

  public float getG() {
    return g;
  }

  public float getB() {
    return b;
  }

  public float getA() {
    return a;
  }

}
