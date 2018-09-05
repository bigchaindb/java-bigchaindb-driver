/*
 * Copyright BigchainDB GmbH and BigchainDB contributors
 * SPDX-License-Identifier: (Apache-2.0 AND CC-BY-4.0)
 * Code is Apache-2.0 and docs are CC-BY-4.0
 */
package com.bigchaindb.builders;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bigchaindb.constants.BigchainDbApi;
import com.bigchaindb.model.ApiEndpoints;
import com.bigchaindb.model.BigChainDBGlobals;
import com.bigchaindb.model.Connection;
import com.bigchaindb.util.JsonUtils;
import com.bigchaindb.util.NetworkUtils;
import com.bigchaindb.ws.BigchainDbWSSessionManager;
import com.bigchaindb.ws.MessageHandler;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class BigchainDbConfigBuilder {

    private static final Logger log = LoggerFactory.getLogger(BigchainDbConfigBuilder.class);
    public ConfigBuilder builder = new ConfigBuilder();
    /**
     * Instantiates a new bigchain db config builder.
     */
    private BigchainDbConfigBuilder() {
    }

    /**
     * adds connection details to global configuration
     * @param connections the base url
     * @return the i tokens
     */
    public static ITokens addConnections(List<Connection> connections) {
        return new BigchainDbConfigBuilder.ConfigBuilder(connections);
    }

    /**
     * Base url.
     *
     * @param baseUrl the base url
     * @return the i tokens
     */
    public static ITokens baseUrl(String baseUrl) {
        return new BigchainDbConfigBuilder.ConfigBuilder(baseUrl);
    }
   
    /**
     * The Interface ITokens.
     */
    public interface ITokens {

        /**
         * Adds the token.
         *
         * @param key the key
         * @param map the map
         * @return the i tokens
         */
        ITokens addToken(String key, String map);

        /**
         * Web socket monitor.
         *
         * @param messageHandler the message handler
         * @return the i tokens
         */
        ITokens webSocketMonitor(MessageHandler messageHandler);

        /**
         * override timeout for connections discovery and requests
         * @param timeInMs timeout in milliseconds
         * @return i tokens
         */
        ITokens setTimeout(int timeInMs);
        
        /**
         * Setup.
         */
        void setup();
    }

    /**
     * The Class Builder.
     */
    protected static class ConfigBuilder implements ITokens {

        /**
         * The baser url.
         */
        String baseUrl;

        /**
         * The tokens.
         */
        Map<String, String> tokens = new HashMap<String, String>();

        /**
         * The http client.
         */
        OkHttpClient httpClient;

        /**
         * The setup wsockets.
         */
        boolean setupWsockets = false;

        /**
         * The message handler.
         */
        MessageHandler messageHandler = null;

        /**
         * Instantiates a new builder
         */
        public ConfigBuilder() {
        	
        }
        
        /**
         * Instantiates a new builder.
         *
         * @param baseUrl the base url
         */
        public ConfigBuilder(String baseUrl) {
            this.baseUrl = baseUrl;
        }
        
        /**
         * Instantiates a new builder with list of connections.
         *
         * @param connections list of connections
         */
        public ConfigBuilder(List<Connection> connections) {
            BigChainDBGlobals.setConnections(connections);
            BigChainDBGlobals.setHasMultipleNodes(true);
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * com.bigchaindb.builders.BigchainDbConfigBuilder.ITokens#addToken(java.
         * lang.String, java.lang.String)
         */
        @Override
        public ITokens addToken(String key, String value) {
            tokens.put(key, value);
            return this;
        }

        /*
         * (non-Javadoc)
         *
         * @see com.bigchaindb.builders.BigchainDbConfigBuilder.ITokens#setup()
         */
        @Override
        public void setup() {
            this.configureConnections();
            this.setMetaValuesToConnections();
            try {
                this.configure();
            } catch (TimeoutException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        /**
         * set meta values like timeToRetryForConnection, retryCount etc
         */
        private void setMetaValuesToConnections() {
            for(int i=0; i < BigChainDBGlobals.getConnections().size(); i++) {
                BigChainDBGlobals.getConnections().get(i).setRetryCount(0);
                BigChainDBGlobals.getConnections().get(i).setTimeToRetryForConnection(0);
            }
        }

        /**
         * configure nodes and reset timeout
         * @throws TimeoutException exception on timeout
         */
        public void configure() throws TimeoutException {
            //reset time till timeout
            //TODO: check if there are multiple connections or 
            //only one
            BigChainDBGlobals.resetTimeTillTimeout();
            this.configureNodeToConnect();
        }
        
        /**
         * configure nodes for connection
         * @throws TimeoutException exception on timeout
         */
        public void configureNodeToConnect() throws TimeoutException {
            Connection nodeToConnect = null;
            try {
                if(System.currentTimeMillis() > BigChainDBGlobals.getTimeTillTimeout()) {
                    throw new TimeoutException();
                }
                
                if(BigChainDBGlobals.isHasMultipleNodes()) {
                    //get node to connect to
                    nodeToConnect = this.getConnectionWithEarliestAvailability();
                }
                else {
                    nodeToConnect = BigChainDBGlobals.getCurrentNode();
                }
                this.prepareHeader(nodeToConnect);
                this.waitTillTimetoConnect(nodeToConnect.getTimeToRetryForConnection());
                boolean connected = this.connect(nodeToConnect);
                //check if node is connected
                if(!connected) {
                    log.info("Couldn't connect to node - " + BigChainDBGlobals.getCurrentNode().getConnection().get("baseUrl") +
                            " after " + BigChainDBGlobals.getCurrentNode().getRetryCount() + " retry(s)...");
                    this.processConnectionFailure(nodeToConnect);
                    configureNodeToConnect();
                }
                else {
                    log.info("Connected to node - " + BigChainDBGlobals.getCurrentNode().getConnection().get("baseUrl") +
                            " after " + BigChainDBGlobals.getCurrentNode().getRetryCount() + " retry(s)...");
                    this.processConnectionSuccess(nodeToConnect);
                }
            
            } catch (Exception e) {
                if(e instanceof TimeoutException) {
                    throw new TimeoutException("Couldn't connect to any given nodes in " + BigChainDBGlobals.getTimeout() 
                            + " ms. Check if the nodes are up and running!");
                }
                else {
                    log.info("Couldn't connect to node - " + BigChainDBGlobals.getCurrentNode().getConnection().get("baseUrl") +
                                " after " + BigChainDBGlobals.getCurrentNode().getRetryCount() + " retry(s)...");

                    this.processConnectionFailure(nodeToConnect);
                    configureNodeToConnect();
                }
            }
            
            
        }
        
        /**
         * handle connection failure and update meta values for that connection
         * @param nodeToConnect node to connect
         */
        public void processConnectionFailure(Connection nodeToConnect) {
            int nodeIndex = BigChainDBGlobals.getConnections().indexOf(nodeToConnect);
            Connection conn = BigChainDBGlobals.getConnections().get(nodeIndex);
            
            //calculate time to retry for new connection
            long timeToRetryConnection = BigChainDBGlobals.DELAY * (long) Math.pow(2, conn.getRetryCount());
            //set whichever is smaller (this makes sure that timeToRetryConnection is always smaller than half of timeout
            long timeToRetryCap = BigChainDBGlobals.getTimeout()/2;
            timeToRetryConnection = timeToRetryConnection < timeToRetryCap ? timeToRetryConnection : timeToRetryCap;

            conn.setTimeToRetryForConnection(System.currentTimeMillis() + timeToRetryConnection);
            
            //increment retry count for this node
            int retryCount = conn.getRetryCount();
            conn.setRetryCount(++retryCount);
            //update state of node in list of connections
            BigChainDBGlobals.getConnections().set(nodeIndex, conn);
            
        }
        
        /**
         * handle successful connection and reset meta values for that connection
         * @param connectedNode connected node
         */
        public void processConnectionSuccess(Connection connectedNode) {
            int nodeIndex = BigChainDBGlobals.getConnections().indexOf(connectedNode);
            Connection conn = BigChainDBGlobals.getConnections().get(nodeIndex);
            
            //reset time to retry for connection
            conn.setTimeToRetryForConnection(0);
            
            //reset retryCount
            conn.setRetryCount(0);
            
            //reset time till timeout
            BigChainDBGlobals.resetTimeTillTimeout();
            
            //update state of node in list of connections
            BigChainDBGlobals.getConnections().set(nodeIndex, conn);        
        }

        /**
         * get the earliest available BigchainDB node for connection
         * @return Connection
         */
        private Connection getConnectionWithEarliestAvailability() {
            
            //sort connections in ascending order of timeToRetryForConnection
            BigChainDBGlobals.getConnections().sort(Comparator.comparingLong(Connection ::getTimeToRetryForConnection));
            
            //get the first node
            Connection nodeToConnect = BigChainDBGlobals.getConnections().get(0);
            //set this node as current node to connect
            BigChainDBGlobals.setCurrentNode(nodeToConnect);
            
            return nodeToConnect;
        }
        

        /**
         * wait till time to connect to new node
         * @param timeToRetryToConnect time to wait (in ms)
         * @throws TimeoutException exception on timeout
         */
        private void waitTillTimetoConnect(long timeToRetryToConnect) throws TimeoutException {
            
            //get current time in milliseconds
            long currentTimeInMs = System.currentTimeMillis();
            
            //wait till its time to connect to node
            while(currentTimeInMs < timeToRetryToConnect) {
            	//this scenario should not occur, but for the sake of better error handling
                if(System.currentTimeMillis() > BigChainDBGlobals.getTimeTillTimeout()) {
                    throw new TimeoutException();
                }
                currentTimeInMs = System.currentTimeMillis();
            }
            
            return;
            
        }
        
        /**
         * connect to the given node
         * @param connection connection details
         * @return true or false depending on if the connection was successful
         * @throws Exception exception
         */
         public boolean connect(Connection connection) throws Exception {
             boolean isConnectionSuccessful = false;
             BigChainDBGlobals.setAuthorizationTokens(tokens);
             BigChainDBGlobals.setBaseUrl(connection.getConnection().get("baseUrl") + "/api" + BigchainDbApi.API_VERSION);
             BigChainDBGlobals.setWsSocketUrl(connection.getConnection().get("baseUrl") + "/api" + BigchainDbApi.API_VERSION + BigchainDbApi.STREAMS);
             Response connectionResponse = null;
             
             if (!BigChainDBGlobals.isConnected() || ( this.httpClient == null && BigChainDBGlobals.getHttpClient() == null )) {
                 BigChainDBGlobals.setHttpClient(buildDefaultHttpClient());
             }
             
             try {
                 connectionResponse =  NetworkUtils.sendGetRequest(connection.getConnection().get("baseUrl") + "/api" + BigchainDbApi.API_VERSION);
                 BigChainDBGlobals.setApiEndpoints(JsonUtils.fromJson(connectionResponse.body().string(),
                         ApiEndpoints.class));
                 
                 //check if connection was successful
                 if(connectionResponse.code() == 200) {
                     isConnectionSuccessful = true;
                 }
                 
                 //set up web sockets
                 if (this.setupWsockets) {

                     // we create another thread for processing the endpoint.
                     new Thread(new Runnable() {
                         @Override
                         public void run() {
                             new BigchainDbWSSessionManager(URI.create(BigChainDBGlobals.getApiEndpoints().getStreams()),
                                     messageHandler);
                         }
                     }).start();
                 }
                 
             } catch (ConnectException ex) {
                 throw new ConnectException();
             } catch (Exception e) {
                 throw new Exception();
             } 
             return isConnectionSuccessful;
         }
         
         /**
          * Builds the default http client.
          *
          * @return the ok http client
          */
         private OkHttpClient buildDefaultHttpClient() {
             long httpTimeout = BigChainDBGlobals.getTimeTillTimeout() - System.currentTimeMillis();
             return new OkHttpClient.Builder().connectTimeout(httpTimeout, TimeUnit.MILLISECONDS).writeTimeout(httpTimeout, TimeUnit.MILLISECONDS)
                     .readTimeout(httpTimeout, TimeUnit.MILLISECONDS).addInterceptor(authInterceptor).build();
         }
       
         /**
          * The auth interceptor.
          */
         private Interceptor authInterceptor = new Interceptor() {
             @Override
             public Response intercept(Interceptor.Chain chain) throws IOException {
                 Request originalRequest = chain.request();

                 if (tokens == null) return chain.proceed(originalRequest);

                 // Add authorization header with updated authorization value to
                 // intercepted request
                 Request.Builder authorisedRequest = originalRequest.newBuilder();

                 for (String key : tokens.keySet()) {
                     authorisedRequest = authorisedRequest.addHeader(key, tokens.get(key));
                 }

                 return chain.proceed(authorisedRequest.build());
             }
         };
         
         /**
          * extract headers and provision them in tokens for authorisation
          * @param connection connection details
          * @return
          */
         @SuppressWarnings("unchecked")
        private Map<String, String> prepareHeader(Connection connection){
             
            tokens = (Map<String, String>) connection.getConnection().get("headers");
            return tokens;
             
         }
         
         /**
          * configure for single connection
          */
         private void configureConnections() {
             if(!BigChainDBGlobals.isHasMultipleNodes()) {
                 Map<String, Object> config = new HashMap<String, Object>();
                 config.put("baseUrl", this.baseUrl);
                 config.put("headers", tokens);
                 Connection connection = new Connection(config);
                 List<Connection> connections = new ArrayList<>();
                 connections.add(connection);
                 BigChainDBGlobals.setConnections(connections);
                 BigChainDBGlobals.setCurrentNode(connection);
             }
         }
         
        /*
         * (non-Javadoc)
         *
         * @see com.bigchaindb.builders.BigchainDbConfigBuilder.ITokens#
         * webSocketMonitor(com.bigchaindb.ws.MessageHandler)
         */
        @Override
        public ITokens webSocketMonitor(MessageHandler messageHandler) {
            this.setupWsockets = true;
            this.messageHandler = messageHandler;
            return this;
        }

        /*
         * (non-Javadoc)
         *
         * @see com.bigchaindb.builders.BigchainDbConfigBuilder.ITokens#
         * setTimeout(int)
         */
        @Override
        public ITokens setTimeout(int timeInMs) {
            BigChainDBGlobals.setTimeout(timeInMs);
            return this;
        }
    }
}
