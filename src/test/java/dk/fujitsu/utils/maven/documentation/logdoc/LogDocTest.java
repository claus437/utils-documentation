package dk.fujitsu.utils.maven.documentation.logdoc;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: claus
 * Date: 03-09-11
 * Time: 13:22
 * To change this template use File | Settings | File Templates.
 */
public class LogDocTest {
    private LogDoc subject;
    private String comment;
    private String statement;
    private String level;
    private String className;
    private int doWork;

    @Before
    public void setup() {
        className = "LogTest";
        comment = "A B";
        statement = "\"logging\"";
        level = "info";

        subject = new LogDoc(new File("./test.log"));
        subject.preparationWorker = new PrepareWorkStub(this);
        subject.generatorWorker = new GenerateWorkStub(this);

        subject.onRoundBegin(new File("src/java"));
        subject.onFileBegin(new File("src/java/LogTest.java"));
    }

    @Test
    public void testLevelParsing() {
        String[] levels = {"trace", "debug", "info", "warn", "error", "fatal"};

        for (int i = 0; i < levels.length; i++) {
            level = levels[i];

            subject.onLineRead("//@LOGDOC A B");
            subject.onLineRead("LOG." + level + "(\"logging\")");

            Assert.assertEquals(i + 1, doWork);
        }
    }

    @Test
    public void testStatementParsing() {
        String[] statements = {
            "\"logging\" + getA() + \" of \" + getB()",
            "getA() + \" of \" + getB() + \" is a\"",
            " get() "
        };

        for (int i = 0; i < statements.length; i++) {
            statement = statements[i];

            subject.onLineRead("//@LOGDOC A B");
            subject.onLineRead("    ");
            subject.onLineRead("LOG.info(" + statement + ")");

            Assert.assertEquals(i + 1, doWork);
        }
    }

    @Test
    public void testCanHaveEmptyMiddleLines() {
        subject.onLineRead("//@LOGDOC A B");
        subject.onLineRead("    ");
        subject.onLineRead("LOG.info(\"logging\")");

        Assert.assertEquals(1, doWork);
    }

    @Test
    public void testModeSwitch() {
        subject.worker = null;

        subject.onRoundBegin(new File("."));

        subject.onFileBegin(new File("./LogTest.java"));
        Assert.assertEquals(subject.preparationWorker, subject.worker);
        Assert.assertEquals(true, subject.onFileComplete());

        subject.onFileBegin(new File("./LogTest.java"));
        Assert.assertEquals(subject.generatorWorker, subject.worker);
        Assert.assertEquals(false, subject.onFileComplete());
    }

    private class PrepareWorkStub extends PrepareWork {
        private LogDocTest subject;

        public PrepareWorkStub(LogDocTest subject) {
            this.subject = subject;
        }

        public void onFileBegin(String className) {
            Assert.assertEquals("className incorrect", subject.className, className);
        }

        public void doWork(String level, String comment, String statement) {
            doWork ++;
            Assert.assertEquals("comment incorrect", subject.comment, comment);
            Assert.assertEquals("statement incorrect", subject.statement, statement);
            Assert.assertEquals("level incorrect", subject.level, level);
        }

        public void onFileComplete() {
        }
    }

    private class GenerateWorkStub extends GenerateWork {
        private LogDocTest subject;

        public GenerateWorkStub(LogDocTest subject) {
            super(null);
            this.subject = subject;
        }

        @Override
        public void onFileBegin(String className) {
        }

        @Override
        public void doWork(String level, String comment, String statement) {
            doWork++;
        }

        @Override
        public void setColumnWidths(int[] columnWidths) {
        }

    }
}
