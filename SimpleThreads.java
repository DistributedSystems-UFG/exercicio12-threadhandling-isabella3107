public class SimpleThreads {

    static void threadMessage(String message) {
        String threadName = Thread.currentThread().getName();
        System.out.format("%s: %s%n", threadName, message);
    }


    private static class MessageLoop implements Runnable {
        public void run() {
            String importantInfo[] = {
                "Mares eat oats",
                "Does eat oats",
                "Little lambs eat ivy",
                "A kid will eat ivy too"
            };

            try {
                for (int i = 0; i < importantInfo.length; i++) {
                    Thread.sleep(4000);
                    threadMessage(importantInfo[i]);
                }
            } catch (InterruptedException e) {
                threadMessage("MessageLoop interrupted!");
            }
        }
    }

    // Nova thread CPU-intensive
    private static class HeavyComputation implements Runnable {

        public void run() {

            long number = 2;

            try {
                while (true) {

            
                    if (Thread.interrupted()) {
                        throw new InterruptedException();
                    }

                    if (isPrime(number)) {
                        threadMessage("Prime found: " + number);
                    }

                    number++;
                }

            } catch (InterruptedException e) {
                threadMessage("Heavy computation interrupted!");
            }
        }


        private boolean isPrime(long n) {

            if (n < 2) return false;

            for (long i = 2; i <= Math.sqrt(n); i++) {

                
                if (Thread.currentThread().isInterrupted()) {
                    return false;
                }

                if (n % i == 0) {
                    return false;
                }
            }

            return true;
        }
    }

    public static void main(String args[])
            throws InterruptedException {

        long patience = 10000; // 10 segundos

        threadMessage("Starting MessageLoop thread");
        Thread messageThread = new Thread(new MessageLoop());
        messageThread.start();

        threadMessage("Starting HeavyComputation thread");
        long startTime = System.currentTimeMillis();

        Thread heavyThread = new Thread(new HeavyComputation());
        heavyThread.start();

        while (heavyThread.isAlive()) {

            threadMessage("Waiting for HeavyComputation...");

            heavyThread.join(1000);

        
            if ((System.currentTimeMillis() - startTime > patience)
                    && heavyThread.isAlive()) {

                threadMessage("Tired of waiting for HeavyComputation!");

            
                heavyThread.interrupt();

                heavyThread.join();
            }
        }

        // Espera a thread original terminar
        messageThread.join();

        threadMessage("Finally!");
    }
}
