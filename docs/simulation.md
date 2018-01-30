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
