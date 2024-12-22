package by.smertex.server;

import by.smertex.server.request.realisation.RequestDistributorImpl;

import java.io.IOException;

public class Start {
    public static void main(String[] args) throws IOException {
        new RequestDistributorImpl().start();
    }
}
