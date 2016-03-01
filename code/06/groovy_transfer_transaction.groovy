import org.nem.core.model.Account;
import org.nem.core.connect.client.DefaultAsyncNemConnector;
import org.nem.core.connect.HttpMethodClient;
import org.nem.core.connect.ErrorResponseDeserializerUnion;
import org.nem.core.node.NodeEndpoint;
import org.nem.core.connect.client.NisApiId;
import org.nem.core.serialization.Deserializer;
import org.nem.core.model.BlockFactory;
import org.nem.core.model.TransferTransaction;
import org.nem.core.time.UnixTime;
import org.nem.core.crypto.KeyPair
import org.nem.core.crypto.PrivateKey
import org.nem.core.model.primitive.Amount
import org.nem.core.model.TransferTransactionAttachment
import org.nem.core.serialization.BinarySerializer
import org.nem.core.model.ncc.RequestPrepare
import org.nem.core.crypto.Signer
import org.nem.core.model.ncc.NemAnnounceResult
import org.nem.core.connect.HttpJsonPostRequest
import org.nem.core.node.NisPeerId;
import org.nem.core.model.ncc.RequestAnnounce
import org.nem.core.model.Address;
import org.nem.core.model.HashUtils
import org.nem.core.time.SystemTimeProvider
//import org.nem.core.time.NetworkTimeStamp
import java.lang.*;

// TimeInstant for transaction constructor
timeProvider=new SystemTimeProvider()
ts_ti=timeProvider.getCurrentTime().addHours(24);

// recipient, account2
recipient_account=new Account(Address.fromEncoded("TC7JJL-Y3MCEU-4TGKYK-IE67D5-YDAAT6-Z2OKOX-4ZNX".replaceAll("-","")))

// sender, account1
private_key= PrivateKey.fromHexString("myprivatekey")
sender_key_pair= new KeyPair(private_key)
sender_account=new Account(sender_key_pair)


// amount in microXEMs
amount = new Amount(1234*1000000);

// empty attachment
attach = new TransferTransactionAttachment()


transaction=new TransferTransaction(timeProvider.getCurrentTime(),
                           sender_account,
                           recipient_account,
                           amount,
                           attach)
transaction.setDeadline(ts_ti);
node = NodeEndpoint.fromHost("bob.nem.ninja");
http = new HttpMethodClient<ErrorResponseDeserializerUnion>();
conn = new DefaultAsyncNemConnector<NisPeerId>(http,{ e -> throw new RuntimeException(e.toString()) ;});
conn.setAccountLookup(Account.metaClass.&invokeConstructor  )
final byte[] transferBytes = BinarySerializer.serializeToBytes(transaction.asNonVerifiable());
final RequestPrepare preparedTransaction = new RequestPrepare(transferBytes);

// sign transaction and send to nis
final Signer signer = transaction.getSigner().createSigner();
final RequestAnnounce announce = new RequestAnnounce(
                                          preparedTransaction.getData(),
                                          signer.sign(preparedTransaction.getData()).getBytes());
f = conn.postAsync( node,
                    NisApiId.NIS_REST_TRANSACTION_ANNOUNCE,
                    new HttpJsonPostRequest(announce)).get()
final NemAnnounceResult result = new NemAnnounceResult(f);
if (result.isError()) {
	println "ERROR"
	println result.getCode()
	println result.getMessage()

} else {
	println result.getTransactionHash()
}
