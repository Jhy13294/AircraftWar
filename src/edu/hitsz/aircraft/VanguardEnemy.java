package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import java.util.LinkedList;
import java.util.List;

public class VanguardEnemy extends AbstractEnemy {
    public VanguardEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    @Override
    public List<BaseBullet> shoot() {
        return new LinkedList<>();
    }
}
