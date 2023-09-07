package ru.rus.cs.db.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "file", schema = "public")
public class FileTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    @Column(nullable = false)
    private String filename;
    @Column(nullable = false)
    private LocalDateTime date;
    @Column
    private String type;
    @Lob

    @Column(nullable = false, columnDefinition="clob")
    private byte[] content;
    @Column(nullable = false)
    private Long size;
    @Column(nullable = false)
    private Long userId;


    public FileTable(String fileName, LocalDateTime date, String type, byte[] content, long size, Long userId) {
        this.filename = fileName;
        this.date = date;
        this.type = type;
        this.content = content;
        this.size = size;
        this.userId = userId;
    }
}
