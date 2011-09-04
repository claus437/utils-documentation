package dk.fujitsu.utils.maven.documentation.logdoc;

import dk.fujitsu.utils.maven.documentation.FileHandler;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogDoc implements FileHandler {
    private static final Logger LOGGER = Logger.getLogger(LogDoc.class);
    private static String[] levels = {"trace", "debug", "info", "warn", "error", "fatal"};
    private File base;
    private String comment;
    DocWorker worker;
    PrepareWork preparationWorker;
    GenerateWork generatorWorker;
    private PrintStream out;

    public LogDoc(File file) {
        out = openFile(file);

        preparationWorker = new PrepareWork();
        generatorWorker = new GenerateWork(out);

        // @LOGDOC Whenever a new LogDoc Object has been created.
        LOGGER.debug("LogDoc initialized with out file " + file.getAbsolutePath());
    }

    public void onRoundBegin(File base) {
        this.base = base;
        // @LOGDOC when a new round begins where logdoc is part of
        LOGGER.debug("begin round for base " + base.getAbsolutePath());
    }

    public void onFileBegin(File file) {
        toggleWorker();

        // @LOGDOC When logdoc is requested to start extraction on a file
        LOGGER.debug("started work on file " + file.getAbsolutePath() + " with worker " + worker.getClass().getSimpleName());
        worker.onFileBegin(getClassName(file));
    }

    public void onLineRead(String line) {
        String level;
        String statement;

        if (line.contains("@" + "LOGDOC")) {
            // @LOGDOC When LOGDOC was found in a file
            LOGGER.debug("found LOGDOC in line " + line);

            comment = line.substring(line.indexOf("@" + "LOGDOC") + ("@" +"LOGDOC").length()).trim();
            return;
        }

        if (comment == null) {
            return;
        }

        if (line.trim().isEmpty()) {
            // @LOGDOC When an empty line was found between LOGDOC and the log statement
            LOGGER.debug("found LOGDOC in line " + line);

            return;
        }

        level = getLevel(line);
        statement = getStatement(line);

        worker.doWork(level, comment, statement);

        // @LOGDOC When the complete log entry has successfully been parsed
        LOGGER.debug("found LOGDOC in line " + line);

        comment = null;
    }

    public boolean onFileComplete() {
        generatorWorker.setColumnWidths(preparationWorker.getColumnWidths());

        // @LOGDOC When a file has been fully read
        LOGGER.debug("file read request for another round " + (worker == preparationWorker));

        return worker == preparationWorker;
    }

    public boolean onRoundComplete() {
        return false;
    }

    public void destroy() {
        if (out != null) {
            out.flush();
            out.close();
        }

        // @LOGDOC when everything is done and there is nothing more to do.
        LOGGER.info("LogDoc destroyed");
    }

    private String getLevel(String line) {
        for (String level : levels) {
            if (line.matches(".*\\." + level + "\\W*\\(.*")) {
                return level;
            }
        }

        throw new RuntimeException("unable to find level in line \"" + line + "\" known levels are " + Arrays.asList(levels));
    }

    private String getStatement(String line) {
        Pattern pattern;
        Matcher matcher;

        pattern = Pattern.compile("\\((.*)\\)");
        matcher = pattern.matcher(line);

        if (!matcher.find()) {
            throw new RuntimeException("failed extracting log statement from line " + line);
        }

        return matcher.group(1);
    }

    private String getClassName(File file) {
        String className;

        className = file.getAbsolutePath().substring(base.getAbsolutePath().length() + 1);
        className = className.substring(0, className.length() - ".java".length());

        if (File.separator.equals("\\")) {
            className = className.replaceAll("\\\\", ".");
        } else {
            className = className.replaceAll(File.separator, ".");
        }

        return className;
    }

    private void toggleWorker() {
        if (worker == null) {
            worker = preparationWorker;
            return;
        }

        if (worker == preparationWorker) {
            worker = generatorWorker;
        } else {
            worker = preparationWorker;
        }

        // @LOGDOC whenever the internal worker is toggled
        LOGGER.debug("worker changed to" + worker.getClass().getSimpleName());
    }

    private PrintStream openFile(File file) {
        PrintStream stream;

        try {
            stream = new PrintStream(new FileOutputStream(file));
        } catch (IOException x) {
            throw new RuntimeException("unable to open file " + file.getAbsolutePath() + " for writing, " + x.getMessage());
        }

        return stream;
    }
}
