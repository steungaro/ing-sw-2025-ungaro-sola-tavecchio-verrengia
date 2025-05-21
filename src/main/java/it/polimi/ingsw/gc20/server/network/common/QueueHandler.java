package it.polimi.ingsw.gc20.server.network.common;

import it.polimi.ingsw.gc20.common.message_protocol.toserver.Message;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QueueHandler {
    private static final Logger LOGGER = Logger.getLogger(QueueHandler.class.getName());
    private final BlockingQueue<Message> messageQueue;
    private static QueueHandler instance;
    private final Thread dequeuerThread;
    private volatile boolean running = true;

    private QueueHandler() {
        this.messageQueue = new LinkedBlockingQueue<>();
        this.dequeuerThread = new Thread(this::processMessages);
        this.dequeuerThread.setName("MessageDequeuer");
        this.dequeuerThread.setDaemon(true);
        this.dequeuerThread.start();
    }

    public static QueueHandler getInstance() {
        if (instance == null) {
            instance = new QueueHandler();
        }
        return instance;
    }

    public void enqueue(Message message) {
        if (message == null) {
            LOGGER.warning("Attempted to enqueue null message");
            return;
        }

        try {
            messageQueue.put(message);
            LOGGER.info("Message enqueued: " + message);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.log(Level.WARNING, "Interrupted while enqueuing message", e);
        }
    }

    private void processMessages() {
        LOGGER.info("Message dequeuer thread started");

        while (running) {
            try {
                // Take() will block until a message is available
                Message message = messageQueue.take();
                LOGGER.info("Processing message: " + message);
                // Delegate to appropriate controller method based on message type
                message.handleMessage();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LOGGER.log(Level.WARNING, "Dequeuer thread interrupted", e);
                break;
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error processing message", e);
                // Continue processing next message
            }
        }

        LOGGER.info("Message dequeuer thread stopping");
    }

    public void shutdown() {
        running = false;
        dequeuerThread.interrupt();
        LOGGER.info("QueueHandler shutdown initiated");
    }
}