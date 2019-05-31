import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;

import java.io.File;

public class ServerBootstrap {

    public static FtpServer getFtpServer() {
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
