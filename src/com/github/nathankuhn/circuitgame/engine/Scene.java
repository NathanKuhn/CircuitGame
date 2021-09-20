package com.github.nathankuhn.circuitgame.engine;

import com.github.nathankuhn.circuitgame.utils.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Scene {

    private Camera mainCamera;
    private List<RenderObject> renderObjects;
    // TODO lights

    public Scene() {
        mainCamera = new Camera(new Vector3f(0.0f, 0.0f, 2.0f), new Vector3f(0.0f, 0.0f, 0.0f));
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