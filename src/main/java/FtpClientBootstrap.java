import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;

public class FtpClientBootstrap {

    private String hostname;
    private int port;
    private String user;
    private String password;
    private FTPClient ftp;

    public FtpClientBootstrap(String hostname, int port, String user, String password, FTPClient ftp) {
        this.hostname = hostname;
        this.port = port;
        this.user = user;
        this.password = password;
        this.ftp = ftp;
    }

    void openConnection() throws IOException {
        ftp = new FTPClient();

        ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));

        ftp.connect("localhost", Integer.valueOf(port));
        int reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            throw new IOException("Exception in connecting to FTP Server");
        }


        ftp.login(user, password);
        ftp.setFileType(FTP.BINARY_FILE_TYPE);
        ftp.enterLocalPassiveMode();
    }





    public void uploadFile(String localFileFullName, String fileName, String hostDir)
            throws Exception {
        try(InputStream input = new FileInputStream(new File(localFileFullName))){
            String remoteFile = hostDir + fileName;
            boolean done = this.ftp.storeFile(remoteFile, input);

            if (done) {
                System.out.println("Upload of file done successfully.");
            }
            else{
                System.out.println("Upload failed");
            }
        }
    }


    void closeConnection() throws IOException {
        ftp.disconnect();
    }
}
