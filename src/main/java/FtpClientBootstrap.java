import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

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

    public void openConnection() throws IOException {
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
        ftp.setControlKeepAliveTimeout(300);


    }


    public void storeFile(String localFileFullName, String fileName, String hostDir)
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
            ftp.logout();
        }
    }



    public void uploadFile(String localFileFullName, String fileName, String hostDir)
            throws Exception {

        try(FileInputStream input = new FileInputStream(new File(localFileFullName))){

                String remoteFile = hostDir + fileName;

                OutputStream outputStream = this.ftp.appendFileStream(remoteFile);

                System.out.println(outputStream == null);

                FileChannel upload = input.getChannel();

                WritableByteChannel outChannel = Channels.newChannel(outputStream);

                long position = 0;      // The position within the file at which the transfer is to begin;
                long size = upload.size();

                System.out.println("To transfer: " + size);

                while (position < size ) {
                    System.out.println("-----");
                    position += upload.transferTo(position, 3000000, outChannel);

                    System.out.println("Current position: " + position);

                    outputStream.flush();
                }



//                while (upload.transferTo(currPos, 30000, channel) > 0 ) {
//                    System.out.println("Curr Pos: " + currPos);
//
//                    currPos += 30000;
//
//
//                }
//                        outputStream.flush();
                outputStream.close();

            }


            ftp.logout();

    }

    public void uploadNewFile(String remoteFile,File localFile, long remoteSize) throws IOException {


        long step = localFile.length() / 100;
        long process = 0;
        long localreadbytes = 0L;
        RandomAccessFile raf = new RandomAccessFile(localFile,"r");
        OutputStream out = this.ftp.appendFileStream(remoteFile);

//        if(remoteSize>0){
//            this.ftp.setRestartOffset(remoteSize);
//            process = remoteSize /step;
//            raf.seek(remoteSize);
//            localreadbytes = remoteSize;
//        }
        byte[] bytes = new byte[1024 * 1024];
        int c;
        while((c = raf.read(bytes))!= -1){
            System.out.println("1");

            out.write(bytes,0,c);
            localreadbytes+=c;

            System.out.println("2");

            if(localreadbytes / step != process){
                process = localreadbytes / step;
                System.out.println("process: " + process);
        }
            out.flush();
            System.out.println(localreadbytes);
        }

        raf.close();
        out.close();
        System.out.println(this.ftp.completePendingCommand());


    }



    void closeConnection() throws IOException {
        ftp.disconnect();
    }
}
