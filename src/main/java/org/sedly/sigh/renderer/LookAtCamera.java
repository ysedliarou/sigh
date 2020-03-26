package org.sedly.sigh.renderer;

import org.sedly.sigh.math.Matrix4f;
import org.sedly.sigh.math.Transformation;
import org.sedly.sigh.math.Vector3f;
import org.sedly.sigh.math.projection.CacheableProjection;
import org.sedly.sigh.math.projection.PerspectiveProjection;
import org.sedly.sigh.math.projection.Projection;
import org.sedly.sigh.math.rotation.LookAtRotation;

public class LookAtCamera implements Camera {

    public static class Builder {

        private Vector3f position = Vector3f.ZERO;

        private Vector3f target = Vector3f.UNIT_Z.negate();

        private Projection projection = PerspectiveProjection.DEFAULT;

        public Builder setPosition(Vector3f position) {
            if (position != null) {
                this.position = position;
            }
            return this;
        }

        public Builder setTarget(Vector3f target) {
            if (target != null) {
                this.target = target;
            }
            return this;
        }

        public Builder setProjection(Projection projection) {
            if (projection != null) {
                this.projection = projection;
            }
            return this;
        }

        public LookAtCamera build() {

            Vector3f direction = target.sub(position).normalize();
            Vector3f up = direction.cross(Vector3f.UNIT_Y.cross(direction));
            Vector3f right = direction.cross(up);

            return new LookAtCamera(position, direction, up, right, new CacheableProjection(projection));
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

    private LookAtCamera(Vector3f position, Vector3f direction, Vector3f up, Vector3f right, Projection projection) {
        this.position = position;
        this.direction = direction;
        this.up = up;
        this.right = right;
        this.projection = projection;
    }

    @Override
    public Vector3f getPosition() {
        return position;
    }

    @Override
    public Matrix4f getProjection() {
        return projection.project();
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
