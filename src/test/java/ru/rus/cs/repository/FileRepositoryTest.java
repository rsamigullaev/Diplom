package ru.rus.cs.repository;


import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.rus.cs.db.model.FileTable;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import ru.rus.cs.config.SystemJpaTest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest

public class FileRepositoryTest {

    @Autowired
    FileRepository fileRepository;

    public static final String FILENAME = "file";
    public static final String FILENAME_2 = "file2";
    public static final String NEW_FILE_NAME = "new_file";
    public static final Long USER_ID = 1L;
    public static final Optional<Long> USER_ID_OPTIONAL = Optional.of(1L);

    public static final MockMultipartFile TEST_FILE
            = new MockMultipartFile(
            "file",
            "hello.txt",
            MediaType.TEXT_PLAIN_VALUE,
            "Hello, World!".getBytes()
    );

    public static FileTable TEST_CLOUD_FILE = null;
    public static FileTable TEST_CLOUD_FILE_2 = null;

    static {
        try {
            TEST_CLOUD_FILE = new FileTable(FILENAME,
                    LocalDateTime.parse("2018-12-30T19:34:50.63"),
                    TEST_FILE.getContentType(),
                    TEST_FILE.getBytes(),
                    TEST_FILE.getSize(),
                    USER_ID);
            TEST_CLOUD_FILE_2 = new FileTable(FILENAME_2,
                    LocalDateTime.parse("2018-12-30T19:34:50.63"),
                    TEST_FILE.getContentType(),
                    TEST_FILE.getBytes(),
                    TEST_FILE.getSize(),
                    USER_ID);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @DisplayName("Сохранение(загрузка) файла. Число insert должно равняться 1, select 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_data.sql"
    })

    void insertFile_thenAssertDmlCount() {

        //when
        FileTable savedFile = fileRepository.save(TEST_CLOUD_FILE);

        //then
        assertThat(savedFile.getFilename()).isEqualTo(FILENAME);
        assertThat(1);
        assertThat(1);
        assertThat(0);
        assertThat(0);


    }

    @DisplayName("Удаление файла. Число insert должно равняться 1, select 2, delete 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_data.sql"
    })
    void deleteFile_thenAssertDmlCount() {

        //when
        FileTable savedFile = fileRepository.save(TEST_CLOUD_FILE);
        fileRepository.deleteByUserIdAndFilename(USER_ID, FILENAME);
        FileTable deletedFile = fileRepository.findByUserIdAndFilename(USER_ID, FILENAME);

        //then
        assertThat(savedFile.getFilename()).isEqualTo(FILENAME);
        assertThat(deletedFile).isNull();
        assertThat(0);
        assertThat(1);
        assertThat(0);
        assertThat(1);

    }

    @DisplayName("Скачивание файла. Число insert должно равняться 1, select 2")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_data.sql"
    })
    void downloadFile_thenAssertDmlCount() {
        //when
        FileTable savedFile = fileRepository.save(TEST_CLOUD_FILE);
        FileTable downloadedFile = fileRepository.findByUserIdAndFilename(USER_ID, FILENAME);

        //then
        assertThat(savedFile.getFilename()).isEqualTo(FILENAME);
        assertThat(downloadedFile.getFilename()).isEqualTo(FILENAME);
        assertThat(2);
        assertThat(1);
        assertThat(0);


    }

    @DisplayName("Изменение имени файла. Число insert должно равняться 1, select 2, update 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_data.sql"
    })
    void updateFile_thenAssertDmlCount() {

        //when
        FileTable savedFile = fileRepository.save(TEST_CLOUD_FILE);
        FileTable beforeUpdate = fileRepository.findByUserIdAndFilename(USER_ID, FILENAME);
        fileRepository.updateFilenameByUserId(USER_ID, FILENAME, NEW_FILE_NAME);
        FileTable updatedFileOld = fileRepository.findByUserIdAndFilename(USER_ID, FILENAME);
        FileTable updatedFileNew = fileRepository.findByUserIdAndFilename(USER_ID, NEW_FILE_NAME);

        //then
        assertThat(savedFile.getFilename()).isEqualTo(FILENAME);
        assertThat(beforeUpdate.getFilename()).isEqualTo(FILENAME);
        Assertions.assertNull(updatedFileOld);
        assertThat(updatedFileNew.getFilename()).isEqualTo(NEW_FILE_NAME);
        assertThat(4);
        assertThat(1);
        assertThat(0);

    }

    @DisplayName("Получение списка файлов. Число insert должно равняться 2, select 2")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_data.sql"
    })
    void getAllFiles_thenAssertDmlCount() {
        //given
        List<FileTable> expectedList = new ArrayList<>();
        expectedList.add(TEST_CLOUD_FILE_2);
        expectedList.add(TEST_CLOUD_FILE);

        //when
        FileTable savedFile1 = fileRepository.save(TEST_CLOUD_FILE);
        FileTable savedFile2 = fileRepository.save(TEST_CLOUD_FILE_2);
        List<FileTable> resultList = fileRepository.findAllByUserIdWithLimit(USER_ID, 2);

        //then
        assertThat(savedFile1.getFilename()).isEqualTo(FILENAME);
        assertThat(savedFile2.getFilename()).isEqualTo(FILENAME_2);
        Assertions.assertEquals(expectedList, resultList);


    }
}
