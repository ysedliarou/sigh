package org.sedly.sigh.shader.light;

import org.sedly.sigh.math.Color;

public class BaseLight {

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
