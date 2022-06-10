import java.io.IOException;
import java.io.Serializable;
import java.security.Signature;
import java.util.*;

public class BlockChain implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int Id;
    private final long Timestamp;
    private String HashOfThePreviousBlock;
    long elapsedNanos;
    private final String HashOfTheBlock;
    private long magicNumber;
    private int countZero;
    List<BlockChain> blockChains;
    static int idMessage = 0;
    List<Message> messagesPrevious;
    List<Message> listMessage;

    public int getCountZero() {
        return countZero;
    }

    public List<Message> getListMessage() {
        return listMessage;
    }

    public void printData() {
        long threadId = Thread.currentThread().getId();
        System.out.println();
        System.out.println("Block:");
        System.out.println("Created by miner #" + threadId);
        System.out.println(threadId + " gets 100 VC");
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
            System.out.println("Block data:");
            System.out.println("No transactions");
        }
        System.out.printf("Block was generating for %d seconds\n", (int) elapsedNanos / 10);
        if (elapsedNanos / 10 < 15) {
            countZero = countZero + 1;
            System.out.println("N was increased to " + countZero);
        } else if (elapsedNanos / 10 >= 15 && elapsedNanos / 10 < 60) {
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
        this.elapsedNanos = 0;
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
        magicNumber = random.nextLong();
        if (Id == 1) {
            listMessage = new ArrayList<>();
            idMessage = random.nextInt();
            int a = random.nextInt(100);
            String str = generateString() + " sent " + a + " VC to " + generateString();
            Message message = new Message(str, idMessage);
            if (verifySignature(message)) {
                listMessage.add(message);
                //i++;
            }
            return StringUtil.applySha256(Id + String.valueOf(Timestamp) + HashOfThePreviousBlock + magicNumber);
        } else {
            listMessage = new ArrayList<>();
            while (!hashCode.startsWith(stringMultiply(countZero))) {
                if (magicNumber % 5000 == 0) {
                    if (listMessage.size() == 0) {
                        idMessage = random.nextInt();
                    } else {
                        int max = listMessage.stream().reduce((x, y) -> x.getId() > y.getId() ? x : y).get().getId();
                        idMessage = random.nextInt();
                        while (idMessage < max) {
                            idMessage = random.nextInt();
                        }
                    }
                    int a = random.nextInt(100);
                    Message message = new Message(generateString() + " sent " + a + " VC to " + generateString(), idMessage);
                    if (verifySignature(message)) {
                        listMessage.add(message);
                        //i++;
                    }
                }
                hashCode = StringUtil.applySha256(Id + String.valueOf(Timestamp) + HashOfThePreviousBlock + magicNumber);
                magicNumber = random.nextLong();

            }
            elapsedNanos = new Date().getTime() - Timestamp;
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

    public String generateString() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 7;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();
        generatedString = generatedString.substring(0, 1).toUpperCase() + generatedString.substring(1);
        return generatedString;
    }
}
