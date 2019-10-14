package org.sedly.sigh.light;

import org.sedly.sigh.math.Vector3f;

public class SpotLight {

  public static final SpotLight DEFAULT = new SpotLight(PointLight.DEFAULT, Vector3f.ZERO, 0);

  private PointLight pointLight;

  private Vector3f direction;

  private float cutoff;

  public SpotLight(PointLight pointLight, Vector3f direction, float cutoff) {
    this.pointLight = pointLight;
    this.direction = direction;
    this.cutoff = cutoff;
  }

  public PointLight getPointLight() {
    return pointLight;
  }

  public Vector3f getDirection() {
    return direction;
  }

  public float getCutoff() {
    return cutoff;
  }

}