public class BlockChain {
    private int Id;
    private long Timestamp;
    private String HashOfThePreviousBlock;
    private String HashOfTheBlock;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public long getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(long timestamp) {
        Timestamp = timestamp;
    }

    public String getHashOfThePreviousBlock() {
        return HashOfThePreviousBlock;
    }

    public void setHashOfThePreviousBlock(String hashOfThePreviousBlock) {
        HashOfThePreviousBlock = hashOfThePreviousBlock;
    }

    public String getHashOfTheBlock() {
        return HashOfTheBlock;
    }

    public void setHashOfTheBlock(String hashOfTheBlock) {
        HashOfTheBlock = hashOfTheBlock;
    }

    public BlockChain(int id, long timestamp, String hashOfThePreviousBlock, String hashOfTheBlock) {
        Id = id;
        Timestamp = timestamp;
        HashOfThePreviousBlock = hashOfThePreviousBlock;
        HashOfTheBlock = hashOfTheBlock;
    }
}
