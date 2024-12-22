package by.smertex.server.request.realisation;

import by.smertex.server.exception.ClientResponseException;
import by.smertex.server.exception.ClientSocketCloseException;
import by.smertex.server.request.interfaces.RequestHandler;
import by.smertex.server.util.PropertiesManager;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class RequestHandlerImpl implements Runnable, RequestHandler {

    private static final String BUFFER_SIZE_KEY = "handler.buffer.size";

    private final SocketChannel socketChannel;

    private final ByteBuffer buffer;

    public RequestHandlerImpl(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
        this.buffer = ByteBuffer.allocate(Integer.parseInt(PropertiesManager.get(BUFFER_SIZE_KEY)));
    }

    @Override
    @SuppressWarnings("all")
    public void run() {
        try {
            while (socketChannel.isOpen()) {
                echoResponse();
            }
        } finally {
            try {
                socketChannel.close();
            } catch (IOException e) {
                throw new ClientSocketCloseException(e.getMessage());
            }
        }
    }

    @Override
    public void echoResponse() {
        try {
            while (socketChannel.read(buffer) > 0){
                System.out.println(new String(buffer.array()));
                buffer.flip();
                socketChannel.write(buffer);
            }
        } catch (IOException e) {
            throw new ClientResponseException(e.getMessage());
        }
    }
}
