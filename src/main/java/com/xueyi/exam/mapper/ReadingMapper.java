package com.xueyi.exam.mapper;

import com.xueyi.exam.beans.Reading;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author mike
 * @since 2020-12-11
 */
@Repository
public interface ReadingMapper extends BaseMapper<Reading> {

    List<Reading> getReadingsByPageId(int pageId);
}
