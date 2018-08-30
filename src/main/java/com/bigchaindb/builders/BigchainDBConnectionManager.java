package com.bigchaindb.builders;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
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

public class BigchainDBConnectionManager {

    private static final Logger log = LoggerFactory.getLogger(BigchainDbConfigBuilder.class);

    Map<String, String> tokens = new HashMap<String, String>();
    OkHttpClient httpClient;
    boolean setupWsockets = false;
    MessageHandler messageHandler = null;
    
    

    public BigchainDBConnectionManager(List<Connection> connections) {
        BigChainDBGlobals.setConnections(connections);
    }

    /**
     * initialize node and api endpoints
     * @throws TimeoutException
     */
    public void initialize () throws TimeoutException {
        this.setMetaValuesToConnections();
        this.configure();
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
     * @throws TimeoutException
     */
    public void configure() throws TimeoutException {
        //reset time till timeout
        BigChainDBGlobals.resetTimeTillTimeout();
        this.configureNodeToConnect();
    }
    
    /**
     * configure nodes for connection
     * @throws TimeoutException
     */
    public void configureNodeToConnect() throws TimeoutException {
        Connection nodeToConnect = null;
        try {
            if(System.currentTimeMillis() > BigChainDBGlobals.getTimeTillTimeout()) {
                throw new TimeoutException();
            }
            //get node to connect to
            nodeToConnect = this.getConnectionWithEarliestAvailability();
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
     * @param nodeToConnect
     */
    public void processConnectionFailure(Connection nodeToConnect) {
        int nodeIndex = BigChainDBGlobals.getConnections().indexOf(nodeToConnect);
        Connection conn = BigChainDBGlobals.getConnections().get(nodeIndex);
        
        //calculate time to retry for new connection
        long timeToRetryConnection = BigChainDBGlobals.DELAY * (2 ^ conn.getRetryCount());
        //set whichever is smaller (this makes sure that timeToRetryConnection is always smaller than half of timeout
        timeToRetryConnection = timeToRetryConnection < BigChainDBGlobals.getTimeout() ? timeToRetryConnection : BigChainDBGlobals.getTimeout();
        conn.setTimeToRetryForConnection(System.currentTimeMillis() + timeToRetryConnection);
        
        //increment retry count for this node
        int retryCount = conn.getRetryCount();
        conn.setRetryCount(++retryCount);
        //update state of node in list of connections
        BigChainDBGlobals.getConnections().set(nodeIndex, conn);
        
    }
    
    /**
     * handle successful connection and reset meta values for that connection
     * @param connectedNode
     */
    protected void processConnectionSuccess(Connection connectedNode) {
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
     * @throws TimeoutException
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
        }
        
        return;
        
    }
    
    /**
     * connect to the given node
     * @param connection
     * @return true or false depending on if the connection was successful
     * @throws Exception
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
      * @param connection
      * @return
      */
     private Map<String, String> prepareHeader(Connection connection){
         
        tokens = (Map<String, String>) connection.getConnection().get("headers");
        return tokens;
         
     }

}
