package DiningPhilosophers;

import java.util.concurrent.locks.*;

public class DiningServerImpl implements DiningServer {

    // different philosopher states
    enum State {
        THINKING, HUNGRY, EATING
    };

    // number of philosophers
    public static final int NUM_OF_PHILS = 5;

    // array to record each philosopher's state
    private State[] state;
//The Initialization of the object from type lock
    Lock lock;
    Condition[] chopsticks;

    public DiningServerImpl() {
        lock = new ReentrantLock();
        state = new State[NUM_OF_PHILS];
        chopsticks = new Condition[NUM_OF_PHILS];

        //- The part of the initialization in the pseudocode-//
        for (int i = 0; i < NUM_OF_PHILS; i++) {

            state[i] = State.THINKING;
            //assosiate ever chopstick to a conditional variable
            chopsticks[i] = lock.newCondition();
        }

    }

    private void test(int number) throws InterruptedException {
        if (state[left_phil(number)] != State.EATING && (state[number] == State.HUNGRY) && (state[right_phil(number)] != State.EATING)) {

            state[number] = State.EATING;

        } else {
            chopsticks[number].signal();
            

        }
    }

  
    int  left_phil(int number) {
        return (number - 1 + NUM_OF_PHILS) % NUM_OF_PHILS;

    }

    int right_phil(int number) {

        return (number + 1) % NUM_OF_PHILS;
    }
  // called by a philosopher when they wish to eat 
    @Override
    public void takeForks(int pnum) {
        lock.lock();
        try {
            state[pnum] = State.HUNGRY;
            test(pnum);
            if (state[pnum] != State.EATING) {
                chopsticks[pnum].await();
            }

        } catch (InterruptedException e) {
        } finally {
            lock.unlock();
        }

    }

    // called by a philosopher when they are finished eating 
    @Override
    public void returnForks(int pnum) {
        lock.lock();
        try{
            state[pnum]=State.THINKING;
            test(right_phil(pnum));
            test(left_phil(pnum));
            
            
        
        }
        catch(InterruptedException e){}
        finally{
        lock.unlock();
        }
    }
}
