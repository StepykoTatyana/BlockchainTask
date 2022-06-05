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
    List<String> messages;
    List<String> messagesPrevious;
    static int i = 0;
    static List<String> list;
    static List<String> listConst = List.of(
            "Tom: Hey, I'm first!",
            "Tom: Hey, I'm second also!",
            "Sarah: It's not fair!",
            "Sarah: You always will be first because it is your blockchain!",
            "Sarah: Anyway, thank you for this amazing chat.",
            "Tom: You're welcome :)",
            "Nick: Hey Tom, nice chat",
            "Tanya: Hey, I'm Tanya!",
            "Tanya: I'm Norway!",
            "Tanya: I'm developer, and you?",
            "Tom: Hey, I'm a student!",
            "Sarah: Hey, I'm a professor!",
            "Nick: Hey, I'm a artist!",
            "Tanya: Nice to meet you Nick",
            "Tanya: Nice to meet you Tom",
            "Tanya: Nice to meet you Sarah",
            "Tom: Nice to meet you");


    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

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
        if (Id > 1) {
            if (messagesPrevious.size() == 0) {
                System.out.println("Block data: no messages");
            } else {
                System.out.println("Block data:");
                messagesPrevious.forEach(System.out::println);
            }
        } else {
            System.out.println("Block data: no messages");
        }

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
            messagesPrevious = null;
        } else {
            if (blockChains != null) {
                getHashPrevious();
                countZero = blockChainList.get(blockChainList.size() - 1).getCountZero();
                messagesPrevious = getHashPreviousMessage();
            }
        }
        HashOfTheBlock = generateHash();
        messages = list;

    }


    public String generateHash() {
        String hashCode = "";
        Random random = new Random();
        int k = 0;
        magicNumber = random.nextLong();
        if (Id == 1) {
            list = new ArrayList<>();
            list.add(listConst.get(i));
            i++;
            return StringUtil.applySha256(Id + String.valueOf(Timestamp) + HashOfThePreviousBlock + magicNumber);
        } else {
            list = new ArrayList<>();
            while (!hashCode.startsWith(stringMultiply(countZero))) {
                if (k % 4000 == 0) {
                    list.add(listConst.get(i));
                    i++;
                    System.out.println(list);
                }
                hashCode = StringUtil.applySha256(Id + String.valueOf(Timestamp) + HashOfThePreviousBlock + magicNumber);
                magicNumber = random.nextLong();
                k++;

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


    private List<String> getHashPreviousMessage() {
        String fileName = "blockchains.data";
        BlockChain[] blockChains1 = blockChains.toArray(new BlockChain[0]);
        try {
            SerializationUtils.serialize(blockChains1, fileName);
            BlockChain[] blockChainList = (BlockChain[]) SerializationUtils.deserialize(fileName);
            messages = blockChainList[Id - 2].getMessages();
            return messages;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
