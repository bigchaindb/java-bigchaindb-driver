<!---
Copyright BigchainDB GmbH and BigchainDB contributors
SPDX-License-Identifier: (Apache-2.0 AND CC-BY-4.0)
Code is Apache-2.0 and docs are CC-BY-4.0
--->

[![Build Status](https://travis-ci.org/bigchaindb/java-bigchaindb-driver.svg?branch=master)](https://travis-ci.org/bigchaindb/java-bigchaindb-driver)
[![Gitter](http://badges.gitter.im/bigchaindb/bigchaindb.svg)](https://gitter.im/bigchaindb/bigchaindb)
[![java-bigchaindb-driver](media/repo-banner@2x.png)](https://www.bigchaindb.com)

> Official Java driver for [BigchainDB](https://github.com/bigchaindb/bigchaindb) created by [bigchaindb](https://bigchaindb.com).

**Please note**: This driver is compatible with android API 23 and later well.
## Compatibility

| BigchainDB Server | BigchainDB Java Driver |
| ----------------- |------------------------------|
| `2.x`             | `1.x`                      |


## Contents

* [Installation and Usage](#installation-and-usage)
* [Example: Create a transaction](#example-create-a-transaction)
* [Documentation](#bigchaindb-documentation)
* [Authors](#authors)
* [License](#license)

## Installation

The build system supports both maven and gradle -

##### Maven users 
- In your `pom.xml` add java-driver as dependency

```		
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
##### Gradle users
- In your `build.gradle` add java-driver as compiling dependency

```
dependencies {
    compile 'com.bigchaindb.bigchaindb-driver:1.0'
    }
    
```
then 

```
./gradlew install
```
## Usage
### Set up your configuration
- If you don't have app-id and app-key, you can register one at [https://testnet.bigchaindb.com/](https://testnet.bigchaindb.com/)

```java
BigchainDbConfigBuilder
	.baseUrl("https://test.bigchaindb.com/")
	.addToken("app_id", <your-app-id>)
	.addToken("app_key", <your-app-key>).setup();
```

### Example: Prepare keys, assets and metadata
```java
//    prepare your keys
net.i2p.crypto.eddsa.KeyPairGenerator edDsaKpg = new net.i2p.crypto.eddsa.KeyPairGenerator();
KeyPair keyPair = edDsaKpg.generateKeyPair();

//    New asset
Map<String, String> assetData = new TreeMap<String, String>() {{
    put("city", "Berlin, DE");
    put("temperature", "22");
    put("datetime", new Date().toString());
}};

//    New metadata
MetaData metaData = new MetaData();
metaData.setMetaData("what", "My first BigchainDB transaction");
```

### Example: Create a Transaction
```java	
//    Set up your transaction
Transaction transaction = BigchainDbTransactionBuilder
	.init()
	.addAssets(assetData, TreeMap.class)
	.addMetaData(metaData)
	.operation(Operations.CREATE)
	.buildOnly((EdDSAPublicKey) keyPair.getPublic());
```

### Example: Create and Sign Transaction
```java
//    Set up your transaction
Transaction transaction = BigchainDbTransactionBuilder
	.init()
	.addAssets(assetData, TreeMap.class)
	.addMetaData(metaData)
	.operation(Operations.CREATE)
	.buildAndSignOnly((EdDSAPublicKey) keyPair.getPublic(), (EdDSAPrivateKey) keyPair.getPrivate());

```

### Example: Create, Sign and Send a Transaction
```java
//    Set up your transaction
Transaction transaction = BigchainDbTransactionBuilder
	.init()
	.addAssets(assetData, TreeMap.class)
	.addMetaData(metaData)
	.operation(Operations.CREATE)
	.buildAndSign((EdDSAPublicKey) keyPair.getPublic(), (EdDSAPrivateKey) keyPair.getPrivate())
	.sendTransaction();

```

### Example: Setup Config with Websocket Listener
```java
public class MyCustomMonitor implements MessageHandler {
	@Override
	public void handleMessage(String message) {
		ValidTransaction validTransaction = JsonUtils.fromJson(message, ValidTransaction.class);
	}
}

// config
BigchainDbConfigBuilder
	.baseUrl("https://test.bigchaindb.com/")
	.addToken("app_id", <your-app-id>)
	.addToken("app_key", <your-app-key>)
	.webSocketMonitor(new MyCustomMonitor())
	.setup();
```

<h2>Api Wrappers</h2>
<h3>Transactions</h3>

<h4>Send a Transaction</h4>

```java
TransactionsApi.sendTransaction(Transaction transaction) throws IOException
```

<h4>Send a Transaction with Callback</h4>

```java
TransactionsApi.sendTransaction(Transaction transaction, final GenericCallback callback) 
```

<h4>Get Transaction given a Transaction Id</h4>

```java
Transaction TransactionsApi.getTransactionById(String id) throws IOException
```

<h4>Get Transaction given an Asset Id</h4>

```java
Transactions TransactionsApi.getTransactionsByAssetId(String assetId, Operations operation)
```

<h3>Outputs</h3>

<h4>Get Outputs given a public key</h4>

```java
Outputs getOutputs(String publicKey) throws IOException
```

<h4>Get Spent Outputs given a public key</h4>

```java
Outputs getSpentOutputs(String publicKey) throws IOException
```

<h4>Get Unspent Outputs given a public key</h4>

```java
Outputs getUnspentOutputs(String publicKey) throws IOException
```

<h3>Assets</h3>

<h4>Get Assets given search key</h4>

```java
Assets getAssets(String searchKey) throws IOException
```

<h4>Get Assets given search key and limit</h4>

```java
Assets getAssetsWithLimit(String searchKey, String limit) throws IOException
```

<h3>Blocks</h3>

<h4>Get Blocks given block id</h4>

```java
Block getBlock(String blockId) throws IOException
```

<h4>Get Blocks given transaction id</h4>

```java
List<String> getBlocksByTransactionId(String transactionId) throws IOException
```

<h3>MetaData</h3>

<h4>Get MetaData given search key</h4>

```java
MetaDatas getMetaData(String searchKey) throws IOException
```

<h4>Get MetaData given search key and limit</h4>

```java
MetaDatas getMetaDataWithLimit(String searchKey, String limit) throws IOException
```

<h3>Validators</h3>

<h4>Gets the the local validators set of a given node</h4>

```java
Validators getValidators() throws IOException
```


## BigchainDB Documentation

- [HTTP API Reference](https://docs.bigchaindb.com/projects/server/en/latest/http-client-server-api.html)
- [The Transaction Model](https://docs.bigchaindb.com/projects/server/en/latest/metadata-models/transaction-model.html?highlight=crypto%20conditions)
- [Inputs and Outputs](https://docs.bigchaindb.com/projects/server/en/latest/metadata-models/inputs-outputs.html)
- [Asset Transfer](https://docs.bigchaindb.com/projects/py-driver/en/latest/usage.html#asset-transfer)
- [All BigchainDB Documentation](https://docs.bigchaindb.com/)

## Authors
- inspired by [http://github.com/authenteq/java-bigchaindb-driver](http://github.com/authenteq/java-bigchaindb-driver). 

- The [bigchaindb](https://bigchaindb.com) team and others including - 
	- @bodia
	- @alvin-reyes
	- @agwego
	- @nf-PostQuantum
	- @Rokko11
	- @tzclucian
	- @kremalicious
	- @avanaur
	- @GerardoGa
	- @bakaoh
	- @innoprenuer

## Licenses

See [LICENSE](LICENSE) and [LICENSE-docs](LICENSE-docs).

Exception: `src/main/java/com/bigchaindb/util/Base58.java` has a different license; see the comments at the top of that file for more information.
