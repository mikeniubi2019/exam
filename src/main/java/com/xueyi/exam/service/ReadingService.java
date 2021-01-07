package com.xueyi.exam.service;

import com.xueyi.exam.beans.Reading;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mike
 * @since 2020-12-11
 */
public interface ReadingService extends IService<Reading> {

    List<Reading> getReadingsByPageId(int pageId);
}
