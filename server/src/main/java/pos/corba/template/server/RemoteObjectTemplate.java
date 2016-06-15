/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos.corba.template.server;

import org.omg.PortableServer.Servant;
import org.omg.CORBA.ORB;

/**
 *
 * @author vmvini
 *  
 * This interface ensures that your remote object's implementation have the ORB property.
 */
public interface RemoteObjectTemplate{
    
    void setORB(ORB orb);
    
    /**
     * You must implement 'return this' here, 
     * because your remote object should implement the *POA abstract class as well, which is a Servant. 
     * @return Servant
     */
    Servant castThisToServant();

}
