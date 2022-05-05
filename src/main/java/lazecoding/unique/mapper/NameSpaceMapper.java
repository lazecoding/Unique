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
     * @param type        类型
     */
    void add(@Param("namespaceId") String namespaceId, @Param("description") String description, @Param("type") int type);

    /**
     * 删除（但是只能删除 type = 0）
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
