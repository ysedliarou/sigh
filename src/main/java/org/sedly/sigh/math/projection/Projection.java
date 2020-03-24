package org.sedly.sigh.math.projection;

import org.sedly.sigh.math.Matrix4f;

// https://solarianprogrammer.com/2013/05/22/opengl-101-matrices-projection-view-model/
public interface Projection {

  float DEFAULT_NEAR = 0.1f;
  float DEFAULT_FAR = 1000f;

  Matrix4f projection();

}
