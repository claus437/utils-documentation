package dk.fujitsu.utils.maven.documentation.logdoc;

import java.io.PrintStream;

/**
 * Created by IntelliJ IDEA.
 * User: claus
 * Date: 03-09-11
 * Time: 13:01
 * To change this template use File | Settings | File Templates.
 */
public class GenerateWork implements DocWorker {
    PrintStream printer;
    private int[] columnWidths;

    public GenerateWork(PrintStream out) {
        this.printer = out;
    }

    public void onFileBegin(String className) {
        printer.print("h5.");
        printer.println(className);

        printer.flush();

        printer.print("||");
        printer.print(String.format("%-" + columnWidths[1] + "s", "level"));
        printer.print("||");
        printer.print(String.format("%-" + columnWidths[2] + "s", "when"));
        printer.print("||");
        printer.print(String.format("%-" + columnWidths[3] + "s", "what"));
        printer.println("|");

        printer.flush();
    }


    public void doWork(String level, String comment, String statement) {
        printer.print("|");
        printer.print(String.format("%-" + (columnWidths[1] + 1) + "s", level));
        printer.print("|");
        printer.print(String.format("%-" + (columnWidths[2] + 1) + "s", comment));
        printer.print("|");
        printer.print(String.format("%-" + (columnWidths[3] + 1) + "s", statement));
        printer.println("|");

        printer.flush();
    }

    public void setColumnWidths(int[] columnWidths) {
        this.columnWidths = columnWidths;

        if (columnWidths[1] < "level".length()) {
            columnWidths[1] = "level".length();
        }

        if (columnWidths[2] < "when".length()) {
            columnWidths[2] = "when".length();
        }

        if (columnWidths[3] < "what".length()) {
            columnWidths[3] = "what".length();
        }
    }
}
