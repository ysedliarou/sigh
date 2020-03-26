package org.sedly.sigh.math.projection;

import org.sedly.sigh.math.Matrix4f;

public class CacheableProjection implements Projection {

    private Projection delegate;

    private Matrix4f cache = null;

    public CacheableProjection(Projection delegate) {
        this.delegate = delegate;
    }

    @Override
    public Matrix4f project() {
        if (cache == null) {
            cache = delegate.project();
        }
        return cache;
    }
}
