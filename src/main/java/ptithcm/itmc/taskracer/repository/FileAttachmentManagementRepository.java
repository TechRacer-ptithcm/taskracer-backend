package ptithcm.itmc.taskracer.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ptithcm.itmc.taskracer.common.file.FileInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Repository
public class FileAttachmentManagementRepository {
    @Value("${taskracer.file.storage-dir}")
    private String storageDir;

    public FileInfo storeFile(MultipartFile file) throws IOException {
        var path = Path.of(storageDir);
        var fileExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        var fileName = StringUtils.cleanPath(file.getOriginalFilename()).toLowerCase()
                .replaceAll("[^a-z0-9.\\s]", "")
                .replaceAll("\\s+", "-");
        var nameWithoutExtension = fileName.replace("." + fileExtension, "");
        var getRandomUUID = UUID.randomUUID().toString();
        var finalName = nameWithoutExtension + "-" + getRandomUUID + "." + fileExtension;

        var filePath = path.resolve(finalName).normalize().toAbsolutePath();

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return FileInfo.builder()
                .name(finalName)
                .size(file.getSize())
                .contentType(file.getContentType())
                .md5Checksum(DigestUtils.md5DigestAsHex(file.getInputStream()))
                .path(filePath.toString())
                .build();
    }
}
