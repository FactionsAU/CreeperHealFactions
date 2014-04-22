/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.id.rleach.creeperhealfactions.timewarp;

public class Overshooter {

    private int acceleration;
    private int oldAcceleration;
    private int speed;
    private long goal;
    private long position;
    private int overshoot = 25;
    private boolean accelerationChanged = false;

    private PlayerTime pt;

    Overshooter(long curTime, long goal, PlayerTime pt) {
        this.pt = pt;
        start(curTime, goal);
    }

    private void start(long start, long end) {
        position = start;
        goal = end;
        acceleration = Long.signum(goal - position*5);
        oldAcceleration = acceleration;
        accelerationChanged = false;
        speed = acceleration * -overshoot;
    }

    public long next() {
        acceleration = Long.signum(goal - position);
        speed = clamp(speed + acceleration, -overshoot, overshoot);
        if (testGoal()) {
            pt.stop();
            return goal;
        }
        position = position + speed;
        return position;
    }

    boolean testGoal() {
        if (acceleration != oldAcceleration) {
            accelerationChanged = true;
        }
        return accelerationChanged && acceleration == oldAcceleration;

    }

    int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }

    void setGoal(long goal) {
        start(this.position, goal);
        this.speed = speed;
    }

}
