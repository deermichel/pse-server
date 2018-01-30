Sensor Configuration
=====

In order to address and use a sensor in Vispar, you need to create a sensor
configuration file with ending `.conf` in the sensor configuration path (default:
`./sensors`). The file contains a json-object with the following attributes:

| Attribute              | Description
| -----------------------| ------------
| name                   | Name of the sensor (only a-z, 0-9, A-Z)
| description            | Description of the sensor
| endpoint               | Endpoint where the sensor should send its data (only a-z, 0-9, A-Z)
| timestamp (optional)   | Use timestamp supplied by sensor (value is the key where timestamp is found; has to be a long of millis since 1970)
| attributes             | Attributes (names and their types) supplied by the sensor

The "attributes" field is an object itself where each nested object has the following
attributes:

| Attribute              | Description
| -----------------------| ------------
| "object name"          | Key where the attribute is found
| name                   | Name of the attribute (how it should be referenced, only a-z, 0-9, A-Z)
| type                   | Type of the attribute (only INTEGER, DOUBLE or STRING)

Example - Temperature sensor (temp1.conf)
-----

Here is an example configuration file for a basic temperature sensor:

```
{
    "name": "temp1",                                    // sensor is referenced as "temp1" in Vispar
    "description": "Temperature sensor by Temptec",     // free text
    "endpoint": "temp1",                                // sensor sends its data to http://<serverIp>/sensor/temp1
    "attributes": {
        "value": {                                      // get attribute from "value" field in sensor data
            "name": "value",                            // attribute is referenced as "value" in Vispar
            "type": "INTEGER"                           // attribute is an integer
        },
        "other.room": {                                 // get attribute from "room" field in "other" object in sensor data
            "name": "room",                             // attribute is referenced as "room" in Vispar
            "type": "STRING"                            // attribute is a string
        }
    }
}
```

_Comments (//) are not valid in JSON and only added for clarification._

The corresponding JSON data of the sensor could be:

```
{
    "value": "23",
    "humidity": "0.45324",
    "other": {
        "room": "Kitchen",
        "version": "2.1.0"
    }
}
```

_Attributes not referenced in the sensor configuration will be ignored._

Example - Water sensor (water.conf)
-----

Here is an example configuration file for a basic water level sensor:

```
{
    "name": "water",                                    // sensor is referenced as "water" in Vispar
    "description": "Water sensor in outdoor pond",      // free text
    "endpoint": "water",                                // sensor sends its data to http://<serverIp>/sensor/water
    "timestamp": "metadata.time",                       // use sensor timestamp provided in "time" field in "metadata" object in sensor data
    "attributes": {
        "fillLevel[0]": {                               // get attribute from index 0 of "fillLevel" array in sensor data
            "name": "minFillLevel",                     // attribute is referenced as "minFillLevel" in Vispar
            "type": "DOUBLE"                            // attribute is a double
        },
        "fillLevel[1]": {                               // get attribute from index 1 of "fillLevel" array in sensor data
            "name": "maxFillLevel",                     // attribute is referenced as "maxFillLevel" in Vispar
            "type": "DOUBLE"                            // attribute is a double
        }
    }
}
```

_Comments (//) are not valid in JSON and only added for clarification._

The corresponding JSON data of the sensor could be:

```
{
    "fillLevel": [ 1.2347, 1.4927 ],
    "metadata": {
        "time": "1517338221934"
    }
}
```
