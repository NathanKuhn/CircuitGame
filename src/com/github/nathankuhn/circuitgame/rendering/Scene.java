package com.github.nathankuhn.circuitgame.rendering;

import com.github.nathankuhn.circuitgame.utils.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Scene {

    private Camera mainCamera;
    private List<RenderObject> renderObjects;
    // TODO lights

    public Scene(Camera camera) {
        mainCamera = camera;
        renderObjects = new ArrayList<>();
    }

    public int numRenderObjects() {
        return renderObjects.size();
    }
    public RenderObject getRenderObject(int index) {
        return renderObjects.get(index);
    }
    public Camera getMainCamera() {
        return mainCamera;
    }

    public void addRenderObject(RenderObject renderObject) {
        renderObjects.add(renderObject);
    }
}
