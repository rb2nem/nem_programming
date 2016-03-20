# Other transaction types

As mentioned in chapter 6, transfer transactions are only one of multiple transaction types available in NEM.
In this chapter we will look at other transaction types and how to generate.

## Common operations

Here are operations that might have to be done similarly for multiple transaction types. Rahter than repeating them
they are all mentioned here at once.

### Timestamp

As previously, the timestamp of the transaction is obtained from an instance of SystemTimeProvider:
```
timeProvider=new SystemTimeProvider()
timestampt=timeProvider.getCurrentTime()
```


### Sender Account
The account of the sender as as previously built from its private key:
```
private_key= PrivateKey.fromHexString("5e7cfb371bd02616747900a1ec223675036ca5de56729b158012b086def6ae01")
sender_key_pair= new KeyPair(private_key)
sender_account=new Account(sender_key_pair)
```

### Recipient account
From public key
```
new Account(Address.fromPublicKey(PublicKey.fromHexString("REPLACE_WITH_HEX_STRING")))
```

or from account:
```
new Account(Address.fromEncoded("REPLACE_WITH_ACCOUNT".replaceAll("-","")))
```

### Serialising a transaction

```
// serialize transaction
final byte[] transferBytes = BinarySerializer.serializeToBytes(transaction.asNonVerifiable());
final Signer signer = transaction.getSigner().createSigner();

new File('tx.data').bytes = transferBytes
new File('tx.sign').bytes = signer.sign(transferBytes).getBytes()
```

### Anouncing a transaction
Initialise the announce:
```
final RequestAnnounce announce = new RequestAnnounce( transferBytes, signature);
```
and send it to NIS (see chapter 6 for details):
```
f = conn.postAsync( node,
        NisApiId.NIS_REST_TRANSACTION_ANNOUNCE,
                new HttpJsonPostRequest(announce))

```

## Multisig conversion

### Code description

Converting an account to a multisig account is also done by a transaction: MultisigAggregateModificationTransaction.
The arguments of the contructor of this transaction are:
  * the transaction timestamp (TimeInstant instance)
  * the sender (Account instance)
  * a list of modifications to apply (Collection<MultisigCosignatoryModification>).


The two first arguments are covered in the common operations of this chapter. The third one is specific.
After having initialised the account instance for the cosignatory, we build an `ArrayList` of `MultisigCosignatoryModification`,
to which we add an instance of `MultisigCosignatoryModification` specifying the modification type (`MultisigModificationType.AddCosignatory`) 
and the cosignatory:
```
modifications = new  ArrayList<MultisigCosignatoryModification>();
modification = new MultisigCosignatoryModification(MultisigModificationType.AddCosignatory, cosignatory);
modifications.add( modification );
```

Now we can initialised an instance of `MultisigAggregateModificationTransaction` 
```
transaction=new MultisigAggregateModificationTransaction(timeProvider.getCurrentTime(),
                           sender_account,
                                                      modifications)
transaction.setDeadline(ts_ti);
```

which can then be serialised and announced.

Find a working offline transaction creator code in `code/08/offline_multisig_conversion.groovy`

### Links

  * [MultisigAggregateModificationTransaction](http://www.nem.ninja/org.nem.core/org/nem/core/model/MultisigAggregateModificationTransaction.html)
  * [MultisigModificationType](http://www.nem.ninja/org.nem.core/org/nem/core/model/MultisigModificationType.html)
  * [MultisigAggregateModificationTransaction](http://www.nem.ninja/org.nem.core/org/nem/core/model/MultisigAggregateModificationTransaction.html)
