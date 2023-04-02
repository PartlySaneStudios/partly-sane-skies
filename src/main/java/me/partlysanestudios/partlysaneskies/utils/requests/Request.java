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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.utils.Utils;

public class Request {
    // The get request
    private URL url;
    // The function that will activate when the response is finished
    // Function can take a String which is the response to the API call
    private RequestRunnable whenFinished;
    // If true, the request will execute while in the main thread of the client
    // freezing it and stopping anything else from happening while awaiting a response.
    // Only use in specific use cases
    private boolean inMainThread;
    // If true, the runnable will execute on the the next frame of the main thread
    // Useful for when modifying things with the client
    private boolean executeOnNextFrame;

    // A string that contains the response code
    private String stringReponseCode;
    // An int that contains the response code
    private int intResposeCode;
    // The String that contains the JSON response
    private String requestResponse;

    // A boolean to determing if there was an unknown failure
    // Gets set to true when setFailed(reason) is called
    private boolean otherFailure;

    private ArrayList<Integer> tryAgainOnCodes;
    
    public Request (URL url, RequestRunnable function, boolean inMainThread, boolean executeOnNextFrame) {
        this.url = url;
        this.whenFinished = function;
        this.inMainThread = inMainThread;
        this.executeOnNextFrame = executeOnNextFrame;

        this.tryAgainOnCodes = new ArrayList<Integer>();
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
    // Returns a referance to itself for easy chaining
    public Request addTryAgainResponse(int responseCode) {
        tryAgainOnCodes.add(responseCode);

        return this;
    }

    // Returns a string with the response, or returns a string starting with "Error:" if the response 
    // does not exist, or if the response code is not HTTP_OK
    public String getResponse() {
        if (this.requestResponse == null) {
            return "Error: {NO_REPONSE}";
        }
        else if (this.intResposeCode != HttpURLConnection.HTTP_OK) {
            return "Error: " + this.stringReponseCode + ":" + this.intResposeCode;
        }
        return this.requestResponse;
    }

    // Returns if the response should run on the main thread
    public boolean isMainThread() {
        return this.inMainThread;
    }

    // Returns of the executeable code should run in the next frame on the main thread,
    // or in the current thread
    public boolean isRunNextFrame() {
        return this.executeOnNextFrame;
    }

    // Flags this request as failed and gives a reason why
    public void setFailed(String reason) {
        this.stringReponseCode = reason;
        this.otherFailure = true;
    }

    // Returns true if the respose code is http ok and it has no other failure
    public boolean hasSucceeded() {
        if (this.intResposeCode != HttpURLConnection.HTTP_OK) {
            return false;
        }

        if (otherFailure) {
            return false;
        }

        return true;
    }

    // Submits the getRequest
    public void getRequest() throws IOException {
        // Opens a new connection with the url
        HttpURLConnection httpURLConnection = (HttpURLConnection) this.url.openConnection();
        // Sets the browser as Mozilla to bypass an insecure restrictions
        httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
        // Gets the response code
        int responseCode = httpURLConnection.getResponseCode();
        this.intResposeCode = responseCode;
        // Gets the response message
        this.stringReponseCode = httpURLConnection.getResponseMessage();

        // If the code is not HTTP_OK -- if the request failed
        if (responseCode != HttpURLConnection.HTTP_OK) {
            if (tryAgainOnCodes.contains(responseCode)) {
                RequestsManager.newRequest(this);
                return;
            }

            // If the print API errors setting is on, send a message to the client
            if (PartlySaneSkies.config.printApiErrors) {
                Utils.sendClientMessage("Error: " + httpURLConnection.getResponseMessage() + ":" + httpURLConnection.getResponseCode() + "\nContact PSS admins for more information");
            }
            // If not, simply print the error message to the console log 
            else {
                System.out.println("Error: " + httpURLConnection.getResponseMessage() + ":" + httpURLConnection.getResponseCode() + "\nContact PSS admins for more information");
            }
            
            // Discornnect the connection
            httpURLConnection.disconnect();
        }

        else {
            // Read the response as a string
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // set the requestResponse to the string that was read as a response
            this.requestResponse = response.toString();

            // Disconnect
            httpURLConnection.disconnect();
        }
        
        if (whenFinished == null) {
            return;
        }

        // If supposed to run in the next frame, run in next frame
        if (executeOnNextFrame) {
            PartlySaneSkies.minecraft.addScheduledTask(() -> {
                whenFinished.run(this);
            });
            return;
        }

        // Runs on current thread
        whenFinished.run(this);
    }
}
