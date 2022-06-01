import java.util.*;
import java.util.concurrent.*;


public class Part_1 {
    public static void main(String[] args) throws InterruptedException {
        final int THREADS = 10; // кол-во потоков
        ExecutorService executor = Executors.newFixedThreadPool(THREADS);
        List<BlockChain> blockChains = new ArrayList<>();


// 1 способ синхронизации потоков с помощью CountDownLatch
        final CountDownLatch order = new CountDownLatch(5);
        for (int i = 1; i <= 5; i++) {
            Runnable runnable = () -> {
                try {
                    Thread.sleep((long) (Math.random() * 12000));
                    BlockChain blockChain = new BlockChain(blockChains.size() + 1, blockChains);
                    blockChains.add(blockChain);
                    blockChain.printData();
                    order.countDown();

                } catch (Exception ignored) {
                }
            };
            executor.execute(runnable);
        }
        order.await();




/* 2 способ синхронизации потоков
//        for (int i = 1; i <= 6; i++) {
//            Future<BlockChain> future = executor.submit(() -> {
//                BlockChain blockChain = new BlockChain(blockChains.size() + 1, blockChains);
//                blockChains.add(blockChain);
//                return null;
//            });
//            if (!future.isDone()){
//                future.get();
//            }
//        }


 */
/* 3 способ синхронизации потоков
//        for (int i = 1; i <= 6; i++) {
//
//            BlockChain blockChain = new BlockChain(blockChains.size() + 1, blockChains);
//            blockChains.add(blockChain);
//            Thread t = new Thread(new CountThread(blockChain, blockChains));
//            t.setName("Thread " + i);
//
//            //blockChains = countThread.getBlockChainList();
////            System.out.println(blockChains);
//            if (blockChains.size() == 6) {
//                break;
//            }
//            t.start();
//        }
 */
        executor.shutdown();
    }

}
