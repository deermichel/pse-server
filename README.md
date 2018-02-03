Vispar Server - CEP-PSE Project WS17/18
=====

This is the server component of the Vispar software product. Vispar is a
full-featured complex event processing suite offering graphical editing of
complex patterns and subsequent deployment and recognition using the WSO2
Siddhi Engine. It was developed during the PSE project at the Karlsruhe
Institute of Technology in WS17/18.

Requirements
-----

* Linux or Mac OS (Windows might work - but is _NOT_ tested)
* Java 8
* MongoDB (running on localhost:27017 without authentication)
* Mail SMTP server (running on localhost:25 without authentication - only needed
    if you want to use email actions). Postfix is recommended for simple use cases.
* Firewall configured to allow connections via port 8080 and 8081.
* Sensor configuration files placed in folder `sensors`

Start server
-----

```
$ java [startup options] -jar path/to/vispar-server.jar

// startup options
-Drequestport=8080                      // set port for api requests and sensor endpoints
-Dsocketport=8081                       // set port for socket (used for socket actions)
-Ddatabase="localhost:27017"            // set url to MongoDB database
-Dconfigpath="sensors"                  // set path to sensor configuration files
-Dnoshell                               // disable interactive shell (to run as service)
```

Commands
-----

```
adduser <username> <password>           // add a new user
help                                    // show available commands
listpatterns                            // list all patterns and their deployment status
listusers                               // list all users
removeuser <username>                   // remove a user
simulate <path/to/file.sim>             // start simulation specified in given file
stop                                    // stop the server
```

Further Documentation
-----

* [Sensor Configuration](docs/sensorconfig.md)
* [Simulations](docs/simulation.md)
* [Installation on Raspberry Pi](docs/rpi-install.md)
* [API Specification](docs/api.md)


_&#169; 2017-2018 Mina Akram Zaher, Jonas Bühler, Florian Pfisterer,
Carlos Rosero, Micha Hanselmann, Nico Weidmann._
