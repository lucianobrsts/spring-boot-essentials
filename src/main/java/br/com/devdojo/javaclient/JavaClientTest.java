package br.com.devdojo.javaclient;

import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JavaClientTest {
    public static void main(String[] args) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String user = "toyo";
        String password = "devdojo";

        try {
            URL url = new URL("http://localhost:8080/v1/protected/studants/");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.addRequestProperty("Authorization", "Basic "  + encodeUsernamePassword(user, password));
            System.out.println(encodeUsernamePassword(user, password));
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder jsonB = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonB.append(line);
            }
            System.out.println(jsonB.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(reader);

            if(connection != null) {
                connection.disconnect();
            }
        }
    }

    private static String encodeUsernamePassword(String user, String password) {
        String userPassword = user + ":" + password;
        return new String(Base64.encodeBase64(userPassword.getBytes()));
    }
}
