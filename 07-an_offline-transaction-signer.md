# Creating transactions offline

Creating transactions on an offline system is a feature enabling you to protect your
private keys, being sure they can't be captured and sent online by any malware.

After reasing Chapter 6, you might have thought that implementing an offline transaction signer
must be easy with the code we already have. And that is absolutely true!

For the offline part, the only thing we have to change is that rather than passing the 
transaction and its signature's bytes to the RequestAnnounce constructor, we save it to 
separate files:

```
final byte[] transferBytes = BinarySerializer.serializeToBytes(transaction.asNonVerifiable());
final Signer signer = transaction.getSigner().createSigner();

new File('tx.data').bytes = transferBytes
new File('tx.sign').bytes = signer.sign(transferBytes).getBytes()
```

In Java you will need some more boilerplate code to write to a file, but it isn't complex.

Once the transaction data and signature are written to files, these can be put on an online system
where the second part of our solution will read them and send a RequestAccounce to NIS:
```
// read transaction and its signature
final byte[] transferBytes = new File("tx.data").bytes
final byte[] signature = new File("tx.sign").bytes

// send to nis
final RequestAnnounce announce = new RequestAnnounce( transferBytes, signature);

```

And that's all there is, you have an offline transaction signer!
The code for both components is in `code/07`.
