import com.hazelcast.core.HazelcastInstance;
import com.orientechnologies.orient.server.hazelcast.OHazelcastPlugin;
import java.io.FileNotFoundException;


public class ProgrammaticOHazelcastPlugin extends OHazelcastPlugin {

    //For 1.7rc2 and above
    @Override

    protected HazelcastInstance configureHazelcast() throws FileNotFoundException {

        HazelcastInstance instance = null;
        //HazelcastInstance instance = ... //create the HazelcastInstance programmatically according to the hazelcast documentation
        return instance;
    }

}