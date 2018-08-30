/*
 * Copyright BigchainDB GmbH and BigchainDB contributors
 * SPDX-License-Identifier: (Apache-2.0 AND CC-BY-4.0)
 * Code is Apache-2.0 and docs are CC-BY-4.0
 */
package com.bigchaindb.builders;

import com.bigchaindb.api.TransactionsApi;
import com.bigchaindb.constants.Operations;
import com.bigchaindb.cryptoconditions.types.Ed25519Sha256Condition;
import com.bigchaindb.cryptoconditions.types.Ed25519Sha256Fulfillment;
import com.bigchaindb.json.strategy.TransactionDeserializer;
import com.bigchaindb.json.strategy.TransactionsDeserializer;
import com.bigchaindb.model.*;
import com.bigchaindb.util.DriverUtils;
import com.bigchaindb.util.JsonUtils;
import com.bigchaindb.util.KeyPairUtils;
import com.google.api.client.util.Base64;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializer;
import net.i2p.crypto.eddsa.EdDSAEngine;
import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * The Class BigchainDbTransactionBuilder.
 */
public class BigchainDbTransactionBuilder {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(BigchainDbTransactionBuilder.class);

    /**
     * Instantiates a new bigchain db transaction builder.
     */
    private BigchainDbTransactionBuilder() {
    }

    /**
     * Inits the.
     *
     * @return the builder
     */
    public static Builder init() {
        return new BigchainDbTransactionBuilder.Builder();
    }

    /**
     * The Interface IAssetMetaData.
     */
    public interface ITransactionAttributes {

        /**
         * Operation.
         *
         * @param operation the operation
         * @return the i asset meta data
         */
        ITransactionAttributes operation(Operations operation);

        /*
         * Adds the asset.
         *
         * @param key
         *            the key
         * @param value
         *            the value
         * @return the i asset meta data
         */
        //ITransactionAttributes addAsset(String key, String value);

        ITransactionAttributes addAssetDataClass(Class assetDataClass, JsonDeserializer<?> jsonDeserializer);

        ITransactionAttributes addOutput(String amount, EdDSAPublicKey... publicKey);

        ITransactionAttributes addOutput(String amount);

        ITransactionAttributes addOutput(String amount, EdDSAPublicKey publicKey);

        ITransactionAttributes addInput(String fullfillment, FulFill fullFill, EdDSAPublicKey... publicKey);

        ITransactionAttributes addInput(String fullfillment, FulFill fullFill);

        ITransactionAttributes addInput(String fullfillment, FulFill fullFill, EdDSAPublicKey publicKey);

        ITransactionAttributes addInput(Details fullfillment, FulFill fullFill, EdDSAPublicKey... publicKey);

        /**
         * Adds the assets.
         *
         * @param assets the assets
         * @return the i asset meta data
         */
        ITransactionAttributes addAssets(Object assets, Class assetsDataClass);

        /**
         * Adds the meta data.
         *
         * @param metaData the json object
         * @return the i asset meta data
         */
        ITransactionAttributes addMetaData(Object metaData);

        /**
         * Add the class and deserializer for metadata
         *
         * @param metaDataClass    the class of the metadata object
         * @param jsonDeserializer the deserializer
         * @return
         */
        ITransactionAttributes addMetaDataClassDeserializer(Class metaDataClass, JsonDeserializer<?> jsonDeserializer);

        /**
         * Add the class and serializer for metadata
         *
         * @param metaDataClass  the class of the metadata object
         * @param jsonSerializer the deserializer
         * @return
         */
        ITransactionAttributes addMetaDataClassSerializer(Class metaDataClass, JsonSerializer<?> jsonSerializer);

        /**
         * Builds the.
         *
         * @param publicKey the public key
         * @return the i build
         * @throws Exception 
         */
        IBuild build(EdDSAPublicKey publicKey) throws Exception;

        /**
         * Builds the and sign.
         *
         * @param publicKey  the public key
         * @param privateKey the private key
         * @return the i build
         * @throws Exception 
         */
        IBuild buildAndSign(EdDSAPublicKey publicKey, EdDSAPrivateKey privateKey) throws Exception;

        /**
         * Builds the and sign and return.
         *
         * @param publicKey the public key
         * @return the transaction
         * @throws Exception 
         */
        Transaction buildOnly(EdDSAPublicKey publicKey) throws Exception;

        /**
         * Builds the and sign and return.
         *
         * @param publicKey  the public key
         * @param privateKey the private key
         * @return the transaction
         * @throws Exception 
         */
        Transaction buildAndSignOnly(EdDSAPublicKey publicKey, EdDSAPrivateKey privateKey) throws Exception;
    }

    /**
     * The Interface IBuild.
     */
    public interface IBuild {

        /**
         * Send transaction.
         *
         * @return the transaction
         * @throws IOException Signals that an I/O exception has occurred.
         * @throws TimeoutException 
         */
        Transaction sendTransaction() throws TimeoutException;

        /**
         * Send transaction.
         *
         * @param callback the callback
         * @return the transaction
         * @throws IOException Signals that an I/O exception has occurred.
         * @throws TimeoutException 
         */
        Transaction sendTransaction(GenericCallback callback) throws TimeoutException;
    }

    /**
     * The Class Builder.
     */
    public static class Builder implements ITransactionAttributes, IBuild {

       private BigchainDBConnectionManager connectionManager = new BigchainDBConnectionManager(BigChainDBGlobals.getConnections());

        /**
         * The metadata.
         */
        private Object metadata = null;

        /**
         * The assets.
         */
        private Object assets = null;
        private Class assetsDataClass = null;

        /**
         * The inputs.
         */
        private List<Input> inputs = new ArrayList<>();

        /**
         * The outputs.
         */
        private List<Output> outputs = new ArrayList<>();

        /**
         * The public key.
         */
        private EdDSAPublicKey publicKey;

        /**
         * The transaction.
         */
        private Transaction transaction;

        /**
         * The operation.
         */
        private Operations operation;

        @Override
        public ITransactionAttributes addOutput(String amount) {
            return addOutput(amount, this.publicKey);
        }

        @Override
        public ITransactionAttributes addOutput(String amount, EdDSAPublicKey publicKey) {
            EdDSAPublicKey[] keys = new EdDSAPublicKey[]{publicKey};
            return addOutput(amount, keys);
        }

        @Override
        public ITransactionAttributes addOutput(String amount, EdDSAPublicKey... publicKeys) {
            for (EdDSAPublicKey publicKey : publicKeys) {
                Output output = new Output();
                Ed25519Sha256Condition sha256Condition = new Ed25519Sha256Condition(publicKey);
                output.setAmount(amount);
                output.addPublicKey(KeyPairUtils.encodePublicKeyInBase58(publicKey));
                Details details = new Details();
                details.setPublicKey(KeyPairUtils.encodePublicKeyInBase58(publicKey));
                details.setType("ed25519-sha-256");
                output.setCondition(new Condition(details, sha256Condition.getUri().toString()));
                this.outputs.add(output);
            }
            return this;
        }

        @Override
        public ITransactionAttributes addInput(String fullfillment, FulFill fullFill) {
            return addInput(fullfillment, fullFill, this.publicKey);
        }

        @Override
        public ITransactionAttributes addInput(String fullfillment, FulFill fullFill, EdDSAPublicKey publicKey) {
            EdDSAPublicKey[] keys = new EdDSAPublicKey[]{publicKey};
            return addInput(fullfillment, fullFill, keys);
        }

        @Override
        public ITransactionAttributes addInput(String fullfillment, FulFill fullFill, EdDSAPublicKey... publicKeys) {
            for (EdDSAPublicKey publicKey : publicKeys) {
                Input input = new Input();
                input.setFullFillment(fullfillment);
                input.setFulFills(fullFill);
                input.addOwner(KeyPairUtils.encodePublicKeyInBase58(publicKey));
                this.inputs.add(input);
            }
            return this;
        }

        @Override
        public ITransactionAttributes addInput(Details fullfillment, FulFill fullFill, EdDSAPublicKey... publicKeys) {
            for (EdDSAPublicKey publicKey : publicKeys) {
                Input input = new Input();
                input.setFullFillment(fullfillment);
                input.setFulFills(fullFill);
                input.addOwner(KeyPairUtils.encodePublicKeyInBase58(publicKey));
                this.inputs.add(input);
            }
            return this;
        }

        public ITransactionAttributes addAssetDataClass(Class assetDataClass, JsonDeserializer<?> jsonDeserializer) {
            return this;
        }

        /**
         * Add
         *
         * @param metaDataClass    the class of the metadata object
         * @param jsonDeserializer the deserializer
         * @return self
         */
        @Override
        public ITransactionAttributes addMetaDataClassDeserializer(Class metaDataClass, JsonDeserializer<?> jsonDeserializer) {
            TransactionDeserializer.setMetaDataClass(metaDataClass);
            TransactionsDeserializer.setMetaDataClass(metaDataClass);
            JsonUtils.addTypeAdapterDeserializer(metaDataClass, jsonDeserializer);
            return this;
        }

        public ITransactionAttributes addMetaDataClassSerializer(Class metaDataClass, JsonSerializer<?> jsonSerializer) {
            JsonUtils.addTypeAdapterSerializer(metaDataClass, jsonSerializer);
            return this;
        }

        public ITransactionAttributes addMetaData(Object object) {
            this.metadata = object;
            return this;
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * com.bigchaindb.builders.BigchainDbTransactionBuilder.IAssetMetaData#
         * build(net.i2p.crypto.eddsa.EdDSAPublicKey)
         */
        @Override
        public IBuild build(EdDSAPublicKey publicKey) throws Exception{
            this.transaction = new Transaction();
            this.publicKey = publicKey;

            if (this.outputs.isEmpty()) {
                this.addOutput("1");
            }
            for (Output output : this.outputs) {
                this.transaction.addOutput(output);
            }

            if (this.inputs.isEmpty()) {
                this.addInput(null, null);
            }
            for (Input input : this.inputs) {
                this.transaction.addInput(input);
            }

            if (this.operation == Operations.CREATE 
                    || this.operation == Operations.TRANSFER) {
                this.transaction.setOperation(this.operation.name());
            }
           else {
               throw new Exception("Invalid Operations value. Accepted values are [Operations.CREATE, Operations.TRANSFER]");
           }

            if (String.class.isAssignableFrom(this.assets.getClass())) {
                // interpret as an asset ID
                this.transaction.setAsset(new Asset((String) this.assets));
            } else {
                // otherwise it's an asset
                this.transaction.setAsset(new Asset(this.assets, this.assetsDataClass));
            }
            this.transaction.setMetaData(this.metadata);
            this.transaction.setVersion("2.0");

            this.transaction.setId(null);

            return this;
        }

        /**
         * Sign.
         *
         * @param privateKey the private key
         * @throws InvalidKeyException      the invalid key exception
         * @throws SignatureException       the signature exception
         * @throws NoSuchAlgorithmException the no such algorithm exception
         */
        private void sign(EdDSAPrivateKey privateKey)
                throws InvalidKeyException, SignatureException, NoSuchAlgorithmException {
            String temp = this.transaction.toHashInput();
            JsonObject transactionJObject = DriverUtils.makeSelfSortingGson(temp);

            byte[] sha3Hash;
            if (Operations.TRANSFER.name().equals(this.transaction.getOperation())) {
                // it's a transfer operation: make sure to update the hash pre-image with
                // the fulfilling transaction IDs and output indexes
                StringBuilder preimage = new StringBuilder(transactionJObject.toString());
                for (Input in : this.transaction.getInputs()) {
                    if (in.getFulFills() != null) {
                        FulFill fulfill = in.getFulFills();
                        String txBlock = fulfill.getTransactionId() + String.valueOf(fulfill.getOutputIndex());
                        preimage.append(txBlock);
                    }
                }
                sha3Hash = DriverUtils.getSha3HashRaw(preimage.toString().getBytes());
            } else {
                // otherwise, just get the message digest
                sha3Hash = DriverUtils.getSha3HashRaw(transactionJObject.toString().getBytes());
            }

            // signing the transaction
            Signature edDsaSigner = new EdDSAEngine(MessageDigest.getInstance("SHA-512"));
            edDsaSigner.initSign(privateKey);
            edDsaSigner.update(sha3Hash);
            byte[] signature = edDsaSigner.sign();
            Ed25519Sha256Fulfillment fulfillment = new Ed25519Sha256Fulfillment(this.publicKey, signature);
            this.transaction.getInputs().get(0)
                    .setFullFillment(Base64.encodeBase64URLSafeString(fulfillment.getEncoded()));
            this.transaction.setSigned(true);

            String id = DriverUtils.getSha3HashHex(
                    DriverUtils.makeSelfSortingGson(this.transaction.toHashInput()).toString().getBytes());
            this.transaction.setId(id);
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * com.bigchaindb.builders.BigchainDbTransactionBuilder.IAssetMetaData#
         * buildAndSign(net.i2p.crypto.eddsa.EdDSAPublicKey,
         * net.i2p.crypto.eddsa.EdDSAPrivateKey)
         */
        @Override
        public IBuild buildAndSign(EdDSAPublicKey publicKey, EdDSAPrivateKey privateKey) throws Exception {
            try {
                this.build(publicKey);
                this.sign(privateKey);
            } catch (InvalidKeyException | SignatureException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return this;
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * com.bigchaindb.builders.BigchainDbTransactionBuilder.IAssetMetaData#
         * buildAndSignAndReturn(net.i2p.crypto.eddsa.EdDSAPublicKey)
         */
        @Override
        public Transaction buildOnly(EdDSAPublicKey publicKey) throws Exception {
            this.build(publicKey);
            return this.transaction;
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * com.bigchaindb.builders.BigchainDbTransactionBuilder.IAssetMetaData#
         * buildAndSignAndReturn(net.i2p.crypto.eddsa.EdDSAPublicKey,
         * net.i2p.crypto.eddsa.EdDSAPrivateKey)
         */
        @Override
        public Transaction buildAndSignOnly(EdDSAPublicKey publicKey, EdDSAPrivateKey privateKey) throws Exception {
            this.buildAndSign(publicKey, privateKey);
            return this.transaction;
        }

        /*
         * (non-Javadoc)
         *
         * @see com.bigchaindb.builders.BigchainDbTransactionBuilder.IBuild#
         * sendTransaction(com.bigchaindb.model.GenericCallback)
         */
        @Override
        public Transaction sendTransaction(GenericCallback callback) throws TimeoutException {
        	if(!BigChainDBGlobals.isConnected()) {
        		connectionManager.processConnectionFailure(BigChainDBGlobals.getCurrentNode());
        		connectionManager.configureNodeToConnect();
        	}
            TransactionsApi.sendTransaction(this.transaction, callback);
            connectionManager.processConnectionSuccess(BigChainDBGlobals.getCurrentNode());
            return this.transaction;
        }

        /*
         * (non-Javadoc)
         *
         * @see com.bigchaindb.builders.BigchainDbTransactionBuilder.IBuild#
         * sendTransaction()
         */
        @Override
        public Transaction sendTransaction() throws TimeoutException {
        	if(!BigChainDBGlobals.isConnected()) {
        		connectionManager.processConnectionFailure(BigChainDBGlobals.getCurrentNode());
        		connectionManager.configureNodeToConnect();
        	}
            try {
				TransactionsApi.sendTransaction(this.transaction);
				connectionManager.processConnectionSuccess(BigChainDBGlobals.getCurrentNode());
			} catch (IOException e) {
        		sendTransaction();
			}
            return this.transaction;
        }

        /**
         * Add an asset along with the assetDataClass
         *
         * @param obj             the asset data
         * @param assetsDataClass the type of the asset data class
         * @return self
         */
        public ITransactionAttributes addAssets(Object obj, Class assetsDataClass) {
            this.assets = obj;
            this.assetsDataClass = assetsDataClass;
            return this;
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * com.bigchaindb.builders.BigchainDbTransactionBuilder.IAssetMetaData#
         * operation(com.bigchaindb.constants.Operations)
         */
        @Override
        public ITransactionAttributes operation(Operations operation) {
            this.operation = operation;
            return this;
        }
    }
}
