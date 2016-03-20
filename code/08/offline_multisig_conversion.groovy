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

import org.nem.core.model.MultisigCosignatoryModification
import org.nem.core.model.MultisigModificationType
import org.nem.core.model.MultisigMinCosignatoriesModification
import org.nem.core.model.MultisigAggregateModificationTransaction
import org.nem.core.crypto.PublicKey



import java.lang.*;

// TimeInstant for transaction deadline, set 24 hours in the future
timeProvider=new SystemTimeProvider()
ts_ti=timeProvider.getCurrentTime().addHours(24);

// sender, from private key string (account1)
private_key= PrivateKey.fromHexString("5e7cfb371bd02616747900a1ec223675036ca5de56729b158012b086def6ae01")
sender_key_pair= new KeyPair(private_key)
sender_account=new Account(sender_key_pair)


// recipient, from destination address (account2)
cosignatory=new Account(Address.fromPublicKey(PublicKey.fromHexString("43e693f8236370b03566b24c2a897a291409b8d4c7e195698d1ed5b9f7cdb9d6")))

modifications = new  ArrayList<MultisigCosignatoryModification>();
modification = new MultisigCosignatoryModification(MultisigModificationType.AddCosignatory, cosignatory);
modifications.add( modification );


// create transaction instance and set its deadline
transaction=new MultisigAggregateModificationTransaction(timeProvider.getCurrentTime(),
                           sender_account,
                           modifications)
transaction.setDeadline(ts_ti);

// serialize transaction
final byte[] transferBytes = BinarySerializer.serializeToBytes(transaction.asNonVerifiable());
final Signer signer = transaction.getSigner().createSigner();

new File('tx.data').bytes = transferBytes
new File('tx.sign').bytes = signer.sign(transferBytes).getBytes()
