import RPi.GPIO as GPIO
import json
import requests
import time

# settings
button_ports = [ 15, 13, 12, 11 ]
base_url = "http://nicopi.local:8080/sensor/button"

# send post request
def send_post_request(state, button):
    data = json.dumps({ "state": state })
    endpoint_url = base_url + str(button)
    try:
        requests.post(endpoint_url, data)
    except requests.ConnectionError:
        print "ERROR: Endpoint '" + endpoint_url + "' cannot be reached."

# button state change callback
def button_onchange(channel):
    for button, port in enumerate(button_ports):
        if channel == port:
            state = GPIO.input(port)
            if state == 1:
                send_post_request(state, button)

# setup GPIO
GPIO.setmode(GPIO.BOARD)
for port in button_ports:
    GPIO.setup(port, GPIO.IN, pull_up_down=GPIO.PUD_UP)

# register callback
for port in button_ports:
    GPIO.add_event_detect(port, GPIO.RISING, callback=button_onchange, bouncetime=200)

# do nothing
try:
    while True:
        time.sleep(1)
except KeyboardInterrupt:
    GPIO.cleanup()
