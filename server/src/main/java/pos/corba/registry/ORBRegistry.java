package pos.corba.registry;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.Servant;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 19/06/2016, 17:14:04
 */
public final class ORBRegistry implements Registry<org.omg.CORBA.Object> {

    private NamingContextExt context;
    private ORB orb;
    private POA rootPOA;

    public static Registry create() {
        Properties propeties = new Properties();
        String[] a = new String[]{"-ORBInitialPort", "1050", " -ORBInitialHost", " localhost"};
        return new ORBRegistry(a, propeties);
    }

    private ORBRegistry(String[] a, Properties propeties) {
        try {
            orb = ORB.init(a, propeties);
            rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            org.omg.CORBA.Object namespace = orb.resolve_initial_references("NameService");
            context = NamingContextExtHelper.narrow(namespace);
        } catch (Exception ex) {
            Logger.getLogger(ORBRegistry.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //TODO: Usar Reflection.. 
    
    @Override
    public void bind(String name, Servant obj) {
        try {
            org.omg.CORBA.Object ref = rootPOA.servant_to_reference(obj);
            NameComponent path[] = context.to_name(name);
//            Talvez o private R getRemoteObject de CorbaServer
//            Hello referenceRemote = HelloHelper.narrow(ref);
//            context.rebind(path, referenceRemote);
        } catch (Exception ex) {
            Logger.getLogger(ORBRegistry.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void unbind(String name) {
        try {
            NameComponent path[] = context.to_name(name);
            context.unbind(path);
        } catch (Exception ex) {
            Logger.getLogger(ORBRegistry.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String reference(Object object) {
        return orb.object_to_string(object);
    }

    @Override
    public void active() {
        try {
            rootPOA.the_POAManager().activate();
            orb.run();
            System.out.println("POA activate e ORB run");
        } catch (Exception ex) {
            Logger.getLogger(ORBRegistry.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void close() {
        try {
            orb.shutdown(false);
            context.destroy();
        } catch (Exception ex) {
            Logger.getLogger(ORBRegistry.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}