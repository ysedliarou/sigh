package org.sedly.sigh.math.rotation;

import org.sedly.sigh.math.Matrix4f;
import org.sedly.sigh.math.Vector3f;

public class LookAtRotation implements Rotation {

    private Vector3f forward;

    private Vector3f up;

    private Vector3f right;

    public Vector3f getForward() {
        return forward;
    }

    public Vector3f getUp() {
        return up;
    }

    public Vector3f getRight() {
       return right;
    }

    public LookAtRotation(Vector3f forward, Vector3f up) {
        this.forward = forward.normalize();
        this.up = up.normalize();
        this.right = this.forward.cross(this.up).normalize();
    }

    private static Matrix4f rotation(Vector3f forward, Vector3f up, Vector3f right) {

        return new Matrix4f(new float[][] {
                {right.getX(),     right.getY(),     right.getZ(),     0},
                {up.getX(),        up.getY(),        up.getZ(),        0},
                {forward.getX(),   forward.getY(),   forward.getZ(),   0},
                {0,                0,                0,                1}
        });
    }

    @Override
    public Matrix4f rotate() {
        return rotation(forward, up, right);
    }
}
