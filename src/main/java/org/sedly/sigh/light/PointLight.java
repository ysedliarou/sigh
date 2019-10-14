package org.sedly.sigh.light;

import org.sedly.sigh.math.Vector3f;

public class PointLight {

  public static final PointLight DEFAULT = new PointLight(BaseLight.DEFAULT, Attenuation.DEFAULT, Vector3f.ZERO, 0);

  private BaseLight baseLight;

  private Attenuation attenuation;

  private Vector3f position;

  private float range;

  public PointLight(BaseLight baseLight, Attenuation attenuation,
      Vector3f position, float range) {
    this.baseLight = baseLight;
    this.attenuation = attenuation;
    this.position = position;
    this.range = range;
  }

  public BaseLight getBaseLight() {
    return baseLight;
  }

  public Attenuation getAttenuation() {
    return attenuation;
  }

  public Vector3f getPosition() {
    return position;
  }

  public float getRange() {
    return range;
  }
}
