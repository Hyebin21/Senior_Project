import java.nio.ByteBuffer;
import java.util.Arrays;

/*
 * ���� ����� �ʿ��� ���� 6����
 * ����Ʈ���� ���� / �ٷ� �� ������ �ؽ� / ��ŬƮ���ؽ�(���� ������ �ؽ�) / ���� �����ð�(Ÿ�ӽ�����) / ä�� ���̵� / nonce
 * ���⼭ ����Ʈ���� ������ �츮���� �ʿ� ���� -> ����
 * �ٷ� �� ������ �ؽ� = prev
 * ��ŬƮ���ؽ�(���� ������ �ؽ�) = hash
 * ���� �����ð� = timestamp (�̰� Transaction.java�� ����)
 * ä�� ���̵� = numberOfZeros
 * nonce = nonce
 */

public class Block {
	//LENGTH���� ������ ������ �̸� ����ϱ� ���ؼ� �ʿ�
	private static final int    FROM_LENGTH             = 4;
    private static final int    TIMESTAMP_LENGTH    = 8;
//    private static final int    BOOLEAN_LENGTH          = 2;
    private static final int    NUM_OF_ZEROS_LENGTH     = 4;
    private static final int    NONCE_LENGTH            = 4;
    private static final int    BLOCK_LENGTH            = 4;
    private static final int    LENGTH_LENGTH           = 4;

    
    public String               from;			//ä����
//    public boolean              confirmed= false;
    //confirmed �ʿ��Ѱ��� �� �𸣰���. ���������� true�� �ٲ��ִ� ���ɹ��� �־ ��ȿ����
    //��Ÿ���� �� ������... ����� ����������~!
    public long                 timestamp;		//���� ���� �ð�
    public int                  numberOfZeros;	//0�� ���� = ���̵� ����
    public int                  nonce;			//nonce
    public int                  blockLength;	//������ ����
    public Transaction[]        transactions;	//�ŷ� ���
    public byte[]               prev;			//���� ������ �ؽð�
    public byte[]               hash;			//���� ������ �ؽð�
    
    public Block() { }

    public Block(String from, byte[] prevHash, byte[] hash, Transaction[] transactions, int blockLength) {
        this.from = from;
        this.prev = prevHash;
        this.hash = hash;
        this.timestamp = 0;
        this.transactions = transactions;
        this.blockLength = blockLength;
    }
    
    public void updateTimestamp() {
        this.timestamp = System.currentTimeMillis();
    }

    public int getBufferLength() {
        int transactionsLength = 0;
        for (Transaction t : transactions)
            transactionsLength += LENGTH_LENGTH + t.getBufferLength();
        return  FROM_LENGTH + from.length() +
  //              BOOLEAN_LENGTH + 
        		TIMESTAMP_LENGTH +
                NUM_OF_ZEROS_LENGTH +
                NONCE_LENGTH +
                BLOCK_LENGTH +
                LENGTH_LENGTH + prev.length + 
                LENGTH_LENGTH + hash.length + 
                LENGTH_LENGTH + transactionsLength;
    }
    
    public void fromBuffer(ByteBuffer buffer) {
        final int fLength = buffer.getInt();
        final byte[] fBytes = new byte[fLength];
        buffer.get(fBytes, 0, fLength);
        from = new String(fBytes);

//        confirmed = parseBoolean(buffer.getChar());
        numberOfZeros = buffer.getInt();
        nonce = buffer.getInt();
        blockLength = buffer.getInt();
        
        timestamp = buffer.getLong();

        { // previous hash
            final int length = buffer.getInt();
            prev = new byte[length];
            buffer.get(prev);
        }

        { // next hash
            final int length = buffer.getInt();
            hash = new byte[length];
            buffer.get(hash);
        }

        int tLength = buffer.getInt();
        transactions =  new Transaction[tLength];
        for (int i=0; i < tLength; i++) {
            int length = buffer.getInt();
            final byte[] bytes = new byte[length];
            buffer.get(bytes);
            final ByteBuffer bb = ByteBuffer.wrap(bytes);
            final Transaction t = new Transaction();
            t.fromBuffer(bb);
            transactions[i] = t;
        }
    }

    public void toBuffer(ByteBuffer buffer) {
        final byte[] fBytes = from.getBytes();
        buffer.putInt(fBytes.length);
        buffer.put(fBytes);
        
//        buffer.putChar(getBoolean(confirmed));
        buffer.putLong(timestamp);
        buffer.putInt(numberOfZeros);
        buffer.putInt(nonce);
        buffer.putInt(blockLength);

        buffer.putInt(prev.length);
        buffer.put(prev);

        buffer.putInt(hash.length);
        buffer.put(hash);

        buffer.putInt(transactions.length);
        for (Transaction t : transactions) {
            buffer.putInt(t.getBufferLength());
            t.toBuffer(buffer);
        }
    }
    
    /*
     * �� ���� �κ��� �� ������ ���߿� ä�������� �ް� �����ϴµ� ���°� ����. �ϴ� �ּ�ó��. �ּ��ȿ� ���������� ������ѳ�����
     */
    
    /*
    @Override
    public int hashCode() {
        int hashCode = 0;
        hashCode += from.length();
 //       if (confirmed)
 //           hashCode += 1;
  		hashCode += timestamp;
        hashCode += nonce;
        hashCode += blockLength;
        hashCode += numberOfZeros;
        hashCode += transactions.length;
        for (Transaction t : transactions)
            hashCode += t.hashCode();
        for (byte b : prev)
            hashCode += b;
        for (byte b : hash)
            hashCode += b;
        return 31 * hashCode;
    }

    
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Block))
            return false;
        Block c = (Block) o;
        if (!(c.from.equals(from)))
            return false;
//        if (confirmed != c.confirmed)
//            return false;
 		if (timestamp != c.timestamp)
 			return false;
        if (nonce != c.nonce)
            return false;
        if (blockLength != c.blockLength)
            return false;
        if (numberOfZeros != c.numberOfZeros)
            return false;
        if (c.transactions.length != this.transactions.length)
            return false;
        { // compare transactions
            for (int i=0; i<c.transactions.length; i++) {
                if (!(c.transactions[i].equals(this.transactions[i])))
                    return false;
            }
        }
        if (!(Arrays.equals(c.prev, prev)))
            return false;
        if (!(Arrays.equals(c.hash, hash)))
            return false;
        return true;
    }
    */
    

    public String toString() {
        StringBuilder builder = new StringBuilder();
 //       builder.append("isValid=").append(confirmed).append("\n");
        builder.append("time='").append(timestamp).append("\n");
        builder.append("numberOfZerosToCompute=").append(numberOfZeros).append("\n");
        builder.append("nonce=").append(nonce).append("\n");
        builder.append("blockLength=").append(blockLength).append("\n");
        builder.append("prev=[").append(HashUtils.bytesToHex(prev)).append("]\n");
        builder.append("hash=[").append(HashUtils.bytesToHex(hash)).append("]\n");
        builder.append("block={").append("\n");
        for (Transaction t : transactions) {
            builder.append("transaction={").append("\n");
            builder.append(t.toString()).append("\n");
            builder.append("}").append("\n");
        }
        builder.append("}");
        return builder.toString();
    }

}