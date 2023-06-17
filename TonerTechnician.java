import java.util.Random;
import java.util.concurrent.TimeUnit;

public class TonerTechnician implements Runnable{
    private String name;
    private ThreadGroup threadGroup;
    private ServicePrinter printer;

    public TonerTechnician(String name,ThreadGroup group, ServicePrinter printer) {
        super();
        this.name = name;
        this.threadGroup = group;
        this.printer = printer;
    }

    public void run(){
        Random random = new Random();
        for(int i =0;i<3;i++){
            printer.replaceTonerCartridge();
            int sleepingTime= random.nextInt(3000)+1000;
            try {
                Thread.sleep(sleepingTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.printf("Toner Technician %s was interrupted during sleeping time " + "after replacing toner cartridge no. %d.\n", sleepingTime, i);
            }
        }

        System.out.printf("Toner Technician finished attempts to replace toner cartridges.\n", name);
    }

}
