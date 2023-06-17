import java.util.Random;
import java.util.Random;

public class PaperTechnician implements Runnable{
    private String name;
    private ThreadGroup threadGroup;
    private ServicePrinter printer;
    private int count;

    public PaperTechnician(String name,ThreadGroup group, ServicePrinter printer) {
        super();
        this.name = name;
        this.threadGroup = group;
        this.printer = printer;
    }

    public void run() {
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            printer.refillPaper();
            int sleepingTime = random.nextInt(3000) + 1000;
            try {
                Thread.sleep(sleepingTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.printf("Paper Technician %s was interrupted during sleeping time " +
                                "after refilling paper pack no. %d.\n",
                        sleepingTime, i);
            }
        }
        System.out.printf("Paper Technician %s finished attempts to refill printer with paper packs.\n", name);
    }
}
//@Override
//public void run() {
//    int count = 0;
//
//    for(int i = 0; i < 3; i++){
//        printer.refillPaper();
//
//        if(((LaserPrinter)printer).isPaperRefilled()){
//            count++;
//        }
//
//        //Math.random() generates a number between 0.0 to 1.0
//        int num = ((int)(Math.random() * 100 + 1)); //Gives random number between 0 - 100
//
//        try {
//            Thread.sleep(num);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//    System.out.println("Paper Technician Finished, packs of paper used: "+ count);
//
//}

