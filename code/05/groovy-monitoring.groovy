import org.nem.core.model.Account;
import org.nem.core.model.Address;
import org.nem.core.connect.client.DefaultAsyncNemConnector;
import org.nem.core.connect.HttpMethodClient;
import org.nem.core.connect.ErrorResponseDeserializerUnion;
import org.nem.core.node.NodeEndpoint;
import org.nem.core.connect.client.NisApiId;
import org.nem.core.serialization.Deserializer;
import org.nem.core.model.BlockFactory;
import org.nem.core.model.TransactionTypes
import org.nem.core.model.HashUtils


node = NodeEndpoint.fromHost("bob.nem.ninja");
http = new HttpMethodClient<ErrorResponseDeserializerUnion>();

conn = new DefaultAsyncNemConnector<NisApiId>(http,{ e -> throw new RuntimeException() ;});
conn.setAccountLookup(Account.metaClass.&invokeConstructor  )

// need to remove "-" from address, or comparison fails later in the script
monitored_address=Address.fromEncoded("TCJMM2-MN62XC-Y4TREH-ACOJ3M-2FOEC2-MUKCSX-XVR6".replaceAll("-",""))
previous=-1
while (true) {
	f = conn.getAsync(node, NisApiId.NIS_REST_CHAIN_LAST_BLOCK, null)
	block=BlockFactory.VERIFIABLE.deserialize(f.get())

	if (block.getHeight()==previous){
		sleep 3000
		continue;
	} else {
		println "new block"
		println block.getHeight()
	}

	previous=block.getHeight()
	block.getTransactions().each { t ->  
					  // way to print infor of the object
					  //println new JsonBuilder( t ).toPrettyString()
					  //println "---"
					  //println new JsonBuilder( t.getAccounts()  ).toPrettyString()

					  if (t.getType()==TransactionTypes.TRANSFER) {
						  if (t.getAccounts().collect({it.getAddress()}).any({it.equals(monitored_address)})) {
							  println "TRANSFER of ${t.getXemTransferAmount().getNumNem()} XEMs found!"
							  println "Tx hash: ${HashUtils.calculateHash(t)}"

							  if (t.getRecipient().getAddress()==monitored_address) {
								  println "account is recipient, receiving money?"
							  } else {
								  println "account is NOT recipient"
							  }
							  if (t.getDebtor().getAddress()==monitored_address) {
								  println "account is paying fee, sending money...."
							  } else {
								  println "account is not paying fee, receiving money!"
							  }
							  if (t.getSigner().getAddress()==monitored_address) {
								  println "account is signer"
							  } else {
								  println "account is NOT signer"
							  }
							  if (t.getMessage().canDecode()){
								  println "Message is ${new String(t.getMessage().getDecodedPayload(),"UTF-8")}"
							  } else {
								  println "Cannot decode message...."
							  }
						  } } } 
	sleep 3000

}
println "done"
return 0

