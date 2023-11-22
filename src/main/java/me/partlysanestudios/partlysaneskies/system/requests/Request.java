//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.system.requests;

import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.utils.ChatUtils;
import me.partlysanestudios.partlysaneskies.utils.SystemUtils;
import org.apache.logging.log4j.Level;
import org.lwjgl.Sys;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Request {
    // The get request
    private final URL url;
    // The function that will activate when the response is finished
    // Function can take a String which is the response to the API call
    private final RequestRunnable whenFinished;
    // If true, the request will execute while in the main thread of the client
    // freezing it and stopping anything else from happening while awaiting a response.
    // Only use in specific use cases
    private final boolean inMainThread;
    // If true, the runnable will execute on the next frame of the main thread
    // Useful for when modifying things with the client
    private final boolean executeOnNextFrame;

    // A string that contains the response code
    private String stringResponseCode;
    // An int that contains the response code
    private int intResponseCode;
    // The String that contains the JSON response
    private String requestResponse;

    // A boolean to determining if there was an unknown failure
    // Gets set to true when setFailed(reason) is called
    private boolean otherFailure;

    private final ArrayList<Integer> tryAgainOnCodes;
    
    public Request (URL url, RequestRunnable function, boolean inMainThread, boolean executeOnNextFrame) {
        this.url = url;
        this.whenFinished = function;
        this.inMainThread = inMainThread;
        this.executeOnNextFrame = executeOnNextFrame;

        this.tryAgainOnCodes = new ArrayList<>();
        this.otherFailure = false;
    }

    public Request (String url, RequestRunnable function, boolean inMainThread, boolean executeOnNextFrame) throws MalformedURLException {
        this(new URL(url), function, inMainThread, executeOnNextFrame);
    }

    public Request (URL url, RequestRunnable function) {
        this(url, function, false, false);
    }

    public Request (String url, RequestRunnable function) throws MalformedURLException {
        this(url, function, false, false);
    }

    // Adds codes where the request will try again if it failed
    // Returns a reference to itself for easy chaining
    public Request addTryAgainResponse(int responseCode) {
        tryAgainOnCodes.add(responseCode);

        return this;
    }

    // Returns a string with the response, or returns a string starting with "Error:" if the response 
    // does not exist, or if the response code is not HTTP_OK
    public String getResponse() {
        if (this.requestResponse == null) {
            return "Error: {NO_RESPONSE}";
        }
        else if (this.intResponseCode != HttpURLConnection.HTTP_OK) {
            getErrorMessage();
        }
        return this.requestResponse;
    }

    // Returns if the response should run on the main thread
    public boolean isMainThread() {
        return this.inMainThread;
    }

    // Returns if the executable code should run in the next frame on the main thread,
    // or in the current thread
    public boolean isRunNextFrame() {
        return this.executeOnNextFrame;
    }

    // Flags this request as failed and gives a reason why
    public void setFailed(String reason) {
        this.stringResponseCode = reason;
        this.otherFailure = true;
    }

    public String getErrorMessage() {
        return "Error: " + this.stringResponseCode + ":" + this.intResponseCode;
    }


    public void setFailed() {
        this.otherFailure = true;
    }

    // Returns true if the response code is http OK, and it has no other failure
    public boolean hasSucceeded() {
        if (this.intResponseCode != HttpURLConnection.HTTP_OK) {
            return false;
        }

        return !otherFailure;
    }

    // Submits the request
    public void startRequest() throws IOException {
        // Opens a new connection with the url
        HttpURLConnection httpURLConnection = (HttpURLConnection) this.url.openConnection();
        // Sets the browser as Mozilla to bypass an insecure restrictions
        httpURLConnection.setRequestProperty("User-Agent", "Partly-Sane-Skies/" + PartlySaneSkies.VERSION);
        // Gets the response code
        int responseCode = httpURLConnection.getResponseCode();
        this.intResponseCode = responseCode;
        // Gets the response message
        this.stringResponseCode = httpURLConnection.getResponseMessage();

        // If the code is not HTTP_OK -- if the request failed
        if (responseCode != HttpURLConnection.HTTP_OK) {
            if (tryAgainOnCodes.contains(responseCode)) {
                RequestsManager.newRequest(this);
                return;
            }

            // If the print API errors setting is on, send a message to the client
            if (PartlySaneSkies.config.printApiErrors) {
                ChatUtils.INSTANCE.sendClientMessage("Error: " + httpURLConnection.getResponseMessage() + ":" + httpURLConnection.getResponseCode() + "\nContact PSS admins for more information");
            }
            // If not, simply print the error message to the console log 
            else {
                SystemUtils.INSTANCE.log(Level.ERROR, "Error: " + httpURLConnection.getResponseMessage() + ":" + httpURLConnection.getResponseCode() + "\nContact PSS admins for more information");
            }
            SystemUtils.INSTANCE.log(Level.ERROR, "Error: " + httpURLConnection.getResponseMessage() + ":" + httpURLConnection.getResponseCode() + "\nURL: " + this.url);
            // Disconnect the connection
            httpURLConnection.disconnect();
        }

        // Read the response as a string
        BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // set the requestResponse to the string that was read as a response
        this.requestResponse = response.toString();

        // Disconnect
        httpURLConnection.disconnect();

        
        if (whenFinished == null) {
            return;
        }

        // If supposed to run in the next frame, run in the next frame
        if (executeOnNextFrame) {
            PartlySaneSkies.minecraft.addScheduledTask(() -> whenFinished.run(this));
            return;
        }

        // Runs on current thread
        whenFinished.run(this);
    }

    public RequestRunnable getWhatToRunWhenFinished() {
        return whenFinished;
    }

    public URL getURL() {
        return url;
    }
}
