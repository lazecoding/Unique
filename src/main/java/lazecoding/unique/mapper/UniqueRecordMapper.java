package lazecoding.unique.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import lazecoding.unique.model.UniqueRecord;

import java.util.List;

/**
 * @className: UniqueRecordMapper
 * @description:
 * @datetime: 2020/10/12   21:17
 * @author: lazecoding
 */
@Mapper
public interface UniqueRecordMapper {
    List<String> getAllTags();

    void updateMaxId(@Param("tag") String tag);

    UniqueRecord getUniqueRecord(@Param("tag") String tag);

    void updateMaxIdByCustomStep(@Param("tag") String tag, @Param("step") int step);
}
