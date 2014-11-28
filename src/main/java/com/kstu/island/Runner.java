package com.kstu.island;

import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;
import java.util.Scanner;

public class Runner {

    private static Chat ts;
    private static String nickname;
    private static String start = "+-------------------------------------------------------------+\n" +
            "|                      ISLAND CHAT 1.0                        |\n" +
            "|I'll go build my own lunar lander, with blackjack and hookers|\n" +
            "|                                                             |\n" +
            "|                           2014 ©                            |\n" +
            "+-------------------------------------------------------------+";


    public static void main(String[] args) throws Exception {
        Scanner scan = new Scanner(System.in);
        if (args.length > 0) {
            nickname = args[0];
        } else {
            System.out.println("Enter Nickname: ");
            nickname = scan.next();
        }
        if (nickname.toLowerCase().equals("admin")) {
            System.out.println("Initializing server..." + InetAddress.getLocalHost());
            Chat service = new ConsoleChat();
            Registry reg = LocateRegistry.createRegistry(1099);
            String serviceName = "ConsoleChat";
            reg.rebind(serviceName, service);
            //scan = new Scanner(System.in);
            // System.out.println("Enter Nickname: ");
            // nickname = scan.next();
            // System.out.println(start);
            log(service);
            //while (true) {
            //    String message = scan.next();
            //    if (message != null) {
            //        sendMessage(service, message, nickname);
            //    }
            //}
        } else {
            System.out.println(start);
            System.out.println("Enter server IPv4: ");
            String ip = scan.next();
            String objectName = "rmi://" + ip + "/ConsoleChat";
            ts = (Chat) Naming.lookup(objectName);
            monitor(ts);
            sendMessage(ts, "Wake up, " + nickname + " follow the White Rabbit:", "");
            while (true) {
                String message = scan.next();
                if (message != null) {
                    sendMessage(ts, message, nickname);
                }
            }
        }
    }

    /**
     * Sends message to all clients and prints to server
     *
     * @param ts Chat implemented object
     */
    private static void sendMessage(Chat ts, String message, String nickname) {
        try {
            ts.setMessage(nickname + ":" + message);//to Server
            ts.print(nickname + ":" + message);//to All Clients
        } catch (RemoteException e) {
            //  e.printStackTrace();
        }
    }

    /**
     * Prints to client console new messages
     *
     * @param chat Chat implemented object
     */
    public static void monitor(final Chat chat) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                while (true) {
                    String message = null;
                    try {
                        message = chat.getMessage();
                        if (message != null && !message.startsWith(nickname)) {
                            System.out.println(message);
                            chat.setMessage(null);
                        }
                    } catch (RemoteException e) {
                        System.out.println("SERVER DISCONNECT :(");
                        break;
                    }
                }
            }
        };

        thread.start();
    }


    public static void log(final Chat chat) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                FileWriter sw = null;
                try {
                    while (true) {
                        sw = silentOpen();
                        String message = null;
                        message = chat.getMessage();
                        if (message != null) {
                            //System.out.println(message);
                            sw.write(new Date().getTime() + "| " + message + "\r\n");
                            chat.setMessage(null);
                        }
                        silentClose(sw);
                    }
                } catch (RemoteException e) {
                    // break;
                } catch (IOException e) {
                    //e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    private static void silentClose(FileWriter sw) {
        try {
            sw.close();
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    private static FileWriter silentOpen() {
        FileWriter fw = null;
        try {
            fw = new FileWriter("ISLANDCHAT.txt", true);
        } catch (IOException e) {
            //e.printStackTrace();
        }
        return fw;
    }

}


