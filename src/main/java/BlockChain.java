import java.io.IOException;
import java.io.Serializable;
import java.security.Signature;
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
    static int idMessage = 0;
    static int i = 0;
    List<Message> messagesPrevious;
    List<Message> listMessage;

    public int getId() {
        return Id;
    }

    public long getTimestamp() {
        return Timestamp;
    }

    public int getCountZero() {
        return countZero;
    }

    public List<Message> getListMessage() {
        return listMessage;
    }

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
            "Nick: Hey, I'm a artist111!",
            "Tanya: Nice to meet you Nick111",
            "Tanya: Nice to meet you Tom111",
            "Tanya: Nice to meet you Sarah111",
            "Tom: Nice to meet you");


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
                messagesPrevious.forEach(x -> System.out.println(x.getData()));
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

    public BlockChain(int id, List<BlockChain> blockChainList) throws Exception {
        this.Id = id;
        this.blockChains = blockChainList;
        this.Timestamp = new Date().getTime();

        if (Id == 1) {
            this.HashOfThePreviousBlock = "0";
            this.countZero = 0;
            this.messagesPrevious = null;
        } else {
            if (blockChains != null) {
                getHashPrevious();
                this.countZero = blockChainList.get(blockChainList.size() - 1).getCountZero();
                this.messagesPrevious = getHashPreviousMessage();
            }
        }
        HashOfTheBlock = generateHash();

    }


    public String generateHash() throws Exception {
        String hashCode = "";
        Random random = new Random();
        int k = 0;
        magicNumber = random.nextLong();
        if (Id == 1) {
            listMessage = new ArrayList<>();
            idMessage = random.nextInt();
            Message message = new Message(listConst.get(i), idMessage);
            if (verifySignature(message)){
                listMessage.add(message);
                i++;
            }
            return StringUtil.applySha256(Id + String.valueOf(Timestamp) + HashOfThePreviousBlock + magicNumber);
        } else {
            listMessage = new ArrayList<>();
            while (!hashCode.startsWith(stringMultiply(countZero))) {
                if (k % 8000 == 0) {
                    if (listMessage.size() == 0) {
                        idMessage = random.nextInt();
                    } else {
                        int max = listMessage.stream().reduce((x, y) -> x.getId() > y.getId() ? x : y).get().getId();
                        idMessage = random.nextInt();
                        while (idMessage < max) {
                            idMessage = random.nextInt();
                        }
                    }
                    Message message = new Message(listConst.get(i), idMessage);
                    if (verifySignature(message)){
                        listMessage.add(message);
                        i++;
                    }
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

    public void getHashPrevious() {
        String fileName = "blockchains.data";
        BlockChain[] blockChains1 = blockChains.toArray(new BlockChain[0]);
        try {
            SerializationUtils.serialize(blockChains1, fileName);
            BlockChain[] blockChainList = (BlockChain[]) SerializationUtils.deserialize(fileName);
            HashOfThePreviousBlock = blockChainList[Id - 2].getHashOfTheBlock();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    private List<Message> getHashPreviousMessage() {
        String fileName = "blockchains.data";
        BlockChain[] blockChains1 = blockChains.toArray(new BlockChain[0]);
        try {
            SerializationUtils.serialize(blockChains1, fileName);
            BlockChain[] blockChainList = (BlockChain[]) SerializationUtils.deserialize(fileName);
            return blockChainList[Id - 2].getListMessage();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean verifySignature(Message message) throws Exception {
        Signature sig = Signature.getInstance("SHA1withRSA");
        sig.initVerify(message.getPublicKey());
        sig.update((byte) message.getId());
        sig.update(message.getData().getBytes());
        return sig.verify(message.getSignature());
    }
}
