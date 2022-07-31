package lazecoding.unique.service;

import lazecoding.unique.mapper.UniqueRecordMapper;
import lazecoding.unique.model.UniqueRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * SegmentManager
 *
 * @author lazecoding
 */
@Component("segmentManager")
public class SegmentManager {

    @Autowired
    private UniqueRecordMapper uniqueRecordMapper;

    /**
     * 申请号段
     */
    @Transactional(rollbackFor = Exception.class)
    public UniqueRecord updateMaxIdAndGetUniqueRecord(String namespace, String region, String tag) {
        uniqueRecordMapper.updateMaxId(namespace, region, tag);
        return uniqueRecordMapper.getUniqueRecord(namespace, region, tag);
    }

    /**
     * 申请号段（自定义步长）
     */
    @Transactional(rollbackFor = Exception.class)
    public UniqueRecord updateMaxIdByCustomStepAndGetLeafAlloc(String namespace, String region, String tag, int step) {
        uniqueRecordMapper.updateMaxIdByCustomStep(namespace, region, tag, step);
        return uniqueRecordMapper.getUniqueRecord(namespace, region, tag);
    }

}