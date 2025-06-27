# Galaxy Trucker

This project is a software implementation of the board game "Galaxy Trucker", developed for the Software Engineering Final Project course at Politecnico di Milano (Academic Year 2024-2025). It includes a client-server architecture, adhering to the game's rules and specified technical requirements.

## Group Components
- Stefano Ungaro (209901)
- Alessandro Sola (210105)
- Michele Tavecchio (210160)
- Alessandro Verrengia (212680)

## Advanced Features
The following *advanced features* have been implemented in this project:
- **Trial Flight**: The client of the first player can choose between a standard game and a "trial flight" game (pages 1-15 of the manual). The server then implements the two versions of the game rules with differentiated boards.
- **Multiple Games**: The server is designed to manage multiple games concurrently. This allows players to choose which open game to join or to create a new one.
- **Persistence**: The server periodically saves the game state to disk, allowing execution to resume from where it left off even after a server crash. To resume a game, players must reconnect to the server using the same nicknames once it is active again.  

## Launch Instructions
The project consists of a server-side JAR and two client-side JARs. Please ensure you have JavaSE installed on your system.

### Server Launch
The server manages multiple games. Please run the jar file and follow the instruction on screen to set up your network,

#### Windows/macOS/Linux
  ``` bash

java -jar server.jar

```

Note: a `server-no-fx.jar` is also available for environments without JavaFX support. This does not affect the server's functionality.

### Client Launch
Each client instance represents a player and can participate in only one game at a time. The client interface can be either textual (TUI) or graphical (GUI), and it can communicate with the server via Socket or RMI.


Please choose the corresponding TUI or GUI jar file.

#### Windows (PowerShell)
Before launching the client JARs, you will need to execute two PowerShell functions to enable proper font visualization (this is recommended for full TUI experience):

```bash
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8

chcp 65001

```

To launch with a **Graphical User Interface** (GUI):

```bash
java -jar client-gui-win.jar

```

To launch with a **Textual User Interface** (TUI):

```bash
java -jar client-tui.jar

```

#### macOS
To launch with a **Graphical User Interface** (GUI):

```bash
java -jar client-gui-macos.jar

```

To launch with a **Textual User Interface** (TUI):

```bash
java -jar client-tui.jar

```

#### Linux
To launch with a **Graphical User Interface** (GUI):

```bash
java -jar client-gui-linux.jar

```

To launch with a **Textual User Interface** (TUI):

```bash
java -jar client-tui.jar

```

## Server

You can connect remotely to our server to play Galaxy Trucker. As RMI support is limited by our Provider, only Socket connection type is available. 
Run the TUI or GUI JAR file and provide the following address when prompt:
- IP: 129.152.16.203
- Port: 25565

## Deliverables

You can find a UML Sequence Diagram for RMI and Socket connection in some peculiar parts of the game (login, lobby creation, place component). 
UML class diagrams are provided for client and server, please refer to `UML (CG20).pdf` for more details.
A network architecture documentation file can also be found for specific description of network classes and design patterns.

### Javadoc
Javadoc documentation is available for both client and server code. You can find it in the zip file in the deliverables section of this project.