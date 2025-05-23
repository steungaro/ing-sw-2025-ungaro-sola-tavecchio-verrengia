package it.polimi.ingsw.gc20.server.network.common;

import it.polimi.ingsw.gc20.common.message_protocol.toclient.Ping;
import it.polimi.ingsw.gc20.server.network.NetworkService;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class HeartbeatService {
    private static final Logger LOGGER = Logger.getLogger(HeartbeatService.class.getName());
    private static final long PING_INTERVAL_MS = 30000; // 10 seconds
    private static final long TIMEOUT_MS = 90000; // 30 seconds
    private static final String BROADCAST = "__BROADCAST__";
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private static HeartbeatService instance;
    private final Map<String, Long> clientPingTime = new HashMap<>();

    private HeartbeatService() {
        // Private constructor for singleton
    }

    public static HeartbeatService getInstance() {
        if (instance == null) {
            instance = new HeartbeatService();
        }
        return instance;
    }

    public void start() {
        scheduler.scheduleAtFixedRate(this::service, 0, PING_INTERVAL_MS, TimeUnit.MILLISECONDS);
        LOGGER.info("Heartbeat service started");
    }

    public void handlePingResponse(String clientUsername) {
        long currentTime = (System.currentTimeMillis());
        clientPingTime.put(clientUsername, currentTime);
    }

    private void service() {
        pingAllClients();
        removeOfflineClients();
    }

    private void pingAllClients() {
        NetworkService.getInstance().broadcastMessage(new Ping(BROADCAST));
    }

    public void stop() {
        scheduler.shutdown();
        LOGGER.info("Heartbeat service stopped");
    }

    public void removeOfflineClients() {
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