package pos.corba.examples;

import ifpb.locator.ServerContext;
import ifpb.locator.context.IIOPContext; 
import java.util.logging.Level;
import java.util.logging.Logger;
import pos.Message;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 19/06/2016, 01:25:12
 */
public class ClientExample {

    public static void main(String[] args) {
        try {
            ServerContext contexto = new ServerContext(new IIOPContext());
            System.out.println("Name: "+contexto.bean("Message").name());
            Message message = contexto.bean("Message").locate();         
            System.out.println("Message : " +message.hello());
        } catch (Exception ex) {
            Logger.getLogger(ClientExample.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
