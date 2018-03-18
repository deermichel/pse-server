import Adafruit_DHT
import json
import requests
import time

# settings
sensor_port = 4 # bcm numbering!
endpoint_url = "http://mh-macbook:8080/sensor/dht11"

# send post request
def send_post_request(temperature, humidity):
    data = json.dumps({ "temperature": temperature, "humidity": humidity })
    try:
        requests.post(endpoint_url, data)
    except requests.ConnectionError:
        print "ERROR: Endpoint '" + endpoint_url + "' cannot be reached."

# sensor value change callback
last_state = (-1.0, -1.0)
def sensor_onchange(temperature, humidity):
    global last_state
    new_state = (temperature, humidity)
    if new_state != last_state:
        last_state = new_state
        send_post_request(*new_state)

# do constant polling
try:
    while True:
        humidity, temperature = Adafruit_DHT.read_retry(11, sensor_port)
        if humidity is not None and temperature is not None:
            sensor_onchange(temperature, humidity)
        else:
            print "ERROR: Failed to read sensor."
        time.sleep(1)
except KeyboardInterrupt:
    pass
