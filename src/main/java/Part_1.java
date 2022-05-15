import java.util.*;

public class Part_1 {
    public static void main(String[] args) {
        Map<Integer, BlockChain> map = new LinkedHashMap<>();
        for (int i = 1; i <= 5; i++) {
            long date = new Date().getTime();
            String previousCode = "0";
            if (i != 1) {
                previousCode = map.get(i - 1).getHashOfTheBlock();
            }
            String hashCode = StringUtil.applySha256(i + String.valueOf(date) + previousCode);
            BlockChain blockChain = new BlockChain(i, date, previousCode, hashCode);
            map.put(i, blockChain);
            System.out.println("Block:");
            System.out.println("Id: " + i);
            System.out.println("Timestamp: " + date);
            System.out.println("Hash of the previous block:");
            System.out.println(previousCode);
            System.out.println("Hash of the block:");
            System.out.println(hashCode);
            System.out.println();
        }
    }
}
