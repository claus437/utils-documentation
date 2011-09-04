package dk.fujitsu.utils.maven.documentation;

import dk.fujitsu.utils.maven.documentation.logdoc.LogDoc;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class DocRunner {
    private static final Logger LOGGER = Logger.getLogger(DocRunner.class);
    private List<FileHandler> handlerList;
    private File SourceFolder;

    public DocRunner() {
        handlerList = new ArrayList<FileHandler>();
    }

    public void setSourceFolder(File sourceFolder) {
        this.SourceFolder = sourceFolder;
    }

    public void setLogDocOutputFile(File outputFile) {
        handlerList.add(new LogDoc(outputFile));
    }

    public void run() {
        FileLoop loop;

        try {
            loop = new FileLoop(handlerList, SourceFolder);
            loop.run();
        } finally {
            destroy(handlerList);
        }
    }

    private void destroy(List<FileHandler> handlers) {
        for (FileHandler handler : handlers) {
            try {
                handler.destroy();
            } catch (Throwable x) {
                // @LOGDOC If a one the documentation extract runners threw an exception during destruction.
                LOGGER.warn("failed destroying handler " + handler.getClass().getCanonicalName());
            }
        }
    }

    public static void main(String[] args) {
        DocRunner runner;
        String value;

        if (args == null || args.length == 0) {
            showUsage();
            System.exit(1);
        }

        runner = new DocRunner();
        runner.setSourceFolder(new File(args[0]));

        value = getArgument(args, "--logdoc", "-ld");
        if (value == null) {
            runner.setLogDocOutputFile(new File("logdoc.output"));
        } else {
            runner.setLogDocOutputFile(new File(value));
        }

        runner.run();
    }


    public static void showUsage() {
        System.out.println("usage DocRunner [source] (-ld|--logdoc) outputfile)");
        System.out.println("");
        System.out.println("  -ld | --logdoc     The name of the file to store the extracted log statements");
        System.out.println("                     defaults to current director/logdoc.output");
    }

    public static String getArgument(String[] args, String... keys) {
        for (int i = 0; i < args.length; i++) {
            for (String key : keys) {
                if (args[i].equals(key)) {
                    if (args.length > i + 1) {
                        return args[i + 1];
                    } else {
                        throw new RuntimeException("missing value for argument " + key);
                    }
                }
            }
        }

        return null;
    }
}
