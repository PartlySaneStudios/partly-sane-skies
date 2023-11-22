//
// Written by Su386 and the Polyfrost Team.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.system.requests;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationException;
import me.partlysanestudios.partlysaneskies.PartlySaneSkies;
import me.partlysanestudios.partlysaneskies.utils.ChatUtils;
import net.minecraft.client.Minecraft;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public class PolyfrostUrsaMinorRequest extends Request {
    private static String token;
    private static Instant expiry;
    private static String username;
    private static String serverId;

    // The JsonObject that contains the JSON response
    private String requestResponse;

    public PolyfrostUrsaMinorRequest(URL url, RequestRunnable function, boolean inMainThread, boolean executeOnNextFrame) {
        super(url, function, inMainThread, executeOnNextFrame);
    }

    public PolyfrostUrsaMinorRequest(String url, RequestRunnable function, boolean inMainThread, boolean executeOnNextFrame) throws MalformedURLException {
        super(url, function, inMainThread, executeOnNextFrame);
    }

    public PolyfrostUrsaMinorRequest(URL url, RequestRunnable function) {
        super(url, function);
    }

    public PolyfrostUrsaMinorRequest(String url, RequestRunnable function) throws MalformedURLException {
        super(url, function);
    }

    @Override
    public void startRequest() { // handles auth for you
        URL url = super.getURL();
        HttpURLConnection connection = setupConnection(url);

        if (connection == null) return; // if connection is null (something bad happened), return null. you probably want null checks in your code
        try (InputStreamReader input = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8)) {
            String str = IOUtils.toString(input);
            if (username != null && serverId != null) { // if we passed username and serverid, we need to get the token and expiry to use in future requests
                token = connection.getHeaderField("x-ursa-token"); // get the token and expiry from the headers
                expiry = calculateExpiry(connection.getHeaderField("x-ursa-expires"));
                username = null; // reset username and serverid
                serverId = null;
            }
            if (str == null) { // if the element is null or not a json object, return null
                return;
            }


            this.requestResponse = str;
        } catch (IOException e) {
            ChatUtils.INSTANCE.sendClientMessage("IOException: Contact PSS admins with your logs");
            e.printStackTrace();
            ChatUtils.INSTANCE.sendClientMessage(e.getMessage());
            ChatUtils.INSTANCE.sendClientMessage(e.getLocalizedMessage());
            return; // if something bad happened, return null
        }

        RequestRunnable whenFinished = super.getWhatToRunWhenFinished();

        if (whenFinished == null) {
            return;
        }

        // If supposed to run in the next frame, run in the next frame
        if (super.isRunNextFrame()) {
            PartlySaneSkies.minecraft.addScheduledTask(() -> whenFinished.run(this));
            return;
        }

        // Runs on current thread
        whenFinished.run(this);
    }

    private static HttpURLConnection setupConnection(URL url) {
        HttpURLConnection connection;
        try {
            connection = ((HttpURLConnection) url.openConnection());
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            connection.addRequestProperty("User-Agent", "Partly-Sane-Skies/" + PartlySaneSkies.VERSION); // IMPORTANT: change this to your own user agent
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            connection.setDoOutput(true);
            if (token != null && expiry != null && expiry.isAfter(Instant.now())) { // if we have a token and it hasnt expired, use it
                connection.addRequestProperty("x-ursa-token", token); // add the token and expiry to the headers
            } else {
                token = null; // reset token and expiry
                expiry = null; // this is important, otherwise we will keep using the old token
                if (authorize()) { // if we successfully authorized, add the username and serverid we used to authorize to the headers
                    connection.addRequestProperty("x-ursa-username", username);
                    connection.addRequestProperty("x-ursa-serverid", serverId);
                } else {
                    return null;
                }
            }
            return connection;
        } catch (IOException e) {
            ChatUtils.INSTANCE.sendClientMessage("IOException: Contact PSS admins with your logs");
            e.printStackTrace();
            ChatUtils.INSTANCE.sendClientMessage(e.getMessage());
            ChatUtils.INSTANCE.sendClientMessage(e.getLocalizedMessage());
            return null;
        }
    }

    private static boolean authorize() {
        String serverId = UUID.randomUUID().toString(); // generate a random serverid
        try {
            GameProfile profile = Minecraft.getMinecraft().getSession().getProfile(); // get the user's game profile
            String token = Minecraft.getMinecraft().getSession().getToken(); // get the user's token
            Minecraft.getMinecraft().getSessionService().joinServer(profile, token, serverId); // authenticate with MC to confirm with the server that we are who we say we are and to prevent spam
            username = profile.getName(); // set the username and serverid - we will use these later and pass them to the server
            PolyfrostUrsaMinorRequest.serverId = serverId;
        } catch (AuthenticationException e) {
            ChatUtils.INSTANCE.sendClientMessage("Authentication Exception: Contact PSS admins with your logs");
            e.printStackTrace();
            ChatUtils.INSTANCE.sendClientMessage(e.getMessage());
            ChatUtils.INSTANCE.sendClientMessage(e.getLocalizedMessage());
            return false;
        }
        return true;
    }

    private static Instant calculateExpiry(String expiry) {
        try {
            long expiryLong = Long.parseLong(expiry); // parse the expiry to a long
            return Instant.ofEpochMilli(expiryLong); // convert the long to an instant
        } catch (NumberFormatException e) {
            return Instant.now().plus(Duration.ofMinutes(55)); // if the expiry is invalid, return 55 minutes from now which should be safe enough
        }
    }

    public String getResponse() {
        if (this.requestResponse == null) {
            return "Error: {NO_RESPONSE}";
        }
        else if (!hasSucceeded()) {
            super.getErrorMessage();
        }
        return this.requestResponse;
    }
}