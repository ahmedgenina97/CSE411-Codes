/*
 * To change this license header, choose License Headers inServer Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template inServer the editor.
 */
package com.mycompany.tier3app;

import java.net.*;
import java.io.*;

/**
 *
 * @author Ahmed Genina
 */
public class ClientSide {

    public static void main(String[] args) throws IOException {

        String hostName = "127.0.0.1";
        int portNumber = 6666;
        System.out.println("Client is waiting for input");

        try (
                Socket echoSocket = new Socket(hostName, portNumber);
                PrintWriter outServer
                = new PrintWriter(echoSocket.getOutputStream(), true);
                BufferedReader inServer
                = new BufferedReader(
                        new InputStreamReader(echoSocket.getInputStream()));
                BufferedReader driverInput
                = new BufferedReader(
                        new InputStreamReader(System.in))) {
            String userInput;
            while ((userInput = driverInput.readLine()) != null) {
                outServer.println(userInput);
                System.out.println(inServer.readLine());
            }
        } catch (UnknownHostException exc) {
            System.err.println("Caught UnknownHostException");
            System.exit(1);
        } catch (IOException exc) {
            System.err.println("Caught IOException");
            System.exit(1);
        }
    }
}
