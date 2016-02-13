# Introduction

## Nem Presentation

Link to blockchain intro.

Nem is providing:

* editable n-of-m multi-sig: an action in a Nem account is authorized only if n of m key identified as authorised validate it. 
  When you create your account, it is a 1-of-1 account, meaning only you need to validate any operation. It is however possible
  to modify the account to add authorised keys, and specify how many of them need to validate an operation. This can be 1-of-2,
  or 2-of-5, and can evolve over time.
* mosaic, an implementation of asset also known as colored coins.
* namespaces.
* a client (NCC) - server (NIS) approach, easing the development of solutions on Nem as your software only has to talk to a NIS instance.
* software backed by a test suite
* Proof of Importance
* a peers reputation system
* a one minute average block time
* delegated harvesting: not need to let your own computer connected to compute blocks and reap fees, you can delegate your importance to 
  a NIS instance of your choice without risk of loosing your funds
* a currency named XEM, used to pay for fees of operations on the blockchain

## Integrating with Nem

There are two ways to integrate with Nem: the first using Nem libraries, the other using the REST API accessible on a NIS instance.

### Library approach

This approach is the easiest, but requires you to develop on the Java Virtual Machine, as you use the Nem libraries to interact with the 
Nem blockchain. Complete documentation is available as [javadoc](http://www.nem.ninja/org.nem.core/).

### REST API

This approach lets you use any language, but is more cumbersome as you interact with a NIS instance providing a REST interface and you, 
or your rest library, need to manage all communication with NIS.

## Testing net

Before you run your code on the Nem blockchain, you might want to validate it in a test environment, without having to spend real XEMS
to pay the fees of your operations. That's the purpose of the test net, a version of the Nem blockchain used for testing not only your
applications, but also newer versions of the Nem software itself.

If you run a NIS instance yourself, you can configure it with the key `nem.network`. For you NIS to join the production Nem blockchain, 
set its value to `mainnet`, and for it to use the test blockchain, set its value to `testnet`. `mainnet` and `testnet`  is also the way we will identify 
in this document the two environments. 

If you don't run your own NIS instance, but still want to work in the testnet, you can find a list of NIS instance part of the testnet at
[http://bob.nem.ninja:8765/#/nodes/](http://bob.nem.ninja:8765/#/nodes/).

## Technical links

* [technical paper](http://blog.nem.io/nem-technical-report/)
* [NIS API](http://bob.nem.ninja/docs/)
* [NCC API](http://nem.io/ncc/index.html)
* [Javadoc](http://www.nem.ninja/org.nem.core/)
* [Nem's Github](https://github.com/NewEconomyMovement)
* [Bitcoin and Cryptocurrency Technologies](https://freedom-to-tinker.com/blog/randomwalker/the-princeton-bitcoin-textbook-is-now-freely-available/)
