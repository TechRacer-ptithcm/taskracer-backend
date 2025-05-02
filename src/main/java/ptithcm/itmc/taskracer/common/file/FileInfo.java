package ptithcm.itmc.taskracer.common.file;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileInfo {
    String name;
    String contentType;
    long size;
    String md5Checksum;
    String path;
}
