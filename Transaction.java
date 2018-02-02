import java.nio.ByteBuffer;
//import java.security.Signature;
import java.util.Arrays;

public class Transaction {
	//LENGTH들은 버퍼의 공간을 미리 계산하기 위해서 필요
	private static final int    FROM_LENGTH         = 4;
    private static final int    TO_LENGTH           = 4;
    private static final int    VALUE_LENGTH        = 4;
    private static final int    HEADER_LENGTH       = 4;
    private static final int    LENGTH_LENGTH       = 4;
    private static final int    TIMESTAMP_LENGTH    = 8;

    public String               from;		//누가
    public String               to;			//누구에게
    public int                  value;		//얼마를
    public String               header;
    public long                 timestamp;
//    public ByteBuffer           signature;	//인코딩에 필요
    //header와 signature를 KeyUtils바이트 암호화해서 생긴 것을
    //transaction에서 sig로 넣음. header는 여전히 존재
    //즉, signature는 KeyUtils로 암호화하기 위한 계산에 이용되는 것 뿐
    //ex. header가 "A gets 50 coins."이면 이것을 signature와 함께 암호화해서
    //그결과가 transaction의 signature로 다시 들어감. header는 여전히 "A gets 50 coins."
    //만약에 우리가 이런 서명을 거래단위로는 적용하지 않을 것이라면 이 변수와 이 과정은 모두 필요 없음
    
    public Transaction[]        inputs;		
    public Transaction[]        outputs;	
    
    public Transaction() { }

    public Transaction(String from, String to, String header, int value, Transaction[] inputs, Transaction[] outputs) {
        this.from = from;
        this.to = to;
        this.value = value;
        this.header = header;
        this.timestamp = 0;
  //      final byte[] sig = new byte[signature.length];
  //      System.arraycopy(signature, 0, sig, 0, signature.length);
  //      this.signature = ByteBuffer.wrap(sig);

        this.inputs = new Transaction[inputs.length];
        for (int i=0; i<inputs.length; i++)
            this.inputs[i] = inputs[i];

        this.outputs = new Transaction[outputs.length];
        for (int i=0; i<outputs.length; i++)
            this.outputs[i] = outputs[i];
        
    }
/*    
    public static final Transaction newSignedTransaction(Signature signature, 
            String from, String to, 
            String header, int value, 
            Transaction[] inputs, Transaction[] outputs){
    			final byte[] sig = KeyUtils.signMsg(signature, header.getBytes());
    			Transaction transaction = new Transaction(from, to, header, value, inputs, outputs);
    			return transaction;
    		}
*/
    public void updateTimestamp() {
        this.timestamp = System.currentTimeMillis();
    }
    
    public int getBufferLength() {
        int iLength = 0;
        for (Transaction t : inputs)
            iLength += LENGTH_LENGTH + t.getBufferLength();

        int oLength = 0;
        for (Transaction t : outputs)
            oLength += LENGTH_LENGTH + t.getBufferLength();

        int length =    LENGTH_LENGTH + iLength +
                        LENGTH_LENGTH + oLength +
                        TIMESTAMP_LENGTH +
                        VALUE_LENGTH +
                        HEADER_LENGTH + header.getBytes().length + 
                        FROM_LENGTH + from.getBytes().length + 
                        TO_LENGTH + to.getBytes().length;
        return length;
    }
    
    public void fromBuffer(ByteBuffer buffer) {
    	/*
        { // signature
            int sLength = buffer.getInt();
            byte[] bSignature = new byte[sLength];
            buffer.get(bSignature);
            this.signature = ByteBuffer.wrap(bSignature);
        }
		*/
        { // inputs
            int iLength = buffer.getInt();
            this.inputs = new Transaction[iLength];
            for (int i=0; i<iLength; i++) {
                int tLength = buffer.getInt();
                Transaction t = new Transaction();
                final byte[] bytes = new byte[tLength];
                buffer.get(bytes);
                ByteBuffer bb = ByteBuffer.wrap(bytes);
                t.fromBuffer(bb);
                this.inputs[i] = t;
            }
        }

        { // ouputs
            int oLength = buffer.getInt();
            this.outputs = new Transaction[oLength];
            for (int i=0; i<oLength; i++) {
                int tLength = buffer.getInt();
                Transaction t = new Transaction();
                final byte[] bytes = new byte[tLength];
                buffer.get(bytes);
                ByteBuffer bb = ByteBuffer.wrap(bytes);
                t.fromBuffer(bb);
                this.outputs[i] = t;
            }
        }
        
        timestamp = buffer.getLong();
        value = buffer.getInt();

        final int mLength = buffer.getInt();
        final byte[] mBytes = new byte[mLength];
        buffer.get(mBytes, 0, mLength);
        header = new String(mBytes);

        final int fLength = buffer.getInt();
        final byte[] fBytes = new byte[fLength];
        buffer.get(fBytes, 0, fLength);
        from = new String(fBytes);

        final int tLength = buffer.getInt();
        final byte[] tBytes = new byte[tLength];
        buffer.get(tBytes, 0, tLength);
        to = new String(tBytes);
    }
    
    public void toBuffer(ByteBuffer buffer) {
    	/*
        { // signature
            buffer.putInt(signature.limit());
            buffer.put(signature);
            signature.flip();
        }
        */

        { // inputs
            buffer.putInt(inputs.length);
            for (Transaction t : inputs) {
                buffer.putInt(t.getBufferLength());
                t.toBuffer(buffer);
                // do not flip buffer here
            }
        }

        { // outputs
            buffer.putInt(outputs.length);
            for (Transaction t : outputs) {
                buffer.putInt(t.getBufferLength());
                t.toBuffer(buffer);
                // do not flip buffer here
            }
        }

        buffer.putLong(timestamp);
        buffer.putInt(value);

        final int mLength = header.length();
        buffer.putInt(mLength);
        final byte[] mBytes = header.getBytes();
        buffer.put(mBytes);

        final byte[] fBytes = from.getBytes();
        buffer.putInt(fBytes.length);
        buffer.put(fBytes);

        final byte[] oBytes = to.getBytes();
        buffer.putInt(oBytes.length);
        buffer.put(oBytes);
    }
    
    public String toString() {
        StringBuilder builder = new StringBuilder();
 //       builder.append("signature=[").append(HashUtils.bytesToHex(this.signature.array())).append("]\n");
        builder.append("inputs=").append(inputs.length).append("\n");
        builder.append("outputs=").append(outputs.length).append("\n");
        builder.append("time='").append(timestamp).append("'\n");
        builder.append("value='").append(value).append("'\n");
        builder.append("from='").append(from).append("'\n");
        builder.append("to='").append(to).append("'\n");
        builder.append("header=[").append(header).append("]");
        return builder.toString();
    }

}
