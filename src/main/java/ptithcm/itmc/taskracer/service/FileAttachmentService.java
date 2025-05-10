package ptithcm.itmc.taskracer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ptithcm.itmc.taskracer.common.file.FileInfo;
import ptithcm.itmc.taskracer.service.provider.IFileAttachmentProvider;

@Service
@RequiredArgsConstructor
public class FileAttachmentService {
    private final IFileAttachmentProvider provider;

    public FileInfo getFile(String fileName) {
        return provider.getFile(fileName);
    }
}
