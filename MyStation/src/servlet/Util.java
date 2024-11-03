package servlet;

import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import remoteService.StationPersoRemote;

public class Util {
    
        static StationPersoRemote getUser() throws Exception {
            // Configuration pour accéder au EJB distant
            Properties props = new Properties();
            props.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
            props.put(Context.PROVIDER_URL, "http-remoting://localhost:9090");

            Context context = new InitialContext(props);
            StationPersoRemote u= (StationPersoRemote) context.lookup("ejb:/central/StationPersoBean!remoteService.StationPersoRemote");

            return u;
        }   
}
