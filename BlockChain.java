import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;

public class BlockChain {
	public static final String              NO_ONE              = "no one";
    public static final Signature           NO_ONE_SIGNATURE;
    public static final byte[]              NO_ONE_PUB_KEY;
    static{
    	try {
        	final KeyPairGenerator gen = KeyPairGenerator.getInstance("DSA", "SUN");
            final SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            gen.initialize(512, random);

            final KeyPair pair = gen.generateKeyPair();
            final PrivateKey privateKey = pair.getPrivate();
            NO_ONE_SIGNATURE = Signature.getInstance("SHA1withDSA", "SUN");
            NO_ONE_SIGNATURE.initSign(privateKey);

            final PublicKey publicKey = pair.getPublic();
            NO_ONE_PUB_KEY = publicKey.getEncoded();
        }catch(Exception e) {
        	throw new RuntimeException(e);    	
        }
    }
    
    protected static final boolean DEBUG = Boolean.getBoolean("debug");
    private static final byte[] INITIAL_HASH = new byte[0];    
    public String from_name;
    private Transaction from_Trans;
    private Block from_Block;
    
}
