Installation on Raspberry Pi
=====

This document will guide you through the installation process of the Vispar
server on a Raspberry Pi 2 or 3. It was proven to work successfully on
3rd February 2018 with a Raspberry Pi 3 Model B and Raspbian Stretch Lite.

1. Install Raspbian on the Raspberry Pi and setup the Pi's network connection. You
    need access to the console of the Pi to proceed (e.g. via ssh).

2. Update the system:
```bash
$ sudo apt update && sudo apt upgrade
```

3. Install Java 8 (the headless variant also works):
```bash
$ sudo apt install openjdk-8-jre
```

4. Install MongoDB:
```bash
$ sudo apt install mongodb-server
```

5. Install Postfix (used as mail server for Email actions) and follow the installation instructions:
```bash
$ sudo apt install postfix
```
_Note: Depending on your mail recipients host, you need further configuration to
pass the spam filter (e.g. Gmail does not necessarily allow to receive mails
from the Pi)._

5. Copy the `server.jar` file to the Pi. For this guide, it will be placed in
    the home directory (`~/server.jar`). Navigate to the folder where you placed
    the file (`cd ~` in this case).

6. Create the `sensors` folder for your sensor configuration files:
```bash
$ mkdir sensors
```

7. Start the Vispar server:
```bash
$ java -jar server.jar
```
