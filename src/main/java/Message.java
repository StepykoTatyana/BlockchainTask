import java.io.*;
import java.security.*;


public class Message implements Serializable {
    private static  KeyPairGenerator keyGen;

    static {
        try {
            keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(1024);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private final int id;
    private final String data;
    private final byte[] signature;
    private final PublicKey publicKey;
    private final PrivateKey privateKey;


    public PublicKey getPublicKey() {
        return publicKey;
    }

    public byte[] getSignature() {
        return signature;
    }

    public String getData() {
        return data;
    }

    public int getId() {
        return id;
    }


    //The constructor of Message class builds the list that will be written to the file.
    //The list consists of the message and the signature.
    public Message(String data, int id) {
        KeyPair pair = keyGen.generateKeyPair();
        this.publicKey = pair.getPublic();
        this.privateKey = pair.getPrivate();
        this.id = id;
        this.data = data;
        signature = sign(data, id);

    }

    //The method that signs the data using the private key that is stored in keyFile path
    public byte[] sign(String data, int id) {
        try {
            Signature dsa = Signature.getInstance("SHA1withRSA");
            dsa.initSign(privateKey);
            dsa.update((byte) id);
            dsa.update(data.getBytes());
            return dsa.sign();
        } catch (Exception ignored) {
            return null;
        }
    }
}