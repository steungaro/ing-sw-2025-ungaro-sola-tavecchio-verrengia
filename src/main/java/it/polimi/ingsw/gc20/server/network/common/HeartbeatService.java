package it.polimi.ingsw.gc20.server.network.common;

import it.polimi.ingsw.gc20.common.message_protocol.toclient.Ping;
import it.polimi.ingsw.gc20.server.network.NetworkService;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * The {@code HeartbeatService} class is a singleton service responsible for monitoring
 * the connectivity status of clients in a networked environment. The service periodically
 * sends ping messages to all connected clients and tracks their responses to determine
 * if they are still online.
 * <p>
 * The functionality provided by this service includes:
 * - Broadcasting ping messages to all connected clients at regular intervals.
 * - Updating the most recent ping response time for each client.
 * - Identifying and disconnecting clients that have not responded within a specified timeout period.
 * - Managing client connectivity status via collaboration with the {@code NetworkService}.
 */
public class HeartbeatService {
    private static final Logger LOGGER = Logger.getLogger(HeartbeatService.class.getName());
    private static final long PING_INTERVAL_MS = 30000; // 10 seconds
    private static final long TIMEOUT_MS = 90000; // 30 seconds
    private static final String BROADCAST = "__BROADCAST__";
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> scheduledFuture;
    private static HeartbeatService instance;
    private final Map<String, Long> clientPingTime = new HashMap<>();

    /**
     * Private constructor for the {@code HeartbeatService} class.
     * This constructor is intentionally made private to enforce the singleton pattern,
     * ensuring that only one instance of the service can exist.
     * The singleton instance can be accessed using the {@link #getInstance()} method.
     */
    private HeartbeatService() {
        // Private constructor for singleton
    }

    /**
     * Retrieves the singleton instance of the {@code HeartbeatService} class.
     * This method ensures that only one instance of the service is created
     * and shared across the application, following the singleton design pattern.
     * If the instance does not already exist, it initializes a new one.
     *
     * @return the singleton instance of {@code HeartbeatService}.
     */
    public static HeartbeatService getInstance() {
        if (instance == null) {
            instance = new HeartbeatService();
        }
        return instance;
    }

    /**
     * Starts the HeartbeatService by scheduling a periodic task to execute the
     * {@code service()} method at a fixed rate. The periodic task involves sending
     * ping requests to all connected clients and removing offline clients, ensuring
     * proactive monitoring and connection management.
     * <p>
     * The scheduling starts immediately and repeats every {@code PING_INTERVAL_MS} milliseconds.
     * Additionally, this method logs the initiation of the heartbeat service for
     * tracking purposes.
     */
    public void start() {
        scheduledFuture = scheduler.scheduleAtFixedRate(this::service, 0, PING_INTERVAL_MS, TimeUnit.MILLISECONDS);
        LOGGER.info("Heartbeat service started");
    }

    /**
     * Updates the last recorded ping time for a specific client.
     * This method is invoked when a ping response is received from a client,
     * and it updates the time at which the latest response was received.
     *
     * @param clientUsername the username of the client whose ping response is being handled
     *                        and whose last ping time is to be updated.
     */
    public void handlePingResponse(String clientUsername) {
        long currentTime = (System.currentTimeMillis());
        clientPingTime.put(clientUsername, currentTime);
    }

    /**
     * Executes the core logic of the heartbeat service, managing periodic tasks
     * essential for monitoring and maintaining client connections.
     * This method is implicitly invoked at regular intervals as part of the
     * heartbeat service's scheduled task to ensure reliable network communication
     * and client management.
     */
    private void service() {
        pingAllClients();
        removeOfflineClients();
    }

    /**
     * Sends a ping message to all connected clients in the network.
     * This method uses the {@code NetworkService} singleton to broadcast
     * a {@code Ping} message with a broadcast identifier to all clients.
     * It ensures that all clients are periodically notified to maintain
     * active connections or detect inactive clients.
     */
    private void pingAllClients() {
        NetworkService.getInstance().broadcastMessage(new Ping(BROADCAST));
    }

    /**
     * Stops the heartbeat service by shutting down the scheduler instance associated
     * with this service and logging the shutdown operation.
     * This method ensures that periodic operations and tasks managed by the
     * HeartbeatService are cleanly terminated, preventing resource leaks and leaving
     * the system in a consistent state.
     */
    public void stop() {
        scheduler.shutdown();
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
        }
        LOGGER.info("Heartbeat service stopped");
    }

    /**
     * Removes clients from the system that have been inactive for a duration longer
     * than the specified timeout period. This method iterates through the list of
     * clients and identifies those whose last recorded ping time exceeds the timeout
     * threshold. Identified offline clients are disconnected and removed from the
     * internal registry of active clients.
     * <p>
     * The process involves:
     * - Checking the elapsed time since each client's last ping.
     * - Disconnecting clients deemed offline using the {@code NetworkService}
     *   singleton and its {@code getClient} and {@code disconnect} methods.
     * - Removing the disconnected clients from the internal time tracking map.
     * - Logging the disconnection of each offline client.
     */
    private void removeOfflineClients() {
        long currentTime = System.currentTimeMillis();
        List<String> offlineClients = new ArrayList<>();

        for (Map.Entry<String, Long> entry : clientPingTime.entrySet()) {
            if (currentTime - entry.getValue() > TIMEOUT_MS) {
                offlineClients.add(entry.getKey());
            }
        }

        for (String clientUsername : offlineClients) {
            NetworkService.getInstance().getClient(clientUsername).disconnect();
            clientPingTime.remove(clientUsername);
            LOGGER.info("Disconnected offline client: " + clientUsername);
        }
    }
}