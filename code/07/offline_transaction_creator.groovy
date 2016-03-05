import org.nem.core.model.Account;
import org.nem.core.model.TransferTransaction;
import org.nem.core.time.UnixTime;
import org.nem.core.crypto.KeyPair
import org.nem.core.crypto.PrivateKey
import org.nem.core.model.primitive.Amount
import org.nem.core.model.TransferTransactionAttachment
import org.nem.core.serialization.BinarySerializer
import org.nem.core.crypto.Signer
import org.nem.core.model.Address;
import org.nem.core.time.SystemTimeProvider

import java.lang.*;

// TimeInstant for transaction deadline, set 24 hours in the future
timeProvider=new SystemTimeProvider()
ts_ti=timeProvider.getCurrentTime().addHours(24);

// recipient, from destination address (account2)
recipient_account=new Account(Address.fromEncoded("TC7JJL-Y3MCEU-4TGKYK-IE67D5-YDAAT6-Z2OKOX-4ZNX".replaceAll("-","")))

// sender, from private key string (account1)
private_key= PrivateKey.fromHexString("privatekeyhexstring")
sender_key_pair= new KeyPair(private_key)
sender_account=new Account(sender_key_pair)


// amount in microXEMs
amount = new Amount(1234*1000000);

// empty attachment
attach = new TransferTransactionAttachment()

// create transaction instance and set its deadline
transaction=new TransferTransaction(timeProvider.getCurrentTime(),
                           sender_account,
                           recipient_account,
                           amount,
                           attach)
transaction.setDeadline(ts_ti);

// serialize transaction
final byte[] transferBytes = BinarySerializer.serializeToBytes(transaction.asNonVerifiable());
final Signer signer = transaction.getSigner().createSigner();

new File('tx.data').bytes = transferBytes
new File('tx.sign').bytes = signer.sign(transferBytes).getBytes()
