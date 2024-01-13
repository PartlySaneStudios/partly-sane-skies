//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.api;

import me.partlysanestudios.partlysaneskies.PartlySaneSkies;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class RequestsManager {

    private static final Queue<Request> requestsQueue = new LinkedList<>();
    private static long lastRequestTime = 0;

    public static void init() {
    }

    public static void run() {
        if (requestsQueue.isEmpty()) {
            return;
        }

        // The time in milliseconds between requests
        long timeBetweenRequests = Math.round(PartlySaneSkies.config.timeBetweenRequests * 1000);
        // If the time has not elapsed between requests
        if (onCooldown(lastRequestTime, timeBetweenRequests)) {
            return;
        }

        // Requests and removes the 1st element in the queue
        Request element = requestsQueue.poll();
        // If the request is supposed to run in the main thread
        lastRequestTime = PartlySaneSkies.getTime();
        if (element == null) {
            return;
        }
        if (element.isMainThread()) {
            try {
                element.startRequest();
            } catch (IOException e) {
                element.setFailed("{THREW_IOEXEPCTION}");
                e.printStackTrace();
            }
        }
        // Else
        else {
            // Creates a new thread to execute request
            new Thread(() -> {
                try {
                    element.startRequest();
                } catch (IOException e) {
                    element.setFailed();
                    // If supposed to run in the next frame, run in the next frame
                    if (element.isRunNextFrame()) {
                        PartlySaneSkies.minecraft.addScheduledTask(() -> element.getWhatToRunWhenFinished().run(element));
                        return;
                    }

                    // Runs on current thread
                    element.getWhatToRunWhenFinished().run(element);
                    e.printStackTrace();
                }
            }).start();
        }
    }

    // Adds a new request and returns its position in queue
    public static void newRequest(Request request) {
        if (request == null) {
            return;
        }

        requestsQueue.add(request);

    }

    // Returns of true if the time has not elapsed
    public static boolean onCooldown(long lastTime, long length) {
        if (PartlySaneSkies.getTime() > lastTime + length) {
            return false;
        }
        return true;
    }
}
