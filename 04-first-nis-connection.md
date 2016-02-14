# Our first NIS connection

Until now, our code was running offline, with no interaction at all with the NEM blockchain.
For us to integrate with the NEM network and blockchain, we need interact with a NEM node.
Good news, that's what we'll do in this chapter!

## Block height request

Our first program in this chapter will connect to a NIS instance (a node of the NEM network), and request its blockheight.

### Code description

To connect to the NEM network, you can either run your own NIS server as explained in the
appendices of this book, or you can lookup the ip address of a server on [nodeexplorer.com](http://www.nodeexplorer.com).
A node in the NEM network is represented by the class [NodeEndpoint](http://www.nem.ninja/org.nem.core/org/nem/core/node/NodeEndpoint.html) in the
java API, and can be instanciated with the method [fromHost](http://www.nem.ninja/org.nem.core/org/nem/core/node/NodeEndpoint.html#fromHost-java.lang.String-)
taking as argument an IP address or a host name.

The way the NIS interactions are structured in your program is the following:
* First you connect to a NIS instance
* then you send a request, identifying the NIS API ID you are requesting
* when you have the response, you deserialise it with the help of NEM's java api
* finally you can use the received data.

To connect to the NIS instance, we use a [DefaultAsyncNemConnector<TApiId>](http://www.nem.ninja/org.nem.core/org/nem/core/connect/client/DefaultAsyncNemConnector.html), which is parameterised by the API ID we will use, which in our case is [NisApiId](http://www.nem.ninja/org.nem.core/org/nem/core/connect/client/NisApiId.html). 
[NisApiId](http://www.nem.ninja/org.nem.core/org/nem/core/connect/client/NisApiId.html) is an enum listing all calls that can be issued through this API. In our case,
we will issue the call `NisApiId.NIS_REST_CHAIN_HEIGHT`.

Another API is available: [NisPeerId](http://www.nem.ninja/org.nem.core/org/nem/core/node/NisPeerId.html), enabling you to interact with a node, such as 
requesting infor about it with `REST_NODE_INFO`, getting its peers list with `REST_NODE_PEER_LIST` or even pushing a block to it with `REST_PUSH_BLOCK`.

But let's focus on out call to `NisApiId.NIS_REST_CHAIN_HEIGHT` for now. The `DefaultAsyncNemConnector<TApiId> `constructor takes 2 arguments: an `HttpMethodClient<ErrorResponseDeserializerUnion>` instance and an object implementing the interface `ErrorResponseStrategy`.

The first parameter can be simply constructed with `new HttpMethodClient<ErrorResponseDeserializerUnion>().The `ErrorResponseDeserializerUnion` is simply a union that will contain either a `ErrorResponse` (available through the [getError()](http://www.nem.ninja/org.nem.core/org/nem/core/connect/ErrorResponse.html#getError--) method) or a `Deserializer` (available through the [getDeserializer](http://www.nem.ninja/org.nem.core/org/nem/core/connect/ErrorResponseDeserializerUnion.html#getDeserializer--) method)

The second parameter must be an object implementing the `ErrorResponseStrategy` interface, which is a functional interface. Hence the second parameter can be a method reference or a lambda expression. We will pass a lambda expression, and it will be called every time the `ErrorResponseDeserializerUnion` instance contains an error (which can be checked with the method [hasError()](http://www.nem.ninja/org.nem.core/org/nem/core/connect/ErrorResponseDeserializerUnion.html#hasError--).

The syntax for lambda differs between Java 8 and Groovy. Whereas in Java 8 you would write:
```
e -> {throw new RuntimeException() ;}
```
in Groovy you write:
```
{ e -> throw new RuntimeException() ;}
```

FIXME:more details about the requirement to set the account lookup
There's one last subtlety requiring some explanation about `DefaultAsyncNemConnector<NisApiId>`.
`DefaultAsyncNemConnector<NisApiId>` requires a method to lookup accounts.
In our case, looking up an account can simple be the instanciation of a new account, so we will
pass a reference to the Account constructor as account lookup method.
Here again we encounter a difference between Groovy and Java 8. In Java 8, a reference ot the Account constructor is obtained with `Account::new`
whereas in Groovy we write `Account.metaClass.&invokeConstructor`.

To issue the request, we can use the method [getAsync](http://www.nem.ninja/org.nem.core/org/nem/core/connect/client/DefaultAsyncNemConnector.html#getAsync-org.nem.core.node.NodeEndpoint-TApiId-java.lang.String-) from the `DefaultAsyncNemConnector<NisApiId>` instance.
It takes as argument the `NodeEndpoint` instance identifying the node we want to send the request to, the ApiId we want to use, in our case this is `NisApiId.NIS_REST_CHAIN_HEIGHT`, and a query string which in our case can be se to null. FIXME:more details about the query.

The `getAsync` call returns a `CompletableFuture` holding a `Deserializer`. As we sent a request to get the block height,
we can pass this deserializer to the contructor of the class `BlockHeight` to initialise a new instance with the response
to our request.


### Code

After all these explanations, here is the code.
The only addition compared to what is explained above is the import statements.
All the rest is code transcripted from the explanations above.


```
import org.nem.core.model.primitive.BlockHeight;
import org.nem.core.model.Account;
import org.nem.core.connect.client.DefaultAsyncNemConnector;
import org.nem.core.connect.HttpMethodClient;
import org.nem.core.connect.ErrorResponseDeserializerUnion;
import org.nem.core.node.NodeEndpoint;
import org.nem.core.connect.client.NisApiId;
import org.nem.core.serialization.Deserializer;
import org.nem.core.model.BlockFactory;


node = NodeEndpoint.fromHost("87.98.159.171");
http = new HttpMethodClient<ErrorResponseDeserializerUnion>();
conn = new DefaultAsyncNemConnector<NisApiId>(http,{ e -> throw new RuntimeException() ;});
conn.setAccountLookup(Account.metaClass.&invokeConstructor  )
f=conn.getAsync(node,NisApiId.NIS_REST_CHAIN_HEIGHT,null)
bh = new BlockHeight(f.get())
```


### Links to javadoc

* [DefaultAsyncNemConnector()](http://www.nem.ninja/org.nem.core/org/nem/core/connect/client/DefaultAsyncNemConnector.html#DefaultAsyncNemConnector-org.nem.core.connect.HttpMethodClient-org.nem.core.connect.client.ErrorResponseStrategy-)
* [HttpMethodClient](http://www.nem.ninja/org.nem.core/org/nem/core/connect/client/ErrorResponseStrategy.html)
* [ErrorResponseDeserializerUnion](http://www.nem.ninja/org.nem.core/org/nem/core/connect/client/ErrorResponseStrategy.html)
* [ErrorResponseStrategy](http://www.nem.ninja/org.nem.core/org/nem/core/connect/client/ErrorResponseStrategy.html)
* [ErrorResponse](http://www.nem.ninja/org.nem.core/org/nem/core/connect/ErrorResponse.html)
* [Deserializer](http://www.nem.ninja/org.nem.core/org/nem/core/serialization/Deserializer.html)
* [blockHeight](http://www.nem.ninja/org.nem.core/org/nem/core/model/primitive/BlockHeight.html)
