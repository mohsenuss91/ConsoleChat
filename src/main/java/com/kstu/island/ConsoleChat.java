package com.kstu.island;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ConsoleChat extends UnicastRemoteObject implements Chat {
    String message;

    public ConsoleChat() throws RemoteException {
        super();
    }

    @Override
    public String getMessage() throws RemoteException {
        return message;
    }

    @Override
    public void print(String str) throws RemoteException {
        System.out.println(str);
    }

    @Override
    public void setMessage(String str) throws RemoteException {//from client to server
        message = str;
    }

}
