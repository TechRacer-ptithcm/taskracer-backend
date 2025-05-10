package ptithcm.itmc.taskracer.controller.process.file;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ptithcm.itmc.taskracer.exception.ResourceNotFound;
import ptithcm.itmc.taskracer.service.FileAttachmentService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("media")
@RequiredArgsConstructor
public class FileController {
    private final FileAttachmentService fileAttachmentService;

    @GetMapping("{fileName}")
    public ResponseEntity<?> getStaticFile(@PathVariable String fileName)
            throws IOException {
        var data = fileAttachmentService.getFile(fileName);
        Path path = Paths.get(data.getPath());
        if (!Files.exists(path)) {
            throw new ResourceNotFound("File not found on storage");
        }
        var resource = new ByteArrayResource(Files.readAllBytes(path));
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.parseMediaType(data.getContentType()))
                .body(resource);
    }
}
