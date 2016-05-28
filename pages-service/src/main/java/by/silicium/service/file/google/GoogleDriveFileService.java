package by.silicium.service.file.google;

import by.silicium.domain.File;
import by.silicium.service.file.FileService;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.FileList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kiryl_Parfiankou on 5/23/2016.
 */
@Service
public class GoogleDriveFileService implements FileService {

    private GoogleCredential credential;

    private static Logger logger = LoggerFactory.getLogger(GoogleDriveFileService.class);

    /** Application name. */
    private static final String APPLICATION_NAME="resource-connector";

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;

    /** Directory to store user credentials for this application. */
    private static final java.io.File CLIENT_SECRET = new java.io.File(
            System.getProperty("user.home"), ".credentials/resource-connector.json");

    /** Global instance of the {@link FileDataStoreFactory}. */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    private Drive driveService;



    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    public GoogleDriveFileService() {
        try {
            GoogleCredential credential = authorize();
            driveService = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME).build();
            logger.info("GoogleDriveFileService was constructed");
        }catch(IOException e) {
            logger.error("GoogleDriveFileService was not constructed");
            e.printStackTrace();
        }
    }

    public static GoogleCredential authorize() throws IOException {
        return GoogleCredential.fromStream(new FileInputStream(CLIENT_SECRET)).createScoped(DriveScopes.all());
    }

    @Override
    public String getList() {

        StringBuffer sbuf = new StringBuffer();
        try {
            logger.info("Pre get!");
            List<com.google.api.services.drive.model.File> result = new ArrayList<>();
            Drive.Files.List request = driveService.files().list();
            request.setPageSize(10);
            logger.info("After get!");
            do {
                try {
                    FileList files = request.execute();
                    logger.info("size: " + files.getFiles().size());

                    if (logger.isInfoEnabled()) {
                        for(com.google.api.services.drive.model.File file: files.getFiles()) {
                            logger.info("File: " + file.getName());
                        }
                    }

                    result.addAll(files.getFiles());
                    request.setPageToken(files.getNextPageToken());

                } catch (IOException e) {
                    logger.error("An error occurred: " + e.getMessage());
                    request.setPageToken(null);
                }
            } while (request.getPageToken() != null &&
                    request.getPageToken().length() > 0);


            for (com.google.api.services.drive.model.File file: result){
                sbuf.append(file.getName());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return sbuf.toString();
    }
}
