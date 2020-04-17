# Quick overview


This project contains two simple rules - AG1.drl and AG2.drl with each one mapped to an agenda groups AG1 and AG2 respectively. Take a look at these rules to see how they have been defined with `agenda-group` metadata and the conditions for which they would get executed. 

Agenda group helps to classify rules into groups that are to be executed whenever the focus gets set to an agenda group.  To understand this, deploy this project into Decision Central and invoke the REST API `{base_url}/server/containers/instances/{containerId}` as prescribed below. 

The API payload accepts one or more run-time commands to trigger the execution of rules and you can specify the required set of commands to be executed. For instance, in the payload given below, I've specified the commands:

 - insert - to insert the data-object required for rule evaluation
 - set-focus - to set the focus on the required agenda group so that rules that are qualified with in the agenda group will be executed 
 - fire-all-rules - to be qualified rules
 - get-objects - to specify the objects that are to be returned back

```
{
  "lookup": "session",
  "commands": [
    {
      "insert": {
        "disconnected": false,
        "out-identifier": "input",
        "return-object": true,
        "object": {
          "com.bala.rf_test.Applicant": {
            "age": 20,
            "existingCustomer": true,
            "annualIncome": 200000,
            "mortgageAmount": 500000,
            "accountBalance": 100000,
            "bankruptcy": false,
            "numberOfDefaultPaymentsLast12Months": 5,
            "yearsWithBank": 10,
            "totalCreditScore": 0
          }
        }
      }
    },
    {
      "set-focus": {
        "name": "AG2"
      }
    },
    {
      "set-focus": {
        "name": "AG1"
      }
    },
    {
      "fire-all-rules": {}
    },
    {
      "get-objects": {
        "out-identifier": "out"
      }
    }
  ]
}
```

On executing the REST API with the aforementioned payload, you'll see that rules that belong to agenda group AG1 will get executed first followed by the ones that belong to AG2. In the server console/log, you'll notice the following output

```
17:26:11,476 INFO  [stdout] (default task-36) Rule fired: Rule that belongs to Agend Group - AG1
17:26:11,476 INFO  [stdout] (default task-36) Rule fired: Rule that belongs to Agend Group - AG2
```

 ***Note:*** Agenda groups work like a stack, so, look carefully at the set-focus ordering below.

