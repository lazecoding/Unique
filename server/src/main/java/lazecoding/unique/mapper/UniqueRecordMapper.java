package lazecoding.unique.mapper;

import lazecoding.unique.model.UniqueRecord;
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
     * 获取 namespace-region 下 tags
     *
     * @param namespaceId namespaceId
     * @param region      区域
     * @return List<String>
     */
    List<String> getTags(@Param("namespaceId") String namespaceId, @Param("region") String region);

    /**
     * 判断 namespace-region 下是否存在某 tag
     *
     * @param namespaceId namespaceId
     * @param region      区域
     * @param tag         tag
     * @return tag
     */
    String existTag(@Param("namespaceId") String namespaceId, @Param("region") String region, @Param("tag") String tag);

    /**
     * 判断 namespace 下是否存在任意 tag
     *
     * @param namespaceId namespaceId
     * @return tag
     */
    String hasAnyTag(@Param("namespaceId") String namespaceId);

    /**
     * 在 namespace-region 下新增 tag
     *
     * @param uniqueRecord UniqueRecord
     * @return uid
     */
    void add(UniqueRecord uniqueRecord);

    /**
     * 在 namespace-region 下删除 tag
     *
     * @param namespaceId namespaceId
     * @param region      区域
     * @param tag         tag
     */
    void remove(@Param("namespaceId") String namespaceId, @Param("region") String region, @Param("tag") String tag);

    /**
     * 更新号段，step 取默认值
     *
     * @param namespaceId namespaceId
     * @param region      区域
     * @param tag         tag
     */
    void updateMaxId(@Param("namespaceId") String namespaceId, @Param("region") String region, @Param("tag") String tag);

    /**
     * 获取 UniqueRecord
     *
     * @param namespaceId namespaceId
     * @param region      区域
     * @param tag         tag
     * @return UniqueRecord
     */
    UniqueRecord getUniqueRecord(@Param("namespaceId") String namespaceId, @Param("region") String region, @Param("tag") String tag);

    /**
     * 更新号段，自定义 step
     *
     * @param namespaceId namespaceId
     * @param region      区域
     * @param tag         tag
     * @param step        步长
     */
    void updateMaxIdByCustomStep(@Param("namespaceId") String namespaceId, @Param("region") String region, @Param("tag") String tag, @Param("step") int step);
}
