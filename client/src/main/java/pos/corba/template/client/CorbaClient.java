/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos.corba.template.client;


import org.omg.CosNaming.NamingContextExt;
import org.omg.CORBA.*;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;
import pos.corba.template.addressconfig.Address;


/**
 *
 * @author vmvini
 */
public abstract  class CorbaClient<T> {
    
    
    protected abstract void doBusinessLogic(T remoteObject);
    
    protected abstract T getRemoteObject(NamingContextExt ncRef, String resourceName) throws NotFound, CannotProceed, InvalidName;
    
    protected abstract String getResourceName();
    
    public void doCorbaThings() throws InvalidName, NotFound, CannotProceed, org.omg.CORBA.ORBPackage.InvalidName{
        Address address = new Address();
        ORB orb = ORB.init(address.getConnectionArgs(), System.getProperties());
        org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
        NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
        String name = getResourceName();
        T remoteObject = getRemoteObject(ncRef, name);
        doBusinessLogic(remoteObject);
    }
    
}
