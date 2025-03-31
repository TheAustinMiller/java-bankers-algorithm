import java.util.Random;
public class Consumer implements Runnable {
    private int[] demand;
    private int numOfCustomers;
    private int numOfResources;
    private Random randy;
    private Banker bank;
    private int[] request;
    public Consumer(int custNum, int[] demand, Banker bank) {
        this.numOfCustomers = custNum;
        this.demand = new int[demand.length];
        this.bank = bank;
        System.arraycopy(demand, 0, this.demand, 0, demand.length);
        numOfResources = demand.length;
        request = new int[numOfResources];
        randy = new Random();
    }
    public void run() {
        boolean running = true;
        while (running) {
            try {
                sleepy();
                for (int i = 0; i < numOfResources; i++) {
                    request[i] = randy.nextInt(demand[i] + 1);
                }

                if (bank.requestResources(numOfCustomers, request)) {
                    sleepy();

                    bank.releaseResources(numOfCustomers, request);
                }
            } catch (InterruptedException ie) {
                running = false;
            }
        }
        System.out.println("Customer " + numOfCustomers + ": Interrupted.");
    }

    private static void sleepy() throws InterruptedException {
        int time = (int) (5 * Math.random());
        Thread.sleep(time * 1000);
    }
}
