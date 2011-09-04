package dk.fujitsu.utils.maven.documentation.logdoc;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class GenerateWorkTest {
    private GenerateWork subject = new GenerateWork(System.out);
    private List<String> lines;

    @Before
    public void setup() {
        lines = new ArrayList<String>();

        subject.printer = new PrintWriterStub(lines);
    }

    @Test
    public void testOutputBelow() {
        subject.setColumnWidths(new int[]{6, 1, 1, 1});

        subject.onFileBegin("MyTest");
        subject.doWork("1", "2", "3");

        subject.printer.flush();

        Assert.assertEquals(3, lines.size());
        Assert.assertEquals("h5.MyTest", lines.get(0));
        Assert.assertEquals("||level||when||what|", lines.get(1));
        Assert.assertEquals("|1     |2    |3    |", lines.get(2));
    }

    @Test
    public void testGreater() {
        subject.setColumnWidths(new int[]{6, 7, 8, 9});

        subject.onFileBegin("MyTest");
        subject.doWork("1", "2", "3");

        subject.printer.flush();

        Assert.assertEquals(3, lines.size());
        Assert.assertEquals("h5.MyTest", lines.get(0));
        Assert.assertEquals("||level  ||when    ||what     |", lines.get(1));
        Assert.assertEquals("|1       |2        |3         |", lines.get(2));
    }


    private class PrintWriterStub extends PrintStream {
        private List<String> lines;
        private StringBuffer buffer;

        public PrintWriterStub(List<String> lines) {
            super(new ByteArrayOutputStream());
            this.lines = lines;
            buffer = new StringBuffer();
        }

        @Override
        public void print(String s) {
            buffer.append(s);
        }

        public void println(String s) {
            buffer.append(s);
            lines.add(buffer.toString());
            buffer.setLength(0);
        }
    }
}
