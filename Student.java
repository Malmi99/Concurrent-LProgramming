import java.util.Random;

public class Student implements Runnable{
    private String name;
    private ThreadGroup threadGroup;
    private Printer printer;
    private int docCount;
    private int documentPageCount;
    public Student(String name, ThreadGroup group, Printer printer) {
        super();
        this.name = name;
        this.threadGroup = group;
        this.printer = printer;
        this.printer = printer;
    }

    @Override
    public void run() {
        Random random = new Random();
        for (int i = 0; i < 5; i++) {

            String documentName = "document" + (i + 1);
            //Document document = new Document(this.getName(), documentName,getRandomPageCount());
            Document document = new Document(this.name, documentName,getRandomPageCount());
            printer.printDocument(document);
            documentPageCount += document.getNumberOfPages();
            docCount++;

            int sleepingTime= random.nextInt(101);
            try {
                Thread.sleep(sleepingTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Thread.currentThread().getName() +"------ "+ "finished printing:"+docCount+" Documents,"+documentPageCount+" "+"Pages");
    }

    public int getRandomPageCount(){
        return new Random().nextInt(20) + 1;
    };

}
