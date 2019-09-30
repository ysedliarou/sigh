package org.sedly.sigh.model;

public class Texture {

  private int textureId;

  private float shineDamper = 1.0f;
  private float reflectivity = 0.0f;


  public Texture(int textureId) {
    this.textureId = textureId;
  }

  public int getTextureId() {
    return textureId;
  }

  public float getShineDamper() {
    return shineDamper;
  }

  public void setShineDamper(float shineDamper) {
    this.shineDamper = shineDamper;
  }

  public float getReflectivity() {
    return reflectivity;
  }

  public void setReflectivity(float reflectivity) {
    this.reflectivity = reflectivity;
  }
}
