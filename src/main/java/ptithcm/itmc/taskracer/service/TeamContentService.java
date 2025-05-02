package ptithcm.itmc.taskracer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ptithcm.itmc.taskracer.common.web.response.PageableObject;
import ptithcm.itmc.taskracer.repository.FileAttachmentManagementRepository;
import ptithcm.itmc.taskracer.service.dto.team.TeamContentDto;
import ptithcm.itmc.taskracer.service.mapper.team.TeamContentServiceMapper;
import ptithcm.itmc.taskracer.service.processor.ITeamContentProcessor;
import ptithcm.itmc.taskracer.service.provider.ITeamContentProvider;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j(topic = "SERVICE-TEAM-CONTENT")
@RequiredArgsConstructor
public class TeamContentService {
    private final ITeamContentProcessor processor;
    private final ITeamContentProvider provider;
    private final TeamContentServiceMapper mapper;
    private FileAttachmentManagementRepository fileManagementRepository;

    public TeamContentDto getById(UUID id) {
        return provider.getById(id);
    }

    public PageableObject<List<TeamContentDto>> getAll(Pageable page) {
        return provider.getAll(page);
    }

    public TeamContentDto create(TeamContentDto teamContentDto, List<MultipartFile> files) throws IOException {
        for(MultipartFile file : files) {
            log.info("File name: {}", file.getOriginalFilename());
            var fileData = fileManagementRepository.storeFile(file);
            log.info("File data: {}", fileData);
        }
        var data = processor.save(teamContentDto);
        return mapper.toDto(data);
    }

    public TeamContentDto update(TeamContentDto teamContentDto) {
        var data = processor.update(teamContentDto);
        return mapper.toDto(data);
    }

    public void delete(UUID id, UUID userId) {
        processor.delete(id, userId);
    }
}
