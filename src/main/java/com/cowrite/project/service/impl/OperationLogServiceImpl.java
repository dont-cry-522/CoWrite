package com.cowrite.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowrite.project.mapper.OperationLogMapper;
import com.cowrite.project.model.entity.OperationLog;
import com.cowrite.project.service.OperationLogService;
import org.springframework.stereotype.Service;

/**
 * 日志服务实现类
 *
 * @author heathcetide
 */
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements OperationLogService {
}
