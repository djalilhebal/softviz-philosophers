package me.djalil.philo;

import javax.script.*;

public class Philosopher extends Thread {

    int i;
    String color;
    String activity = "STARTING";
    String errorMessage = null;
    int health = 100;

    ScriptEngine engine;

    Philosopher(String name, String color, int i) {
        this.i = i;
        this.setName(name);
        this.color = color;

        ScriptEngineManager manager = new ScriptEngineManager();
        engine = manager.getEngineByName("nashorn");
    }

    @Override
    public void run() {
        try {
            engine.getBindings(SimpleScriptContext.ENGINE_SCOPE).putAll(SimControl.sharedBindings);
            engine.put("_activity", this.activity);
            engine.put("_i", this.i);
            engine.eval(SimControl.userAlgo);
            engine.eval("(typeof philosopher == 'function' ? philosopher : philosophe)(_i);");
        } catch (Exception e) { // ScriptException | InterruptedException
            activity = "ERROR";
            errorMessage = e.getMessage();

            if (SimControl.DEBUG) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

    /**
     * Throws an interruptedException with a 'reason' message
     * The final message should read "PHILOSOPHER panicked because he REASON"
     */
    void maybePanic() throws InterruptedException {
        /*
        - Wants to eat but holding no fork

        - Can't reach fork

        - "There's no spoon" *-* if forkIndex not in [0; N[

        - Trying to take a fork that is already held, or:
        - Their held fork gets stolen

        like:
        throw new InterruptedException("could not reach the fork");
        or maybe use `PhilosopherPanic extends Exception`?
         */
        if (this.interrupted()) {
            throw new InterruptedException("was interrupted");
        }
    }

    // public String getName() { return name; }

    public String getColor() { return this.color; }

    // Has this philosopher starved or panicked?
    public boolean isDead() {
        return health == 0 || activity.equals("ERROR");
    }

    public String getActivity() {
        if (!isDead()) {
            // Sync the 'activity' value from the engine
            String _activity = (String)engine.get("_activity");
            if (_activity != null) {
                activity = _activity;
            }
        }
        return activity;
    }

    public String getNarration() {
        return narrate(this);
    }

    /**
     * Narrate the Philosopher's activity
     * e.g. "is thinking" or "is sleeping" or "messed up"
     */
    static String narrate(Philosopher p) {
        boolean isBlocked = p.getState().equals(Thread.State.BLOCKED) || p.getState().equals(Thread.State.WAITING);
        if (p.getHealth() == 0) {
            return "has starved";
        } else if (isBlocked) {
            return "is sleeping";
        } else {
            String doingOrError = p.getActivity();
            if (doingOrError.equals("ERROR")) {
                return "messed up";
            } else {
                return "is " + doingOrError.toLowerCase();
            }
        }
    }

    public int getHealth() {
        return health;
    }

    // Change `heath` by `delta` but always keep it between 0 and 100
    public void setHealthDelta(int delta) {
        health += delta;

        if (health > 100) {
            health = 100;
        } else if (health < 0) {
            health = 0;
        }
    }

}
