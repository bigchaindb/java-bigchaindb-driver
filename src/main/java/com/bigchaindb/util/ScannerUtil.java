/*
 * Copyright BigchainDB GmbH and BigchainDB contributors
 * SPDX-License-Identifier: (Apache-2.0 AND CC-BY-4.0)
 * Code is Apache-2.0 and docs are CC-BY-4.0
 */
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
