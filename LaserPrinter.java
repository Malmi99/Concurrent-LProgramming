import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LaserPrinter implements ServicePrinter{
    private String printerId;
    private int currentPaperLevel;
    private int currentTonerLevel;
    private int numOfDocumentsPrinted;
    private ThreadGroup threadGroup;
    public LaserPrinter( String printerId, int currentPaperLevel, int currentTonerLevel,ThreadGroup threadGroup) {
        this.printerId = printerId;
        this.currentPaperLevel = currentPaperLevel;
        this.currentTonerLevel = currentTonerLevel;
        this.numOfDocumentsPrinted = 0;
        this.threadGroup= threadGroup;
    }

    private final Lock reenteredLock = new ReentrantLock(true);
    private final Condition condition = reenteredLock.newCondition();
    public String toString() {
        return "LaserPrinter{" +
                "PrinterID: '" + printerId + '\'' + ", " +
                "Paper Level: " + currentPaperLevel + ", " +
                "Toner Level: " + currentTonerLevel + ", " +
                "Documents Printed: " + numOfDocumentsPrinted +
                '}';
    }
    @Override
    public void replaceTonerCartridge() {
        try{
            reenteredLock.lock();
            while(currentTonerLevel >= Minimum_Toner_Level){
                if(isAllStudentsHaveCompleted()){
                    System.out.printf("Checking toner>>>>> " + "No need to refill toner anymore. Current Toner Level is %d\n", currentTonerLevel);
                    break;
                }

                System.out.println("Current tonerLevel: " + currentTonerLevel+ " Toner has not reached to the minimum level.No need to replace the toner level this time");
                condition.await(5000, TimeUnit.MILLISECONDS);
            }
            if(currentTonerLevel<Minimum_Toner_Level){
                currentTonerLevel+= Full_Toner_Level;
                condition.signalAll();
                System.out.println("Successfully replaced Toner Cartridge. New Toner Level is: "+ currentTonerLevel);
            }
        }

        catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            reenteredLock.unlock();
        }

    }

    @Override
    public void refillPaper() {
        try{
            reenteredLock.lock();
            while((currentPaperLevel+SheetsPerPack)>Full_Paper_Tray){
                if(isAllStudentsHaveCompleted()){
                    System.out.println("Current paperLevel: " + currentPaperLevel+ "--> So All students Have completed printing. No need to refill again");
                    break;
                }
                else {
                    System.out.printf("Checking  Current Paper Level... " + "Paper Tray cannot be refilled at this time (exceeds maximum paper level). " + "Current paper level is %d and Maximum paper level is %d.\n", currentPaperLevel, Full_Paper_Tray);
                    condition.await(5000, TimeUnit.MILLISECONDS);
                }

            }

            if((currentPaperLevel+SheetsPerPack)<= Full_Paper_Tray){
                System.out.println("Checking papers>>>>>>>>Refilling printer with paper");
                currentPaperLevel += SheetsPerPack;
               System.out.println("Paper Technician Refilled Tray With Pack of Paper Successfully. New Paper level: " +currentPaperLevel+" pack of paper used: ");
                condition.signalAll();
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            reenteredLock.unlock();
        }

    }

    @Override
    public void printDocument(Document document) {

        try{
            String student = document.getUserID();
            String docName = document.getDocumentName();
            int numberOfPages = document.getNumberOfPages();
            reenteredLock.lock();
            //int numberOfPages= document.getNumberOfPages();
            while( currentTonerLevel<numberOfPages || currentPaperLevel<numberOfPages) {
                if (currentTonerLevel < numberOfPages) {
                    System.out.printf("[%s][%s][%d pages]-Out of paper and toner. Current Paper Level is %d and Toner Level is %d.\n" , student, docName, numberOfPages, currentPaperLevel, currentTonerLevel);
                } else if (currentPaperLevel < numberOfPages) {
                    System.out.printf("[%s][%s][%d pages]-Out of paper. Current Paper Level is %d.\n" , student, docName, numberOfPages, currentPaperLevel);

                } else {
                    System.out.printf("[%s][%s][%d pages]-Out of toner. Current Toner Level is %d.\n" , student, docName, numberOfPages, currentTonerLevel);
                }

                condition.await();
            }

            if (currentTonerLevel >= numberOfPages && currentPaperLevel >= numberOfPages) {
                currentTonerLevel -= numberOfPages;
                currentPaperLevel -= numberOfPages;
                numOfDocumentsPrinted++;


                System.out.printf("[%s][%s][%d pages]-Successfully printed the document. New Paper Level is %d and Toner Level is %d.\n",
                        student, docName, numberOfPages,currentPaperLevel, currentTonerLevel);

            }
            condition.signalAll();// toner and student
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally{
            reenteredLock.unlock();

        }


    }

    private boolean isAllStudentsHaveCompleted() {
        return threadGroup.activeCount() == 0;
    }}
