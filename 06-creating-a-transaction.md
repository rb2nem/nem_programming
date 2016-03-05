# Creating a transaction

There are different kinds of transactions, all inheriting from the `Transaction` class:
ImportanceTransferTransaction, MosaicDefinitionCreationTransaction,
MosaicSupplyChangeTransaction, MultisigAggregateModificationTransaction,
MultisigSignatureTransaction, MultisigTransaction,
ProvisionNamespaceTransaction, and the one we will use in this chapter: TransferTransaction.

The Transaction constructor takes as arguments:

* the timestamp at which the transaction is created
* the sender account
* the recipient account
* the amount
* an attachment

The timestamp can be had with the method `getCurrentTime()` from an `SystemTimeProvider` instance.
The recipient account instance can be initialised with `Address.fromEncoded(addressString)` method.
The sender account however has to be initialised with the private key of the account to be able
to sign the transaction. We will create a `PrivateKey` instance with its static method `fromHexString` 
taking as argument the private key as a hex string (containing only digits and letters a to f included).
This `PrivateKey` instance can then be passed to the `KeyPair`constructor, which in its turn can be passed to the `Account`constructor:

```
private_key= PrivateKey.fromHexString(my_private_key_as_hex_string)
sender_key_pair= new KeyPair(private_key)
sender_account=new Account(sender_key_pair)
```

The amount to transfer is passed in microXEMs to the constructor of `Amount`.
As for the attachment, we'll make it an empty attachment with a new instance of `TransferTransactionAttachment`.

Now that all arguments to initialise a transaction are known, we can go ahead:
```
transaction=new TransferTransaction(timeProvider.getCurrentTime(),
                           sender_account,
                           recipient_account,
                           amount,
                           attach)

```
The first thing to do is set the transaction deadline. There again we use the `SystemTimeProvider`:
```
ts_ti=timeProvider.getCurrentTime().addHours(24);
transaction.setDeadline(ts_ti);
```

Now that the transaction is set up, we need to send a `RequestAnnounce` to NIS. The `RequestAnnounce`
constructor takes as arguments the serialised transaction and its signature:
```
// serialize transaction
final byte[] transferBytes = BinarySerializer.serializeToBytes(transaction.asNonVerifiable());

// sign transaction and send to nis
final Signer signer = transaction.getSigner().createSigner();
final RequestAnnounce announce = new RequestAnnounce(
                                        transferBytes,
                                        signer.sign(transferBytes).getBytes());
```

Now we can send the announce request to NIS as we did before with `postAsync`:
```
f = conn.postAsync( node,
                    NisApiId.NIS_REST_TRANSACTION_ANNOUNCE,
                    new HttpJsonPostRequest(announce))
```

All what is left to do is check the result:
```
if (result.isError()) {
        println "ERROR"
        println result.getCode()
        println result.getMessage()

} else {
        // print transaction hash if success
        println result.getTransactionHash()
}

```

The whole code is available at code/06/groovy_transfer_transaction.groovy.


## Links
[Transaction](http://www.nem.ninja/org.nem.core/org/nem/core/model/Transaction.html)
[TransferTransaction](http://www.nem.ninja/org.nem.core/org/nem/core/model/TransferTransaction.html)
[SystemTimeProvider](http://www.nem.ninja/org.nem.core/org/nem/core/time/SystemTimeProvider.html)
[Address](http://www.nem.ninja/org.nem.core/org/nem/core/model/Address.html)
[PrivateKey](http://www.nem.ninja/org.nem.core/org/nem/core/crypto/PrivateKey.html)
[KeyPair](http://www.nem.ninja/org.nem.core/org/nem/core/crypto/KeyPair.html)
[Account](http://www.nem.ninja/org.nem.core/org/nem/core/model/Account.html)
[Amount](http://www.nem.ninja/org.nem.core/org/nem/core/model/primitive/Amount.html)
[RequestAnnounce](http://www.nem.ninja/org.nem.core/org/nem/core/model/ncc/RequestAnnounce.html)
