package com.github.nathankuhn.circuitgame.rendering;

import com.github.nathankuhn.circuitgame.utils.Vector3f;
import com.github.nathankuhn.circuitgame.utils.Vector3i;
import com.github.nathankuhn.circuitgame.utils.VectorMath;

public class RayHit {

    private Ray ray;
    private float t;
    private Vector3f hitPosition;
    private Vector3i hitNormal;

    public RayHit(Ray ray, float t) {
        this.ray = ray;
        this.t = t;

        hitPosition = VectorMath.Add(VectorMath.Scale(ray.getDirection(), t), ray.getOrigin());
        hitNormal = new Vector3i();

        float xf = hitPosition.x - (int)hitPosition.x;
        float yf = hitPosition.y - (int)hitPosition.y;
        float zf = hitPosition.z - (int)hitPosition.z;

        int stepX = (ray.getDirection().x > 0) ? 1 : -1;
        int stepY = (ray.getDirection().y > 0) ? 1 : -1;
        int stepZ = (ray.getDirection().z > 0) ? 1 : -1;

        if (xf < yf && xf < zf) {
            hitNormal.x = -stepX;
        } else if (yf < xf && yf < zf) {
            hitNormal.y = -stepY;
        } else {
            hitNormal.z = -stepZ;
        }
    }

    public Ray getRay() {
        return ray;
    }

    public float getT() {
        return t;
    }

    public Vector3f getHitPosition() {
        return hitPosition;
    }

    public Vector3i getHitNormal() {
        return hitNormal;
    }
}
