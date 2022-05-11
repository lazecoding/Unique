package lazecoding.unique.mapper;

import lazecoding.model.UniqueRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * UniqueRecordMapper
 *
 * @author lazecoding
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
     * 判断 namespace 下是否存在某 tag
     *
     * @param namespaceId namespaceId
     * @param tag         tag
     * @return tag
     */
    String existTag(@Param("namespaceId") String namespaceId, @Param("tag") String tag);

    /**
     * 在 namespace 下新增 tag
     *
     * @param uniqueRecord UniqueRecord
     * @return uid
     */
    void add(UniqueRecord uniqueRecord);

    /**
     * 在 namespace 下删除 tag
     *
     * @param namespaceId namespaceId
     * @param tag         tag
     */
    void remove(@Param("namespaceId") String namespaceId, @Param("tag") String tag);

    /**
     * 更新号段，step 取默认值
     *
     * @param namespaceId namespaceId
     * @param tag          tag
     */
    void updateMaxId(@Param("namespaceId") String namespaceId, @Param("tag") String tag);

    /**
     * 获取 UniqueRecord
     *
     * @param namespaceId namespaceId
     * @param tag          tag
     * @return UniqueRecord
     */
    UniqueRecord getUniqueRecord(@Param("namespaceId") String namespaceId, @Param("tag") String tag);

    /**
     * 更新号段，自定义 step
     *
     * @param namespaceId namespaceId
     * @param tag          tag
     * @param step         步长
     */
    void updateMaxIdByCustomStep(@Param("namespaceId") String namespaceId, @Param("tag") String tag, @Param("step") int step);
}
