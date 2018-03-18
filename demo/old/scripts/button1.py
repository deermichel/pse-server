import RPi.GPIO as GPIO
import json
import requests
import time

# settings
button_port = 11
endpoint_url = "http://mh-macbook:8080/sensor/button1"

# send post request
def send_post_request(state):
    data = json.dumps({ "state": state })
    try:
        requests.post(endpoint_url, data)
    except requests.ConnectionError:
        print "ERROR: Endpoint '" + endpoint_url + "' cannot be reached."

# button state change callback
def button_onchange(channel):
    if channel == button_port:
        send_post_request(GPIO.input(button_port))

# setup GPIO
GPIO.setmode(GPIO.BOARD)
GPIO.setup(button_port, GPIO.IN)

# register callback
GPIO.add_event_detect(button_port, GPIO.BOTH, callback=button_onchange)

# do nothing
try:
    while True:
        time.sleep(1)
except KeyboardInterrupt:
    GPIO.cleanup()
