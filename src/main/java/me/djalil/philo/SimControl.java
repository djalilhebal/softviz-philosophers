package me.djalil.philo;

import javax.script.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SimControl {
    static final boolean DEBUG = true;

    static boolean stopped = false;
    static Bindings sharedBindings;

    static SimModel model;
    static SimView view;

    static String userVars, userAlgo;

    public static void main(String[] args) {
        if (args.length == 1) {
            try {
                String presetName = args[0];
                Path varsPath = Paths.get("presets/" + presetName + ".vars.js");
                Path algoPath = Paths.get("presets/" + presetName + ".algo.js");
                userVars = wrapper + new String(Files.readAllBytes(varsPath), "UTF-8");
                userAlgo = wrapper + new String(Files.readAllBytes(algoPath), "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }

        SimControl.start();
    }

    public static void start() {
        setupSharedBindings();

        model = new SimModel();
        model.start();

        view = new SimViewText(model);
        view.start();
    }

    public static void stop() {
        stopped = true;
        view.stop();
        model.stop();
    }

    static void setupSharedBindings() {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("nashorn");
        engine.put("N", SimModel.N);
        try {
            engine.eval(userVars);
            sharedBindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
        } catch (ScriptException e) {
            // Alert user that there's a problem in the variable definitions...
            e.printStackTrace();
            System.exit(1);
        }
    }

    // Act on behalf of Philosopher 'who'
    // FIXME: `engine.put("_thread", this)` in Java and `_thread.meth()` in JS  didn't work
    public static void act(int who, String what, int param) throws InterruptedException {
        Philosopher p = model.philos[who];
        p.maybePanic();
        if (p.isDead()) return;

        if (what.equals("fillHealth")) {
            p.setHealthDelta(100);
        } else if (what.equals("setOwner")) {
            model.table.setOwner(param, who);
        } else if (what.equals("unsetOwner")) {
            model.table.unsetOwner(param, who);
        }
    }

    // TODO: wrapper = getResource...
    static String wrapper =
            "// injected: _i, _activity\n" +
            "\n" +
            "var Semaphore = Java.type(\"java.util.concurrent.Semaphore\");\n" +
            "var Thread = Java.type(\"java.lang.Thread\");\n" +
            "var SimControl = Java.type(\"me.djalil.philo.SimControl\");\n" +
            "\n" +
            "function askMaster(what, param) {\n" +
            "  if (param == undefined) {\n" +
            "    param = -1;\n" +
            "  }\n" +
            "  SimControl.act(_i, what, param);\n" +
            "}\n" +
            "\n" +
            "function sleepSecs(n) {\n" +
            "  Thread.sleep(n * 1000);\n" +
            "}\n" +
            "\n" +
            "function Sem(n) {\n" +
            "  return new Semaphore(n, true);\n" +
            "}\n" +
            "\n" +
            "function p(x) {\n" +
            "  x.acquire();\n" +
            "}\n" +
            "\n" +
            "function v(x) {\n" +
            "  x.release();\n" +
            "}\n" +
            "\n" +
            "function getRandomInt(min, max) {\n" +
            "  min = Math.ceil(min);\n" +
            "  max = Math.floor(max);\n" +
            "  return Math.floor(Math.random() * (max - min + 1)) + min;\n" +
            "}\n" +
            "\n" +
            "function takeFork(j) {\n" +
            "  askMaster(\"setOwner\", j);\n" +
            "}\n" +
            "\n" +
            "function putFork(j) {\n" +
            "  askMaster(\"unsetOwner\", j);\n" +
            "}\n" +
            "\n" +
            "function think() {\n" +
            "  _activity = \"THINKING\";\n" +
            "  sleepSecs(getRandomInt(5, 10));\n" +
            "  //_activity = \"WORKING\";\n" +
            "}\n" +
            "\n" +
            "function eat() {\n" +
            "  _activity = \"EATING\";\n" +
            "  sleepSecs(getRandomInt(5, 10));\n" +
            "  askMaster(\"fillHealth\");\n" +
            "  //_activity = \"WORKING\";\n" +
            "}\n" +
            "\n" +
            "// Translate to French\n" +
            "var manger = eat;\n" +
            "var penser = think;\n" +
            "var prendreFourchette = takeFork;\n" +
            "var poserFourchette = putFork;\n" +
            "// (typeof philosopher == 'function' ? philosopher : philosophe)(i);\n";
}
