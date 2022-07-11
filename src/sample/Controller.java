package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;

import java.io.*;

public class Controller implements Runnable {
    boolean useAppText = false;
    String target;

    @FXML
    private Button btnst;

    @FXML
    public TextArea txtinfo;

    @FXML
    private ProgressIndicator spinner;

    @FXML
    public void initialize() {
        spinner.setVisible(false);
        btnst.setOnAction(event -> {
            txtinfo.clear();
            ping();
        });
        // 5675757
    }

    public void ping() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        spinner.setVisible(true);
        btnst.setDisable(true);

        try {
            final ProcessBuilder processBuilder = new ProcessBuilder("cmd", "/C", "powercfg /batteryreport");
            final Process process = processBuilder.start();

            final InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream(), "Cp866");
            final BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String s = bufferedReader.readLine();
            if (s == null) {
                final InputStreamReader errStreamReader = new InputStreamReader(process.getErrorStream(), "Cp866");
                final BufferedReader bufferedErr = new BufferedReader(errStreamReader);
                while (appendErr(bufferedErr)) {
                }
                bufferedErr.close();
                errStreamReader.close();
            }
            else {
                target = s.substring(s.lastIndexOf(" ") + 1, s.lastIndexOf("."));
            }

            bufferedReader.close();
            inputStreamReader.close();

            process.waitFor();
            process.destroy();
        } catch (final Exception ex) {
            ex.printStackTrace();
        }

        Platform.runLater(() -> {
            btnst.setDisable(false);
            spinner.setVisible(false);
            if (!useAppText) {
                txtinfo.appendText("Полный отчет о батарее: " + target + "\n");
                txtinfo.appendText("Износ батареи составляет " + Parce.targetFile(target) + " %");
            }
        });
    }

    private boolean appendErr(final BufferedReader reader) {
        useAppText = true;
        try {
            String line = reader.readLine();
            if (line == null) {
                return false;
            }
                Platform.runLater(() -> txtinfo.appendText(line + "\n"));
            return true;

        } catch (final IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}

