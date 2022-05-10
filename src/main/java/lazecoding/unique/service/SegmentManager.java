package lazecoding.unique.service;

import lazecoding.model.UniqueRecord;
import lazecoding.unique.mapper.UniqueRecordMapper;
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
    public UniqueRecord updateMaxIdAndGetUniqueRecord(String tag) {
        uniqueRecordMapper.updateMaxId(tag);
        return uniqueRecordMapper.getUniqueRecord(tag);
    }

    /**
     * 申请号段（自定义步长）
     */
    @Transactional(rollbackFor = Exception.class)
    public UniqueRecord updateMaxIdByCustomStepAndGetLeafAlloc(String tag, int step) {
        uniqueRecordMapper.updateMaxIdByCustomStep(tag, step);
        return uniqueRecordMapper.getUniqueRecord(tag);
    }

}