/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.id.rleach.creeperhealfactions.timewarp;

public class Overshooter extends Transition{

    private int acceleration;
    private int oldAcceleration;
    private int speed;
    private long goal;
    private long position;
    private int overshoot = 25;
    private boolean accelerationChanged = false;

    private PlayerTime pt;

    /** Overshooter is a "simple" class to make an animation between 2 values.
    * it starts off with a speed in the opposite direction to the goal, but accelerates
    * towards the goal until the goal is passed, at which stage the direction of 
    * acceleration is reversed. 
    * Speed is also clamped to a maximum value.
    * When the animation is complete, PlayerTime.stop is called to remove it from the 
    * active animations list.
    * 
    * This also works in both the positive and negative directions just fine.
    * <pre>
    * - y-axis position. 
    *| 
    *|             *** <-overshoot             >
    *|            *   * 
    *| ----------*-----*Goal
    *|          *
    *| *-------*--Start
    *|  *     *
    *|   *   *
    *|    *** <-undershoot                     >
    * \----------------------- Time
    * </pre>
    * 
    * http://puu.sh/8iBIe.jpg paint drawing, if it is still up.
    **/
    Overshooter(long curTime, long goal, PlayerTime pt) {
        super(curTime,goal,pt);
    }

    //initialize the animation starting position/velocity/acceleration
    @Override
    void start(long start, long end) {
        position = start;
        goal = end;
        //Always towards the goal from the position
        acceleration = Long.signum(goal - position*5);
        oldAcceleration = acceleration;
        accelerationChanged = false;
        //initial speed, in opposite direction of goal for nice undershoot.
        speed = acceleration * -overshoot;
    }

    //get the next position of the animation
    @Override
    public long next() {
        //Always towards the goal from the position
        acceleration = Long.signum(goal - position);
        //Restrict max speed for smooth animation
        speed = clamp(speed + acceleration, -overshoot, overshoot);
        //if goal reached, notify parent
        if (testGoal()) {
            pt.stop();
            return goal;
        }
        //otherwise keep on truckin'
        position = position + speed;
        return position;
    }

    //whether the goal has been reached (after the overshoot)
    boolean testGoal() {
        //remember if we overshoot
        if (acceleration != oldAcceleration) {
            accelerationChanged = true;
        }
        //if overshot previously and goal reached.
        return accelerationChanged && acceleration == oldAcceleration;

    }
    //clamps the value to the specified range.
    int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }

    //resets a new goal whilst maintaining speed/momentum
    @Override
    public void setGoal(long goal) {
        start(this.position, goal);
        this.speed = speed;
    }

}
