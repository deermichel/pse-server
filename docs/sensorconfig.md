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
| timestamp (optional)   | Use timestamp supplied by sensor (value is the key where timestamp is found)
| attributes             | Attributes (names and their types) supplied by the sensor

The "attributes" field is an object itself where each nested object has the following
attributes:

| Attribute              | Description
| -----------------------| ------------
| "object name"          | Key where the attribute is found
| name                   | Name of the attribute (how it should be referenced, only a-z, 0-9, A-Z)
| type                   | Type of the attribute (only INTEGER, DOUBLE or STRING)

Example - Temperature sensor
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
