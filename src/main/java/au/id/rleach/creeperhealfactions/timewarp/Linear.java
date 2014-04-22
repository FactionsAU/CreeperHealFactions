package au.id.rleach.creeperhealfactions.timewarp;


public class Linear extends Transition {

    final int SPEED = 10;
    
    public Linear(long start, long goal, PlayerTime pt) {
        super(start, goal, pt);
    }

    @Override
    void start(long start, long end) {
        this.position = start;
        this.goal = end;
    }

    @Override
    public long next() {
        int diff = Long.signum(goal - position)*SPEED;
        if(Math.abs(goal - position) <= SPEED){
            this.stop();
            return goal;
        }
        this.position = this.position + diff;
        return position;
    }

    @Override
    public void setGoal(long goal) {
        this.goal = goal;
    }
    
}
