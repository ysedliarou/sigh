package org.sedly.sigh.math.projection;

import org.sedly.sigh.math.Matrix4f;

public interface Projection {

  float NEAR = 0.1f;
  float FAR = 1000f;

  Matrix4f projection();

}
