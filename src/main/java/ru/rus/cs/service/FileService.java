package ru.rus.cs.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.rus.cs.db.model.FileTable;
import ru.rus.cs.db.model.UserTable;
import ru.rus.cs.exception.FileCloudException;
import ru.rus.cs.exception.InputDataException;
import ru.rus.cs.exception.UnauthorizedException;
import ru.rus.cs.mapper.FileMapper;
import ru.rus.cs.repository.AuthenticationRepository;
import ru.rus.cs.repository.FileRepository;
import ru.rus.cs.repository.UserRepository;
import ru.rus.cs.web.model.FileWebResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {
    private final AuthenticationRepository authenticationRepository;
    private final UserRepository userRepository;
    private final FileRepository fileRepository;
    private final FileMapper fileMapper;

    @Transactional
    public void uploadFile(
            final String authToken,
            final String filename,
            final MultipartFile file
    ) {
        final var userId = getUserIdFromToken(authToken);
        if (userId.isEmpty()) {
            log.error("Invalid auth-token: Unauthorized");
            throw new UnauthorizedException("Invalid auth-token: Unauthorized");
        }

        final var fileDb = fileRepository.findByUserIdAndFilename(userId.get(), filename);
        if (fileDb != null) {
            log.error(String.format(" The file with name %s already exists in the storage", filename));
            throw new InputDataException(String.format(" The file with name %s already exists in the storage. " +
                    "Please enter a new name for the file ", filename));
        }

        try {
            final var fileTable = new FileTable(filename, LocalDateTime.now(), file.getContentType(), file.getBytes(), file.getSize(), userId.get());
            fileRepository.save(fileTable);
            log.info("Success upload file. User with ID {}", userId.get());
        } catch (IOException e) {
            log.error("InputDataException: Upload file: Input data exception");
            throw new InputDataException("Upload file: Input data exception");
        }
    }

    @Transactional
    public void deleteFile(final String authToken, final String filename) {
        final var userId = getUserIdFromToken(authToken);
        if (userId.isEmpty()) {
            log.error("Invalid auth-token: Unauthorized");
            throw new UnauthorizedException("Invalid auth-token: Unauthorized");
        }

        fileRepository.deleteByUserIdAndFilename(userId.get(), filename);
        final var deletedFile = fileRepository.findByUserIdAndFilename(userId.get(), filename);
        if (deletedFile != null) {
            log.error("FileCloudException : The file has not been deleted");
            throw new FileCloudException("The file has not been deleted");
        }

        log.info("Success delete file. User with ID {}", userId.get());
    }

    @Transactional
    public FileTable downloadFile(final String authToken, final String filename) {
        final var userId = getUserIdFromToken(authToken);
        if (userId.isEmpty()) {
            log.error("Invalid auth-token: Unauthorized");
            throw new UnauthorizedException("Invalid auth-token: Unauthorized");
        }

        final var fileTable = fileRepository.findByUserIdAndFilename(userId.get(), filename);
        if (fileTable == null) {
            log.error(String.format("FileCloudException: The file with name %s was not found in the storage", filename));
            throw new FileCloudException(String.format("The file with name %s was not found in the storage", filename));
        }

        log.info("File downloaded successfully. User with ID {}", userId.get());
        return fileTable;
    }

    @Transactional
    public void editFile(
            final String authToken,
            final String filename,
            final String newFileName
    ) {
        final var userId = getUserIdFromToken(authToken);
        if (userId.isEmpty()) {
            log.error("Invalid auth-token: Unauthorized");
            throw new UnauthorizedException("Invalid auth-token: Unauthorized");
        }

        fileRepository.updateFilenameByUserId(userId.get(), filename, newFileName);

        final var editedFile = fileRepository.findByUserIdAndFilename(userId.get(), newFileName);
        if (editedFile == null) {
            log.error("FileCloudException : Edited file not found ");
            throw new FileCloudException("Edited file not found ");
        } else {
            log.info(String.format("File %s edited successfully. New filename: %s", filename, editedFile.getFilename()));
        }
    }

    @Transactional
    public List<FileWebResponse> getAllFiles(String authToken, int limit) {
        final var userId = getUserIdFromToken(authToken);
        if (userId.isEmpty()) {
            log.error("Invalid auth-token: Unauthorized");
            throw new UnauthorizedException("Invalid auth-token: Unauthorized");
        }

        final var files = fileRepository.findAllByUserIdWithLimit(userId.get(), limit);
        if (files == null) {
            log.error("FileCloudException: List of files not received ");
            throw new FileCloudException("List of files not received ");
        }

        return files.stream()
                .map(fileMapper::cloudFileToFileWebResponse)
                .sorted(Comparator.comparing(FileWebResponse::filename))
                .collect(Collectors.toList());
    }


    public Optional<Long> getUserIdFromToken(String authToken) {
        if (authToken.startsWith("Bearer ")) {
            final var authTokenWithoutBearer = authToken.split(" ")[1];
            final var username = authenticationRepository.getUserNameByToken(authTokenWithoutBearer);
            final var user = userRepository.findByUsername(username);
            return user.map(UserTable::getId);
        }

        return Optional.empty();
    }
}
