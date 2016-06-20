## An example of the CorbaTemplateMethod API

###Project development Overview

* find the .idl file *Message.idl*
`../examples/src/main/java`

* compile the idl file
`Idlj -fserver -fall Message.idl`

* code the class `MessageImpl` with implementation de `Message`
```java
public class MessageImpl extends MessagePOA {

    @Override
    public String hello() {
       return "Hello World";
    }
}
```
* create server-side object
```java
public class RegistryExample {

    public static void main(String[] args) throws Exception {
        Address config = Address.create("localhost", "1050");
        MessageImpl message = new MessageImpl();
        Registry registry = ORBRegistry.create(config);
        registry.bind("Message", message);
        registry.active();
    }
}
```
* create clien-side object using [Locator API](https://github.com/ricardojob/service-locator)
```java
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
```
* find the directory of compiled classes
`../examples/target/classes`

* start the *orbd*
`orbd -ORBInitialPort 1050 -ORBInitialHost localhost`

* run the `RegistryExample` and `ClientExample`