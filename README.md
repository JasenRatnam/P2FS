# P2FS-Project
COEN 366 Communication Networks and Protocols Project - Fall 2021

## Objective
Develop a Peer to Peer File System
To allow for users (clients/peers) to share files (Text files)

## Description
The project consists of implementing in Java a Peer to Peer File System(P2FS), over UDP and TCP.
P2FS consists of several clients and one server

### Server
- Communicates to Clients through UDP
- Register and Deregister Clients
- Keep track of the registered Clients.
- Keep track of how the Clients can be reached.
- Keep track of the list of files available from each Client. 
- Responds and inform the Clients about each other upon requests.
- Always available at a fixed UDP socket and listening for incoming messages.
- Information stored in the server is persistent

### Clients
- Communicates to other Clients through TCP for file transfer.
- Communicates to Server through UDP to get information about other clients.
- Information:
   - Name
   - IP Address
   - UDP Socket Number
   - TCP Socket Number

## Core Features
1. Registration and De-registration
2. Publishing file related information
3. Retrieving information from the server
4. File transfer between clients
5. Clients updating their contact information (mobility)
6. Using a log file and printing directly into the screen, the communications between entities is reported

## Tools and Technologies
- Languages:
  - Java
- Technology:
  - IntelliJ
  - TCP
  - UDP
  - Command Prompt
  
## Team Members
- Jasen Ratnam			          
  - 40094237
- Bahnan Danho			        
  - 40044767
- Farhan-Nabil Alamgir
  - 40045344
