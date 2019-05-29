import org.apache.commons.net.ftp.FTPClient;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ServerBootstrap {

    public static void main(String[] args) throws Exception {

        FtpServer server = getFtpServer();
        server.start();

        FtpClientBootstrap ftpClientBootstrap = new FtpClientBootstrap("localhost",10100,
                "anonymous", "1", new FTPClient());
        ftpClientBootstrap.openConnection();
        ftpClientBootstrap.uploadFile("/Users/asia/IdeaProjects/FileTransfer/src/main/resources/asia.txt",
                "image.txt",
                "/image2/");

        ftpClientBootstrap.closeConnection();;

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

//        BaseUser user = new BaseUser();
//        List<Authority> authorities = new ArrayList<Authority>();
//        authorities.add(new WritePermission());
//        user.setAuthorities(authorities);
        return server;
    }


}
