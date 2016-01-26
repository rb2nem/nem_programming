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

## Integrating with Nem

There are two ways to integrate with Nem: the first using Nem libraries, the other using the REST API accessible on a NIS instance.

### Library approach

This approach is the easiest, but requires you to develop on the Java Virtual Machine, as you use the Nem libraries to interact with the 
Nem blockchain. Complete documentation is available as [javadoc](http://www.nem.ninja/org.nem.core/).

### REST API

This approach lets you use any language, but is more cumbersome as you interact with a NIS instance providing a REST interface and you, 
or your rest library, need to manage all communication with NIS.

## Technical links

* [technical paper](http://blog.nem.io/nem-technical-report/)
* [NIS API](http://bob.nem.ninja/docs/)
* [NCC API](http://nem.io/ncc/index.html)
* [Javadoc](http://www.nem.ninja/org.nem.core/)
* [Nem's Github](https://github.com/NewEconomyMovement)

