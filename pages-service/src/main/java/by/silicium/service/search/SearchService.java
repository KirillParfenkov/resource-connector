package by.silicium.service.search;

import by.silicium.domain.Page;

import java.util.List;

/**
 * Created by Kiryl Parfiankou on 29.5.16.
 */
public interface SearchService {
    List<Page> searchFilesByFolder(String folderName);
}
