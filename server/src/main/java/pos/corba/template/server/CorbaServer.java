/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos.corba.template.server;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;
import org.omg.PortableServer.POAPackage.ServantNotActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;
import pos.corba.template.addressconfig.Address;
import java.lang.reflect.Constructor;
/**
 * 
 * @author vmvini
 * @param <R> This must be the remote object interface generated by your IDL
 * @param <T> This must be the POA abstract class implementation and must implement the RemoteObjectTemplate interface
 */
public abstract class CorbaServer<R, T extends RemoteObjectTemplate> {
    
    protected ORB orb;
    protected Address address = new Address();
    protected POA rootPOA;
    
    /**
     * You should define the remote object's name here
     * @return remote object's name
     */
    protected abstract String getResourceName();
    
    /**
     * if you want to do something when the remote object is deployed, implement this method
     * @param remoteObject 
     */
    protected abstract void afterDeploy(R remoteObject);
    
    /**
     * if you want to do something before the server shutdown, implement this method.
     */
    protected abstract void beforeShutdown();
    
    public void startServer() throws InvalidName, ServantNotActive, WrongPolicy, org.omg.CosNaming.NamingContextPackage.InvalidName, NotFound, CannotProceed, AdapterInactive, NoSuchMethodException, ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException{
        
        setORB();
        setPOA();
            
        T localObject = buildLocalObject();
        localObject.setORB(orb);
            
        org.omg.CORBA.Object ref = rootPOA.servant_to_reference( localObject.castThisToServant() );
        
        R href = getRemoteObject(ref);
            
        org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            
        NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
            
        String name = getResourceName();
            
        NameComponent path[] = ncRef.to_name(name);
            
        ncRef.rebind(path, (org.omg.CORBA.Object)href);
            
        rootPOA.the_POAManager().activate();
            
        afterDeploy(href);
            
        orb.run();
            
        beforeShutdown();
    }
    
    private  T buildLocalObject() throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
        Class<T> c = getConcreteClass();
        System.out.println(getConcreteClassName());
        Constructor constructor = c.getConstructor();
        Object localObject = constructor.newInstance();
        return (T)localObject;
    }
    
    private R getRemoteObject(org.omg.CORBA.Object ref) throws NoSuchMethodException, ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
        java.lang.Object o = invokeNarrow(getGenericTypeClassName()+"Helper", ref);
        return (R)o;
    }
    
    private void setORB(){
        orb = ORB.init(address.getConnectionArgs(), System.getProperties());
    }
    
    private void setPOA() throws InvalidName{
        rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
    }
    
    
    private Class<T> getConcreteClass(){
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
   
    }
    private String getConcreteClassName(){
        return getConcreteClass().getName();
    }
    
    private  Class<R> getClassByGenericType(){
       return (Class<R>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
    
    private String getGenericTypeClassName(){
        return getClassByGenericType().getName();
    }
    
    private Method getMethod(String className, String methodName, Class parameterType) throws ClassNotFoundException, NoSuchMethodException{
        Class myclass = Class.forName(className);
        Method method = myclass.getMethod(methodName, parameterType);
        return method;
    }
    
    private java.lang.Object invokeNarrow(String helperName, org.omg.CORBA.Object o ) throws NoSuchMethodException, ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
        Method narrow = getMethod(helperName, "narrow", org.omg.CORBA.Object.class);
        java.lang.Object resp = narrow.invoke(null, o);
        return resp;
    }
    
}
