package org.sedly.sigh.renderer;

import org.sedly.sigh.math.Matrix4f;
import org.sedly.sigh.math.Vector3f;

public interface Camera {

    Vector3f getPosition();

    Matrix4f getProjection();

    Matrix4f getView();

}
