package com.bigchaindb.util;

import java.util.Scanner;


/**
 * The Class ScannerUtil.
 */
public class ScannerUtil {

    /**
     * exit when enter string "exit".
     */
    public static void monitorExit() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) if ("exit" .equals(scanner.nextLine())) break;
        }
    }
}
