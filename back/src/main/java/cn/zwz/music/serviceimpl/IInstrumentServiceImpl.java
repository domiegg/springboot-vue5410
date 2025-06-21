package cn.zwz.music.serviceimpl;

import cn.zwz.music.mapper.InstrumentMapper;
import cn.zwz.music.entity.Instrument;
import cn.zwz.music.service.IInstrumentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 乐器库存 服务层接口实现
 * @author 郑为中
 * CSDN: Designer 小郑
 */
@Slf4j
@Service
@Transactional
public class IInstrumentServiceImpl extends ServiceImpl<InstrumentMapper, Instrument> implements IInstrumentService {

    @Autowired
    private InstrumentMapper instrumentMapper;
}