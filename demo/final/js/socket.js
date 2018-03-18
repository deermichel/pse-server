const gpio = require("rpi-gpio")
const WebSocket = require("ws")
const connection = new WebSocket("ws://nicopi.local:8081")

// pins
const led_blue = 19
const led_green = 18
const buzzer = 16

// setup gpio
gpio.reset()
gpio.setup(led_blue, gpio.DIR_OUT)
gpio.setup(led_green, gpio.DIR_OUT)
gpio.setup(buzzer, gpio.DIR_OUT)

// message handler
connection.onmessage = (e) => {
    console.log("Received: " + e.data)
    if (e.data.startsWith("led.blue.")) {
        let duration = e.data.substring(9)
        gpio.write(led_blue, true)
        setTimeout(() => gpio.write(led_blue, false), duration)
    } else if (e.data.startsWith("led.green.")) {
        let duration = e.data.substring(10)
        gpio.write(led_green, true)
        setTimeout(() => gpio.write(led_green, false), duration)
    } else if (e.data.startsWith("buzzer.")) {
        let duration = e.data.substring(7)
        gpio.write(buzzer, true)
        setTimeout(() => gpio.write(buzzer, false), duration)
    }
}

// error
connection.onerror = (error) => {
    console.log(error)
}

// connected
connection.onopen = (error) => {
    console.log("Connected.")
}
