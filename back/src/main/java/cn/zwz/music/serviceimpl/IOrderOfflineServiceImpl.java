package cn.zwz.music.serviceimpl;

import cn.zwz.music.mapper.OrderOfflineMapper;
import cn.zwz.music.entity.OrderOffline;
import cn.zwz.music.service.IOrderOfflineService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 线下订单 服务层接口实现
 * @author 郑为中
 * CSDN: Designer 小郑
 */
@Slf4j
@Service
@Transactional
public class IOrderOfflineServiceImpl extends ServiceImpl<OrderOfflineMapper, OrderOffline> implements IOrderOfflineService {

    @Autowired
    private OrderOfflineMapper orderOfflineMapper;
}