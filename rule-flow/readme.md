# Quick overview


This project contains two simple in the file `rules some-rules.drl` with each one mapped to ruleflow groups `group1` and `group2`. Take a look at these rules to see how they have been defined with `ruleflow-group` metadata and the conditions for which they would get executed. 

Ruleflow group enables grouping of rules and allows BPMN models/ruleflow models to declaratively specify when rule groups are allowed to be fired when the control lands at specific step. To understand this, deploy this project into Decision Central and invoke the REST API `{base_url}/server/containers/instances/{containerId}` as prescribed below. 

The API payload accepts one or more run-time commands to trigger the execution of rules and you can specify the required set of commands to be executed. For instance, in the payload given below, I've specified the commands:

 - insert - to insert the data-object required for rule evaluation - which are two string objects; one with the value `ok` and the other one with the value `continue` inorder to pass through the branching condition defined in the BPMN model for the execution of ruleflow group `group2`
 - set-process - which BPMN/rule flow model to be executed
 - fire-all-rules - fire the qualified rules
 - get-objects - to specify the objects that are to be returned back

```
{
   "lookup":"session",
   "commands":[
      {
         "insert":{
            "out-identifier":"in",
            "object":{
               "java.lang.String":"ok"
            }
         }
      },
      {
         "insert":{
            "object":{
               "java.lang.String":"continue"
            }
         }
      },
      {
         "start-process":{
            "processId":"ruleflow.simple-ruleflow"
         }
      },
      {
         "fire-all-rules":""
      },
      {
         "get-objects":{
            "out-identifier":"objects"
         }
      }
   ]
}
```

On executing this API with the aforementioned payload, you'll see that rules that belong to ruleflow group `group1` will get executed first followed by the ones that belong to `group2`. In the server console/log, you'll notice the following output

```
17:45:29,341 INFO  [stdout] (default task-43) check 1 fired
17:45:29,342 INFO  [stdout] (default task-43) check 2 fired
```

Also, note that I've added an agenda event listener to the same project - class `SampleAgendaEventListener`. Decision Manager emits all sorts of events: when facts are inserted into the engine, when rules are matched, when rules are fired, etc.  EventListeners can be used to log and debug rule execution by listening to these events and build required event logging output based on the event data. For example, in the SampleAgendaEventListener, the event listener logs which rules have got fired by implementing its afterMatchFired method. The event listener could be hooked onto to the project by mapping the listener in the project's kmodule.xml. 

To do that from Decision Central, go to the project's `Settings` tab, choose `Kie bases` then select the `KIE sessions` and  specify the event listener like this for the chosen KIE session.

![Settings](https://github.com/bbalakriz/rule-flow__agend-agroup__event-listener/blob/master/images/1.png)

![Settings](https://github.com/bbalakriz/rule-flow__agend-agroup__event-listener/blob/master/images/2.png)


Alternatively, you can add the details to the project's `kmodule.xml` like this. 

```
<kmodule xmlns="http://www.drools.org/xsd/kmodule" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <kbase name="rules" default="false" eventProcessingMode="stream" equalsBehavior="identity">
    <ksession name="session" type="stateless" default="true" clockType="realtime">
      <listeners>
        <agendaEventListener type="com.myspace.ruleflow.SampleAgendaEventListener"/>
      </listeners>
    </ksession>
  </kbase>
</kmodule>
```

With this, you'll see in the server console/log the following output that shows the triggering of the implemnetation inside the Event listner's afterMatchFired method. 

```
17:45:29,341 INFO  [stdout] (default task-43) check 1 fired
17:45:29,341 INFO  [com.myspace.ruleflow.SampleAgendaEventListener] (default task-43) The rule that has got fired is:check 1
17:45:29,342 INFO  [stdout] (default task-43) check 2 fired
17:45:29,343 INFO  [com.myspace.ruleflow.SampleAgendaEventListener] (default task-43) The rule that has got fired is:check 2
```

You can also generalize the Eventlistener and put it into an external JAR so that it can be used across projects. The JAR can then be defined as a dependency to the project by adding it to the project's pom.xml. Also, the JAR needs to be place in the class path of the Kieserver - that is at the location {RHDM_JBOSS_HOME}/standalone/deployments/kie-server.war/WEB-INF/lib/ so that the class is available during runtime execution.




