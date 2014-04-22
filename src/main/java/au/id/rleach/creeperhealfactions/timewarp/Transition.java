
package au.id.rleach.creeperhealfactions.timewarp;

public abstract class Transition {
    protected long goal;
    protected long position;
    protected PlayerTime pt;

    public Transition(long start, long goal, PlayerTime pt){
        this.pt = pt;
        start(start, goal);
        }
    
    //initialize the animation starting position/velocity/acceleration
    abstract void start(long start, long end);

    /** get the next position of the animation if goal is reached, return goal and 
     * call stop. 
     * @return next value
     */
    abstract public long next();
    
    //called when end is reached.
    void stop(){
        pt.stop();
    }
    
    //resets a new goal whilst mid transition.
    abstract public void setGoal(long goal);
    
}
