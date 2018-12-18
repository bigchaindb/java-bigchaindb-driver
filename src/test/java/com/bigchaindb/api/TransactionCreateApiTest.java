/*
 * Copyright BigchainDB GmbH and BigchainDB contributors
 * SPDX-License-Identifier: (Apache-2.0 AND CC-BY-4.0)
 * Code is Apache-2.0 and docs are CC-BY-4.0
 */
package com.bigchaindb.api;

import com.bigchaindb.api.TransactionsApi;
import com.bigchaindb.builders.BigchainDbTransactionBuilder;
import com.bigchaindb.constants.Operations;
import com.bigchaindb.exceptions.TransactionNotFoundException;
import com.bigchaindb.json.strategy.MetaDataDeserializer;
import com.bigchaindb.json.strategy.MetaDataSerializer;
import com.bigchaindb.json.strategy.TransactionDeserializer;
import com.bigchaindb.json.strategy.TransactionsDeserializer;
import com.bigchaindb.model.*;
import com.bigchaindb.util.JsonUtils;

import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import okhttp3.Response;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.KeyPair;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


/**
 * The Class TransactionCreateApiTest.
 */
public class TransactionCreateApiTest extends AbstractApiTest {

    public static String TRANSACTION_ID = "4957744b3ac54434b8270f2c854cc1040228c82ea4e72d66d2887a4d3e30b317";
    public static String NON_EXISTENT_TRANSACTION_ID = "4957744b3ac5434b8270f2c854cc1040228c8ea4e72d66d2887a4d3e30b317";
    public static String V1_GET_TRANSACTION_JSON = "{\n" +
            "  \"asset\": {\n" +
            "    \"data\": {\n" +
            "      \"msg\": \"Hello BigchainDB!\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"id\": \"4957744b3ac54434b8270f2c854cc1040228c82ea4e72d66d2887a4d3e30b317\",\n" +
            "  \"inputs\": [\n" +
            "    {\n" +
            "      \"fulfillment\": \"pGSAIDE5i63cn4X8T8N1sZ2mGkJD5lNRnBM4PZgI_zvzbr-cgUCy4BR6gKaYT-tdyAGPPpknIqI4JYQQ-p2nCg3_9BfOI-15vzldhyz-j_LZVpqAlRmbTzKS-Q5gs7ZIFaZCA_UD\",\n" +
            "      \"fulfills\": null,\n" +
            "      \"owners_before\": [\n" +
            "        \"4K9sWUMFwTgaDGPfdynrbxWqWS6sWmKbZoTjxLtVUibD\"\n" +
            "      ]\n" +
            "    }\n" +
            "  ],\n" +
            "  \"metadata\": {\n" +
            "    \"sequence\": 0\n" +
            "  },\n" +
            "  \"operation\": \"CREATE\",\n" +
            "  \"outputs\": [\n" +
            "    {\n" +
            "      \"amount\": \"1\",\n" +
            "      \"condition\": {\n" +
            "        \"details\": {\n" +
            "          \"public_key\": \"4K9sWUMFwTgaDGPfdynrbxWqWS6sWmKbZoTjxLtVUibD\",\n" +
            "          \"type\": \"ed25519-sha-256\"\n" +
            "        },\n" +
            "        \"uri\": \"ni:///sha-256;PNYwdxaRaNw60N6LDFzOWO97b8tJeragczakL8PrAPc?fpt=ed25519-sha-256&cost=131072\"\n" +
            "      },\n" +
            "      \"public_keys\": [\n" +
            "        \"4K9sWUMFwTgaDGPfdynrbxWqWS6sWmKbZoTjxLtVUibD\"\n" +
            "      ]\n" +
            "    }\n" +
            "  ],\n" +
            "  \"version\": \"2.0\"\n" +
            "}";

    public static String V1_GET_TRANSACTION_BY_ASSETS_JSON = "[{\n" +
            "  \"asset\": {\n" +
            "    \"id\": \"4957744b3ac54434b8270f2c854cc1040228c82ea4e72d66d2887a4d3e30b317\"\n" +
            "  },\n" +
            "  \"id\": \"79ef6803210c941903d63d08b40fa17f0a5a04f11ac0ff04451553a187d97a30\",\n" +
            "  \"inputs\": [\n" +
            "    {\n" +
            "      \"fulfillment\": \"pGSAIDE5i63cn4X8T8N1sZ2mGkJD5lNRnBM4PZgI_zvzbr-cgUAYRI8kzKaZcrW-_avQrAIk5q-7o_7U6biBvoHk1ioBLqHSBcE_PAdNEaeWesAAW_HeCqNUWKaJ5Lzo5Nfz7QgN\",\n" +
            "      \"fulfills\": {\n" +
            "        \"output_index\": 0,\n" +
            "        \"transaction_id\": \"4957744b3ac54434b8270f2c854cc1040228c82ea4e72d66d2887a4d3e30b317\"\n" +
            "      },\n" +
            "      \"owners_before\": [\n" +
            "        \"4K9sWUMFwTgaDGPfdynrbxWqWS6sWmKbZoTjxLtVUibD\"\n" +
            "      ]\n" +
            "    }\n" +
            "  ],\n" +
            "  \"metadata\": {\n" +
            "    \"sequence\": 1\n" +
            "  },\n" +
            "  \"operation\": \"TRANSFER\",\n" +
            "  \"outputs\": [\n" +
            "    {\n" +
            "      \"amount\": \"1\",\n" +
            "      \"condition\": {\n" +
            "        \"details\": {\n" +
            "          \"public_key\": \"3yfQPHeWAa1MxTX9Zf9176QqcpcnWcanVZZbaHb8B3h9\",\n" +
            "          \"type\": \"ed25519-sha-256\"\n" +
            "        },\n" +
            "        \"uri\": \"ni:///sha-256;lu6ov4AKkee6KWGnyjOVLBeyuP0bz4-O6_dPi15eYUc?fpt=ed25519-sha-256&cost=131072\"\n" +
            "      },\n" +
            "      \"public_keys\": [\n" +
            "        \"3yfQPHeWAa1MxTX9Zf9176QqcpcnWcanVZZbaHb8B3h9\"\n" +
            "      ]\n" +
            "    }\n" +
            "  ],\n" +
            "  \"version\": \"2.0\"\n" +
            "},\n" +
            "{\n" +
            "  \"asset\": {\n" +
            "    \"id\": \"4957744b3ac54434b8270f2c854cc1040228c82ea4e72d66d2887a4d3e30b317\"\n" +
            "  },\n" +
            "  \"id\": \"1fec726a3b426498147f1a1f19a92c187d551a7f66db4b88d666d7dcc10e86a4\",\n" +
            "  \"inputs\": [\n" +
            "    {\n" +
            "      \"fulfillment\": \"pGSAICw7Ul-c2lG6NFbHp3FbKRC7fivQcNGO7GS4wV3A-1QggUARCMty2JBK_OyPJntWEFxDG4-VbKMy853NtqwnPib5QUJIuwPQa1Y4aN2iIBuoqGE85Pmjcc1ScG9FCPSQHacK\",\n" +
            "      \"fulfills\": {\n" +
            "        \"output_index\": 0,\n" +
            "        \"transaction_id\": \"79ef6803210c941903d63d08b40fa17f0a5a04f11ac0ff04451553a187d97a30\"\n" +
            "      },\n" +
            "      \"owners_before\": [\n" +
            "        \"3yfQPHeWAa1MxTX9Zf9176QqcpcnWcanVZZbaHb8B3h9\"\n" +
            "      ]\n" +
            "    }\n" +
            "  ],\n" +
            "  \"metadata\": {\n" +
            "    \"sequence\": 2\n" +
            "  },\n" +
            "  \"operation\": \"TRANSFER\",\n" +
            "  \"outputs\": [\n" +
            "    {\n" +
            "      \"amount\": \"1\",\n" +
            "      \"condition\": {\n" +
            "        \"details\": {\n" +
            "          \"public_key\": \"3Af3fhhjU6d9WecEM9Uw5hfom9kNEwE7YuDWdqAUssqm\",\n" +
            "          \"type\": \"ed25519-sha-256\"\n" +
            "        },\n" +
            "        \"uri\": \"ni:///sha-256;Ll1r0LzgHUvWB87yIrNFYo731MMUEypqvrbPATTbuD4?fpt=ed25519-sha-256&cost=131072\"\n" +
            "      },\n" +
            "      \"public_keys\": [\n" +
            "        \"3Af3fhhjU6d9WecEM9Uw5hfom9kNEwE7YuDWdqAUssqm\"\n" +
            "      ]\n" +
            "    }\n" +
            "  ],\n" +
            "  \"version\": \"2.0\"\n" +
            "}]";

    public static String V1_POST_TRANSACTION_REQUEST = "{\n" +
            "  \"asset\": {\n" +
            "    \"data\": {\n" +
            "      \"msg\": \"Hello BigchainDB!\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"id\": \"4957744b3ac54434b8270f2c854cc1040228c82ea4e72d66d2887a4d3e30b317\",\n" +
            "  \"inputs\": [\n" +
            "    {\n" +
            "      \"fulfillment\": \"pGSAIDE5i63cn4X8T8N1sZ2mGkJD5lNRnBM4PZgI_zvzbr-cgUCy4BR6gKaYT-tdyAGPPpknIqI4JYQQ-p2nCg3_9BfOI-15vzldhyz-j_LZVpqAlRmbTzKS-Q5gs7ZIFaZCA_UD\",\n" +
            "      \"fulfills\": null,\n" +
            "      \"owners_before\": [\n" +
            "        \"4K9sWUMFwTgaDGPfdynrbxWqWS6sWmKbZoTjxLtVUibD\"\n" +
            "      ]\n" +
            "    }\n" +
            "  ],\n" +
            "  \"metadata\": {\n" +
            "    \"sequence\": 0\n" +
            "  },\n" +
            "  \"operation\": \"CREATE\",\n" +
            "  \"outputs\": [\n" +
            "    {\n" +
            "      \"amount\": \"1\",\n" +
            "      \"condition\": {\n" +
            "        \"details\": {\n" +
            "          \"public_key\": \"4K9sWUMFwTgaDGPfdynrbxWqWS6sWmKbZoTjxLtVUibD\",\n" +
            "          \"type\": \"ed25519-sha-256\"\n" +
            "        },\n" +
            "        \"uri\": \"ni:///sha-256;PNYwdxaRaNw60N6LDFzOWO97b8tJeragczakL8PrAPc?fpt=ed25519-sha-256&cost=131072\"\n" +
            "      },\n" +
            "      \"public_keys\": [\n" +
            "        \"4K9sWUMFwTgaDGPfdynrbxWqWS6sWmKbZoTjxLtVUibD\"\n" +
            "      ]\n" +
            "    }\n" +
            "  ],\n" +
            "  \"version\": \"2.0\"\n" +
            "}";

    public static String V1_POST_TRANSACTION_JSON = "{\n" +
            "  \"asset\": {\n" +
            "    \"data\": {\n" +
            "      \"msg\": \"Hello BigchainDB!\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"id\": \"4957744b3ac54434b8270f2c854cc1040228c82ea4e72d66d2887a4d3e30b317\",\n" +
            "  \"inputs\": [\n" +
            "    {\n" +
            "      \"fulfillment\": \"pGSAIDE5i63cn4X8T8N1sZ2mGkJD5lNRnBM4PZgI_zvzbr-cgUCy4BR6gKaYT-tdyAGPPpknIqI4JYQQ-p2nCg3_9BfOI-15vzldhyz-j_LZVpqAlRmbTzKS-Q5gs7ZIFaZCA_UD\",\n" +
            "      \"fulfills\": null,\n" +
            "      \"owners_before\": [\n" +
            "        \"4K9sWUMFwTgaDGPfdynrbxWqWS6sWmKbZoTjxLtVUibD\"\n" +
            "      ]\n" +
            "    }\n" +
            "  ],\n" +
            "  \"metadata\": {\n" +
            "    \"sequence\": 0\n" +
            "  },\n" +
            "  \"operation\": \"CREATE\",\n" +
            "  \"outputs\": [\n" +
            "    {\n" +
            "      \"amount\": \"1\",\n" +
            "      \"condition\": {\n" +
            "        \"details\": {\n" +
            "          \"public_key\": \"4K9sWUMFwTgaDGPfdynrbxWqWS6sWmKbZoTjxLtVUibD\",\n" +
            "          \"type\": \"ed25519-sha-256\"\n" +
            "        },\n" +
            "        \"uri\": \"ni:///sha-256;PNYwdxaRaNw60N6LDFzOWO97b8tJeragczakL8PrAPc?fpt=ed25519-sha-256&cost=131072\"\n" +
            "      },\n" +
            "      \"public_keys\": [\n" +
            "        \"4K9sWUMFwTgaDGPfdynrbxWqWS6sWmKbZoTjxLtVUibD\"\n" +
            "      ]\n" +
            "    }\n" +
            "  ],\n" +
            "  \"version\": \"2.0\"\n" +
            "}";

    private static final String publicKey = "302a300506032b657003210033c43dc2180936a2a9138a05f06c892d2fb1cfda4562cbc35373bf13cd8ed373";
    private static final String privateKey = "302e020100300506032b6570042204206f6b0cd095f1e83fc5f08bffb79c7c8a30e77a3ab65f4bc659026b76394fcea8";

    /**
     * Test get transaction.
     */
    @Test
    public void testGetTransaction() {
        try {
            Transaction transaction = TransactionsApi.getTransactionById(TRANSACTION_ID);
            assertTrue(transaction.getId().equals(TRANSACTION_ID));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransactionNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test get transaction when the transaction with supplied id is not present.
     */
    @Test(expected = TransactionNotFoundException.class)
    public void testGetNonExistentTransaction() throws TransactionNotFoundException {
        try {
           TransactionsApi.getTransactionById(NON_EXISTENT_TRANSACTION_ID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * Test get transactions by assets id
     */
    @Test
    public void testGetTransactionsByAsset() {
        try {
            Transactions transactions = TransactionsApi.getTransactionsByAssetId(TRANSACTION_ID, Operations.TRANSFER);
            assertTrue(transactions.getTransactions().size() == 2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test build transaction using builder.
     *
     * @throws InvalidKeySpecException
     */
    @Test
    public void testBuildCreateTransaction() {
        try {
            Map<String, String> assetData = new TreeMap<String, String>() {{
                put("msg", "Hello BigchainDB!");
            }};

            MetaData metaData = new MetaData();
            metaData.setId("51ce82a14ca274d43e4992bbce41f6fdeb755f846e48e710a3bbb3b0cf8e4204");
            metaData.setMetaData("msg", "Hello BigchainDB 1!");

            Transaction transaction = BigchainDbTransactionBuilder
                    .init()
                    .addAssets(assetData, TreeMap.class)
                    .addMetaData(metaData)
                    .addMetaDataClassSerializer(MetaData.class, new MetaDataSerializer())
                    .addMetaDataClassDeserializer(MetaDatas.class, new MetaDataDeserializer())
                    .operation(Operations.CREATE)
                    .buildAndSignOnly(
                            (EdDSAPublicKey) Account.publicKeyFromHex(publicKey),
                            (EdDSAPrivateKey) Account.privateKeyFromHex(privateKey));

            assertTrue(transaction.getVersion().equals("2.0"));
            assertTrue(transaction.getAsset().getData() != null);
            assertTrue(transaction.getSigned());
            assertEquals(transaction.getOperation(), "CREATE");

            Input input = transaction.getInputs().get(0);
            assertTrue(input.getOwnersBefore() != null);
            assertTrue(input.getFullFillment() != null);
            assertTrue(input.getFulFills() == null);

            Output output = transaction.getOutputs().get(0);
            assertTrue(output.getAmount() != null);
            assertTrue(output.getCondition().getUri() != null);
            assertTrue(output.getCondition().getDetails().getPublicKey() != null);
            assertTrue(output.getCondition().getDetails().getType() != null);
            assertTrue(output.getPublicKeys() != null);

            assertTrue(((MetaData) transaction.getMetaData()).getMetadata().get("msg").equals("Hello BigchainDB 1!"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Test build only transaction.
     *
     * @throws InvalidKeySpecException
     */
    @Test
    public void testBuildOnlyCreateTransaction() {
        try {
            Map<String, String> assetData = new TreeMap<String, String>() {{
                put("msg", "Hello BigchainDB!");
            }};

            EdDSAPublicKey edDSAPublicKey = (EdDSAPublicKey) Account.publicKeyFromHex(publicKey);
            FulFill fulFill = new FulFill();
            fulFill.setOutputIndex(0);
            fulFill.setTransactionId("2d431073e1477f3073a4693ac7ff9be5634751de1b8abaa1f4e19548ef0b4b0e");

            Transaction transaction = BigchainDbTransactionBuilder
                    .init()
                    .addAssets(assetData, TreeMap.class)
                    .addInput("pGSAIDE5i63cn4X8T8N1sZ2mGkJD5lNRnBM4PZgI_zvzbr-cgUCy4BR6gKaYT-tdyAGPPpknIqI4JYQQ-p2nCg3_9BfOI-15vzldhyz-j_LZVpqAlRmbTzKS-Q5gs7ZIFaZCA_UD", fulFill, edDSAPublicKey)
                    .addOutput("1", edDSAPublicKey)
                    .operation(Operations.CREATE)
                    .buildOnly(edDSAPublicKey);

            assertTrue(transaction.getVersion().equals("2.0"));
            assertTrue(transaction.getSigned() == null);
            assertEquals(transaction.getOperation(), "CREATE");

            Input input = transaction.getInputs().get(0);
            assertTrue(input.getOwnersBefore() != null);
            assertTrue(input.getFullFillment() != null);
            assertTrue(input.getFulFills() != null);
            assertTrue(input.getFulFills().getOutputIndex().equals(0));
            assertTrue(input.getFulFills().getTransactionId().equals("2d431073e1477f3073a4693ac7ff9be5634751de1b8abaa1f4e19548ef0b4b0e"));

            Output output = transaction.getOutputs().get(0);
            assertTrue(output.getAmount() != null);
            assertTrue(output.getCondition().getUri() != null);
            assertTrue(output.getCondition().getDetails().getPublicKey() != null);
            assertTrue(output.getCondition().getDetails().getType() != null);
            assertTrue(output.getPublicKeys() != null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPostTransactionOfCreateUsingBuilder() throws Exception {
        net.i2p.crypto.eddsa.KeyPairGenerator edDsaKpg = new net.i2p.crypto.eddsa.KeyPairGenerator();
        KeyPair keyPair = edDsaKpg.generateKeyPair();
        try {
            ObjectDummy dummyAsset = new ObjectDummy();
            dummyAsset.setId("id");
            dummyAsset.setDescription("asset");
            System.out.println(dummyAsset.toMapString());

            ObjectDummy dummyMeta = new ObjectDummy();
            dummyMeta.setId("id");
            dummyMeta.setDescription("meta");

            Transaction transaction = BigchainDbTransactionBuilder.init().addAssets(dummyAsset, ObjectDummy.class).addMetaData(dummyMeta)
                    .operation(Operations.CREATE)
                    .buildAndSign((EdDSAPublicKey) keyPair.getPublic(), (EdDSAPrivateKey) keyPair.getPrivate())
                    .sendTransaction();
            assertNotNull(transaction.getId());
            assertEquals(transaction.getOperation(), "CREATE");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test post transaction using builder with call back.
     * @throws Exception 
     */
    @Test
    public void testPostCreateTransactionUsingBuilderWithCallBack() throws Exception {
        net.i2p.crypto.eddsa.KeyPairGenerator edDsaKpg = new net.i2p.crypto.eddsa.KeyPairGenerator();
        KeyPair keyPair = edDsaKpg.generateKeyPair();
        try {
            Map<String, String> metaData = new TreeMap<String, String>() {{
                put("what", "bigchaintrans");
            }};
            Map<String, String> assetData = new TreeMap<String, String>() {{
                put("firstname", "alvin");
            }};
            BigchainDbTransactionBuilder.init()
                    .addAssets(assetData, TreeMap.class)
                    .addMetaData(metaData)
                    .operation(Operations.CREATE)
                    .buildAndSign((EdDSAPublicKey) keyPair.getPublic(), (EdDSAPrivateKey) keyPair.getPrivate())
                    .sendTransaction(new GenericCallback() {

                        @Override
                        public void transactionMalformed(Response response) {
                            // System.out.println(response.message());
                            System.out.println("malformed " + response.message());
                        }

                        @Override
                        public void pushedSuccessfully(Response response) {
                            System.out.println("pushedSuccessfully");
                        }

                        @Override
                        public void otherError(Response response) {
                            System.out.println("otherError");

                        }
                    });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPostCreateTransactionOfObjectMetaDataUsingBuilder() throws Exception {
        net.i2p.crypto.eddsa.KeyPairGenerator edDsaKpg = new net.i2p.crypto.eddsa.KeyPairGenerator();
        KeyPair keyPair = edDsaKpg.generateKeyPair();
        try {
            ObjectDummy dummyAsset = new ObjectDummy();
            dummyAsset.setId("id");
            dummyAsset.setDescription("asset");
            System.out.println(dummyAsset.toMapString());

            SomeMetaData metaData = new SomeMetaData();

            Transaction transaction = BigchainDbTransactionBuilder
                    .init()
                    .addAssets(dummyAsset, ObjectDummy.class)
                    .addMetaData(metaData)
                    .operation(Operations.CREATE)
                    .buildAndSign((EdDSAPublicKey) keyPair.getPublic(), (EdDSAPrivateKey) keyPair.getPrivate())
                    .sendTransaction();
            assertNotNull(transaction.getId());
            assertEquals(transaction.getOperation(), "CREATE");

            String jsonString = JsonUtils.toJson(transaction);
            TransactionsDeserializer.setMetaDataClass(SomeMetaData.class);
            TransactionDeserializer.setMetaDataClass(SomeMetaData.class);
            Transaction x = JsonUtils.fromJson(jsonString, Transaction.class);
            SomeMetaData resultMetaData = (SomeMetaData) x.getMetaData();
            Assert.assertEquals(2, resultMetaData.porperty2.intValue());
            Assert.assertEquals(3, resultMetaData.properties.size());
            Assert.assertEquals("three", resultMetaData.properties.get(2));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class ObjectDummy extends DataModel {
        private String id;
        private String description;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public class SomeMetaData {
        public String property1 = "property1";
        public Integer porperty2 = 2;
        public BigDecimal property3 = new BigDecimal("3.3");
        public int property4 = 4;
        public ArrayList<String> properties = new ArrayList<String>() {{
            add("one");
            add("two");
            add("three");
        }};
        public Date date = new Date();
    }
}
