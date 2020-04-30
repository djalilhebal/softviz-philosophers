package me.djalil.philo;

public class SimViewText implements SimView {

/*
    static String viewSample =
        " LEGEND  fork:holder                             \n" +
        "                                                 \n" +
        "                 +-------------+                 \n" +
        "                 |0/  Plato    |                 \n" +
        "                 | is thinking |                 \n" +
        "                 |             |                 \n" +
        "                 | Health: 20  |                 \n" +
        "                 +-------------+                 \n" +
        " +-------------+  4:4        0:* +-------------+ \n" +
        " |4/  Sartre   |                 |1/ Aristotle | \n" +
        " | has starved |                 | is thinking | \n" +
        " |             |                 |             | \n" +
        " | Health:   0 |                 | Health: 40  | \n" +
        " +-------------+  3:2  2:2   1:2 +-------------+ \n" +
        "         +-------------+  +-------------+        \n" +
        "         |3/  Russell  |  |2/  Mill     |        \n" +
        "         | is sleeping |  |   is eating |        \n" +
        "         |             |  |             |        \n" +
        "         | Health: 56  |  | Health: 100 |        \n" +
        "         +-------------+  +-------------+        ";
*/
    static String viewTemplate =
        " LEGEND  fork:holder                             \n" +
        "                                                 \n" +
        "                 +-------------+                 \n" +
        "                 |0/  Plato    |                 \n" +
        "                 | %11s |                 \n" +
        "                 |             |                 \n" +
        "                 | Health: %3d |                 \n" +
        "                 +-------------+                 \n" +
        " +-------------+  4:%c        0:%c +-------------+ \n" +
        " |4/  Sartre   |                 |1/ Aristotle | \n" +
        " | %11s |                 | %11s | \n" +
        " |             |                 |             | \n" +
        " | Health: %3d |                 | Health: %3d | \n" +
        " +-------------+  3:%c  2:%c   1:%c +-------------+ \n" +
        "         +-------------+  +-------------+        \n" +
        "         |3/  Russell  |  |2/  Mill     |        \n" +
        "         | %11s |  | %11s |        \n" +
        "         |             |  |             |        \n" +
        "         | Health: %3d |  | Health: %3d |        \n" +
        "         +-------------+  +-------------+        \n" ;

    DinnerTable t;
    Philosopher[] p;
    boolean stopped = false;

    SimViewText(SimModel model) {
        this.t = model.table;
        this.p = model.philos;
    }

    // Describe e.g. "Plato is thinking"
    String narr(Philosopher p) {
        return Philosopher.narrate(p);
    }

    public void redraw() {
        String view = String.format(viewTemplate,
            narr(p[0]), p[0].getHealth(),
            t.get(4), t.get(0),
            narr(p[4]), narr(p[1]), p[4].getHealth(), p[1].getHealth(),
            t.get(3), t.get(2), t.get(1),
            narr(p[3]), narr(p[2]), p[3].getHealth(), p[2].getHealth()
        );
        System.out.print("\033\143"); // to clear the screen on Linux
        System.out.println(view);
    }

    public void stop() {
        stopped = true;
    }

    public void start() {
        // Redraw every 0.25 secs
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        if (stopped) { this.cancel(); return; }
                        redraw();
                    }
                },
                0,
                250 // 0.25 sec
        );
    }
}
