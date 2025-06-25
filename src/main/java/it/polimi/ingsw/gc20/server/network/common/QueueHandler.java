package it.polimi.ingsw.gc20.server.network.common;

import it.polimi.ingsw.gc20.common.message_protocol.Message;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.*;

/**
 * The QueueHandler class is responsible for managing a queue of messages
 * and processing them in a separate thread. It uses a singleton pattern
 * to ensure a single instance of the queue handler and provides thread-safe
 * methods for enqueuing messages and shutting down the handler.
 * <p>
 * The queue handler operates with a daemon thread that continuously dequeues
 * messages and calls their handling logic.
 */
public class QueueHandler {
    private static final Logger LOGGER = Logger.getLogger(QueueHandler.class.getName());
    private final BlockingQueue<Message> messageQueue;
    private static QueueHandler instance;
    private final Thread dequeuerThread;
    private volatile boolean running = true;

    /**
     * Private constructor that initializes the QueueHandler instance.
     * This constructor is responsible for setting up the message queue
     * and starting a dedicated daemon thread to process messages.
     * <p>
     * The daemon thread continuously dequeues messages from the internal
     * blocking queue and calls their respective handling logic.
     * <p>
     * Note: This constructor is private as part of a singleton pattern
     * to ensure that only one instance of the QueueHandler exists.
     */
    private QueueHandler() {
        this.messageQueue = new LinkedBlockingQueue<>();
        this.dequeuerThread = new Thread(this::processMessages);
        this.dequeuerThread.setName("MessageDequeuer");
        this.dequeuerThread.setDaemon(true);
        this.dequeuerThread.start();
    }

    /**
     * Retrieves the singleton instance of the {@code QueueHandler} class.
     * This method ensures that only one instance of the {@code QueueHandler}
     * exists throughout the application, following the singleton design pattern.
     * If the instance has not been initialized yet, it creates a new {@code QueueHandler}
     * instance and returns it.
     *
     * @return the singleton instance of {@code QueueHandler}.
     */
    public static QueueHandler getInstance() {
        if (instance == null) {
            instance = new QueueHandler();
        }
        return instance;
    }

    /**
     * Adds a {@code Message} object to the internal queue for further processing.
     * This method ensures that the provided message is added to the queue and logs
     * the operation. If the message is {@code null}, it logs a warning and the operation is aborted.
     * If the thread is interrupted while waiting to enqueue the message, it logs an interruption warning
     * and the thread's interrupt status is restored.
     *
     * @param message the {@code Message} instance to be added to the queue.
     *                Must not be {@code null}.
     */
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

    /**
     * Continuously processes messages from the internal blocking queue until the processing
     * thread is terminated. This method is executed by a worker thread and is responsible
     * for managing the lifecycle of the message-processing loop.
     */
    private void processMessages() {
        LOGGER.info("Message dequeuer thread started");

        while (running) {
            try {
                // Take() will block until a message is available
                Message message = messageQueue.take();
                // Delegate to the appropriate controller method based on the message type
                message.handleMessage();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LOGGER.log(Level.WARNING, "Dequeuer thread interrupted", e);
                break;
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error processing message", e);
                // Continue processing the next message
            }
        }

        LOGGER.info("Message dequeuer thread stopping");
    }

    /**
     * Gracefully shuts down the QueueHandler by stopping its processing activities.
     * <p>
     * This method is responsible for halting the message processing loop
     * and interrupting the thread associated with dequeue messages.
     * Once invoked, the shutdown process ensures that no further messages
     * are processed, and the related resources are released appropriately.
     */
    public void shutdown() {
        running = false;
        dequeuerThread.interrupt();
        LOGGER.info("QueueHandler shutdown initiated");
    }
}