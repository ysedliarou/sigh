package org.sedly.sigh.light;

import org.sedly.sigh.math.Color;

public class BaseLight {

  public static final BaseLight DEFAULT  = new BaseLight(Color.WHITE, 0);

  private Color color;

  private float intensity;

  public BaseLight(Color color, float intensity) {
    this.color = color;
    this.intensity = intensity;
  }

  public Color getColor() {
    return color;
  }

  public float getIntensity() {
    return intensity;
  }

}
