import com.orientechnologies.orient.server.OServer;
import com.orientechnologies.orient.server.OServerMain;
import com.orientechnologies.orient.server.config.*;

import com.orientechnologies.orient.server.network.protocol.binary.ONetworkProtocolBinary;
import com.orientechnologies.orient.server.plugin.OServerPluginInfo;

import java.math.BigInteger;
import java.util.*;

/**
 * Created by vcbumg2 on 3/23/16.
 */
public class dbServer {
    private OServer server;
    private static Random random = new Random();

    public dbServer() {
    }

    public static String getName() {
        return new BigInteger(130, random).toString(32).substring(0,5);
    }

    public void startServer() {
        try {
            server = OServerMain.create();

            OServerConfiguration cfg = new OServerConfiguration();
            //network
            OServerNetworkConfiguration netcfg = new OServerNetworkConfiguration();
                //protocol binary
            netcfg.protocols = new ArrayList<>();
            OServerNetworkProtocolConfiguration npc = new OServerNetworkProtocolConfiguration("binary","com.orientechnologies.orient.server.network.protocol.binary.ONetworkProtocolBinary");
            netcfg.protocols.add(npc);

                //listners
            netcfg.listeners = new ArrayList<>();
            OServerNetworkListenerConfiguration nlc = new OServerNetworkListenerConfiguration();
            //nlc.ipAddress = "0.0.0.0";
            nlc.ipAddress = "[::]";
            //nlc.ipAddress = "0:0:0:0:0:0:0:0";

            netcfg.listeners.add(nlc);

            cfg.network = netcfg;

            //users
            List<OServerUserConfiguration> userList = new ArrayList<>();
            userList.add(new  OServerUserConfiguration("root","cody01","*"));
            cfg.users = userList.toArray(new OServerUserConfiguration[userList.size()]);
            //entrys
            List<OServerEntryConfiguration> ec = new ArrayList<>();
            ec.add(new OServerEntryConfiguration("orientdb.www.path","C:/work/dev/orientechnologies/orientdb/releases/1.0rc1-SNAPSHOT/www/"));
            ec.add(new OServerEntryConfiguration("orientdb.config.file","C:/work/dev/orientechnologies/orientdb/releases/1.0rc1-SNAPSHOT/config/orientdb-server-config.xml"));
            ec.add(new OServerEntryConfiguration("server.cache.staticResources","false"));
            ec.add(new OServerEntryConfiguration("log.console.level","debug"));
            ec.add(new OServerEntryConfiguration("log.file.level","fine"));
            //The following is required to eliminate an error or warning "Error on resolving property: ORIENTDB_HOME"
            ec.add(new OServerEntryConfiguration("plugin.dynamic","false"));
            cfg.properties = ec.toArray(new OServerEntryConfiguration[ec.size()]);


            //handlers
            cfg.handlers = new ArrayList<>();
            OServerHandlerConfiguration han = new OServerHandlerConfiguration();
            //han.clazz = "com.orientechnologies.orient.server.hazelcast.OHazelcastPlugin";
            han.clazz = ProgrammaticOHazelcastPlugin.class.getName();
            List<OServerParameterConfiguration> hanparam = new ArrayList<>();
            hanparam.add(new OServerParameterConfiguration("nodeName",getName()));
            hanparam.add(new OServerParameterConfiguration("enabled","true"));
            hanparam.add(new OServerParameterConfiguration("configuration.db.default","/tmp/default-distributed-db-config.json"));
            hanparam.add(new OServerParameterConfiguration("configuration.hazelcast","${ORIENTDB_HOME}/config/hazelcast.xml"));
            han.parameters = hanparam.toArray(new OServerParameterConfiguration[hanparam.size()]);
            cfg.handlers.add(han);

            server.startup(cfg);

            server.activate();

            /*
            server.startup(
                    "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                            + "<orient-server>"
                            + "<network>"
                            + "<protocols>"
                            + "<protocol name=\"binary\" implementation=\"com.orientechnologies.orient.server.network.protocol.binary.ONetworkProtocolBinary\"/>"
                            + "<protocol name=\"http\" implementation=\"com.orientechnologies.orient.server.network.protocol.http.ONetworkProtocolHttpDb\"/>"
                            + "</protocols>"
                            + "<listeners>"
                            + "<listener ip-address=\"0.0.0.0\" port-range=\"2424-2430\" protocol=\"binary\"/>"
                            + "<listener ip-address=\"0.0.0.0\" port-range=\"2480-2490\" protocol=\"http\"/>"
                            + "</listeners>"
                            + "</network>"
                            + "<users>"
                            + "<user name=\"root\" password=\"ThisIsA_TEST\" resources=\"*\"/>"
                            + "</users>"
                            + "<properties>"
                            + "<entry name=\"orientdb.www.path\" value=\"C:/work/dev/orientechnologies/orientdb/releases/1.0rc1-SNAPSHOT/www/\"/>"
                            + "<entry name=\"orientdb.config.file\" value=\"C:/work/dev/orientechnologies/orientdb/releases/1.0rc1-SNAPSHOT/config/orientdb-server-config.xml\"/>"
                            + "<entry name=\"server.cache.staticResources\" value=\"false\"/>"
                            + "<entry name=\"log.console.level\" value=\"info\"/>"
                            + "<entry name=\"log.file.level\" value=\"fine\"/>"
                            //The following is required to eliminate an error or warning "Error on resolving property: ORIENTDB_HOME"
                            + "<entry name=\"plugin.dynamic\" value=\"false\"/>"
                            + "</properties>" + "</orient-server>");
            server.activate();*/
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    public void stopServer() {

        server.shutdown();

    }
    public void getStats()
    {
        System.out.println("Local Node Id: " + server.getDistributedManager().getLocalNodeId());
        System.out.println("Local Node Name: " + server.getDistributedManager().getLocalNodeName());
        System.out.println("node status: " + server.getDistributedManager().getNodeStatus());


        String[] fieldnames = server.getDistributedManager().getClusterConfiguration().fieldNames();
        for(int i=0; i < fieldnames.length; i++)
        {

            System.out.println("-cluster config " + fieldnames[i] + "=" + server.getDistributedManager().getClusterConfiguration().field(fieldnames[i]));
        }
        /*
        Collection<OServerPluginInfo> serverplugins = server.getPlugins();
        for (Iterator iterator = serverplugins .iterator(); iterator.hasNext();) {
            OServerPluginInfo plugin = (OServerPluginInfo) iterator.next();
            System.out.println("name" + plugin.getName());
            System.out.println("disc" + plugin.getDescription());
            System.out.println("ver" + plugin.getVersion());
            System.out.println("web" + plugin.getWeb());

        }
        */
        //String[] fieldnames = server.getDistributedManager().getStats().fieldNames();

        /*
        String[] fieldnames2 = server.getDistributedManager().getNodeConfigurationById(server.getDistributedManager().getLocalNodeId()).fieldNames();
        for(int i=0; i < fieldnames2.length; i++)
        {
            System.out.println("-local config " + fieldnames2[i] + "=" + server.getDistributedManager().getNodeConfigurationById(server.getDistributedManager().getLocalNodeId()).field(fieldnames2[i]));
        }
        */
    }
}

