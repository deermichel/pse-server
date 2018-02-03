import RPi.GPIO as GPIO
import json
import requests
import time

# settings
photo_port = 13
endpoint_url = "http://mh-macbook:8080/sensor/photo1"

# send post request
def send_post_request(state):
    data = json.dumps({ "state": state })
    try:
        requests.post(endpoint_url, data)
    except requests.ConnectionError:
        print "ERROR: Endpoint '" + endpoint_url + "' cannot be reached."

# photo state change callback
last_state = -1
def photo_onchange(channel):
    global last_state
    new_state = GPIO.input(photo_port)
    if (channel == photo_port) and (new_state != last_state):
        last_state = new_state
        send_post_request(new_state)

# setup GPIO
GPIO.setmode(GPIO.BOARD)
GPIO.setup(photo_port, GPIO.IN)

# register callback
GPIO.add_event_detect(photo_port, GPIO.BOTH, callback=photo_onchange)

# do nothing
try:
    while True:
        time.sleep(1)
except KeyboardInterrupt:
    GPIO.cleanup()
