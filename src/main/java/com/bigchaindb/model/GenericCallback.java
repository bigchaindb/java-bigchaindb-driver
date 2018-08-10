package com.bigchaindb.model;

import okhttp3.Response;



/**
 * The Interface TransactionCallback.
 */
public interface GenericCallback {
    
    /**
     * The pushed transaction was accepted in the BACKLOG, but the processing has not been completed.
     *
     * @param response the response
     */
    void pushedSuccessfully(Response response);

    /**
     * The transaction was malformed and not accepted in the BACKLOG. This shouldn't normally happen as the
     * driver ensures the proper transaction creation.
     *
     * @param response the response
     */
    void transactionMalformed(Response response);

    /**
     * All other errors, including server malfunction or network error.
     *
     * @param response the response
     */
    void otherError(Response response);
    
}
