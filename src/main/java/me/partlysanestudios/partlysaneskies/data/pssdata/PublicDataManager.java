//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.data.pssdata;

import me.partlysanestudios.partlysaneskies.system.requests.Request;
import me.partlysanestudios.partlysaneskies.system.requests.RequestsManager;

import java.net.MalformedURLException;
import java.util.HashMap;

public class PublicDataManager {
    private static String REPO_URL = "https://raw.githubusercontent.com/PartlySaneStudios/partly-sane-skies-public-data/main/data/";

    private static HashMap<String, String> fileCache = new HashMap<>();
    private static final Lock lock = new Lock();

    public static String getFile(String path) {
        String fixedPath = path;
        if (fixedPath.startsWith("/")) {
            fixedPath = fixedPath.substring(1);
        }

        if (fixedPath.endsWith("/")) {
            fixedPath = fixedPath.substring(0, fixedPath.length() - 1);
        }

        if (fileCache.containsKey(fixedPath)) {
            return fileCache.get(fixedPath);
        }

        try {
            RequestsManager.newRequest(new Request(REPO_URL + path, r -> {
                if (!r.hasSucceeded()) {
                    lock.notifyAll();
                    return;
                }
                fileCache.put(path, r.getResponse());

                synchronized (lock) {
                    lock.notifyAll();
                }
            }));
        } catch (MalformedURLException e) {
            synchronized (lock) {
                lock.notifyAll();
            }
        }

        try {
            synchronized (lock) {
                lock.wait();
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (!fileCache.containsKey(path)) {
            return "";
        }

        return fileCache.get(path);
    }

    private static class Lock {

    }
}
