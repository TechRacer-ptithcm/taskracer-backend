package ptithcm.itmc.taskracer.service.provider.internal.file;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ptithcm.itmc.taskracer.common.file.FileInfo;
import ptithcm.itmc.taskracer.exception.ResourceNotFound;
import ptithcm.itmc.taskracer.repository.JpaFileAttachmentRepository;
import ptithcm.itmc.taskracer.service.mapper.file.FileMapper;
import ptithcm.itmc.taskracer.service.provider.IFileAttachmentProvider;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "SERVICE-FILE-ATTACHMENT-PROVIDER")
public class DefaultFileAttachmentProvider implements IFileAttachmentProvider {
    private final JpaFileAttachmentRepository jpaFileAttachmentRepository;
    private final FileMapper mapper;

    @Override
    public FileInfo getFile(String fileName) {
        var data = jpaFileAttachmentRepository.findByName(fileName);
        if (data.isEmpty()) {
            throw new ResourceNotFound("File not found");
        }
        return mapper.toDto(data.get());
    }
}
