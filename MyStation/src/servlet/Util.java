package servlet;

import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;

import user.UserEJBRemote;

public class Util {
    
        static UserEJBRemote getUser() throws Exception {
            // Configuration pour accéder au EJB distant
            Properties props = new Properties();
            props.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
            props.put(Context.PROVIDER_URL, "http-remoting://localhost:9090");

            Context context = new InitialContext(props);
            UserEJBRemote u= (UserEJBRemote) context.lookup("ejb:/central/UserEJBBean!user.UserEJB");
            u.testLogin("admin", "test", "", "");

            return u;
        }   
}
