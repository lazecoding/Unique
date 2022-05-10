package lazecoding.unique.mapper;

import lazecoding.model.NameSpace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * NameSpaceMapper
 *
 * @author: lazecoding
 */
@Mapper
public interface NameSpaceMapper {

    /**
     * 新增 namespace
     *
     * @param namespaceId namespaceId
     * @param description 描述
     */
    void add(@Param("namespaceId") String namespaceId, @Param("description") String description);

    /**
     * 删除
     *
     * @param namespaceId namespaceId
     */
    void remove(@Param("namespaceId") String namespaceId);

    /**
     * 根据 namespaceId 获取 NameSpace
     *
     * @param namespaceId namespaceId
     * @return NameSpace
     */
    NameSpace findById(@Param("namespaceId") String namespaceId);

}
