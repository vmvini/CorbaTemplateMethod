package pos.corba.registry;

import org.omg.PortableServer.Servant;

/**
 *
 * @author Ricardo Job
 */
interface Registry<T> {

    //TODO?: public <T> T lookup(String name);

    public void bind(String name, Servant obj);

    public void unbind(String name);

   //TODO?: public String[] list();
    
    //Uma string para representar a referencia
    public String reference(T object);
    
    public void active(); 
    public void close();
}
