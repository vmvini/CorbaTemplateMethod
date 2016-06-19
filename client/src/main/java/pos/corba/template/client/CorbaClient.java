/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos.corba.template.client;


import ifpb.locator.ServerContext;
import ifpb.locator.context.IIOPContext;
import ifpb.locator.named.App;
import ifpb.locator.named.Scoped;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 *
 * @author vmvini
 */
public class CorbaClient{
    
    
    /**
     * Use esse método para fazer lookup em um objeto remoto através de CORBA
     * @param <T> Tipo específico do objeto remoto que você quer recuperar
     * @param resourceName Nome usado para localizar o objeto remoto
     * @param remoteObjectClassType Classe do objeto remoto que você quer recuperar
     * @return
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException 
     */
    public static <T> T getRemoteObject( String resourceName, Class<T> remoteObjectClassType ) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
        
        //instanciando contexto passando o ConfigContext IIOPContext como argumento
        ServerContext contexto = new ServerContext(new IIOPContext(),  App.name(null).namespace(Scoped.EMPTY));
        org.omg.CORBA.Object obj =  contexto.bean(resourceName).locate();
        
        //Acessando classe *Helper até então desconhecida
        String helperName = remoteObjectClassType.getCanonicalName() + "Helper";
        Class myclass = Class.forName(helperName);
        
        //Chamando o metódo narrow da classe *Helper
        Method method = myclass.getMethod("narrow", org.omg.CORBA.Object.class);
        java.lang.Object remoteObj = method.invoke(null, obj);
        
        //retornando objeto remoto com o tipo específico
        return (T)remoteObj;
    }

}
