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

    /**
     * 在 namespace 下新增 tag
     *
     * @param namespaceId namespaceId
     * @param tag         tag
     * @param maxId       最大 Id
     * @param step        步长
     * @param description 描述
     */
    void add(@Param("namespaceId") String namespaceId, @Param("tag") String tag
            , @Param("maxId") long maxId, @Param("step") int step, @Param("description") String description);

    /**
     * 在 namespace 下删除 tag
     *
     * @param namespaceId namespaceId
     * @param tag         tag
     */
    void remove(@Param("namespaceId") String namespaceId, @Param("tag") String tag);


    void updateMaxId(@Param("tag") String tag);

    UniqueRecord getUniqueRecord(@Param("tag") String tag);

    void updateMaxIdByCustomStep(@Param("tag") String tag, @Param("step") int step);
}
