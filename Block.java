import java.nio.ByteBuffer;
import java.util.Arrays;

/*
 * 블록 헤더에 필요한 것은 6가지
 * 소프트웨어 버전 / 바로 앞 블록의 해쉬 / 머클트리해쉬(현재 블록의 해쉬) / 블록 생성시간(타임스탬프) / 채굴 난이도 / nonce
 * 여기서 소프트웨어 버전은 우리에게 필요 없음 -> 삭제
 * 바로 앞 블록의 해쉬 = prev
 * 머클트리해쉬(현재 블록의 해쉬) = hash
 * 블록 생성시간 = timestamp (이건 Transaction.java에 있음)
 * 채굴 난이도 = numberOfZeros
 * nonce = nonce
 */

public class Block {
	//LENGTH들은 버퍼의 공간을 미리 계산하기 위해서 필요
	private static final int    FROM_LENGTH             = 4;
    private static final int    TIMESTAMP_LENGTH    = 8;
//    private static final int    BOOLEAN_LENGTH          = 2;
    private static final int    NUM_OF_ZEROS_LENGTH     = 4;
    private static final int    NONCE_LENGTH            = 4;
    private static final int    BLOCK_LENGTH            = 4;
    private static final int    LENGTH_LENGTH           = 4;

    
    public String               from;			//채굴자
//    public boolean              confirmed= false;
    //confirmed 필요한건지 잘 모르겠음. 직접적으로 true로 바꿔주는 명령문을 넣어서 유효성을
    //나타내는 것 같은데... 맘대로 지워버리기~!
    public long                 timestamp;		//블록 생성 시각
    public int                  numberOfZeros;	//0의 갯수 = 난이도 조절
    public int                  nonce;			//nonce
    public int                  blockLength;	//블록의 길이
    public Transaction[]        transactions;	//거래 목록
    public byte[]               prev;			//이전 블록의 해시값
    public byte[]               hash;			//현재 블록의 해시값
    
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
     * 이 밑의 부분은 내 생각에 나중에 채굴정보를 받고 검증하는데 쓰는거 같음. 일단 주석처리. 주석안에 수정내용은 적용시켜놓았음
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
