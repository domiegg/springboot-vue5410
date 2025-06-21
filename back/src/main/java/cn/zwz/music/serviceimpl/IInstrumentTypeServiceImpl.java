package cn.zwz.music.serviceimpl;

import cn.zwz.music.mapper.InstrumentTypeMapper;
import cn.zwz.music.entity.InstrumentType;
import cn.zwz.music.service.IInstrumentTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 乐器信息 服务层接口实现
 * @author 郑为中
 * CSDN: Designer 小郑
 */
@Slf4j
@Service
@Transactional
public class IInstrumentTypeServiceImpl extends ServiceImpl<InstrumentTypeMapper, InstrumentType> implements IInstrumentTypeService {

    @Autowired
    private InstrumentTypeMapper instrumentTypeMapper;
}