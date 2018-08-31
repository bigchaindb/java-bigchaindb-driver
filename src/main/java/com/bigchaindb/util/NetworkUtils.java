/*
 * Copyright BigchainDB GmbH and BigchainDB contributors
 * SPDX-License-Identifier: (Apache-2.0 AND CC-BY-4.0)
 * Code is Apache-2.0 and docs are CC-BY-4.0
 */
package com.bigchaindb.util;

import okhttp3.*;
import okio.Buffer;

import java.io.IOException;

import com.bigchaindb.model.BigChainDBGlobals;
import com.bigchaindb.model.GenericCallback;


/**
 * The Class NetworkUtils.
 */
public class NetworkUtils {

    /**
     *  The Constant JSON.
     *
     * @param url the url
     * @param body the body
     * @param callback the callback
     */

	private static String bodyToString(final Request request){

	    try {
	        final Request copy = request.newBuilder().build();
	        final Buffer buffer = new Buffer();
	        copy.body().writeTo(buffer);
	        return buffer.readUtf8();
	    } catch (final IOException e) {
	        return "did not work";
	    }
	}
    /**
     * Send post request.
     *
     * @param url      the url
     * @param body     the body
     * @param callback the callback
     */
    public static void sendPostRequest(String url, RequestBody body,
                                       final GenericCallback callback) {

        Request request = new Request.Builder().url(url).post(body).build();

        BigChainDBGlobals.getHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                BigChainDBGlobals.setConnected(false);
                
            }

            @Override
            public void onResponse(Call call, Response response) {
                if (response.code() == 202) callback.pushedSuccessfully(response);
                else if (response.code() == 400) callback.transactionMalformed(response);
                else callback.otherError(response);
                BigChainDBGlobals.setConnected(true);
                response.close();
            }
        });
    }

    /**
     * Send post request.
     *
     * @param url  the url
     * @param body the body
     * @return the response
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static Response sendPostRequest(String url, RequestBody body) throws IOException {
        Response response = null;
        
        try{
            Request request = new Request.Builder().url(url).post(body).build();
            response = BigChainDBGlobals.getHttpClient().newCall(request).execute();
            BigChainDBGlobals.setConnected(true);
        }
        catch(IOException e) {
            BigChainDBGlobals.setConnected(false);
            throw new IOException();
        }

        return response;
    }

    /**
     * Send get request.
     *
     * @param url the url
     * @return the response
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static Response sendGetRequest(String url) throws IOException {
        Response response = null;
        
        try{
           Request request = new Request.Builder().url(url).get().build();
           response = BigChainDBGlobals.getHttpClient().newCall(request).execute();
           BigChainDBGlobals.setConnected(true);
        }
        catch(IOException e) {
            BigChainDBGlobals.setConnected(false);
            throw new IOException();
        }

        return response;

    }

}
