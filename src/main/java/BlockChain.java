import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class BlockChain implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int Id;
    private final long Timestamp;
    private String HashOfThePreviousBlock;
    private final String HashOfTheBlock;
    private long magicNumber;
    private int countZero;
    List<BlockChain> blockChains;

    public int getCountZero() {
        return countZero;
    }

    public int getId() {
        return Id;
    }

    public long getTimestamp() {
        return Timestamp;
    }

    public void printData() {
        long threadId = Thread.currentThread().getId();
        System.out.println();
        System.out.println("Block:");
        System.out.println("Created by miner #" + threadId);
        System.out.println("Id: " + Id);
        System.out.println("Timestamp: " + Timestamp);
        System.out.println("Magic number: " + magicNumber);
        System.out.println("Hash of the previous block:");
        System.out.println(HashOfThePreviousBlock);
        System.out.println("Hash of the block:");
        System.out.println(HashOfTheBlock);
        long elapsedNanos = new Date().getTime() - Timestamp;

        System.out.printf("Block was generating for %d seconds\n", (int) elapsedNanos / 1000);
        if (elapsedNanos / 1000 < 15) {
            countZero = countZero + 1;
            System.out.println("N was increased to " + countZero);
        } else if (elapsedNanos / 1000 >= 15 && elapsedNanos / 1000 < 60) {
            System.out.println("N stays the same");
        } else {
            countZero = countZero - 1;
            System.out.println("N was decreased by " + countZero);
        }
        System.out.println();

    }


    public String getHashOfTheBlock() {
        return HashOfTheBlock;
    }

    public BlockChain(int id, List<BlockChain> blockChainList) {
        Id = id;
        blockChains = blockChainList;
        Timestamp = new Date().getTime();
        if (Id == 1) {
            HashOfThePreviousBlock = "0";
            countZero = 0;
        } else {
            if (blockChains != null) {
                HashOfThePreviousBlock = getHashPrevious();
                countZero = blockChainList.get(blockChainList.size() - 1).getCountZero();
            }
        }
        HashOfTheBlock = generateHash();


    }

    public String generateHash() {
        String hashCode = "";

        Random random = new Random();
        magicNumber = random.nextLong();
        if (Id == 1) {
            return StringUtil.applySha256(Id + String.valueOf(Timestamp) + HashOfThePreviousBlock + magicNumber);
        } else {
            while (!hashCode.startsWith(stringMultiply(countZero))) {
                hashCode = StringUtil.applySha256(Id + String.valueOf(Timestamp) + HashOfThePreviousBlock + magicNumber);
                magicNumber = random.nextLong();
            }
            return hashCode;
        }
    }

    public static String stringMultiply(int n) {
        return "0".repeat(Math.max(0, n));
    }

    public String getHashPrevious() {
        String fileName = "blockchains.data";
        BlockChain[] blockChains1 = blockChains.toArray(new BlockChain[0]);
        try {
            SerializationUtils.serialize(blockChains1, fileName);
            BlockChain[] blockChainList = (BlockChain[]) SerializationUtils.deserialize(fileName);
            HashOfThePreviousBlock = blockChainList[Id - 2].getHashOfTheBlock();
            return HashOfThePreviousBlock;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

}
