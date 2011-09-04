package dk.fujitsu.utils.maven.documentation.logdoc;

public class PrepareWork implements DocWorker {
    private int[] columnWidths;


    public PrepareWork() {
        this.columnWidths = new int[4];
    }

    public void onFileBegin(String className) {
        if (className.length() > columnWidths[0]) {
            columnWidths[0] = className.length();
        }
    }

    public void doWork(String level, String comment, String statement) {
        updateMaxWidths(level, comment, statement);
    }

    public void updateMaxWidths(String... values) {
        for (int i = 0; i < values.length; i++) {
            if (values[i].length() > columnWidths[i + 1]) {
                columnWidths[i + 1] = values[i].length();
            }
        }
    }


    public int[] getColumnWidths() {
        return columnWidths;
    }
}
