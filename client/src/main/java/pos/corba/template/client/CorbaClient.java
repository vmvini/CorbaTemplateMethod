/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos.corba.template.client;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
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
    
    protected abstract String getResourceName();
    
    protected abstract void doBusinessLogic(T remoteObject);
    
    public void doCorbaThings() throws InvalidName, NotFound, CannotProceed, org.omg.CORBA.ORBPackage.InvalidName, NoSuchMethodException, ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
        Address address = new Address();
        ORB orb = ORB.init(address.getConnectionArgs(), System.getProperties());
        org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
        NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
        String name = getResourceName();
        T remoteObject = getRemoteObject(ncRef, name);
        doBusinessLogic(remoteObject);
    }
 
    private T getRemoteObject(NamingContextExt ncRef, String resourceName) throws NotFound, CannotProceed, InvalidName, NoSuchMethodException, ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
        java.lang.Object o = invokeNarrow(getGenericTypeClassName() + "Helper", ncRef.resolve_str(resourceName));
        return (T)o;
    }
    
    private  Class<T> getClassByGenericType(){
       return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
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
