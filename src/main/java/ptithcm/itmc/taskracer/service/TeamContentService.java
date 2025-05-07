package ptithcm.itmc.taskracer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;
import ptithcm.itmc.taskracer.common.web.response.PageableObject;
import ptithcm.itmc.taskracer.repository.FileAttachmentManagementRepository;
import ptithcm.itmc.taskracer.repository.JpaTeamContentRepository;
import ptithcm.itmc.taskracer.repository.model.enumeration.Permission;
import ptithcm.itmc.taskracer.service.dto.team.TeamContentDto;
import ptithcm.itmc.taskracer.service.mapper.team.TeamContentServiceMapper;
import ptithcm.itmc.taskracer.service.processor.ITeamContentProcessor;
import ptithcm.itmc.taskracer.service.provider.ITeamContentProvider;
import ptithcm.itmc.taskracer.service.validator.IEligibilityRoleValidator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j(topic = "SERVICE-TEAM-CONTENT")
@RequiredArgsConstructor
public class TeamContentService {
    private final ITeamContentProcessor processor;
    private final ITeamContentProvider provider;
    private final TeamContentServiceMapper mapper;
    private final FileAttachmentManagementRepository fileManagementRepository;
    private final IEligibilityRoleValidator roleValidator;
    private final Validator validator;
    private final JpaTeamContentRepository jpaTeamContentRepository;

    public TeamContentDto getById(Integer teamId, UUID contentId, UUID userId) {
        roleValidator.validate(userId, teamId, Permission.TEAM_VIEW_POST);
        return mapper.toDto(provider.getById(contentId));
    }

    public PageableObject<List<TeamContentDto>> getAll(Pageable page, Integer teamId, UUID userId) {
        roleValidator.validate(userId, teamId, Permission.TEAM_VIEW_POST);
        var data = provider.getAll(page, teamId);
        return PageableObject.<List<TeamContentDto>>builder()
                .content(mapper.toDto(data.getContent()))
                .totalElements(data.getTotalElements())
                .totalPage(data.getTotalPage())
                .currentPage(data.getCurrentPage())
                .build();
    }

    public TeamContentDto create(Integer teamId, TeamContentDto teamContentDto, UUID userId ,List<MultipartFile> files) throws IOException {
        roleValidator.validate(userId, teamId, Permission.TEAM_CREATE_POST);
        if(files != null) {
            for(MultipartFile file : files) {
                log.info("File name: {}", file.getOriginalFilename());
                var fileData = fileManagementRepository.storeFile(file);
                log.info("File data: {}", fileData);
                if(teamContentDto.getFileAttachmentUrl() == null) {
                    teamContentDto.setFileAttachmentUrl(new ArrayList<>());
                }
                teamContentDto.getFileAttachmentUrl().add(fileData);
            }
        }
        teamContentDto.setUserId(userId);
        teamContentDto.setTeamId(teamId);
        var data = processor.save(teamContentDto);
        log.info("data: {}", data);
        return mapper.toDto(data);
    }

    public TeamContentDto update(Integer teamId,UUID contentId, TeamContentDto teamContentDto, UUID userId, List<MultipartFile> files) throws IOException{
        var contentExist = jpaTeamContentRepository.findById(contentId);
        if(!contentExist.get().getUser().getId().equals(userId))
        {
            roleValidator.validate(userId, teamId, Permission.TEAM_DELETE_POST);
        }
        if(files != null) {
            for(MultipartFile file : files) {
                var fileData = fileManagementRepository.storeFile(file);
                if(teamContentDto.getFileAttachmentUrl() == null) {
                    teamContentDto.setFileAttachmentUrl(new ArrayList<>());
                }
                teamContentDto.getFileAttachmentUrl().add(fileData);
            }
        }
        teamContentDto.setId(contentExist.get().getId());
        var data = processor.update(teamContentDto);
        return mapper.toDto(data);
    }

    public void delete(Integer teamId, UUID contentId, UUID userId) {
        var contentExist = jpaTeamContentRepository.findById(contentId);
        if(!contentExist.get().getUser().getId().equals(userId))
        {
            roleValidator.validate(userId, teamId, Permission.TEAM_DELETE_POST);
        }
        processor.delete(contentId);
    }
}
