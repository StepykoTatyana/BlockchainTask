import java.io.IOException;
import java.util.*;

public class Part_1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter how many zeros the hash must start with: ");
        int countOfZero = scanner.nextInt();

        List<BlockChain> blockChains = new ArrayList<>();
        String previousCode = "0";
        for (int i = 1; i <= 3; i++) {
            long date = new Date().getTime();
            BlockChain blockChain = new BlockChain(i, date, previousCode, countOfZero);
            blockChain.printData();


            blockChains.add(blockChain);
            BlockChain[] blockChains1 = blockChains.toArray(new BlockChain[0]);

            String fileName = "blockchains.data";
            try {
                SerializationUtils.serialize(blockChains1, fileName);
                BlockChain[] blockChainList = (BlockChain[]) SerializationUtils.deserialize(fileName);
                previousCode = blockChainList[i - 1].getHashOfTheBlock();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

}