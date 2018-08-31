/*
 * Copyright BigchainDB GmbH and BigchainDB contributors
 * SPDX-License-Identifier: (Apache-2.0 AND CC-BY-4.0)
 * Code is Apache-2.0 and docs are CC-BY-4.0
 */
package com.bigchaindb.ws;

import static net.jadler.Jadler.port;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.bigchaindb.builders.BigchainDbConfigBuilder;
import com.bigchaindb.model.Connection;

public class WsMonitorTest {

    public static void main(String[] args) {

         Map<String, Object> connConfig = new HashMap<String, Object>();
            List<Connection> connections = new ArrayList<Connection>();
            Map<String, String> headers = new HashMap<String, String>();
            
          //define headers
            headers.put("app_id", "2bbaf3ff");
            headers.put("app_key", "c929b708177dcc8b9d58180082029b8d");
            
            connConfig.put("baseUrl", "http://localhost:" + port());
            connConfig.put("headers", headers);
            Connection conn1 = new Connection(connConfig);
            connections.add(conn1);
            BigchainDbConfigBuilder
            .addConnections(connections)
            .webSocketMonitor(new MessageHandler() {
                        @Override
                        public void handleMessage(String message) {
                        }
                    })
            .setup();
    }
}
