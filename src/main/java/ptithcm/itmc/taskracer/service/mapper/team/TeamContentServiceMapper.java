package ptithcm.itmc.taskracer.service.mapper.team;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ptithcm.itmc.taskracer.common.file.FileInfo;
import ptithcm.itmc.taskracer.repository.model.JpaFileAttachment;
import ptithcm.itmc.taskracer.repository.model.JpaTeamContent;
import ptithcm.itmc.taskracer.service.dto.team.TeamContentDto;
import ptithcm.itmc.taskracer.service.mapper.tier.TierMapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {TierMapper.class, TeamServiceMapper.class})
public interface TeamContentServiceMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "fileAttachmentUrl", expression  = "java(toListUrl(jpa.getFileAttachment()))")
    @Mapping(target = "teamId", source = "team.id")
    TeamContentDto toDto(JpaTeamContent jpa);

    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "team.id", source = "teamId")
    @Mapping(target = "fileAttachment", expression = "java(toListJpaFile(dto.getFileAttachmentUrl()))")
    JpaTeamContent toJpa(TeamContentDto dto);

    default List<FileInfo> toListUrl(List<JpaFileAttachment> files) {
        if (files == null) return null;
        return files.stream()
                .map(file -> FileInfo.builder()
                        .path(file.getFileUrl())
                        .contentType(file.getContentType())
                        .md5Checksum(file.getMd5Checksum())
                        .size(file.getSize())
                        .name(file.getName())
                        .build())
                .toList();
    }

    default List<JpaFileAttachment> toListJpaFile(List<FileInfo> urls) {
        if (urls == null) return null;
        return urls.stream()
                .map(url -> JpaFileAttachment.builder()
                        .fileUrl(url.getPath())
                        .contentType(url.getContentType())
                        .md5Checksum(url.getMd5Checksum())
                        .size(url.getSize())
                        .name(url.getName())
                        .build())
                .toList();
    }

    default List<TeamContentDto> toDto(List<JpaTeamContent> contents)
    {
        return contents.stream().map(
                data -> TeamContentDto.builder()
                        .id(data.getId())
                        .content(data.getContent())
                        .fileAttachmentUrl(toListUrl(data.getFileAttachment()))
                        .likeCount(data.getLikeCount())
                        .teamId(data.getTeam().getId())
                        .userId(data.getUser().getId())
                        .build()
        ).toList();
    }

    default JpaTeamContent merge(JpaTeamContent source, JpaTeamContent target) {
        return JpaTeamContent.builder()
                .id(source.getId())
                .content(target.getContent())
                .user(source.getUser())
                .team(source.getTeam())
                .fileAttachment(source.getFileAttachment())
                .likeCount(source.getLikeCount())
                .build();
    }
}
