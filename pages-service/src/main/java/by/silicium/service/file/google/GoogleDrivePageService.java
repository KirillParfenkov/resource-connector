package by.silicium.service.file.google;

import by.silicium.domain.Page;
import by.silicium.service.file.PageService;
import by.silicium.service.file.converter.FileToPageConverter;
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
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Created by parf on 29.5.16.
 */
@Service
public class GoogleDrivePageService implements PageService {

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

    private static String Q_FOLDER_MIME_TYPE = "mimeType = 'application/vnd.google-apps.folder'";
    private static String Q_FILE_MIME_TYPE = "mimeType = 'application/vnd.google-apps.file'";
    private static String DRIVE_SPACE= "drive";

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

    public GoogleDrivePageService() {
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
    public List<Page> getFolderPages(String path) {

        logger.info("Processing Get Folder Pages request for {} path.", path);
        List<Page> pages = new ArrayList<>();

        try {

            File folder = getFolder(path);
            Drive.Files.List resultList = driveService.files().list();

            if (folder != null) {
                resultList.setQ("'"+ folder.getId() + "'" + " in parents")
                        .setPageSize(DEFAULT_PAGE_SIZE);
            }

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

    private String constructFolderFilter(List<String> folderList) {

        StringBuffer strBuff = new StringBuffer();
        if (folderList != null) {
            for (int i = 0; i < folderList.size(); i++) {
                if (i != 0) {
                    strBuff.append(" or ");
                }
                strBuff.append("name = '" + folderList.get(i) + "'");
            }
        }
        return strBuff.toString();
    }

    private File getFolder(String path) {

        File folder = null;
        Map<String,List<File>> folderMap = new HashMap<>();
        List<String> folderNameList = Arrays.asList(path.split("\\\\"));

        try {
            Drive.Files.List resultListForFolders = driveService.files().list();
            resultListForFolders.setQ(Q_FOLDER_MIME_TYPE + " and (" + constructFolderFilter(folderNameList) + ")");
            resultListForFolders.setSpaces(DRIVE_SPACE);
            resultListForFolders.setPageSize(DEFAULT_PAGE_SIZE);

            FileList folderList;

            do {
                folderList = resultListForFolders.execute();
                List<File> list;

                for (File file: folderList.getFiles()) {
                    list = folderMap.get(file.getName());
                    if (list == null) {
                        list = new ArrayList<>();
                    }
                    list.add(file);
                    folderMap.put(file.getName(), list);
                }
            } while (resultListForFolders.getPageToken() != null &&
                    resultListForFolders.getPageToken().length() > 0);

            List<File> list;
            String parentId = null;
            File lastFolder = null;
            for(int i = 0; i < folderNameList.size(); i++) {

                list = folderMap.get(folderNameList.get(i));
                if (list == null && list.size() > 0) {
                    return null;
                }

                // Checking of the hierarchy
                for(File file: list) {
                    if ( i == 0 && file.getParents() == null) {
                        parentId = file.getId();
                    } else if (i > 0 && parentId == file.getId()) {
                        parentId = file.getId();
                    }
                    lastFolder = file;
                }

                if (lastFolder == null) {
                    return null;
                }
            }

            folder = lastFolder;

        } catch (IOException e) {
            logger.error(e.toString());
        }

        return folder;
    }

    @Override
    public Page getPageById(String id) {
        return null;
    }

    @Override
    public List<Page> getFolderPages() {
        return null;
    }
}