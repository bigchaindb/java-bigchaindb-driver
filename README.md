<!---
Copyright BigchainDB GmbH and BigchainDB contributors
SPDX-License-Identifier: (Apache-2.0 AND CC-BY-4.0)
Code is Apache-2.0 and docs are CC-BY-4.0
--->

[![Build Status](https://travis-ci.com/bigchaindb/java-bigchaindb-driver.svg?branch=master)](https://travis-ci.com/bigchaindb/java-bigchaindb-driver)
[![Gitter](http://badges.gitter.im/bigchaindb/bigchaindb.svg)](https://gitter.im/bigchaindb/bigchaindb)
[![java-bigchaindb-driver](media/repo-banner@2x.png)](https://www.bigchaindb.com)

# Official Java and Android Driver for BigchainDB

**Please note**: This driver is compatible with Android API 23 and later.

## Compatibility

| BigchainDB Server | BigchainDB Java Driver |
| ----------------- |------------------------------|
| `2.x`             | `1.x`                      |

## Contents

* [Installation](#installation)
* [Usage](#usage)
* [API Wrappers](#api-wrappers)
* [BigchainDB Documentation](#bigchaindb-documentation)
* [Authors](#authors)
* [Licenses](#licenses)

## Installation

The build system supports both Maven and Gradle.

### Maven Users

In your `pom.xml` file, add `bigchaindb-driver` as a dependency:

```xml
<dependency>
	<groupId>com.bigchaindb</groupId>
	<artifactId>bigchaindb-driver</artifactId>
	<version>1.0</version>
</dependency>
```

then

```
mvn clean install
```

### Gradle Users

In your `build.gradle` file, add `bigchaindb-driver` as a dependency:

```
dependencies {
    implementation 'com.bigchaindb.bigchaindb-driver:1.2'
    }
```

then

```
./gradlew install
```

## Usage

> A sample of an end-to-end CREATE and TRANSFER operation is available in the gist [https://gist.github.com/innoprenuer/d4c6798fe5c0581c05a7e676e175e515](https://gist.github.com/innoprenuer/d4c6798fe5c0581c05a7e676e175e515)

### Set Up Your Configuration

#### Single-Node Setup

```java
BigchainDbConfigBuilder
	.baseUrl("https://node1.example.com/")
	.addToken("header1", <header1_value>)
	.addToken("header2", <header2_value>).setup();
```

#### Multi-Node Setup (More Robust and Reliable)

> **Note** - multi-node setup is only available in version 1.2 and later
>
> **Assumption** - The following setup assumes that all nodes are all connected within same BigchainDB network.

```java
	//define connections
    Map<String, Object> conn1Config = new HashMap<String, Object>(), 
                 conn2Config = new HashMap<String, Object>();
    
    //define headers for connections
    Map<String, String> headers1 = new HashMap<String, String>();
    Map<String, String> headers2 = new HashMap<String, String>();
    
    //config header for connection 1
    headers1.put("app_id", "<your-app-id>");
    headers1.put("app_key", "<your-app-key>");
    
    //config header for connection 2
    headers2.put("app_id", "<your-app-id>");
    headers2.put("app_key", "<your-app-key>");
    
    //config connection 1
    conn1Config.put("baseUrl", "https://node1.mysite.com/");
    conn1Config.put("headers", headers1);
    Connection conn1 = new Connection(conn1Config);
    
    //config connection 2
    conn2Config.put("baseUrl", "https://node2.mysite.com/");
    conn2Config.put("headers", headers2);
    Connection conn2 = new Connection(conn2Config);
    
    //add connections
    List<Connection> connections = new ArrayList<Connection>();
    connections.add(conn1);
    connections.add(conn2);
    //...You can add as many nodes as you want
    
    BigchainDbConfigBuilder
    .addConnections(connections)
    .setTimeout(60000) //override default timeout of 20000 milliseconds
    .setup();
    
    }  
```

### Example: Prepare Keys, Assets and Metadata

```java
// prepare your keys
net.i2p.crypto.eddsa.KeyPairGenerator edDsaKpg = new net.i2p.crypto.eddsa.KeyPairGenerator();
KeyPair keyPair = edDsaKpg.generateKeyPair();

// New asset
Map<String, String> assetData = new TreeMap<String, String>() {{
    put("city", "Berlin, DE");
    put("temperature", "22");
    put("datetime", new Date().toString());
}};

// New metadata
MetaData metaData = new MetaData();
metaData.setMetaData("what", "My first BigchainDB transaction");
```

### Example: Create an Asset

Performing a CREATE transaction in BigchainDB allocates or issues a digital asset.

```java
// Set up, sign, and send your transaction
Transaction createTransaction = BigchainDbTransactionBuilder
	.init()
	.addAssets(assetData, TreeMap.class)
	.addMetaData(metaData)
	.operation(Operations.CREATE)
	.buildAndSign((EdDSAPublicKey) keyPair.getPublic(), (EdDSAPrivateKey) keyPair.getPrivate())
	.sendTransaction();
```

### Example: Transfer an Asset

Performing a TRANSFER transaction in BigchainDB changes an asset's ownership (or, more accurately, authorized signers):

```java
// Generate a new keypair to TRANSFER the asset to
KeyPair targetKeypair = edDsaKpg.generateKeyPair();

// Describe the output you are fulfilling on the previous transaction
final FulFill spendFrom = new FulFill();
spendFrom.setTransactionId(createTransaction.getId());
spendFrom.setOutputIndex(0);

// Change the metadata if you want
MetaData transferMetadata = new MetaData();
metaData.setMetaData("what2", "My first BigchainDB transaction");

// the asset's ID is equal to the ID of the transaction that created it
String assetId = createTransaction.getId();

// By default, the 'amount' of a created digital asset == "1". So we spend "1" in our TRANSFER.
String amount = "1";

// Use the previous transaction's asset and TRANSFER it
Transaction transferTransaction = BigchainDbTransactionBuilder
	.init()
	.addMetaData(metaData)

	// source keypair is used in the input, because the current owner is "spending" the output to transfer it
	.addInput(null, spendFrom, (EdDSAPublicKey) keyPair.getPublic())

	// after this transaction, the target 'owns' the asset, so, the new output includes the target's public key
	.addOutput(output, (EdDSAPublicKey) targetKeypair.getPublic())

	// reference the asset by ID when doing a transfer
	.addAssets(assetId, String.class)
	.operation(Operations.TRANSFER)

	// the source key signs the transaction to authorize the transfer
	.buildAndSign((EdDSAPublicKey) keyPair.getPublic(), (EdDSAPrivateKey) keyPair.getPrivate())
	.sendTransaction();
```

### Example: Setup Config with WebSocket Listener

```java
public class MyCustomMonitor implements MessageHandler {
	@Override
	public void handleMessage(String message) {
		ValidTransaction validTransaction = JsonUtils.fromJson(message, ValidTransaction.class);
	}
}

// config
BigchainDbConfigBuilder
	.baseUrl("https://api.example.net")
	.addToken("app_id", "2bbaf3ff")
	.addToken("app_key", "c929b708177dcc8b9d58180082029b8d")
	.webSocketMonitor(new MyCustomMonitor())
	.setup();
```

### More Examples

#### Example: Create a Transaction (without signing and without sending)

```java
// Set up your transaction but only build it
Transaction transaction = BigchainDbTransactionBuilder
	.init()
	.addAssets(assetData, TreeMap.class)
	.addMetaData(metaData)
	.operation(Operations.CREATE)
	.buildOnly((EdDSAPublicKey) keyPair.getPublic());
```

#### Example: Create and Sign Transaction (without sending it to the ledger)

```java
//    Set up your transaction
Transaction transaction = BigchainDbTransactionBuilder
	.init()
	.addAssets(assetData, TreeMap.class)
	.addMetaData(metaData)
	.operation(Operations.CREATE)
	.buildAndSignOnly((EdDSAPublicKey) keyPair.getPublic(), (EdDSAPrivateKey) keyPair.getPrivate());
```

## API Wrappers

### Transactions

#### Send a Transaction

```java
TransactionsApi.sendTransaction(Transaction transaction) throws IOException
```

#### Send a Transaction with Callback

```java
TransactionsApi.sendTransaction(Transaction transaction, final GenericCallback callback) 
```

#### Get Transaction given a Transaction Id

```java
Transaction TransactionsApi.getTransactionById(String id) throws IOException
```

#### Get Transaction given an Asset Id

```java
Transactions TransactionsApi.getTransactionsByAssetId(String assetId, Operations operation)
```

### Outputs

#### Get Outputs given a public key

```java
Outputs getOutputs(String publicKey) throws IOException
```

#### Get Spent Outputs given a public key

```java
Outputs getSpentOutputs(String publicKey) throws IOException
```

#### Get Unspent Outputs given a public key

```java
Outputs getUnspentOutputs(String publicKey) throws IOException
```

### Assets

#### Get Assets given search key

```java
Assets getAssets(String searchKey) throws IOException
```

#### Get Assets given search key and limit

```java
Assets getAssetsWithLimit(String searchKey, String limit) throws IOException
```

### Blocks

#### Get Blocks given block id

```java
Block getBlock(String blockId) throws IOException
```

#### Get Blocks given transaction id

```java
List<String> getBlocksByTransactionId(String transactionId) throws IOException
```

### MetaData

#### Get MetaData given search key

```java
MetaDatas getMetaData(String searchKey) throws IOException
```

#### Get MetaData given search key and limit

```java
MetaDatas getMetaDataWithLimit(String searchKey, String limit) throws IOException
```

### Validators

#### Gets the the local validators set of a given node

```java
Validators getValidators() throws IOException
```

## BigchainDB Documentation

* [HTTP API Reference](https://docs.bigchaindb.com/projects/server/en/latest/http-client-server-api.html)
* [The Transaction Model](https://docs.bigchaindb.com/projects/server/en/latest/metadata-models/transaction-model.html?highlight=crypto%20conditions)
* [Inputs and Outputs](https://docs.bigchaindb.com/projects/server/en/latest/metadata-models/inputs-outputs.html)
* [Asset Transfer](https://docs.bigchaindb.com/projects/py-driver/en/latest/usage.html#asset-transfer)
* [All BigchainDB Documentation](https://docs.bigchaindb.com/)

## Authors

Inspired by [http://github.com/authenteq/java-bigchaindb-driver](http://github.com/authenteq/java-bigchaindb-driver).

The [BigchainDB](https://bigchaindb.com) team and others including:

* @bodia
* @alvin-reyes
* @agwego
* @nf-PostQuantum
* @Rokko11
* @tzclucian
* @kremalicious
* @avanaur
* @GerardoGa
* @bakaoh
* @innoprenuer

## Release Process

To execute a release build with Maven, define `performRelease` to enable GPG signing:

`mvn clean package install -DperformRelease`

## Licenses

See [LICENSE](LICENSE) and [LICENSE-docs](LICENSE-docs).

Exception: `src/main/java/com/bigchaindb/util/Base58.java` has a different license; see the comments at the top of that file for more information.
