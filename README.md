# CorbaTemplateMethod
simple corba abstraction to simplify the  client and server coding on java

**This abstraction follows the Template Method design pattern**

So, you must implement your own concrete classes for the client and the server.

**CORBA development Overview**

1- Create an .idl file
2- Compile it with java's idlj 
3- Use the generated codes as dependencies of your client/server codes
4- Implement the client/server abstract methods
