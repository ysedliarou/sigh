package org.sedly.sigh.model;

public class TexturedMesh {

  private Mesh mesh;

  private Texture texture;

  public TexturedMesh(Mesh mesh, Texture texture) {
    this.mesh = mesh;
    this.texture = texture;
  }

  public Mesh getMesh() {
    return mesh;
  }

  public Texture getTexture() {
    return texture;
  }
}
