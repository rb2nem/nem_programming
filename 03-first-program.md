# Our first program
Before we can take a closer look at the test code you ran in the previous chapter,
we need to have a better understanding of NEM's accounts and addresses.

As explained in Chapter 2 of the [Technical Reference](http://nem.io/NEM_techRef.pdf), an account is a cryptographic keypair associated to a mutable state stored on the NEM blockchain. The state of the account is modified when transactions involving it are accepted by the network. 

An account is identified by its address, which is a base-32 encoded triplet consisting of:
* network byte: is it an address on the testnet or the mainnet?
* 160-bit hash of the public key
* 4 byte checksum

From this description we see that we can generate anew account offline, there's no need to be connected to the network to create an account. That's exactly what the est code does!

Let's take a closer look at it. First we import the classes that will be used in the code:
```
import org.nem.core.crypto.KeyPair;
import org.nem.core.model.Address;
import org.nem.core.model.NetworkInfos;
```
Then we create a new key pair. The constructor of the class [KeyPair[(http://www.nem.ninja/org.nem.core/org/nem/core/crypto/KeyPair.html) will create a random new private key and its associated public key:
```
someKey = new KeyPair();
```
With this done, we can print the pivate and public key that were generated:

```
println(String.format("Private key: %s", someKey.getPrivateKey()));
println(String.format(" Public key: %s", someKey.getPublicKey()));
```
As mentioned above, an address is generated from the public key, but also references the network (testnet, mainnet) for which it is valid. That's why we pass 2 arguments to the method [Address.fromPublicKey](http://www.nem.ninja/org.nem.core/org/nem/core/model/Address.html#fromPublicKey-byte-org.nem.core.crypto.PublicKey-): the network byte value for the testnet we get from [NetworkInfos.getTestNetworkInfo](http://www.nem.ninja/org.nem.core/org/nem/core/model/NetworkInfos.html#getTestNetworkInfo--), and the publick key we just generated:

```
anAddress = Address.fromPublicKey(
              NetworkInfos.getTestNetworkInfo().getVersion(),
                                                        someKey.getPublicKey());
```
Once the address is generated, we can print it:
```
println(String.format("    Address: %s", anAddress));
```

