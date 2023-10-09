/*
 * Partly Sane Skies: A Hypixel Skyblock QOL and Economy mod
 * Created by Su386#9878 (Su386yt) and FlagMaster#1516 (FlagHater), the Partly Sane Studios team
 * Copyright (C) ©️ Su386 and FlagMaster 2023
 * This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 * 
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 * 
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */


package me.partlysanestudios.partlysaneskies.utils.requests;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import net.minecraft.client.Minecraft;

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
        lastRequestTime = Minecraft.getSystemTime();
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
        if (Minecraft.getSystemTime() > lastTime + length) {
            return false;
        }
        return true;
    }
}
