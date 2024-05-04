package edu;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TablePrinter {
    private static final Logger LOGGER = LogManager.getLogger();

    private TablePrinter() {

    }

    public static void printTable(String[] tableHeader, String[][] table) {
        int[] widths = calculateWidths(tableHeader, table);

        printTableRow(tableHeader, widths);

        StringBuilder line = new StringBuilder("|");
        for (int width: widths) {
            line.append("-".repeat(width + 2));
            line.append("|");
        }
        LOGGER.info(line);

        for (String[] row: table) {
            printTableRow(row, widths);
        }
    }

    private static void printTableRow(String[] tableRow, int[] widths) {
        StringBuilder row = new StringBuilder("|");

        for (int i = 0; i < tableRow.length; i++) {
            row.append(" ");
            row.append(centerString(tableRow[i], widths[i]));
            row.append("|");
        }

        LOGGER.info(row);
    }

    private static String centerString(String str, int width) {
        int strLength = str == null ? 1 : str.length();

        int offset = Math.max(width - strLength, 0);
        int leftOffset = offset / 2;
        int rightOffset = offset - offset / 2;

        String correctString = str == null ? "-" : str;

        return " ".repeat(leftOffset) + correctString + " ".repeat(rightOffset);
    }

    private static int[] calculateWidths(String[] header, String[][] table) {
        int n = header.length;
        int[] widths = new int[n];

        for (int i = 0; i < n; i++) {
            int maxWidth = header[i].length();

            for (String[] row: table) {
                int width;
                if (row[i] == null) {
                    width = 1;
                } else {
                    width = row[i].length();
                }
                if (width > maxWidth) {
                    maxWidth = width;
                }
            }

            widths[i] = maxWidth;
        }

        return widths;
    }
}
