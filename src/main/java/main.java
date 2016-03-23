import com.orientechnologies.orient.server.OServer;
import com.orientechnologies.orient.server.OServerMain;

public class main {

    public static void main(String[] args) throws Exception {

        dbServer dbs = new dbServer();
        dbs.startServer();

    }
}