package org.sedly.sigh.model;

import org.w3c.dom.Text;

public class Mesh {

  private int vaoId;

  private int vertexCount;

  public Mesh(int vaoId, int vertexCount) {
    this.vaoId = vaoId;
    this.vertexCount = vertexCount;
  }

  public int getVaoId() {
    return vaoId;
  }

  public int getVertexCount() {
    return vertexCount;
  }

}
