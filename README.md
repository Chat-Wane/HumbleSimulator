# HumbleSimulator #

As its name indicates, this project is only a humble and simple simulator. It
aims to make simulations of distributed systems. It encompasses the application
layer and the system layer.

The HumbleSimulator framework allows the developers to run their own schemes of
emits/receipts over a distributed network by defining both the date of
generating and receiving of an operation.

An example of automatic generation of emits/receipts events is available in
[SGGComplete](src/main/java/gdd/scenariogenerators/SGGComplete.java). This
generator requires the network graph and the number of messages emitted by each
process to generate the timeline of events.

## Principle ##

### Timeline ###

The user provides a timeline of events, specifying when and whom generates the
events. Two events are currently considered: the emits/received. However, the
developers can add their own events to specify a particular behaviour on them.

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

Each scenario at the system level can be visualized through two exporters.
*    An aggregating process of events data which accumulates the occurrences of events during the time of simulations.
*    A LaTeX/tikz export of the timeline where each peer is displayed with its emission linked to the date of receipt. It shows the broadcasting of operations across the network. However, this representation scales poorly in number of operations and peers.

## Experiments ##

The following table shows results obtained by running two configuration of the
[LSEQ application](https://github.com/Chat-Wane/LSEQ). The first configuration
uses a random strategy choice (LSEQ) while the other uses a hash strategy
choice (h-LSEQ). The measurements concern the average and maximum bit-length of
the digit part of identifiers. The scenario involves 10 users creating a 100
lines document by repetitively inserting elements at the end (10
operations/user). Each user produces its emit events at a frequency of 100
rounds (arbitrary unit). The latency parameter (time between the emission of
operation and its receipt by the other collaborators) is varying from 5 to 640
rounds.

<table border="1">

<tr> <th> </th> <th> latency </th> <th> 5 </th> <th> 10 </th> <th> 20 </th>
     <th> 40 </th> <th> 80 </th> <th> 160 </th> <th> 320 </th> <th> 640 </th> 
</tr>
<tr> <th rowspan="2"> h-LSEQ </th> <th> avg bit-length </th> <td> 39.8 </td>
     <td> 34.8 </td> <td> 33.3 </td> <td> 23.5 </td> <td> 15.5 </td> 
     <td> 8.9 </td> <td> 7.9 </td> <td> 8.0 </td>
</tr>

<tr> <th> max bit-length </th> <td> 46 </td> <td> 46 </td> <td> 46 </td>
     <td> 46 </td> <td> 36 </td> <td> 19 </td> <td> 19 </td> <td> 19 </td>
</tr>

<tr> <th rowspan="2"> LSEQ </th> <th> avg bit-length </th> <td> 142.2 </td>
     <td> 85.0 </td> <td> 74.9 </td> <td> 40.0 </td> <td> 26.9 </td>
     <td> 14.2 </td> <td> 11.9 </td> <td> 8.8 </td>
</tr>

<tr> <th> max bit-length </th> <td> 342 </td> <td> 201 </td> <td> 181 </td>
     <td> 82 </td> <td> 46 </td> <td> 27 </td> <td> 27 </td> <td> 19 </td>
</tr>


<caption><strong>Table 1</strong>. Experiments over LSEQ and h-LSEQ involving
10 users and variable latency. Each user generates 10 operations to create a
document of 100 lines.</caption>

 </table>
