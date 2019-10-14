package org.sedly.sigh.light;

public class Attenuation {

  public static final Attenuation DEFAULT = new Attenuation(0, 0 , 0);

  private float constant, linear, exponent;

  public Attenuation(float constant, float linear, float exponent) {
    this.constant = constant;
    this.linear = linear;
    this.exponent = exponent;
  }

  public float getConstant() {
    return constant;
  }

  public float getLinear() {
    return linear;
  }

  public float getExponent() {
    return exponent;
  }
}
