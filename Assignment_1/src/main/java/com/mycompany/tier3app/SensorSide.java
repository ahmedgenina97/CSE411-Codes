/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.tier3app;

import java.net.*;
import java.io.*;

/**
 *
 * @author Ahmed Genina
 */
public class SensorSide {

    static final int SENSOR_PORT_NUMBER = 6667;

    String GetReading() {
        /* insert your logic here
         */
        return null;
    }

    public static void main(String[] args) throws IOException {

        System.out.println("Sensor Online");
        SensorSide MySensorSide = new SensorSide();
        while (true) {
            try (
                    var serverServerSocket
                    = new ServerSocket(SENSOR_PORT_NUMBER);
                    var serverSocket = serverServerSocket.accept();
                    var outServer
                    = new PrintWriter(serverSocket.getOutputStream(), true);
                    var inServer
                    = new BufferedReader(new InputStreamReader(
                            serverSocket.getInputStream()));) {
                String serverRequest;
                while ((serverRequest = inServer.readLine()) != null) {
                    if (serverRequest.equalsIgnoreCase("Request Reading")) {
                        String reading = MySensorSide.GetReading();
                        outServer.println(reading);
                    }
                }
            } catch (IOException exc) {
                System.out.println("Caught Server IOException");
                System.out.println(exc.getMessage());
            }
        }
    }
}
