package pos.corba.registry;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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

import java.util.Set;
import java.util.Optional;
import pos.corba.template.addressconfig.Address;
import pos.reflection.ReflectionUtil;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 19/06/2016, 17:14:04
 */
public final class ORBRegistry implements Registry<org.omg.CORBA.Object> {

    private NamingContextExt context;
    private ORB orb;
    private POA rootPOA;

    public static Registry create(Address config) {
        return new ORBRegistry(config.getConnectionArgs(), System.getProperties());
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

    @Override
    public void bind(String name, Servant obj) {
        try {
            org.omg.CORBA.Object ref = rootPOA.servant_to_reference(obj);
            NameComponent path[] = context.to_name(name);
//          pegando superclasse de obj 
//          supondo que obj é um objeto remoto que estende um *POA que estende um Servant
            Class<?> objPOA = obj.getClass().getSuperclass();
//          pegando pacote do projeto gerado pelo IDLJ            
            Package idlPackage = objPOA.getPackage();

//          pegando classes desse pacote            
            Set<Class<? extends java.lang.Object>> idlClasses = ReflectionUtil.getClassesUnderPackage(idlPackage.getName());
//          pegando a classe helper somente
            Optional o = idlClasses.stream().filter(c -> c.toString().endsWith("Helper"))
                    .findFirst();
            String helperName = o.get().toString().split(" ")[1].trim();

            java.lang.Object referenceRemote = getRemoteObject(helperName, ref);
            context.rebind(path, (org.omg.CORBA.Object) referenceRemote);

        } catch (Exception ex) {
            Logger.getLogger(ORBRegistry.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    @Override
    public void bind2(String name, Servant obj) {
        try {
            org.omg.CORBA.Object ref = rootPOA.servant_to_reference(obj);
            NameComponent path[] = context.to_name(name);

            Object referenceRemote = invoke_this(obj);
            context.rebind(path, referenceRemote);

        } catch (Exception ex) {
            Logger.getLogger(ORBRegistry.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private java.lang.Object getRemoteObject(String helperClass, org.omg.CORBA.Object ref) throws NoSuchMethodException, ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        java.lang.Object o = invokeNarrow(helperClass, ref);
        return o;
    }

    private java.lang.Object invokeNarrow(String helperClass, org.omg.CORBA.Object o) throws NoSuchMethodException, ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Method narrow = getMethod(helperClass, "narrow", org.omg.CORBA.Object.class);
        java.lang.Object resp = narrow.invoke(null, o);
        return resp;
    }

    private Method getMethod(String className, String methodName, Class parameterType) throws ClassNotFoundException, NoSuchMethodException {
        Class myclass = Class.forName(className);
        Method method = myclass.getMethod(methodName, parameterType);
        return method;
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

    /**
     * The method *POA_this() call *Helper.narrow(org.​omg.​CORBA.Object)
     */
    private Object invoke_this(Servant obj) throws SecurityException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, IllegalArgumentException {
        return (Object) obj.getClass()
                .getMethod("_this", new Class[]{})
                .invoke(obj, new java.lang.Object[]{});
    }
}
