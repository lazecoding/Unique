package lazecoding.unique.service;

import lazecoding.exception.NilNameSpaceException;
import lazecoding.model.NameSpace;
import lazecoding.model.UniqueRecord;
import lazecoding.unique.mapper.UniqueRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * SegmentManager
 *
 * @author lazecoding
 */
@Component("segmentManager")
public class SegmentManager {

    @Autowired
    private UniqueRecordMapper uniqueRecordMapper;

    @Autowired
    private NameSpaceManager nameSpaceManager;

    /**
     * 获取 namespace 下号段
     */
    public List<String> getTags(String namespaceId) {
        // 1. 获取 NameSpace
        NameSpace nameSpace = nameSpaceManager.findById(namespaceId);
        if (ObjectUtils.isEmpty(nameSpace)){
            throw new NilNameSpaceException("该 namespace 不存在");
        }

        // 2. 判断是不是系统 namespace
        if (nameSpace.getType() == 1){
           namespaceId = "";
        }

        // 3. 根据 namespace 获取，namespaceId nil 获取全部
        return uniqueRecordMapper.getTags(namespaceId);
    }

    /**
     * 申请号段
     */
    @Transactional
    public UniqueRecord updateMaxIdAndGetUniqueRecord(String tag) {
        uniqueRecordMapper.updateMaxId(tag);
        return uniqueRecordMapper.getUniqueRecord(tag);
    }

    /**
     * 申请号段（自定义步长）
     */
    @Transactional
    public UniqueRecord updateMaxIdByCustomStepAndGetLeafAlloc(String tag, int step) {
        uniqueRecordMapper.updateMaxIdByCustomStep(tag, step);
        return uniqueRecordMapper.getUniqueRecord(tag);
    }

}