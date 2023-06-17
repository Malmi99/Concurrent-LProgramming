public class PrintingSystem {
    public static void main(String[] args) throws InterruptedException{

        ThreadGroup students = new ThreadGroup("students");
        ThreadGroup technicians = new ThreadGroup("technicians");

        ServicePrinter printer = new LaserPrinter("HP Printer", 250, 500, students);

        ThreadGroup studentGroup = new ThreadGroup("Students");
        ThreadGroup technicianGroup = new ThreadGroup("Technicians");

        Student studentRunnable01 = new Student("Mary", studentGroup, printer);
        Student studentRunnable02 = new Student("Mike", studentGroup, printer);
        Student studentRunnable03 = new Student("Emma", studentGroup, printer);
        Student studentRunnable04 = new Student("Wiki", studentGroup, printer);

        Thread studentThread01 = new Thread(studentGroup, studentRunnable01, "Mary");
        Thread studentThread02 = new Thread(studentGroup, studentRunnable02, "Mike");
        Thread studentThread03 = new Thread(studentGroup, studentRunnable03, "Emma");
        Thread studentThread04 = new Thread(studentGroup, studentRunnable04, "Wiki");

        Runnable paperTechnician = new PaperTechnician("PaperTech", technicianGroup, printer);
        Runnable tonerTechnician = new TonerTechnician("TonerTech", technicianGroup, printer);

        Thread paperTechThread = new Thread(technicianGroup, paperTechnician, "PaperTech");
        Thread tonerTechThread = new Thread(technicianGroup, tonerTechnician, "TonerTech");
        studentThread01.start();
        studentThread02.start();
        studentThread03.start();
        studentThread04.start();
        paperTechThread.start();
        tonerTechThread.start();

        studentThread01.join();
        studentThread02.join();
        studentThread03.join();
        studentThread04.join();
        paperTechThread.join();
        tonerTechThread.join();
        System.out.println("EXECUTION COMPLETED");
        System.out.println("********************************************PRINTING SYSTEM SUMMARY*************************************************");
        System.out.println(printer.toString());

    }

}
