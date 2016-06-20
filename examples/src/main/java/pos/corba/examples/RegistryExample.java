package pos.corba.examples;

import pos.MessageImpl;
import pos.corba.registry.ORBRegistry;
import pos.corba.registry.Registry;
import pos.corba.template.addressconfig.Address;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 19/06/2016, 19:22:48
 */
public class RegistryExample {

    public static void main(String[] args) throws Exception {
        Address config = Address.create("localhost", "1050");
        MessageImpl message = new MessageImpl();
        Registry registry = ORBRegistry.create(config);
        registry.bind("Message", message);
        registry.active();
    }
}
