package com.bigchaindb.api;

import com.bigchaindb.builders.BigchainDbTransactionBuilder;
import com.bigchaindb.constants.Operations;
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
 * The Class TransactionTransferApiTest.
 */
public class TransactionTransferApiTest extends AbstractApiTest {
  private static final String publicKey = "302a300506032b657003210033c43dc2180936a2a9138a05f06c892d2fb1cfda4562cbc35373bf13cd8ed373";
  private static final String privateKey = "302e020100300506032b6570042204206f6b0cd095f1e83fc5f08bffb79c7c8a30e77a3ab65f4bc659026b76394fcea8";

  /**
   * Test build transaction using builder.
   *
   * @throws InvalidKeySpecException
   */
  @Test
  public void testBuildTransferTransaction() {
    try {
      Map<String, String> assetData = new TreeMap<String, String>() {{
        put("msg", "Hello BigchainDB!");
      }};
      MetaData metaData = new MetaData();

      Transaction transaction = BigchainDbTransactionBuilder
          .init()
          .addAssets(assetData, TreeMap.class)
          .operation(Operations.TRANSFER)
          .addMetaData(metaData)
          .buildAndSignOnly(
              (EdDSAPublicKey) Account.publicKeyFromHex(publicKey),
              (EdDSAPrivateKey) Account.privateKeyFromHex(privateKey));

      assertTrue(transaction.getVersion().equals("2.0"));
      assertTrue(transaction.getSigned());
      assertEquals(transaction.getOperation(), "TRANSFER");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
