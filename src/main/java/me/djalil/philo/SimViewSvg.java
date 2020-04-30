package me.djalil.philo;

import javafx.application.Platform;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.HashMap;

public class SimViewSvg implements SimView {

    // TODO: Maybe use $forksMap, $narrationMap, $healthMap -- simpler and more efficient: HashMap<int, Element>.get(i)
    HashMap<String, Element> $forksMap = new HashMap<>();
    HashMap<String, Element> $philosMap = new HashMap<>();
    boolean stopped = false;
    SimModel model;

    public SimViewSvg(SimModel model, Document $doc) {
        this.model = model;

        // Cache needed display elements
        for (int i = 0; i < 5; i++) {
            $forksMap.put("f" + i, $doc.getElementById("f" + i));
        }

        for (int i = 0; i < 5; i++) {
            String descId = String.format("p%d-desc", i);
            String hpId = String.format("p%d-hp", i);
            $philosMap.put("desc" + i, $doc.getElementById(descId));
            $philosMap.put("hp" + i, $doc.getElementById(hpId));
        }

    }

    public void start() {
        // FIXME: This is just an ugly double-hack
        // Redraw every 0.5 secs
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        if (stopped) { this.cancel(); return; }
                        Platform.runLater(() -> redraw());
                    }
                },
                0,
                500 // 0.5 sec
        );
    }

    public void stop() {
        stopped = true;
        // FIXME: Is this the correct way to free elements?
        $forksMap.clear();
        $philosMap.clear();
    }

    public void redraw() {
        redrawPhilos();
        redrawForks();
    }

    void redrawPhilos() {
        for (Philosopher p : model.philos) {
            $philosMap.get("desc" + p.i).setTextContent(p.getNarration());
            $philosMap.get("hp" + p.i).setTextContent(p.getHealth() + "%");
        }
    }

    // Set the color of each fork to match its holder
    void redrawForks() {
        for (int i = 0; i < 5; i++) {
            int holder = model.table.forks[i];
            String newColor;
            if (holder == -1) {
                newColor = "black";
            } else {
                newColor = model.philos[holder].getColor();
            }
            $forksMap.get("f" + i).setAttribute("fill", newColor);
        }
    }

}
