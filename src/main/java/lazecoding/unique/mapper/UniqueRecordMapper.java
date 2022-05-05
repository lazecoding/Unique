package lazecoding.unique.mapper;

import lazecoding.model.UniqueRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @className: UniqueRecordMapper
 * @description:
 * @datetime: 2020/10/12   21:17
 * @author: lazecoding
 */
@Mapper
public interface UniqueRecordMapper {

    /**
     * 获取 namespace 下 tags
     *
     * @param namespaceId namespaceId
     * @return List<String>
     */
    List<String> getTags(@Param("namespaceId") String namespaceId);

    void updateMaxId(@Param("tag") String tag);

    UniqueRecord getUniqueRecord(@Param("tag") String tag);

    void updateMaxIdByCustomStep(@Param("tag") String tag, @Param("step") int step);
}
