# HumbleSimulator #

As its name indicates, this project is only a humble and simple simulator. It
aims to make simulations of distributed systems. It encompasses the application
layer and the system layer.

The HumbleSimulator framework allows the developers to run their own schemas of
emits/receipts over a distributed network by defining both the date of
generating and receiving of an operation.

An example of automatic generation of emits/receipts events is available in
[SGGComplete](src/main/java/gdd/scenariogenerators/SGGComplete.java). This
generator requires the network graph and the number of messages emited by each
process to generate the timeline of events.

## Principle ##

### Timeline ###

The user provides a timeline of events, specifying when and whom generates the
events. Two events are currently considered: the emits/received. However, the
developpers can add their own events to specify a particular behaviour on them.

### Running scenario ###

Once the timeline of event is set, the application launch the scenario. An emit
event is processed in two steps. First the application level of the process
that must emit generate the operation and therefore create the payload of the
operation. Then, the application transmits the payload to the system layer that
adds the causality tracking mechanism and "broadcast" it through the
network. 

The underlying model of the HumbleSimulator relies on a central repository of
all operations. It adds an entry when an emits is performed and delete one when
all the peers have received the message.

Thus, the receive operations steps are: first the peer grabs the data from the
global operations repository, and verify if it is causally ready. If it is, it
delivers it to the application layer where it is integrated. Otherwise, the 
operation is queued in a buffer until it is ready.

### Export ###

<i>A more detailed description is upcoming...</i>