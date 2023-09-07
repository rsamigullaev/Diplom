package ru.rus.cs.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.rus.cs.db.model.FileTable;

import java.util.List;

@Repository
@Transactional
public interface FileRepository extends JpaRepository<FileTable, Long> {
    void deleteByUserIdAndFilename(final Long userId, final String filename);

    FileTable findByUserIdAndFilename(final Long userId, final String filename);

    @Query(value = "select * from file f where f.user_id = ?1 order by f.id desc limit ?2", nativeQuery = true)
    List<FileTable> findAllByUserIdWithLimit(final Long userId, final int limit);

    @Modifying(clearAutomatically = true)
    @Query("update FileTable f set f.filename = :newFilename where f.filename = :oldFilename and f.userId = :userId")
    void updateFilenameByUserId(
            @Param("userId") Long userId,
            @Param("oldFilename") String oldFilename,
            @Param("newFilename") String newFilename
    );
}
