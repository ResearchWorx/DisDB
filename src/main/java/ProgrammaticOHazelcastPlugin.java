import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.server.hazelcast.OHazelcastPlugin;
import java.io.FileNotFoundException;
import java.net.*;
import java.util.*;


public class ProgrammaticOHazelcastPlugin extends OHazelcastPlugin {
    public static HazelcastInstance instance;
    public static Config hc;

    //For 1.7rc2 and above
    @Override
    public HazelcastInstance configureHazelcast() throws FileNotFoundException {

        //HazelcastInstance instance = null;
        //config
        hc = new Config();
        //<property name="hazelcast.prefer.ipv4.stack">false</property>
        hc.setProperty("hazelcast.prefer.ipv4.stack","false");
        //group config
        GroupConfig gc = new GroupConfig();
        gc.setName("tests");
        gc.setPassword("test01");
        hc.setGroupConfig(gc);
        //
        NetworkConfig nc = new NetworkConfig();
        //nc.setSocketInterceptorConfig()
        InterfacesConfig ic = new InterfacesConfig();
        ic.setEnabled(true);
        //ic.setInterfaces(getAddressList());//set all public addresses
        ic.addInterface("2610:1e0:1700:200::*");
        ic.addInterface("2610:1e0:1700:201::*");
        nc.setInterfaces(ic);

        //nc.setPublicAddress("10.22.2.164");
        nc.setPort(2434);
        //nc.setPublicAddress()
        //join config
        JoinConfig jc = new JoinConfig();
        //mutlicast config
        MulticastConfig mc = new MulticastConfig();
        mc.setEnabled(false);
        mc.setMulticastGroup("235.1.1.1");
        mc.setMulticastPort(2434);
        jc.setMulticastConfig(mc);
        //tcp config
        TcpIpConfig tcpc = new TcpIpConfig();
        tcpc.setEnabled(true);
        //tcpc.addMember("10.20.22.130:2434");
        jc.setTcpIpConfig(tcpc);

        nc.setJoin(jc);
        hc.setNetworkConfig(nc);
        Map<String,ExecutorConfig> ehm = new HashMap<>();
        ExecutorConfig ex = new ExecutorConfig();
        ex.setPoolSize(16);
        ehm.put("executor-service",ex);
        hc.setExecutorConfigs(ehm);
        instance = Hazelcast.newHazelcastInstance(hc);
    /*
        <tcp-ip enabled="true">
        <member>europe0:2434</member>
        <member>europe1:2434</member>
        <member>usa0:2434</member>
        <member>asia0:2434</member>
        <member>192.168.1.0-7:2434</member>
        </tcp-ip>

Add <properties><property name="hazelcast.local.localAddress">IPV4ADDRESSHERE</property></properties> into hazelcast.xml
Add <network><public-address>IPV4ADDRESSHERE</public-address></network>
  */


        ODocument sd = new ODocument();

        //instance.con
        //instance.getConfig().setGroupConfig()

        //look here
        //https://github.com/p14n/StorageMod/tree/master/src/main/java/jhc/data/orient
        //HazelcastInstance instance = ... //create the HazelcastInstance programmatically according to the hazelcast documentation
        return instance;
    }


    /*
    protected HazelcastInstance configureHazelcast() throws FileNotFoundException {
    FileSystemXmlConfig config = new FileSystemXmlConfig(hazelcastConfigFile);
    config.setClassLoader(this.getClass().getClassLoader());
    return Hazelcast.newHazelcastInstance(config);
  }
     */

    public static void addMember(String member) {

        instance.getConfig().getNetworkConfig().getJoin().getTcpIpConfig().addMember(member);
        instance = Hazelcast.newHazelcastInstance(hc);
    }

    private Collection<String> getAddressList() {
        Collection<String> addressList = null;
        try {
            addressList = new ArrayList<>();
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();

                System.out.println("Interface Name: " + networkInterface.getDisplayName());
                //if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                //if (networkInterface.getDisplayName().startsWith("veth") || networkInterface.isLoopback() || !networkInterface.isUp() || !networkInterface.supportsMulticast() || networkInterface.isPointToPoint() || networkInterface.isVirtual()) {
                //if (networkInterface.getDisplayName().startsWith("veth") || networkInterface.isLoopback() || !networkInterface.isUp() || !networkInterface.supportsMulticast() || networkInterface.isPointToPoint() || networkInterface.isVirtual()) {
                if (networkInterface.getDisplayName().startsWith("veth") || networkInterface.isLoopback() || !networkInterface.supportsMulticast() || networkInterface.isPointToPoint() || networkInterface.isVirtual()) {
                    continue; // Don't want to broadcast to the loopback interface
                }

                    for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                            //if((interfaceAddress.getAddress() instanceof Inet6Address) && !interfaceAddress.getAddress().isLinkLocalAddress())
                            InetAddress inAddr = interfaceAddress.getAddress();
                            boolean isGlobal = !inAddr.isSiteLocalAddress() && !inAddr.isLinkLocalAddress();

                                if ((inAddr instanceof Inet6Address) && isGlobal) {
                                //if (inAddr instanceof Inet6Address) {
                                    String[] ipv6addr = inAddr.getHostAddress().toString().split("%");
                                    System.out.println("Added IPv6 Address: " + ipv6addr[0]);
                                    addressList.add(ipv6addr[0]);
                                }
                                else if (inAddr instanceof Inet4Address) {
                                    System.out.println("Added IPv4 Address: " + interfaceAddress.getAddress().getHostAddress());
                                    addressList.add(inAddr.getHostAddress().toString());
                                }
                            }
                        }
                    }


        catch(Exception ex)
        {
            addressList = null;
            System.out.println("getAddressList Error : " + ex.getMessage());
        }
        return addressList;
    }

}