package sample;

import javafx.concurrent.Task;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.ConnectException;
import java.net.Socket;

public class SendFileTask extends Task<Void> {
    File file; //plik do wysłania
    private final static int port = 55000;

    public SendFileTask(File file) {
        this.file = file;
    }

    @Override
    protected Void call() throws Exception {
        updateMessage("Initiating...");
        try {
            Socket clientSocket = new Socket("localhost", port);

            if (clientSocket.isConnected()) {
                updateMessage("In progress");
                FileInputStream in = new FileInputStream(file);
                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
                out.writeUTF(file.getName());
                byte[] buffer = new byte[4096]; //bufor 4KB
                int readSize = 0, progress = 0;
                while ((readSize = in.read(buffer)) > 0) {
                    progress += readSize;
                    out.write(buffer, 0, readSize);
                    updateProgress(progress, file.length());
                    Thread.sleep(2);

                    updateMessage(String.format("%.2f %%", (float)((float)progress / (float)file.length() * 100.f)));
                }
                updateMessage("Completed!");
                in.close();
                out.close();
                clientSocket.close();
            } else {
                throw new ConnectException();
            }
        } catch (ConnectException e) {
            System.out.println("Cannot connect.");
        }
        return null;
    }
}