package ru.rus.cs.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.rus.cs.mapper.FileMapper;
import ru.rus.cs.service.FileService;
import ru.rus.cs.web.model.FileNameEditRequest;
import ru.rus.cs.web.model.FileWebResponse;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;
    private final FileMapper fileMapper;

    @PostMapping("/file")
    public ResponseEntity<?> uploadFile(
            @RequestHeader("auth-token") String authToken,
            @RequestParam("filename") String filename,
            MultipartFile file
    ) {
        fileService.uploadFile(authToken, filename, file);
        log.info(String.format("File %s uploaded successfully", filename));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/file")
    public ResponseEntity<?> deleteFile(
            @RequestHeader("auth-token") String authToken,
            @RequestParam("filename") String filename
    ) {
        fileService.deleteFile(authToken, filename);
        log.info(String.format("File %s deleted successfully", filename));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/file")
    public ResponseEntity<byte[]> downloadFile(@RequestHeader("auth-token") String authToken, @RequestParam("filename") String filename) {
        final var file = fileService.downloadFile(authToken, filename);
        log.info(String.format("File %s loaded successfully", filename));
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file.getContent());
    }

    @PutMapping("/file")
    public ResponseEntity<?> editFile(
            @RequestHeader("auth-token") String authToken,
            @RequestParam("filename") String filename,
            @RequestBody FileNameEditRequest editRequest
    ) {
        fileService.editFile(authToken, filename, editRequest.filename());
        log.info("File edited successfully");
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/list")
    public List<FileWebResponse> getAllFiles(
            @RequestHeader("auth-token") String authToken,
            @RequestParam("limit") Integer limit
    ) {
        final var files = fileService.getAllFiles(authToken, limit);
        log.info(String.format("Files %s received successfully", files));
        List <FileWebResponse> fileWebResponse = files.stream()
                .map(fileMapper::cloudFileToFileWebResponse)
                .sorted(Comparator.comparing(FileWebResponse::filename))
                .collect(Collectors.toList());
        return fileWebResponse;
    }
}
