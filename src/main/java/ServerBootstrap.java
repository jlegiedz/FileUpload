import org.apache.commons.net.ftp.FTPClient;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;

import java.io.File;

public class ServerBootstrap {

    public static void main(String[] args) throws Exception {

        FtpServer server = getFtpServer();
        server.start();

        FtpClientBootstrap ftpClientBootstrap = new FtpClientBootstrap("localhost",10100,
                "anonymous", "1", new FTPClient());
        ftpClientBootstrap.openConnection();
        //ftpClientBootstrap.uploadFile("/Users/asia/IdeaProjects/FileTransfer/src/main/resources/asia.txt",
          //      "text.txt",
            //    "/");
        ftpClientBootstrap.uploadFile("/Users/asia/IdeaProjects/FileTransfer/src/main/resources/norris.jpg",
                "image.jpg",
                "/");


        ftpClientBootstrap.closeConnection();

    }

    private static FtpServer getFtpServer() {
        FtpServerFactory serverFactory = new FtpServerFactory();

        ListenerFactory factory = new ListenerFactory();

        factory.setPort(10100);
        serverFactory.addListener("default", factory.createListener());

        FtpServer server = serverFactory.createServer();

        PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
        userManagerFactory.setFile(new File("myusers.properties"));
        serverFactory.setUserManager(userManagerFactory.createUserManager());
        return server;
    }

}
