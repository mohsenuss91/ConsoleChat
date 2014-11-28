package com.kstu.island;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Chat  extends Remote {


    public String getMessage() throws RemoteException;
    public void setMessage(String str) throws RemoteException;
    public void print(String str) throws RemoteException;

}
