/*
 * Copyright BigchainDB GmbH and BigchainDB contributors
 * SPDX-License-Identifier: (Apache-2.0 AND CC-BY-4.0)
 * Code is Apache-2.0 and docs are CC-BY-4.0
 */
package com.bigchaindb.util;

import org.junit.Test;

import com.bigchaindb.util.ScannerUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class ScannerUtilsTest {

    @Test
    public void testMonitorExit() {
        InputStream fakeIn = new ByteArrayInputStream("exit".getBytes());
        System.setIn(fakeIn);
        ScannerUtil.monitorExit();
    }
}
