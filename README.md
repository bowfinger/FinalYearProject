# SEViLO - Smart Elevator Vision Library Optimisation
This project was created as part of a final year project for the Computer Studies BSc degree course at Liverpool John Moores University.

There are three main modules:
- Algorithm
- Monitoring
- MQTT

The Algorithm module contains the server code to receive a RouteData object and then calculate the optimum route plan using a matrix of weighted costs.

The Monitoring module contains the embedded monitoring system code. This uses the OpenCV 3.0.0 library (included in the "libs" folder) to capture frames from a camera source and count the contours within a certain size range.

The MQTT module is the backbone communication code which both the above use to publish/subscribe. The Messenger class relies on environment variables for the CloudMQTT broker.
