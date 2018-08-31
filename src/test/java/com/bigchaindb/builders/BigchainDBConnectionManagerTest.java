package com.bigchaindb.builders;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.bigchaindb.constants.Operations;
import com.bigchaindb.model.BigChainDBGlobals;
import com.bigchaindb.model.Connection;
import com.bigchaindb.model.MetaData;
import com.bigchaindb.model.Transaction;

import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.jadler.JadlerMocker;
import net.jadler.stubbing.server.jetty.JettyStubHttpServer;

public class BigchainDBConnectionManagerTest {

    
         
         private JadlerMocker bdbNode1, bdbNode2, bdbNode3;
         private int bdbNode1Port, bdbNode2Port, bdbNode3Port;
         Map<String, Object> conn1Config = new HashMap<String, Object>(), 
                 conn2Config = new HashMap<String, Object>(), 
                 conn3Config = new HashMap<String, Object>();
         private final String LOCALHOST = "http://localhost:";
         
         List<Connection> connections = new ArrayList<Connection>();
         Map<String, String> headers = new HashMap<String, String>();
         
          @Before
         public void setUp() {
              //setup mock node 1
             bdbNode1 = new JadlerMocker(new JettyStubHttpServer());
             bdbNode1.start();
             bdbNode1Port = bdbNode1.getStubHttpServerPort();
             System.out.println("port1 - " + bdbNode1Port);
             
             //setup mock node 2
             bdbNode2 = new JadlerMocker(new JettyStubHttpServer());
             bdbNode2.start();
             bdbNode2Port = bdbNode2.getStubHttpServerPort();
             System.out.println("port2 - " + bdbNode2Port);
             
             //set up mock node 3
             bdbNode3 = new JadlerMocker(new JettyStubHttpServer());
             bdbNode3.start();
             bdbNode3Port = bdbNode3.getStubHttpServerPort();
             System.out.println("port3 - " + bdbNode3Port);
             
             //define headers
             headers.put("app_id", "");
             headers.put("app_key", "");
             
             conn1Config.put("baseUrl", LOCALHOST.concat(Integer.toString(bdbNode1Port)));
             conn1Config.put("headers", headers);
             Connection conn1 = new Connection(conn1Config);
             
             conn2Config.put("baseUrl", LOCALHOST.concat(Integer.toString(bdbNode2Port)));
             conn2Config.put("headers", headers);
             Connection conn2 = new Connection(conn2Config);
             
             conn3Config.put("baseUrl", LOCALHOST.concat(Integer.toString(bdbNode3Port)));
             conn3Config.put("headers", headers);
             Connection conn3 = new Connection(conn3Config);
             
             connections.add(conn1);
             connections.add(conn2);
             connections.add(conn3);
             
             
             BigChainDBGlobals.setTimeout(10000);
             
         }
     
          @After
         public void tearDown() {
              if(bdbNode1.isStarted())
                  bdbNode1.close();
              if(bdbNode2.isStarted())
                  bdbNode2.close();
              if(bdbNode3.isStarted())
                  bdbNode3.close();
         }
     
         @Test
         public void nodeIsReusedOnSuccessfulConnection() throws Exception {
             //check that node is up
             if(!bdbNode1.isStarted()) {
                 bdbNode1.start();
             };
        
             bdbNode1
             .onRequest()
             .havingMethodEqualTo("GET")
             .havingPathEqualTo("/api/v1")
             .respond()
             .withStatus(200);
             
             BigchainDbConfigBuilder
             .addConnections(connections)
             .setup();
             
             String actualBaseUrl = (String) BigChainDBGlobals.getCurrentNode().getConnection().get("baseUrl");
             Assert.assertEquals("Failed because of node", LOCALHOST.concat(Integer.toString(bdbNode1Port)), actualBaseUrl);
             
             sendCreateTransaction();
             
             String newBaseUrl = (String) BigChainDBGlobals.getCurrentNode().getConnection().get("baseUrl");
             Assert.assertEquals("Failed because of node", LOCALHOST.concat(Integer.toString(bdbNode1Port)), newBaseUrl);
        
         }
         
         @Test
         public void secondNodeIsUsedIfFirstNodeGoesDown() throws Exception {
             //check that nodes are up
             if(!bdbNode1.isStarted()) {
                 bdbNode1.start();
             };
             
             if(!bdbNode2.isStarted()) {
                 bdbNode2.start();
             };
             
        
             //start listening on node 1
             bdbNode1
             .onRequest()
             .havingMethodEqualTo("GET")
             .havingPathEqualTo("/api/v1")
             .respond()
             .withStatus(200);
             
             //start listening on node 2
             bdbNode2
             .onRequest()
             .havingMethodEqualTo("GET")
             .havingPathEqualTo("/api/v1")
             .respond()
             .withStatus(200);
             
             BigchainDbConfigBuilder
             .addConnections(connections)
             .setup();
             
             //check if driver is connected to first node
             String actualBaseUrl = (String) BigChainDBGlobals.getCurrentNode().getConnection().get("baseUrl");
             Assert.assertEquals("Failed because of node", LOCALHOST.concat(Integer.toString(bdbNode1Port)), actualBaseUrl);
             
             //shut down node 1
             bdbNode1.close();
             
             //now transaction should be send by node 2
             sendCreateTransaction();
             
             //verify driver is connected to node 2
             String newBaseUrl = (String) BigChainDBGlobals.getCurrentNode().getConnection().get("baseUrl");
             Assert.assertEquals("Failed because of node", LOCALHOST.concat(Integer.toString(bdbNode2Port)), newBaseUrl);
        
         }
         
         @Test
         public void verifyMetaValuesForNodesAreUpdatedCorrectly() throws Exception {
             //check that nodes are up
             if(!bdbNode1.isStarted()) {
                 bdbNode1.start();
             };
             
             if(!bdbNode2.isStarted()) {
                 bdbNode2.start();
             };
        
             if(!bdbNode3.isStarted()) {
                 bdbNode3.start();
             };
             
             //start listening on node 1
             bdbNode1
             .onRequest()
             .havingMethodEqualTo("GET")
             .havingPathEqualTo("/api/v1")
             .respond()
             .withStatus(200);
             
             //start listening on node 2
             bdbNode2
             .onRequest()
             .havingMethodEqualTo("GET")
             .havingPathEqualTo("/api/v1")
             .respond()
             .withStatus(200);

             //start listening on node 3
             bdbNode3
             .onRequest()
             .havingMethodEqualTo("GET")
             .havingPathEqualTo("/api/v1")
             .respond()
             .withStatus(200);
             
             BigchainDbConfigBuilder
             .addConnections(connections)
             .setup();
             
             //verify meta values of nodes are initialized as 0
             for(Connection conn : BigChainDBGlobals.getConnections()) {
                 Assert.assertTrue(conn.getTimeToRetryForConnection() == 0);
                 Assert.assertTrue(conn.getRetryCount() == 0);
             }
             
             //check if driver is connected to first node
             String actualBaseUrl = (String) BigChainDBGlobals.getCurrentNode().getConnection().get("baseUrl");
             Assert.assertEquals("Failed because of node", LOCALHOST.concat(Integer.toString(bdbNode1Port)), actualBaseUrl);
             
             //shut down node 1
             bdbNode1.close();
             
            
             //now transaction should be send by node 2
             sendCreateTransaction();
             
             String baseUrl1 = "", baseUrl2 = "";
            //verify meta values of nodes are initialized as 0
             for(Connection conn : BigChainDBGlobals.getConnections()) {
                 
                 baseUrl1 = (String) conn.getConnection().get("baseUrl");
                 if(baseUrl1.equals(LOCALHOST.concat(Integer.toString(bdbNode1Port)))) {
                     Assert.assertTrue(conn.getTimeToRetryForConnection() != 0);
                     Assert.assertTrue((conn.getTimeToRetryForConnection() - System.currentTimeMillis()) <= BigChainDBGlobals.getTimeout());
                     Assert.assertTrue(conn.getRetryCount() == 1); 
                 }
             }

             //verify driver is connected to node 2
             String newBaseUrl = (String) BigChainDBGlobals.getCurrentNode().getConnection().get("baseUrl");
             Assert.assertEquals("Failed because of node", LOCALHOST.concat(Integer.toString(bdbNode2Port)), newBaseUrl);

            //shut down node 2
             bdbNode2.close();
             
             //now transaction should be send by node 3
             sendCreateTransaction();
             
             long T1 = 0, T2 =0;
            //verify meta values of nodes
             for(Connection conn : BigChainDBGlobals.getConnections()) {
                 
                 baseUrl2 = (String) conn.getConnection().get("baseUrl");
                 if(baseUrl2.equals(LOCALHOST.concat(Integer.toString(bdbNode2Port)))) {
                     T2 = conn.getTimeToRetryForConnection();
                     Assert.assertTrue(T2 != 0);
                     Assert.assertTrue((T2 - System.currentTimeMillis()) <= BigChainDBGlobals.getTimeout());
                     Assert.assertTrue(conn.getRetryCount() == 1); 
                 }
                 
                 baseUrl1 = (String) conn.getConnection().get("baseUrl");
                 if(baseUrl1.equals(LOCALHOST.concat(Integer.toString(bdbNode1Port)))) {
                     T1 = conn.getTimeToRetryForConnection();
                     Assert.assertTrue(T1 != 0);
                     Assert.assertTrue(conn.getRetryCount() == 1); 
                 }
                 
             }
             
             //verify that T1 < T2
             Assert.assertTrue(T1 < T2);
         }
         
         @Test(expected = TimeoutException.class)
         public void shouldThrowTimeoutException() throws Exception{
             //check that nodes are up
             if(!bdbNode1.isStarted()) {
                 bdbNode1.start();
             };
             
             if(!bdbNode2.isStarted()) {
                 bdbNode2.start();
             };
        
             //start listening on node 1
             bdbNode1
             .onRequest()
             .havingMethodEqualTo("GET")
             .havingPathEqualTo("/api/v1")
             .respond()
             .withStatus(200);
             
             //start listening on node 2
             bdbNode2
             .onRequest()
             .havingMethodEqualTo("GET")
             .havingPathEqualTo("/api/v1")
             .respond()
             .withStatus(200);
             
             //start listening on node 3
             bdbNode3
             .onRequest()
             .havingMethodEqualTo("GET")
             .havingPathEqualTo("/api/v1")
             .respond()
             .withStatus(200);
             
             BigchainDbConfigBuilder
             .addConnections(connections)
             .setup();

            //check if driver is connected to first node
             String url1 = (String) BigChainDBGlobals.getCurrentNode().getConnection().get("baseUrl");
             Assert.assertEquals("Failed because of node", LOCALHOST.concat(Integer.toString(bdbNode1Port)), url1);
             
             //shut down node 1
             bdbNode1.close();
             
             //now transaction should be send by node 2
             sendCreateTransaction();

            //check if driver is connected to node 2
             String url2 = (String) BigChainDBGlobals.getCurrentNode().getConnection().get("baseUrl");
             Assert.assertEquals("Failed because of node", LOCALHOST.concat(Integer.toString(bdbNode2Port)), url2);
             
             //shut down node 1
             bdbNode2.close();
             
             //now transaction should be send by node 2
             sendCreateTransaction();

            //check if driver is connected to node 3
             String url3 = (String) BigChainDBGlobals.getCurrentNode().getConnection().get("baseUrl");
             Assert.assertEquals("Failed because of node", LOCALHOST.concat(Integer.toString(bdbNode3Port)), url3);
             
             //shut down node 1
             bdbNode3.close();
             
             //now transaction should be send by node 2
             sendCreateTransaction();
         }
         
         public String sendCreateTransaction() throws TimeoutException, Exception{
             
                 
             net.i2p.crypto.eddsa.KeyPairGenerator edDsaKpg = new net.i2p.crypto.eddsa.KeyPairGenerator();
             KeyPair keys = edDsaKpg.generateKeyPair();
             
          // create New asset
             Map<String, String> assetData = new TreeMap<String, String>() {{
                 put("name", "James Bond");
                 put("age", "doesn't matter");
                 put("purpose", "saving the world");
             }};
             System.out.println("(*) Assets Prepared..");

             // create metadata
             MetaData metaData = new MetaData();
             metaData.setMetaData("where is he now?", "Thailand");
             System.out.println("(*) Metadata Prepared..");
             //build and send CREATE transaction
             Transaction transaction = null;
             
              transaction = BigchainDbTransactionBuilder
                     .init()
                     .addAssets(assetData, TreeMap.class)
                     .addMetaData(metaData)
                     .operation(Operations.CREATE)
                     .buildAndSign((EdDSAPublicKey) keys.getPublic(), (EdDSAPrivateKey) keys.getPrivate())
                     .sendTransaction();

             System.out.println("(*) CREATE Transaction sent.. - " + transaction.getId());
             return transaction.getId();
         }
     
}
