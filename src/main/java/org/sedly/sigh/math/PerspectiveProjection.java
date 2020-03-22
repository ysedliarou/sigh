package org.sedly.sigh.math;

public class PerspectiveProjection implements Projection {

    private float near;

    private float far;

    private float fov;

    private int width;

    private int height;

    private PerspectiveProjection() {
        super();
    }

    private PerspectiveProjection(Builder builder) {
        this.near = builder.near;
        this.far = builder.far;
        this.fov = builder.fov;
        this.width = builder.width;
        this.height = builder.height;
    }

    public float getNear() {
        return near;
    }

    public float getFar() {
        return far;
    }

    public float getFov() {
        return fov;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public static class Builder {

        private float near = 0.1f;

        private float far = 100f;

        private float fov = 1.4f;

        private int width;

        private int height;

        private Builder() {
            super();
        }

        public Builder z(float near, float far) {
            this.near = near;
            this.far = far;
            return this;
        }

        public Builder xy(int width, int height) {
            this.width = width;
            this.height = height;
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
        return new Builder().xy(width, height).z(near, far).setFov(fov);
    }

    public Matrix4f projection() {
        float aspectRatio = width / height;
        return projection(aspectRatio, fov, near, far);
    }

    private Matrix4f projection(float aspectRatio, float fov, float near, float far) {
        float tanHalfFov = (float) Math.tan(fov / 2);

        return new Matrix4f(new float[][]{
                {1 / (tanHalfFov * aspectRatio),  0,              0,                    0                           },
                {0,                               1 / tanHalfFov, 0,                    0                           },
                {0,                               0,              far / (far - near),   (far * near) / (far - near) },
                {0,                               0,              1,                    1                           }
        });

    }

}
