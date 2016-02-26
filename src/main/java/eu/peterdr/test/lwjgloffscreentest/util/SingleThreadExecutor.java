package eu.peterdr.test.lwjgloffscreentest.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SingleThreadExecutor {

    private ExecutorService actionQueue;

    private static Logger logger = LogManager.getLogger(SingleThreadExecutor.class.getName());

    public SingleThreadExecutor() {
        this.actionQueue = Executors.newSingleThreadExecutor();
    }

    public void submitAction(final Runnable task) {
        this.actionQueue.submit(() -> {
            try {
                task.run();
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e);
            }
        });
    }

    public <T extends Object> T submitActionAndGetResult(final Callable<T> task) {
        try {
            return this.actionQueue.submit(() -> {
                try {
                    return task.call();
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error(e);
                    return null;
                }
            }).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            logger.error(e);
            return null;
        }
    }

    public void submitActionAndWait(final Runnable task) {
        try {
            this.actionQueue.submit(() -> {
                try {
                    task.run();
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error(e);
                }
            }).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
