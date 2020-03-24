package org.sedly.sigh.math.rotation;

import org.sedly.sigh.math.Matrix4f;
import org.sedly.sigh.math.Quaternion;

public class QuaternionRotation implements Rotation {

    public static final QuaternionRotation DEFAULT = new QuaternionRotation(Quaternion.UNIT);

    // --------------- PROPERTIES ---------------

    private Quaternion quaternion;

    // --------------- GETTERS ---------------

    public Quaternion getQuaternion() {
        return quaternion;
    }

    // --------------- CONSTRUCTORS ---------------

    public QuaternionRotation(final Quaternion quaternion) {
        this.quaternion = quaternion.normalize();
    }

    // --------------- MATH ---------------

    private static Matrix4f rotation(final Quaternion q) {
        return new Matrix4f(new float[][] {
                {1 - 2 * q.getY() * q.getY() - 2 * q.getZ() * q.getZ(),   2 * q.getX() * q.getY() - 2 * q.getZ() * q.getW(),          2 * q.getX() * q.getZ() + 2 * q.getY() * q.getW(),          0},
                {2 * q.getX() * q.getY() + 2 * q.getZ() * q.getW(),       1 - 2 * q.getX() * q.getX() - 2 * q.getZ() * q.getZ(),      2 * q.getY() * q.getZ() - 2 * q.getX() * q.getW(),          0},
                {2 * q.getX() * q.getZ() - 2 * q.getY() * q.getW(),       2 * q.getY() * q.getZ() + 2 * q.getX() * q.getW(),          1 - 2 * q.getX() * q.getX() - 2 * q.getY() * q.getY(),      0},
                {0,                                                       0,                                                          0,                                                          1}
        });
    }

    // --------------- METHODS ---------------

    @Override
    public Matrix4f rotate() {
        return rotation(quaternion);
    }

}
