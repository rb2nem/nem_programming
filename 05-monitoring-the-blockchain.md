# Monitoring the blockchain

You might want to monitor the blockchain to take action when a transaction impacts your account.
the good news is that what we have learned until now is sufficient to implement this!

The complete script is available at `code/05/groovy-monitoring.groovy`.

We define the endpoint connection as earlier, then define the address we want to monitor:
```
monitored_address=Address.fromEncoded("TCJMM2-MN62XC-Y4TREH-ACOJ3M-2FOEC2-MUKCSX-XVR6".replaceAll("-",""))
```
We need to remote the `-` in the address for easy comparison later.
What we will do is getting the last block every 3 seconds, and if we get a block we didn't see earlier, we'll
check the transactions to see if these impact our account. 
To determine if we receive a new block, we check if its height (`block.getHeight()`) is higher than what we have seen earlier.
FIXME: is it possible that we switch to another fork of the chain and miss blocks?

Getting the transactions of a block is easy with `block.getTransactions()`. However in this script we only want to
check transfers, so we check the transaction type: `if (t.getType()==TransactionTypes.TRANSFER)`.

When we see a transfer transaction in a block, we look at the accounts it impacts (`t.getAccounts()`) and if our
monitored address matches one of these accounts (`a.getAddress()`), we examine and possibly print out information like the transaction recipient
(`t.getRecipient()`), the debtor who pays the fee (`t.getDebtor()`), the signer (`t.getSigner()`), and the message (`t.getMessage()`).
The messages is an arrya of bytes, that we can reassemble in a string with `new String(t.getMessage().getDecodedPayload(),"UTF-8")`.

The hash of the transaction is computed with `HashUtils.calculateHash(t)`.


## Links
[TransactionType](http://www.nem.ninja/org.nem.core/org/nem/core/model/TransactionTypes.html)
[Transaction](http://www.nem.ninja/org.nem.core/org/nem/core/model/Transaction.html)
[HashUtils](http://www.nem.ninja/org.nem.core/org/nem/core/model/HashUtils.html)
[Message](http://www.nem.ninja/org.nem.core/org/nem/core/model/Message.html)
[PlainMessage](http://www.nem.ninja/org.nem.core/org/nem/core/messages/PlainMessage.html)
