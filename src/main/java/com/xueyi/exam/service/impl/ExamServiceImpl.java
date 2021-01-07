package com.xueyi.exam.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xueyi.exam.beans.Exam;
import com.xueyi.exam.mapper.ExamMapper;
import com.xueyi.exam.service.ExamService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mike
 * @since 2020-12-05
 */
@Service
public class ExamServiceImpl extends ServiceImpl<ExamMapper, Exam> implements ExamService {

}
