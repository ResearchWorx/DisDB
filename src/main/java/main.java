import java.util.Scanner;

public class main {

    public static void main(String[] args) throws Exception {

        dbServer dbs = new dbServer();
        dbs.startServer();

        //ProgrammaticOHazelcastPlugin.addMember("10.20.22.130:2434");

        System.out.print("Name of DB cluster to join [q to quit]: ");


        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        while (!input.toLowerCase().equals("q")) {

            System.out.print("Name of DB cluster to join [q to quit]: ");
            ProgrammaticOHazelcastPlugin.addMember(input);
            input = scanner.nextLine();
        }
        dbs.stopServer();

    }
}