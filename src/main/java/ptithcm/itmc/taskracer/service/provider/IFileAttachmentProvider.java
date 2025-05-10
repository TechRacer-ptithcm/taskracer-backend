package ptithcm.itmc.taskracer.service.provider;

import ptithcm.itmc.taskracer.common.file.FileInfo;

public interface IFileAttachmentProvider {
    FileInfo getFile(String fileName);
}
