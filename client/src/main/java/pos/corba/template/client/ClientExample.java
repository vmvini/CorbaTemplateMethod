package pos.corba.template.client;

//import hello.Hello;
import ifpb.locator.ServerContext;
import ifpb.locator.context.IIOPContext; 
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 19/06/2016, 01:25:12
 */
public class ClientExample {

    public static void main(String[] args) {
        try {
            Properties p = new Properties();
            p.put("ORBInitialPort", "1050");
            p.put("ORBInitialHost", "localhost");
            p.put(Context.INITIAL_CONTEXT_FACTORY,
                    "com.sun.jndi.cosnaming.CNCtxFactory");

            p.put("java.naming.provider.url", "iiop://localhost:1050");
            Context context = new InitialContext(p);
            
            
//            Hello lookup = (Hello) context.lookup("Hello");
//            System.out.println("\tHello: " +lookup.sayHello());
//            
//            ServerContext contexto = new ServerContext(new IIOPContext());
//            
//            System.out.println("Name: "+contexto.bean("Hello").name());
//            Hello locate = contexto.bean("Hello").locate();
//           
//            System.out.println("\tHello: " +locate.sayHello());
        } catch (Exception ex) {
            Logger.getLogger(ClientExample.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
