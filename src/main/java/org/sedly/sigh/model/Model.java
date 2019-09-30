package org.sedly.sigh.model;

public class Model {

  private float[] positions;

  private int[] indices;

  private float[] texCoords;

  private float[] normals;

  public float[] getPositions() {
    return positions;
  }

  public int[] getIndices() {
    return indices;
  }

  public float[] getTexCoords() {
    return texCoords;
  }

  public float[] getNormals() {
    return normals;
  }

  public Model(float[] positions, int[] indices, float[] texCoords, float[] normals) {
    this.positions = positions;
    this.indices = indices;
    this.texCoords = texCoords;
    this.normals = normals;
  }
}
