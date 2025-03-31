import java.util.Arrays;
public class TheBank implements Banker {
    private int numOfRequests;
    private int numOfCustomers;
    private int[] available;
    private int[][] maximum;
    private int[][] allocation;

    private static final int MAX_REQUESTS = 20;
    public TheBank(int[] avail, int customerCnt) {
        numOfCustomers = customerCnt;
        this.available = avail;
        this.maximum = new int[numOfCustomers][available.length];
        this.allocation = new int[numOfCustomers][available.length];
    }
    public void makeCustomer(int custNum, int[] maxDemand) {
        if (custNum > maximum.length) {
            maximum = resizeCustomerArray(maximum, custNum);
        }
        maximum[custNum] = maxDemand;
    }
    public boolean requestResources(int custNum, int[] request) {
        logRequest();
        for (int i = 0; i < request.length; i++) {
            if (request[i] > available[i]) {
                return false;
            }
            if (request[i] > maximum[custNum][i]) {
                return false;
            }
        }
        if (!safeState(custNum, request)) {
            return false;
        }
        for (int i = 0; i < request.length; i++) {
            available[i] -= request[i];
            allocation[custNum][i] += request[i];
        }
        return true;
    }
    private void logRequest() {
        if (numOfRequests >= MAX_REQUESTS) {
            Thread.currentThread().interrupt();
        }
        numOfRequests++;
    }
    private boolean safeState(int custNum, int[] request) {
        int[] clonedResources = available.clone();
        int[][] clonedAllocation = allocation.clone();

        for (int i = 0; i < clonedResources.length; i++) {
            if (request[i] > clonedResources[i]) {
                return false;
            }
        }
        for (int i = 0; i < clonedResources.length; i++) {
            clonedResources[i] -= request[i];
            clonedAllocation[custNum][i] += request[i];
        }
        boolean[] canFinish = new boolean[numOfCustomers];
        for (int i = 0; i < canFinish.length; i++) {
            canFinish[i] = false;
        }
        for (int i = 0; i < numOfCustomers; i++) {
            for (int j = 0; j < numOfCustomers; j++) {
                if (!canFinish[j]) {
                    for (int k = 0; k < clonedResources.length; k++) {
                        if (!((maximum[j][k] - clonedAllocation[j][k]) > clonedResources[k])) {
                            canFinish[j] = true;
                            for (int l = 0; l < clonedResources.length; l++) {
                                clonedResources[l] += clonedAllocation[j][l];
                            }
                        }
                    }
                }
            }
        }
        for (int i = 0; i < available.length; i++) {
            clonedAllocation[custNum][i] -= request[i];
        }
        for (boolean aCanFinish : canFinish) {
            if (!aCanFinish) {
                return false;
            }
        }
        return true;
    }
    public void getFormation() {
        StringBuilder newString = new StringBuilder();
        newString.append("\n\nAvailable:\n----------\n");
        for (int i = 0; i < available.length; i++) {
            newString.append("Resource " + i + ": " + available[i] + "\n");
        }
        newString.append("\n\nAllocated:\n----------\n");
        for (int i = 0; i < allocation[0].length; i++) {
            int sum = 0;

            for (int j = 0; j < allocation.length; j++) {
                sum += allocation[j][i];
            }
            newString.append("Resource " + i + ": " + sum + "\n");
        }
        newString.append("\n\nMaximum Demand:\n----------");
        for (int i = 0; i < maximum.length; i++) {
            newString.append("\nProcess " + i + ": ");
            for (int j = 0; j < maximum[i].length; j++) {
                newString.append(" Resource " + j + ": " + maximum[i][j]);
            }
        }
        newString.append("\n\nNeed:\n----------");
        for (int i = 0; i < maximum.length; i++) {
            newString.append("\nProcess " + i + ": ");

            for (int j = 0; j < maximum[i].length; j++) {
                newString.append(" Resource " + j + ": " + (maximum[i][j] + maximum[i][j]));
            }
        }
        System.out.println(newString);
    }
    public synchronized void releaseResources(int custNum, int[] release) {
        for (int i = 0; i < release.length; i++) {
            available[i] += release[i];
            allocation[custNum][i] -= release[i];
        }
        getFormation();
    }
    private static int[][] resizeCustomerArray(int[][] original, int newLength) {
        if (original == null) {
            return null;
        }
        final int[][] result = new int[newLength][];
        for (int i = 0; i < original.length; i++) {
            result[i] = Arrays.copyOf(original[i], original[i].length);
        }
        return result;
    }
}
