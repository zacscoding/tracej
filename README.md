# Trace ur java application :)  

this agent help u debug ur java application without modifying source code.

## Trace ur call stack with param, return value, exception, elapsed!  

> example of result   

```aidl
>> Trace method call stack
+--examples/boot/rest/PersonController::getPerson(Ljava/lang/Long;)Lorg/springframework/http/ResponseEntity;[112 ms] : <200 OK OK,Person(id=1, name=Hiva1, age=15, hobbies=[book]),{}>
 -- 1 : 1
| +--examples/boot/service/PersonService::getPersonById(Ljava/lang/Long;)Ljava/util/Optional;[101 ms] : Optional[Person(id=1, name=Hiva1, age=15, hobbies=[book])]
|  -- 1 : 1
============================================================================================
```  

## Getting started  

> build tracej agent

```aidl
$ mvn --projects=tracej-agent clean package -Dmaven.test.skip=true
```  

> settings agent config  

```yaml
###########################################################################
# Configuration of proxy
#
# Suppose that name is "com/demo" and filter-type is "startsWith",
# invoker is false in "proxy.classes" section.
#
# Then will check whether {loaded classes name}.startsWith("com/demo")
# is true or false.
#
# example1) loaded class name = "com/demo2/MyClass"
#   => "com/demo2/MyClass".startsWith("com/demo") == true
#   => will trace i.e change class file.
#
# example2) loaded class name = "net/demo/YourClass"
#   => "net/demo/YourClass".startsWith("com/demo") == false
#   => skip to trace.
###########################################################################
proxy:
  classes:
    - name: com/demo
      # ["equals", "startsWith", "endsWith", "contains", "regex"]
      filtertype: equals
      # this key is valid if type is "regex"
      pattern:
      invoker: true
      methods:
        - name: "*"
          # ["equals", "starts_with", "ends_with", "contains", "regex", "all"]
          filtertype:
          pattern:
          invoker: true

###########################################################################
# Configuration of logs
###########################################################################
log:
  # dump modified classes
  dump:
    enable: true
    path: /home/zaccoding/tracej-dump
  console:
    enable: true
  file:
    enable: false
    path: /home/zaccoding/trace-demo.log
```  

> start ur application with tracej-agent  


- Nomal use  

```
$ java -javaagent:/path/to/tracej-agent.jar -Dtracej.config.path=/path/config.yaml -jar yourapp.jar
```

- Tomcat (bin/setenv.sh)  

```
export CATALINA_OPTS="$CATALINA_OPTS -javaagent:/path/to/tracej-agent.jar"
export CATALINA_OPTS="$CATALINA_OPTS -Dtracej.config.path=/path/config.yaml"
```

- Tomcat (bin/setenv.bat)  

```
set CATALINA_OPTS=%CATALINA_OPTS% -javaagent:/path/to/tracej-agent.jar"
set CATALINA_OPTS=%CATALINA_OPTS% -Dtracej.config.path=/path/config.yaml"
```  

---  

## Changed ur source code during runtime  

> Origin source code

```java
public class PersonService {
    ...

    public Optional<Person> getPersonById(Long id) {
        requireNonNull(id, "id");
        return personRepository.findOneById(id);
    }

    ...
}
```  

> Modified source code(decompiled from class file)  

```java
public class PersonService {
    ...

    public Optional<Person> getPersonById(Long id) {
        TransactionStack.pushTransaction("examples/boot/service/PersonService::getPersonById(Ljava/lang/Long;)Ljava/util/Optional;");
        TransactionStack.appendParam(id);

        try {
            Objects.requireNonNull(id, "id");
            Optional var10000 = this.personRepository.findOneById(id);
            TransactionStack.appendReturnValue(var10000);
            TransactionStack.popTransaction();
            return var10000;
        } catch (Throwable var3) {
            TransactionStack.appendException(var3);
            TransactionStack.popTransaction();
            throw var3;
        }
    }
    ...
}
```

