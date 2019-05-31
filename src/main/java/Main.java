import org.apache.commons.net.ftp.FTPClient;
import org.apache.ftpserver.FtpServer;

import java.io.File;


public class Main {

    public static void main(String[] args) throws Exception {
        FtpServer server = ServerBootstrap.getFtpServer();
        server.start();

        FtpClientBootstrap ftpClientBootstrap = new FtpClientBootstrap("localhost",10100,
                "anonymous", "1", new FTPClient());
        ftpClientBootstrap.openConnection();





//        ftpClientBootstrap.storeFile("/Users/asia/IdeaProjects/FileTransfer/src/main/resources/video.mp4",
//                "vid.mp4",
//                "/");
//
        ftpClientBootstrap.uploadFile("/Users/asia/IdeaProjects/FileTransfer/src/main/resources/video.mp4",
                "vid.mp4",
                "/"
                );

//                ftpClientBootstrap.uploadFile("/Users/asia/IdeaProjects/FileTransfer/src/main/resources/norris.jpg",
//                "vid.jpg",
//                "/"
//                );


//        ftpClientBootstrap.uploadNewFile("remote.mp4",
//                new File("/Users/asia/IdeaProjects/FileTransfer/src/main/resources/video.mp4"),0
//                );



    }

}
