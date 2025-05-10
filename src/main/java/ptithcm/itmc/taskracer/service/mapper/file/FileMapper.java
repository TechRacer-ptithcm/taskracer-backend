package ptithcm.itmc.taskracer.service.mapper.file;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ptithcm.itmc.taskracer.common.file.FileInfo;
import ptithcm.itmc.taskracer.repository.model.JpaFileAttachment;

@Mapper(componentModel = "spring")
public interface FileMapper {
    @Mapping(target = "path", source = "fileUrl")
    FileInfo toDto(JpaFileAttachment jpa);
}
