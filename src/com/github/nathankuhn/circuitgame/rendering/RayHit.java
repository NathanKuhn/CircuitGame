package com.github.nathankuhn.circuitgame.rendering;

import com.github.nathankuhn.circuitgame.utils.Misc;
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

        float xf = hitPosition.x - Misc.Floor(hitPosition.x);
        float yf = hitPosition.y - Misc.Floor(hitPosition.y);
        float zf = hitPosition.z - Misc.Floor(hitPosition.z);

        int stepX = (ray.getDirection().x > 0) ? 1 : -1;
        int stepY = (ray.getDirection().y > 0) ? 1 : -1;
        int stepZ = (ray.getDirection().z > 0) ? 1 : -1;

        if (Misc.Abs(xf * xf - xf) < 0.0005f) {
            hitNormal.x = -stepX;
        } else if (Misc.Abs(yf * yf - yf) < 0.0005f) {
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
