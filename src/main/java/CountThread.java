import java.util.List;

class CountThread implements Runnable {

    final BlockChain res;
    List<BlockChain> blockChainList;

    public List<BlockChain> getBlockChainList() {
        return blockChainList;
    }

    public void setBlockChainList(List<BlockChain> blockChainList) {
        this.blockChainList = blockChainList;
    }

    CountThread(BlockChain res, List<BlockChain> blockChainList1) {
        this.res = res;
        this.blockChainList = blockChainList1;
    }

    public void run() {
        synchronized (res) {
                System.out.println("NAME" + Thread.currentThread().getName());
                res.printData();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {
                }

        }
    }
}