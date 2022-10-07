package com.github.nathankuhn.circuitgame.hud;

import com.github.nathankuhn.circuitgame.display.DisplayManager;
import com.github.nathankuhn.circuitgame.rendering.RenderObject;
import com.github.nathankuhn.circuitgame.utils.Vector2f;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Root extends HudElement{

    private Anchor leftAnchor;
    private Anchor rightAnchor;
    private Anchor upAnchor;
    private Anchor downAnchor;
    private Anchor upperLeftAnchor;
    private Anchor upperRightAnchor;
    private Anchor lowerLeftAnchor;
    private Anchor lowerRightAnchor;

    public Root() {
        super(null, new Vector2f(), new Vector2f(1.0f, 1.0f), true);
        leftAnchor = new Anchor(this, new Vector2f(-1.0f, 0.0f));
        rightAnchor = new Anchor(this, new Vector2f(1.0f, 0.0f));
        upAnchor = new Anchor(this, new Vector2f(0.0f, 1.0f));
        downAnchor = new Anchor(this, new Vector2f(0.0f, -1.0f));
        upperLeftAnchor = new Anchor(this, new Vector2f(-1.0f, 1.0f));
        upperRightAnchor = new Anchor(this, new Vector2f(1.0f, 1.0f));
        lowerLeftAnchor = new Anchor(this, new Vector2f(-1.0f, -1.0f));
        lowerRightAnchor = new Anchor(this, new Vector2f(1.0f, -1.0f));
    }

    public Anchor getLeftAnchor() {
        return leftAnchor;
    }

    public Anchor getRightAnchor() {
        return rightAnchor;
    }

    public Anchor getUpAnchor() {
        return upAnchor;
    }

    public Anchor getDownAnchor() {
        return downAnchor;
    }

    public Anchor getUpperLeftAnchor() {
        return upperLeftAnchor;
    }

    public Anchor getUpperRightAnchor() {
        return upperRightAnchor;
    }

    public Anchor getLowerLeftAnchor() {
        return lowerLeftAnchor;
    }

    public Anchor getLowerRightAnchor() {
        return lowerRightAnchor;
    }

    public void updateSize() {
        setDimensions( new Vector2f(
                DisplayManager.WindowAspectRatio(),
                1.0f
        ));
    }

    public List<HudElement> getAllChildren() {

        Stack<HudElement> stack = new Stack<>();
        stack.addAll(getChildren());

        List<HudElement> ret = new ArrayList<>();

        while(stack.size() > 0) {

            ret.add(stack.lastElement());
            if (stack.lastElement().getChildren() != null) {
                stack.addAll(stack.pop().getChildren());
            } else {
                stack.pop();
            }
        }

        return ret;

    }

    public List<RenderObject> getRenderObjects() {

        Stack<HudElement> stack = new Stack<>();
        stack.addAll(getChildren());

        List<RenderObject> ret = new ArrayList<>();

        while(stack.size() > 0) {

            if (stack.lastElement().getRenderObject() != null) {
                ret.add(stack.lastElement().getRenderObject());
            }

            if (stack.lastElement().getChildren() != null) {
                stack.addAll(stack.pop().getChildren());
            } else {
                stack.pop();
            }
        }

        return ret;
    }

}
