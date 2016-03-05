import org.nem.core.connect.client.DefaultAsyncNemConnector;
import org.nem.core.connect.HttpMethodClient;
import org.nem.core.connect.ErrorResponseDeserializerUnion;
import org.nem.core.node.NodeEndpoint;
import org.nem.core.connect.client.NisApiId;
import org.nem.core.model.ncc.NemAnnounceResult
import org.nem.core.connect.HttpJsonPostRequest
import org.nem.core.node.NisPeerId;
import org.nem.core.model.ncc.RequestAnnounce
import org.nem.core.model.Account;

import java.lang.*;


// setup NIS communications
node = NodeEndpoint.fromHost("bob.nem.ninja");
http = new HttpMethodClient<ErrorResponseDeserializerUnion>();
conn = new DefaultAsyncNemConnector<NisPeerId>(http,{ e -> throw new RuntimeException(e.toString()) ;});
conn.setAccountLookup(Account.metaClass.&invokeConstructor  )

// serialize transaction
final byte[] transferBytes = new File("tx.data").bytes

final byte[] signature = new File("tx.sign").bytes

// sign transaction and send to nis
final RequestAnnounce announce = new RequestAnnounce(
					transferBytes,
					signature);
f = conn.postAsync( node,
		    NisApiId.NIS_REST_TRANSACTION_ANNOUNCE,
		    new HttpJsonPostRequest(announce))

// get the future's value, and construct the announce result
final NemAnnounceResult result = new NemAnnounceResult(f.get());
if (result.isError()) {
	println "ERROR"
	println result.getCode()
	println result.getMessage()

} else {
	println result.getTransactionHash()
}
println "done"
