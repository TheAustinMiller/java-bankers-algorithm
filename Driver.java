import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Driver {
    private static Scanner scanner;
    public static void main(String[] args) {
        String mode = args[0];
        String fileName = args[1];
        try {
            scanner = new Scanner(new File(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String[] firstLine = scanner.nextLine().split(" ");
        int numberOfResource = Integer.parseInt(firstLine[0]);
        String[] secondLine = scanner.nextLine().split(" ");
        int numberOfProcess = Integer.parseInt(secondLine[0]);
        String trash = scanner.nextLine();
        int[] avail = new int[numberOfResource];
        for (int i = 0; i < numberOfResource; i++) {
            avail[i] = scanner.nextInt();
        }
        trash = scanner.nextLine();
        trash = scanner.nextLine();
        int[][] max = new int[numberOfResource][numberOfProcess];
        for (int i = 0; i < max[0].length; i++) {
            String[] newLine = scanner.nextLine().split(" ");
            for (int j = 0; j < max.length; j++) {
                max[j][i] = Integer.parseInt(newLine[j]);
            }
        }
        trash = scanner.nextLine();
        int[][] allo = new int[numberOfResource][numberOfProcess];
        for (int i = 0; i < allo[0].length; i++) {
            String[] newLine = scanner.nextLine().split(" ");
            for (int j = 0; j < allo.length; j++) {
                allo[j][i] = Integer.parseInt(newLine[j]);
            }
        }
        Banker bank = new TheBank(avail, numberOfProcess);
        Thread[] customers = new Thread[numberOfProcess];
        int custNum = 0;
        int resourceNum = 0;
        for (int i = 0; i < numberOfProcess; i++) {
            for (int j = 0; j < numberOfResource; j++) {
                max[resourceNum++][i] = getRandomInt(max[j][i]);
            }
            customers[custNum] = new Thread(new Consumer(custNum, getArray(i, numberOfResource, allo), bank));
            bank.makeCustomer(custNum, getArray(i, numberOfResource, max));
            ++custNum;
            resourceNum = 0;
        }
        for (int i = 0; i < customers.length; i++) {
            customers[i].start();
        }
    }
    private static int getRandomInt(int max) {
        return ThreadLocalRandom.current().nextInt(0, max + 1);
    }
    public static int[] getArray(int y, int resources, int[][] matrix) {
        int[] newArray = new int[resources];
        for (int i = 0; i < resources; i++) {
            newArray[i] = matrix[i][y];
        }
        return newArray;
    }
}
