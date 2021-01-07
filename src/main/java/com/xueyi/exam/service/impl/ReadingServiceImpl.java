package com.xueyi.exam.service.impl;

import com.xueyi.exam.beans.Reading;
import com.xueyi.exam.mapper.ReadingMapper;
import com.xueyi.exam.service.ReadingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mike
 * @since 2020-12-11
 */
@Service
public class ReadingServiceImpl extends ServiceImpl<ReadingMapper, Reading> implements ReadingService {
    @Autowired
    ReadingMapper readingMapper;
    @Override
    public List<Reading> getReadingsByPageId(int pageId) {
        return readingMapper.getReadingsByPageId(pageId);
    }
}
