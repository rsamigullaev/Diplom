package ru.rus.cs.mapper;


import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.rus.cs.db.model.FileTable;
import ru.rus.cs.web.model.FileWebResponse;

@Component
@Mapper(componentModel = "spring")
public interface FileMapper {
    FileWebResponse cloudFileToFileWebResponse(final FileTable table);
}
