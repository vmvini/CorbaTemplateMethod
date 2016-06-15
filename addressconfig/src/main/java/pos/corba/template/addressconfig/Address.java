/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos.corba.template.addressconfig;

/**
 *
 * @author vmvini
 */
public class Address {
    
    private String port;
    private String host;
    
    public Address(){
        port = "1050";
        host = "localhost";
    }
    
    public String[] getConnectionArgs(){
        String[] args = new String[4];
        args[0] = "-ORBInitialPort";
        args[1] = getPort();
        args[2] = "-ORBInitialHost";
        args[3] = getHost();
        
        return args;
    }
    
    public void setPort(String port){
        this.port = port;
    }
    
    public String getPort(){
        return "1050";
    }
    
    public void setHost(String host){
        this.host = host;
    }
    
    public String getHost(){
        return host;
    }
    
}
