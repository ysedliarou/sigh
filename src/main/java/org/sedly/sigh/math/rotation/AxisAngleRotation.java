package org.sedly.sigh.math.rotation;

import org.sedly.sigh.math.Matrix4f;
import org.sedly.sigh.math.Vector3f;

public class AxisAngleRotation implements Rotation {

    // --------------- PROPERTIES ---------------

    private Vector3f axis;

    private float angle;

    // --------------- GETTERS ---------------

    public Vector3f getAxis() {
        return axis;
    }

    public float getAngle() {
        return angle;
    }

    // --------------- CONSTRUCTORS ---------------

    public AxisAngleRotation(final Vector3f axis, final float angle) {
        this.axis = axis;
        this.angle = angle;
    }

    // --------------- MATH ---------------

    private static Matrix4f rotation(final Vector3f axis, final float angle) {

        float cos = (float) Math.cos(angle);
        float sin = (float) Math.sin(angle);
        float t = 1 - cos;

        return new Matrix4f(new float[][]{
                {t * axis.getX() * axis.getX() + cos,               t * axis.getX() * axis.getY() - axis.getZ() * sin,  t * axis.getX() * axis.getZ() + axis.getY() * sin,  0},
                {t * axis.getX() * axis.getY() + axis.getZ() * sin, t * axis.getY() * axis.getY() + cos,                t * axis.getY() * axis.getZ() - axis.getX() * sin,  0},
                {t * axis.getX() * axis.getZ() - axis.getY() * sin, t * axis.getY() * axis.getZ() + axis.getX() * sin,  t * axis.getZ() * axis.getZ() + cos,                0},
                {0,                                                 0,                                                  0,                                                  1}
        });
    }

    // --------------- METHODS ---------------

    @Override
    public Matrix4f rotate() {
        return rotation(axis, angle);
    }

}
