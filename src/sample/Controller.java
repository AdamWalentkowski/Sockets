package sample;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Controller {
    @FXML private Label statusLabel;
    @FXML private ProgressBar progressBar;
    @FXML private ListView<String> fileList;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private FileChooser fChooser = new FileChooser();
    {   fChooser.setTitle("Choose a file to send"); }

    @FXML
    public void ChooseButtonClick(){
        File selectedFile = fChooser.showOpenDialog(null);
        if (selectedFile != null) {
            Task<Void> sendFileTask = new SendFileTask(selectedFile); //klasa zadania
            fileList.getItems().add(selectedFile.getName());
            statusLabel.textProperty().bind(sendFileTask.messageProperty());
            progressBar.progressProperty().bind(sendFileTask.progressProperty());
            executor.submit(sendFileTask);
        }
        else{
            System.out.println("No file chosen or a file is invalid.");
        }
    }

}
