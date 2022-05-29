import java.io.Serializable;
import java.util.Date;
import java.util.Random;

public class BlockChain implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int Id;
    private final long Timestamp;
    private final String HashOfThePreviousBlock;
    private final String HashOfTheBlock;
    private long magicNumber;

    public void printData() {
        System.out.println("Block:");
        System.out.println("Id: " + Id);
        System.out.println("Timestamp: " + Timestamp);
        System.out.println("Magic number: " + magicNumber);
        System.out.println("Hash of the previous block:");
        System.out.println(HashOfThePreviousBlock);
        System.out.println("Hash of the block:");
        System.out.println(HashOfTheBlock);
        long elapsedNanos = new Date().getTime() - Timestamp;
        System.out.printf("Block was generating for %d seconds\n", (int) elapsedNanos / 1000);
        System.out.println();
    }


    public String getHashOfTheBlock() {
        return HashOfTheBlock;
    }

    public BlockChain(int id, long timestamp, String hashOfThePreviousBlock, int countOfZero) {
        Id = id;
        Timestamp = timestamp;
        HashOfThePreviousBlock = hashOfThePreviousBlock;
        HashOfTheBlock = generateHash(countOfZero);
    }

    public String generateHash(int countOfZero) {
        String hashCode = "";

        Random random = new Random();
        magicNumber = random.nextLong();
        if (countOfZero == 0) {
            return StringUtil.applySha256(Id + String.valueOf(Timestamp) + HashOfThePreviousBlock + magicNumber);
        } else {
            while (!hashCode.startsWith(stringMultiply(countOfZero))) {
                hashCode = StringUtil.applySha256(Id + String.valueOf(Timestamp) + HashOfThePreviousBlock + magicNumber);
                magicNumber = random.nextLong();
            }
            return hashCode;
        }
    }

    public static String stringMultiply(int n) {
        return "0".repeat(Math.max(0, n));
    }
}
