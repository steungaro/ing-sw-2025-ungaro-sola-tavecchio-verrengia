# Galaxy Trucker

This project is a software implementation of the board game "[Galaxy Trucker](https://www.craniocreations.it/prodotto/galaxy-trucker)", developed for the Software Engineering Final Project course at Politecnico di Milano (Academic Year 2024-2025). It includes a client-server architecture, adhering to the game's rules and specified technical requirements.

**Final grade: 30/30 cum Laude**

## Group Components
- [Stefano Ungaro](https://github.com/steungaro) (209901)
- [Alessandro Sola](https://github.com/aleo113) (210105)
- [Michele Tavecchio](https://github.com/Mic-Tave) (210160)
- [Alessandro Ferdinando Verrengia](https://github.com/AleVerri-03) (212680)

## Requirements
The project consists of a Java version of the board game **Galaxy Trucker**, made by **Cranio Creations**.

- [Project Requirements](assets/Requisiti%20e%20tabella%20di%20valutazione.pdf)
- [Rules of the Game (EN)](assets/galaxy-trucker-rules-en.pdf)
- [Rules of the Game (IT)](assets/galaxy-trucker-rules-it.pdf)

## Advanced Features
The following *advanced features* have been implemented in this project:
- **Trial Flight**: The client of the first player can choose between a normal game (corresponding to Level 2) and a "trial flight" game (pages 1-15 of the manual). The server then implements the two versions of the game rules with differentiated boards.
- **Multiple Games**: The server is designed to manage multiple games concurrently. This allows players to choose which open game to join or to create a new one.
- **Game Reconnection**: Players can reconnect to an ongoing game if they lose connection, provided they do so within a certain time limit. The server handles disconnections and reconnections, allowing players to continue their game seamlessly.

## Launch Instructions
The project consists of a server-side JAR and two client-side JARs. Please ensure you have JavaSE installed on your system. This project runs on **Java 24**, so make sure you have the correct version installed.

### Server Launch
The server manages multiple games. Please run the JAR file and follow the instruction on screen to set up your network. Note that only one server is needed for multiple clients to connect.

#### Windows/macOS/Linux
  ``` bash

java -jar server.jar

```

Note: a `server-no-fx.jar` is also available for environments without JavaFX support. This does not affect the server's functionality.

### Client Launch
Each client instance represents a player and can participate in only one game at a time. The client interface can be either textual (TUI) or graphical (GUI), and it can communicate with the server via Socket or RMI.

Only one client instance is needed per player, but multple instances can be run on the same machine for testing purposes.


Please choose the corresponding TUI or GUI jar file and follow the instructions below based on your operating system.

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
Note: Please note that the GUI client may not work properly on Intel-based Macs due to JavaFX compatibility issues. If you encounter problems, consider using the TUI client instead or contact us for assistance.

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

Run the TUI or GUI JAR file and provide the following address when prompted:
- **IP address**: 129.152.16.203
- **Port**: 25565

## Deliverables

You can find a [UML Sequence Diagram](deliverables/UML%20Sequence%20Diagrams%20(GC20).pdf) for RMI and Socket connection in some peculiar parts of the game (login, lobby creation, place component). 

[UML class diagrams](deliverables/UML%20(GC20).pdf) are provided for client and server, please refer to `UML (CG20).pdf` for more details.

A [network architecture documentation file](deliverables/Network%20Architecture%20Documentation%20(GC20).pdf) can also be found for specific description of network classes and design patterns.

### Javadoc
Javadoc documentation is available for both client and server code. You can find it in the [zip file](deliverables/apidocs.zip) in the deliverables section of this project.

## License [![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)
This project is licensed under the GPL-v3 License. See the [LICENSE](LICENSE) file for details.

## Copyright
Please note that the game Galaxy Trucker is a copyrighted work by Cranio Creations. This project is an educational implementation and does not intend to infringe on any copyrights. The game rules and assets are used for educational purposes only, and no commercial use is intended.