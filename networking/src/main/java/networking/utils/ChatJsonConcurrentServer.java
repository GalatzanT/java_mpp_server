package networking.utils;

import networking.jsonprotocol.ChatClientJsonWorker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.IServices;

import java.net.Socket;

public class ChatJsonConcurrentServer extends AbsConcurrentServer{
    private IServices chatServer;
    private static Logger logger = LogManager.getLogger(ChatJsonConcurrentServer.class);
    public ChatJsonConcurrentServer(int port, IServices chatServer) {
        super(port);
        this.chatServer = chatServer;
        logger.info("Chat-ChatJsonConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        ChatClientJsonWorker worker=new ChatClientJsonWorker(chatServer, client);

        Thread tw=new Thread(worker);
        return tw;
    }
}
