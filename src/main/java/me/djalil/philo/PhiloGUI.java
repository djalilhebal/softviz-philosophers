package me.djalil.philo;

import javafx.application.Application;
import javafx.concurrent.Worker;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.w3c.dom.Document;

public class PhiloGUI extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    TextArea $userVars = new TextArea();
    TextArea $userAlgo = new TextArea();
    Button $playButton = new Button("Play");
    Button $stopButton = new Button("Stop");
    Button $loadSolutionButton = new Button("Load a correct solution");
    WebView $webView = new WebView();
    WebEngine webEngine = $webView.getEngine();
    Document $doc = null;

    public void playSim() {
        $playButton.setDisable(true);
        $loadSolutionButton.setDisable(true);
        $stopButton.setDisable(false);

        String userVars = $userVars.getText();
        String userAlgo = $userAlgo.getText();

        SimControl.userVars = SimControl.wrapper + userVars;
        SimControl.userAlgo = SimControl.wrapper + userAlgo;

        System.out.println("Running simulation...");
        //SimControl.start();

        SimControl.setupSharedBindings();
        SimControl.model = new SimModel();
        SimControl.view = new SimViewSvg(SimControl.model, $doc);
        SimControl.model.start();
        SimControl.view.start();
    }

    public void stopSim() {
        System.out.println("Stopping the simulation...");
        try {
            SimControl.stop();
        } catch (Exception e) {}
        $playButton.setDisable(false);
        $loadSolutionButton.setDisable(false);
        $stopButton.setDisable(true);
    }

    // Should fail quietly
    public void loadPreset(String name) {
        String presetVars = readText("presets/" + name + ".vars.js");
        String presetAlgo = readText("presets/" + name + ".algo.js");
        $userVars.setText(presetVars);
        $userAlgo.setText(presetAlgo);
    }

    // Maybe `throws IOException`
    public String readText(String fileName) {
        return "FIXME: Read " + fileName;
    }

    // See http://docs.oracle.com/javafx/2/api/javafx/application/HostServices.html#showDocument(java.lang.String)
    void openInBrowser(String url) {
        getHostServices().showDocument(url);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        // ===== USER INPUTS =====

        $userVars.setPrefWidth(500);
        $userVars.setLayoutX(24.0);
        $userVars.setLayoutY(25.0);

        $userAlgo.setPrefWidth(500);
        $userAlgo.setPrefHeight(350);
        $userAlgo.setLayoutX(24);
        $userAlgo.setLayoutY(250);

        // ===== BUTTONS =====

        $playButton.setMinWidth(50);
        $playButton.setLayoutX(74);
        $playButton.setLayoutY(610);
        $playButton.setOnAction(action -> {
            playSim();
        });

        $stopButton.setMinWidth(50);
        $stopButton.setLayoutX(159);
        $stopButton.setLayoutY(610);
        $stopButton.setDisable(true);
        $stopButton.setOnAction(action -> {
            stopSim();
        });

        $loadSolutionButton.setMinWidth(50);
        $loadSolutionButton.setLayoutX(242);
        $loadSolutionButton.setLayoutY(610);
        $loadSolutionButton.setOnAction(action -> {
            //loadPreset("sol2");

            $userVars.setText(
                "// \"state enum\" sorta\n" +
                "THINKING = 0;\n" +
                "HUNGRY   = 1;\n" +
                "EATING   = 2;\n" +
                "\n" +
                "// All philosophers start THINKING\n" +
                "state = [ THINKING, THINKING, THINKING, THINKING, THINKING ];\n" +
                "\n" +
                "mutex = Sem(1);\n" +
                "\n" +
                "s = [ Sem(0), Sem(0), Sem(0), Sem(0), Sem(0) ];"
            );
            $userAlgo.setText(
                "function philosopher(i) {\n" +
                "  while (true) {\n" +
                "    think();\n" +
                "    takeBothForks(i);\n" +
                "    eat();\n" +
                "    putBothForks(i);\n" +
                "  }\n" +
                "}\n" +
                "\n" +
                "function takeBothForks(i) {\n" +
                "  p(mutex);\n" +
                "  state[i] = HUNGRY;\n" +
                "  test(i);\n" +
                "  v(mutex);\n" +
                "  p(s[i]);\n" +
                "  takeFork( i );\n" +
                "  takeFork( (i-1+N)%N );\n" +
                "}\n" +
                "\n" +
                "function putBothForks(i) {\n" +
                "  p(mutex);\n" +
                "  putFork( i );\n" +
                "  putFork( (i-1+N)%N );\n" +
                "  state[i] = THINKING;\n" +
                "  test( (i+1)%N );\n" +
                "  test( (i-1+N)%N );\n" +
                "  v(mutex);\n" +
                "}\n" +
                "\n" +
                "function test(k) {\n" +
                "  if (\n" +
                "    state[k] == HUNGRY &&\n" +
                "    state[ (k-1+N)%N ] != EATING &&\n" +
                "    state[ (k+1)%N ] != EATING\n" +
                "   ) {\n" +
                "      state[k] = EATING;\n" +
                "      v(s[k]);\n" +
                "  }\n" +
                "}"
            );
        });

        Hyperlink $readMeOnGithub = new Hyperlink("README on GitHub");
        $readMeOnGithub.setLayoutX(750);
        $readMeOnGithub.setLayoutY(650);
        $readMeOnGithub.setOnAction(event -> {
            openInBrowser("https://github.com/dreamski21/unlearned");
        });

        Line $line = new Line();
        $line.setEndX(-100);
        $line.setEndY(450);
        $line.setLayoutX(720);
        $line.setLayoutY(115);
        $line.setStartX(-100);
        $line.setStartY(-74);

        // WebView
        $webView.setPrefHeight(600);
        $webView.setPrefWidth(650);
        $webView.setLayoutX(700);
        $webView.setLayoutY(26);
        $webView.setBlendMode(BlendMode.DARKEN);

        // It's recommended to add listener in case of the svg file didn't load from the first try
        // TODO: Maybe only show() the stage when the $doc is ready.
        webEngine.getLoadWorker().stateProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue == Worker.State.SUCCEEDED) {
                        $doc = webEngine.getDocument();
                    }
                }
        );
        webEngine.load(getClass().getResource("table.svg").toExternalForm());

        Group root = new Group(
                $userVars, $userAlgo,
                $playButton, $stopButton, $loadSolutionButton, $line,
                $webView, $readMeOnGithub
        );
        Scene scene = new Scene(root, 1280, 690);
        scene.setFill(Color.GHOSTWHITE);

        primaryStage.setTitle("Philo: Les Philosophes");
        primaryStage.setResizable(false); // TODO: At least make it maximizable
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
