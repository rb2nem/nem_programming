# Our first NIS connection
Until now, our code was running offline, with no interaction at all with the NEM blockchain.
For us to integrate with the NEM network and blockchain, we need interact with a NEM node.
Good news, that's what we'll do in this chapter!

Our first program in this chapter will connect to a NIS instance (a node of the NEM network), and request its blockheight.
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



```
import org.nem.core.crypto.KeyPair;
import org.nem.core.model.Block;
import org.nem.core.model.BlockTypes;
import org.nem.core.model.VerifiableEntity;
import org.nem.core.model.VerifiableEntity.DeserializationOptions;
import org.nem.core.model.primitive.BlockHeight;
import org.nem.core.model.Account;
import org.nem.core.connect.client.DefaultAsyncNemConnector;
import org.nem.core.connect.HttpMethodClient;
import org.nem.core.connect.ErrorResponseDeserializerUnion;
import org.nem.core.node.NodeEndpoint;
import org.nem.core.connect.client.NisApiId;
import org.nem.core.serialization.Deserializer;
import org.nem.core.model.BlockFactory;

import java.util.concurrent.CompletableFuture;
import org.nem.core.connect.ErrorResponse;
import org.nem.core.connect.client.ErrorResponseStrategy;




node = NodeEndpoint.fromHost("87.98.159.171");
http = new HttpMethodClient<ErrorResponseDeserializerUnion>();

conn = new DefaultAsyncNemConnector<NisApiId>(http,{ e -> throw new RuntimeException() ;});
conn.setAccountLookup(Account.metaClass.&invokeConstructor  )
f=conn.getAsync(node,NisApiId.NIS_REST_CHAIN_HEIGHT,null)
bh = new BlockHeight(f.join())
res = conn.getAsync(node, NisApiId.NIS_REST_CHAIN_LAST_BLOCK, null).thenApply(BlockFactory.VERIFIABLE.&deserialize).join();
```


