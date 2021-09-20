package theapp.entity;

public abstract class MobileEntity extends VisibleEntity {
    protected int direction = 0; // 0 = north, 1 = east, 2 = south, 3 = west
    protected boolean moving = false;

    @Override
    public void update () {

    }

    public void move () {

    }

    private boolean isCollision() {
        return false;
    }

    public void render () {

    }
}
