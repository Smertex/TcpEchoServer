package by.smertex.server.request.realisation;

import by.smertex.server.request.interfaces.RequestDistributor;
import by.smertex.server.util.PropertiesManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RequestDistributorImpl implements RequestDistributor {

    private final ExecutorService executor;

    private final ServerSocketChannel serverSocketChannel;

    public RequestDistributorImpl() throws IOException {
        executor = Executors.newCachedThreadPool();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketConfiguration();
    }

    @Override
    @SuppressWarnings("all")
    public void start() {
        while (true) {
            try {
                executor.execute(new RequestHandlerImpl(serverSocketChannel.accept()));
            } catch (IOException e) {
                continue;
            }
        }
    }

    private void serverSocketConfiguration() throws IOException {
        serverSocketChannel.bind(new InetSocketAddress(Integer.parseInt(PropertiesManager.get("server.port"))));
        serverSocketChannel.configureBlocking(true);
    }
}
