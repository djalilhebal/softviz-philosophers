package me.djalil.philo;

public class SimModel {

    static String[] NAMES = {"Plato", "Aristotle", "Mill", "Russell", "Sartre"};
    static String[] COLORS = {"olive", "blue", "magenta", "green", "red"};
    static int N = NAMES.length;

    Philosopher[] philos;
    DinnerTable table;
    boolean stopped = false;

    SimModel() {
        table = new DinnerTable();
        philos = new Philosopher[NAMES.length];
        for (int i = 0; i < NAMES.length; i++) {
            philos[i] = new Philosopher(NAMES[i], COLORS[i], i);
        }
    }

    public void start() {
        for (Philosopher p: philos) {
            p.start();
        }

        // Decrease 'health' of all Philosophers by 1 point every second
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        if (stopped) { this.cancel(); return; }
                        for (Philosopher p : philos) {
                            if (!p.isDead() && !p.getActivity().equals("EATING")) {
                                p.setHealthDelta(-1);
                            };
                        }
                    }
                },
                0,
                1000 // 1 sec
        );

    }

    public void stop() {
        stopped = true;
        for (Philosopher p : philos) {
            p.interrupt();
        }
    }

    // Before restarting a sim, assert everything is clean
    void stopDangerously() {
        for (Philosopher p : philos) {
            if (p.isAlive()) {
                p.stop();
            }
        }
    }

}
