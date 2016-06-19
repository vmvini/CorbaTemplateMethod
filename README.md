# CorbaTemplateMethod
simple corba abstraction to simplify the  client and server coding on java

**The CorbaServer class follows the Template Method design pattern**

So, you must implement your own concrete class for the server.

**Client's Dependencies**

The CorbaClient class is using the ServiceLocator API 

` git clone https://github.com/ricardojob/service-locator.git `



**CORBA development Overview**

1- Create an .idl file

2- Compile it with java's idlj 

3- Use the generated codes as dependencies of your client/server codes

4- Implement the server abstract methods

