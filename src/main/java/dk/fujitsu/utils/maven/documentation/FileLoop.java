package dk.fujitsu.utils.maven.documentation;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileLoop {
    private List<FileHandler> handlerList;
    private List<FileHandler> fileHandlers;
    private File root;

    public FileLoop(List<FileHandler> handlerList, File root) {
        this.handlerList = new ArrayList<FileHandler>(handlerList);
        this.root = root;
    }

    public void run() {
        notifyOnRoundBegin(root);

        loop(root.listFiles());

        notifyOnRoundComplete();

        while (!handlerList.isEmpty()) {
            run();
        }
    }

    private void loop(File[] files) {
        for (File file : files) {
            if (file.isDirectory()) {
                loop(file.listFiles());
            } else {
                fileHandlers = new ArrayList<FileHandler>(handlerList);

                while(!fileHandlers.isEmpty()) {
                    readFile(file);
                }
            }
        }
    }

    private void readFile(File file) {
        BufferedReader reader;
        String line;
        int lineNo;

        notifyOnBeginFile(file);

        reader = openFile(file);
        lineNo = 0;

        try {
            while((line = reader.readLine()) != null) {
                lineNo ++;
                notifyOnLineRead(line);
            }
        } catch (Throwable x) {
            throw new RuntimeException("error in " + file.getAbsolutePath() + " on line " + lineNo + " " + x.getMessage(), x);
        } finally {
            close(reader);
        }

        notifyOnFileComplete();
    }


    private BufferedReader openFile(File file) {
        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader(file));
        } catch (IOException x) {
            throw new RuntimeException(x);
        }

        return reader;
    }

    private void close(Closeable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (IOException x) {
                x.printStackTrace();
            }
        }
    }

    private void notifyOnRoundBegin(File base) {
        for (FileHandler handler : handlerList) {
            handler.onRoundBegin(base);
        }
    }

    private void notifyOnBeginFile(File file) {
        for (FileHandler handler : handlerList) {
            handler.onFileBegin(file);
        }
    }

    private void notifyOnLineRead(String line) {
        for (FileHandler handler : handlerList) {
            handler.onLineRead(line);
        }
    }

    private void notifyOnFileComplete() {
        List<FileHandler> removeList;

        removeList = new ArrayList<FileHandler>();

        for (FileHandler handler : handlerList) {
            if(!handler.onFileComplete()) {
                removeList.add(handler);
            }
        }

        fileHandlers.removeAll(removeList);
    }

    private void notifyOnRoundComplete() {
        List<FileHandler> removeList;

        removeList = new ArrayList<FileHandler>();

        for (FileHandler handler : handlerList) {
            if (!handler.onRoundComplete()) {
                removeList.add(handler);
            }
        }

        handlerList.removeAll(removeList);
    }
}
