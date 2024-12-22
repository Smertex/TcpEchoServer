package by.smertex.server.request.realisation;

import by.smertex.server.exception.ClientResponseException;
import by.smertex.server.exception.ClientSocketCloseException;
import by.smertex.server.util.PropertiesManager;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class RequestHandlerImpl implements Runnable {

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
        Exception exception = new Exception();
        try {
            while (socketChannel.isOpen()) {
                echoResponse(exception);
            }
        } catch (IOException e) {
            exception.addSuppressed(e);
        } finally {
            try {
                socketChannel.close();
            } catch (IOException e) {
                exception.addSuppressed(e);
                throw new ClientSocketCloseException(exception.getMessage());
            }
        }
    }

    private void echoResponse(Exception exception) throws IOException {
        while (socketChannel.read(buffer) > 0){
            System.out.println(new String(buffer.array()));
            buffer.flip();
            socketChannel.write(buffer);
        }
    }
}
