public interface Banker {

    void makeCustomer(int custNum, int[] maxDemand);
    boolean requestResources(int custNum, int[] request);
    void releaseResources(int custNum, int[] release);
    void getFormation();
}


