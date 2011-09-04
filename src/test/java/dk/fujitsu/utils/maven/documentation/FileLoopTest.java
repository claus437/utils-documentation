package dk.fujitsu.utils.maven.documentation;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FileLoopTest implements FileHandler {
    private List<FileHandler> handlerList;
    private FileLoop subject;
    private List<String> onRoundBegin;
    private List<String> onFileBegin;
    private int onLineRead;
    private int onFileComplete;
    private int onRoundComplete;
    private int rounds;
    private int fileRounds;


    @Before
    public void setup() {

        onFileBegin = new ArrayList<String>();
        onRoundBegin = new ArrayList<String>();
        rounds = 1;
        fileRounds = 1;

        handlerList = new ArrayList<FileHandler>();
        handlerList.add(this);
        subject = new FileLoop(handlerList, new File("src/test/resources/testdir"));
    }

    @Test
    public void testRun() {
        subject.run();

        Collections.sort(onFileBegin);

        Assert.assertEquals(Arrays.asList("testdir"), onRoundBegin);
        Assert.assertEquals(1, onRoundComplete);
        Assert.assertEquals(3, onFileComplete);
        Assert.assertEquals(Arrays.asList("TestA.java", "TestB.java", "TestC.java"), onFileBegin);
        Assert.assertEquals(38, onLineRead);
        Assert.assertEquals(3, onFileComplete);
    }


    @Test
    public void testDoubleRound() {
        rounds = 2;

        subject.run();

        Assert.assertEquals(2, onRoundComplete);
        Assert.assertEquals(2, onRoundBegin.size());

        Assert.assertEquals(1, handlerList.size());
    }

    @Test
    public void testSingleRound() {
        subject.run();

        Assert.assertEquals(1, onRoundComplete);
        Assert.assertEquals(1, onRoundBegin.size());

        Assert.assertEquals(1, handlerList.size());
    }



    public void onRoundBegin(File file) {
        onRoundBegin.add(file.getName());
    }

    public boolean onRoundComplete() {
        onRoundComplete ++;
        return onRoundComplete != rounds;
    }

    public void onFileBegin(File file) {
        this.onFileBegin.add(file.getName());
    }

    public void onLineRead(String line) {
        this.onLineRead ++;
    }

    public boolean onFileComplete() {
        this.onFileComplete++;
        return false;
    }

    public void destroy() {

    }
}
