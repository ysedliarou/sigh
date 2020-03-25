package org.sedly.sigh.renderer;

import org.sedly.sigh.math.Matrix4f;
import org.sedly.sigh.math.Transformation;
import org.sedly.sigh.math.Vector3f;
import org.sedly.sigh.math.projection.PerspectiveProjection;
import org.sedly.sigh.math.projection.Projection;
import org.sedly.sigh.math.rotation.LookAtRotation;

public class LookAtCamera implements Camera {

    public static class Builder {

        private Vector3f position = Vector3f.ZERO;

        private Vector3f target = Vector3f.UNIT_Z.negate();

        private Projection projection = PerspectiveProjection.DEFAULT;

        public Builder setPosition(Vector3f position) {
            this.position = position;
            return this;
        }

        public Builder setTarget(Vector3f target) {
            this.target = target;
            return this;
        }

        public Builder setProjection(Projection projection) {
            this.projection = projection;
            return this;
        }

        public LookAtCamera build() {
            return new LookAtCamera(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }


    private Vector3f position;

    private Vector3f direction;

    private Vector3f up;

    private Vector3f right;

    private Projection projection;


    public Vector3f getDirection() {
        return direction;
    }

    public Vector3f getUp() {
        return up;
    }

    private LookAtCamera(Builder builder) {
        this.position = builder.position;
        this.direction = builder.target.sub(builder.position).normalize();
        this.up = direction.cross(Vector3f.UNIT_Y.cross(direction));
        this.right = direction.cross(up);
        this.projection = builder.projection;
    }

    @Override
    public Vector3f getPosition() {
        return position;
    }

    @Override
    public Matrix4f getProjection() {
        return projection.projection();
    }

    @Override
    public Matrix4f getView() {
        return Transformation.builder()
                .setTranslation(position.negate())
                .setRotation(new LookAtRotation(direction, up, right))
                .setOrder(Transformation.Order.RT)
                .build().transform();
    }
}
