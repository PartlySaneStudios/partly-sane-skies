//
// Written by Su386.
// See LICENSE for copright and license notices.
//


package me.partlysanestudios.partlysaneskies.utils.requests;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import me.partlysanestudios.partlysaneskies.PartlySaneSkies;

public class RequestsManager {

    private static Queue<Request> requestsQueue = new LinkedList<Request>();
    private static long lastRequestTime = 0;
    
    // The time in miliseconds between requests
    private static long timeBetweenRequests = 750;

    public static void init() {
    }

    public static void run() {
        if (requestsQueue.isEmpty()) {
            return;
        }

        timeBetweenRequests = Math.round(PartlySaneSkies.config.timeBetweenRequests * 1000);
        // If the time has not elapsed between requests
        if (onCooldown(lastRequestTime, timeBetweenRequests)) {
            return;
        }

        // Requests and removes the 1st element in the queue
        Request element = requestsQueue.poll();
        // If the request is supposed to run in the main thread
        lastRequestTime = PartlySaneSkies.getTime();
        if (element.isMainThread()) {
            try {
                element.getRequest();
            } catch (IOException e) {
                element.setFailed("{THREW_IOEXEPCTION}");
                e.printStackTrace();
            }
        }
        // Else
        else {
            // Creates a new thread to execute request
            new Thread() {
                @Override
                public void run() {
                    try {
                        element.getRequest();
                    } catch (IOException e) {
                        element.setFailed("{THREW_IOEXEPCTION}");
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    // Adds a new request and returns its position in queue
    public static int newRequest(Request request) {
        if (request == null) {
            return -1;
        }

        requestsQueue.add(request);

        return requestsQueue.size();
    }

    // Returns of true if the time has not elapsed
    public static boolean onCooldown(long lastTime, long length) {
        if (PartlySaneSkies.getTime() > lastTime + length) {
            return false;
        }
        return true;
    }
}
