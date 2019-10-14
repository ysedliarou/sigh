package org.sedly.sigh.math;

import static org.sedly.sigh.util.MathUtil.NUMBER_FORMAT;

import java.text.MessageFormat;
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

  @Override
  public String toString() {
    String fr = NUMBER_FORMAT.get().format(r);
    String fg = NUMBER_FORMAT.get().format(g);
    String fb = NUMBER_FORMAT.get().format(b);
    String fa = NUMBER_FORMAT.get().format(a);
    return MessageFormat.format("Color[r={0},g={1},b={2},a={3}]", fr, fg, fb, fa);
  }

}
