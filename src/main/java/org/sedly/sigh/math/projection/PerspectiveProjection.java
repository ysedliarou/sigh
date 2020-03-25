package org.sedly.sigh.math.projection;

import org.sedly.sigh.math.Matrix4f;

public class PerspectiveProjection implements Projection {

    // --------------- CONSTANTS ---------------

    public static final PerspectiveProjection DEFAULT = new Builder().build();

    public static final float DEFAULT_FOV = 1.4f;

    public static final float DEFAULT_ASPECT_RATIO = 1.333f;

    // --------------- BUILDER ---------------

    public static class Builder {

        private float near = DEFAULT_NEAR;

        private float far = DEFAULT_FAR;

        private float fov = DEFAULT_FOV;

        private float aspectRatio = DEFAULT_ASPECT_RATIO;

        private Builder() {
            super();
        }

        public Builder setDepth(float near, float far) {
            this.near = near;
            this.far = far;
            return this;
        }

        public Builder aspectRatio(float aspectRatio) {
            this.aspectRatio = aspectRatio;
            return this;
        }

        public Builder setFov(float fov) {
            this.fov = fov;
            return this;
        }

        public PerspectiveProjection build() {
            return new PerspectiveProjection(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        return builder().aspectRatio(aspectRatio).setDepth(near, far).setFov(fov);
    }

    // --------------- PROPERTIES ---------------

    private final float near, far;

    private final float fov;

    private final float aspectRatio;

    // --------------- GETTERS ---------------

    public float getNear() {
        return near;
    }

    public float getFar() {
        return far;
    }

    public float getFov() {
        return fov;
    }

    public float getAspectRatio() {
        return aspectRatio;
    }

// --------------- CONSTRUCTORS ---------------

    private PerspectiveProjection(Builder builder) {
        this.near = builder.near;
        this.far = builder.far;
        this.fov = builder.fov;
        this.aspectRatio = builder.aspectRatio;
    }

    // --------------- MATH ---------------

    private Matrix4f projection(float aspectRatio, float fov, float near, float far) {
        float tanHalfFov = (float) Math.tan(fov / 2);

        return new Matrix4f(new float[][]{
                {1.0f / (tanHalfFov * aspectRatio),     0,                  0,                    0                           },
                {0,                                     1.0f / tanHalfFov,  0,                    0                           },
                {0,                                     0,                  far / (far - near),   (far * near) / (far - near) },
                {0,                                     0,                  1.0f,                 1.0f                        }
        });

    }

    // --------------- METHODS ---------------

    @Override
    public Matrix4f projection() {
        return projection(aspectRatio, fov, near, far);
    }

}
