/*
 * To change this license header, choose License Headers inClient 
Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template inClient the editor.
 */
package com.mycompany.tier3app;

import java.net.*;
import java.io.*;

/**
 *
 * @author Ahmed Genina
 */
public class ServerSide {

    static final int SENSOR_PORT_NUMBER = 6667;
    static final String SENSOR_HOST_NAME = "127.0.0.1";
    static final int IDLE = 0;
    static final int GET_LOCATION = 1;
    static final int GET_DESTINATION = 2;
    static final int ANALYZE = 3;
    static final int RECOMMEND = 4;

    private boolean IsLocationValid(String location) {
        /* checks whether location format is correct
        add your logic here
         */
        return true;
    }

    private String AnalyzeReading(String reading) {
        /* Analyzes reading and returns a recommendation
        add your logic here
         */
        return "No traffic. Use road x";
    }

    private int FindSensorPortNum(String location, String destination) {
        /* Insert your logic here */
        return SENSOR_PORT_NUMBER;
    }

    private String FindSensorHostName(String location, String destination) {
        /* Insert your logic here */
        return SENSOR_HOST_NAME;
    }

    public static void main(String[] args) throws IOException {
        final int CLIENT_PORT_NUMBER = 6666;

        int serverState = IDLE;
        String location = null;
        String destination = null;
        String recommendation = null;
        String sensorReply = null;
        ServerSide MyServerSide = new ServerSide();
        System.out.println("Server online");

        try (
                var clientServerSocket
                = new ServerSocket(CLIENT_PORT_NUMBER);
                var clientSocket = clientServerSocket.accept();
                var outClient
                = new PrintWriter(clientSocket.getOutputStream(), true);
                var inClient
                = new BufferedReader(
                        new InputStreamReader(
                                clientSocket.getInputStream()));) {
            System.out.println("Server connected to client");
            while (true) {
                String clientString = null;
                while (clientString == null
                        && (serverState == IDLE
                        || serverState == GET_LOCATION
                        || serverState == GET_DESTINATION)) {
                    clientString = inClient.readLine();
                }
                switch (serverState) {
                    case IDLE:
                        location = null;
                        destination = null;
                        recommendation = null;
                        sensorReply = null;
                        if (clientString.equalsIgnoreCase("Recommend Route")) {
                            outClient.println("Request Location");
                            serverState = GET_LOCATION;
                        }
                        break;
                    case GET_LOCATION:
                        if (MyServerSide.IsLocationValid(clientString)) {
                            location = clientString;
                            outClient.println("Request Destination");
                            serverState = GET_DESTINATION;
                        }
                        break;
                    case GET_DESTINATION:
                        if (MyServerSide.IsLocationValid(clientString)) {
                            destination = clientString;
                            outClient.println("Please Wait ...");
                            serverState = ANALYZE;
                        }
                        break;
                    case ANALYZE:
                        int sensorPortNumber
                                = MyServerSide.FindSensorPortNum(
                                        location, destination);
                        String sensorHostName
                                = MyServerSide.FindSensorHostName(
                                        location, destination);
                        try (
                                var sensorSocket = new Socket(
                                        sensorHostName, sensorPortNumber);
                                var outSensor
                                = new PrintWriter(
                                        sensorSocket.getOutputStream(), true);
                                var inSensor
                                = new BufferedReader(new InputStreamReader(
                                        sensorSocket.getInputStream()));) {
                            outSensor.println("Request Reading");
                            while (sensorReply == null) {
                                sensorReply = inSensor.readLine();
                            }
                        } catch (UnknownHostException exc) {
                            System.err.println(
                                    "Caught sensor UnknownHostException");
                            System.exit(1);
                        } catch (IOException exc) {
                            System.out.println("Caught sensor IOException");
                            System.out.println(exc.getMessage());
                        }
                        serverState = RECOMMEND;
                        break;
                    case RECOMMEND:
                        recommendation
                                = MyServerSide.AnalyzeReading(sensorReply);
                        outClient.println(recommendation);
                        outClient.println("Service Finished");
                        serverState = IDLE;
                        break;
                    default:
                        break;
                }
            }
        } catch (IOException exc) {
            System.out.println("Caught client IOException");
            System.out.println(exc.getMessage());
        }
    }
}
