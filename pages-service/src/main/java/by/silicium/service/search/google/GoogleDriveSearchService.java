package by.silicium.service.search.google;

import by.silicium.domain.Page;
import by.silicium.service.file.converter.FileToPageConverter;
import by.silicium.service.file.google.GoogleDrivePageService;
import by.silicium.service.search.SearchService;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kiryl Parfiankou on 29.5.16.
 */
public class GoogleDriveSearchService implements SearchService {

    private GoogleCredential credential;

    private static Logger logger = LoggerFactory.getLogger(GoogleDrivePageService.class);

    /** Application name. */
    private static final String APPLICATION_NAME="resource-connector";

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /** Default page size. */
    private static final int DEFAULT_PAGE_SIZE = 10;

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

    public static GoogleCredential authorize() throws IOException {
        return GoogleCredential.fromStream(new FileInputStream(CLIENT_SECRET)).createScoped(DriveScopes.all());
    }

    public GoogleDriveSearchService() {
        try {
            GoogleCredential credential = authorize();
            driveService = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME).build();
        } catch(IOException e) {
            logger.error("GoogleDriveSearchService was not constructed");
            e.printStackTrace();
        }
    }

    @Override
    public List<Page> searchFilesByFolder(String folderName) {

        logger.info("Processing Search Files By Folder request for {} folder.", folderName);
        List<Page> pages = new ArrayList<>();

        try {

            Drive.Files.List resultList = driveService.files().list();
            resultList.setPageSize(DEFAULT_PAGE_SIZE);

            FileList files;

            do {
                files = resultList.execute();

                for (File file: files.getFiles()) {
                    pages.add(FileToPageConverter.convert(file));
                }
            } while (resultList.getPageToken() != null &&
                    resultList.getPageToken().length() > 0);

        } catch (IOException e) {
            logger.error(e.toString());
        }

        return pages;
    }
}
