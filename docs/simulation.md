Simulations
=====

To simulate events on the Vispar server, you define these events in a
simulation file with ending `.sim`. To start the simulation you use the command
`simulate <path/to/file.sim>`. The file consists of a json-array with each
simulated event being represented by a json-object with the following attributes:

| Attribute              | Description
| -----------------------| ------------
| time                   | When event should be emitted after simulation has been started (in millis)
| sensor                 | Name of the sensor which should be simulated
| repeat (optional)      | Simulate event repeatedly ("count") with a delay ("interval" in millis) between each
| data                   | Attributes values used in simulation (attributes have to match sensor configuration)

The "data" field is an object itself where each nested object has the following form:
```
// simulate a fixed value.
// value type has to match format expected from sensor (integer, string or double).
"<attributeName>": { "fixed": "<value>" }

OR

// simulate an integer value between "min" and "max" (both inclusive).
// min and max are both integers.
"<attributeName>": { "range": [ "min", "max" ] }

OR

// simulate a double value between "min" (incl) and "max" (excl).
// min and max are both doubles.
"<attributeName>": { "drange": [ "min", "max" ] }

OR

// simulate a random value of given options.
// option values have to match format expected from sensor (integer, string or double).
"<attributeName>": { "random": [ "opt1", "opt2", "opt3", ... ] }
```

Example - Temperature sensor simulation (temp.sim)
-----

Here is an example simulation file for the basic temperature sensor defined in
the sensor configuration documentation (temp1). We also have configured an exact
duplicate of the sensor referenced as temp2:

```
[
    {
        "time": "0",                                // simulate event right after simulation has started
        "sensor": "temp1",                          // event of sensor "temp1" will be simulated
        "data": {
            "value": { "fixed": "23" },             // attribute "value" will have fixed (integer) value 23
            "room": { "fixed": "Kitchen" }          // attribute "room" will have fixed (string) value Kitchen
        }
    },
    {
        "time": "1000",                             // simulate event 1000ms after simulation has started
        "sensor": "temp2",                          // event of sensor "temp2" will be simulated
        "data": {
            "value": { "fixed": "21" },             // attribute "value" will have fixed (integer) value 21
            "room": { "fixed": "Floor" }            // attribute "room" will have fixed (string) value Floor
        }
    },
    {
        "time": "2000",                                     // simulate event 2000ms after simulation has started
        "sensor": "temp1",                                  // event of sensor "temp1" will be simulated
        "repeat": { "count": "4", "interval": "500" },      // repeat event 4 times (total: 5) with a pause of each 500ms
        "data": {
            "value": { "range": [ "17", "24" ] },           // attribute "value" will have an integer value between 17 and 24
            "room": { "random": [ "Kitchen", "WC" ] }       // attribute "room" will have the string value Kitchen or WC
        }
    }
]
```

_Comments (//) are not valid in JSON and only added for clarification._

Example - Water sensor simulation (water.sim)
-----

Here is an example simulation file for the basic water sensor defined in
the sensor configuration documentation (water):

```
[
    {
        "time": "500",                                                      // simulate event 500ms after simulation has started
        "sensor": "water",                                                  // event of sensor "water" will be simulated
        "data": {
            "minFillLevel": { "fixed": "4.235" },                           // attribute "minFillLevel" will have fixed (double) value 4.235
            "maxFillLevel": { "drange": [ "4.5", "5.0" ] }                  // attribute "maxFillLevel" will have a double value between 4.5 and 5.0
        }
    },
    {
        "time": "500",                                                      // simulate event simultaneously to other event
        "sensor": "water",                                                  // event of sensor "water" will be simulated
        "data": {
            "minFillLevel": { "fixed": "4.235" },                           // attribute "minFillLevel" will have fixed (double) value 4.235
            "maxFillLevel": { "random": [ "4.3", "4.531", "4.24" ] }        // attribute "maxFillLevel" will have the double value 4.3, 4.531 or 4.24
        }
    }
]
```

_Comments (//) are not valid in JSON and only added for clarification._
