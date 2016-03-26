import java.util.Scanner;

public class main {

    public static void main(String[] args) throws Exception {

        if(args.length == 0)
        {
            System.out.println("arg[0]=delay , args[1]=connect to server");
            System.exit(0);
        }

            int sleeptime = Integer.parseInt(args[0]);
            System.out.println("Sleeping for " + sleeptime + " seconds");
            Thread.sleep(sleeptime);

        dbServer dbs = new dbServer();
        dbs.startServer();

        if(args.length == 2) {

            ProgrammaticOHazelcastPlugin.addMember(args[1]);
            //ProgrammaticOHazelcastPlugin.addMember("10.20.22.130:2434");
        }
        System.out.print("Name of DB cluster to join [q to quit]: ");


        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        while (!input.toLowerCase().equals("q")) {

            if(input.length() > 0) {
                ProgrammaticOHazelcastPlugin.addMember(input);
            }
            else
            {
                dbs.getStats();
            }
            System.out.println("Name of DB cluster to join [q to quit]: ");


            input = scanner.nextLine();
        }
        dbs.stopServer();

    }
}