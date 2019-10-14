package org.sedly.sigh.shader.ex;

public class Shader {

  public static class Builder {

    private int pId, vsId, fsId;

    public Builder programId(int pId) {
      this.pId = pId;
      return this;
    }

    public Builder vertexShaderId(int vsId) {
      this.vsId = vsId;
      return this;
    }

    public Builder fragmentShaderId(int fsId) {
      this.fsId = fsId;
      return this;
    }

    public Shader build() {
      return new Shader(this);
    }
  }

  public static Builder builder() {
    return new Builder();
  }

  private int programId, vertexShaderId, fragmentShaderId;

  private Shader(Builder builder) {
    this.programId = builder.pId;
    this.vertexShaderId = builder.vsId;
    this.fragmentShaderId = builder.fsId;
  }

  public int getProgramId() {
    return programId;
  }

  public int getVertexShaderId() {
    return vertexShaderId;
  }

  public int getFragmentShaderId() {
    return fragmentShaderId;
  }

}
