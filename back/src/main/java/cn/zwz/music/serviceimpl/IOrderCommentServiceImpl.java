package cn.zwz.music.serviceimpl;

import cn.zwz.music.mapper.OrderCommentMapper;
import cn.zwz.music.entity.OrderComment;
import cn.zwz.music.service.IOrderCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 订单评论 服务层接口实现
 * @author 郑为中
 * CSDN: Designer 小郑
 */
@Slf4j
@Service
@Transactional
public class IOrderCommentServiceImpl extends ServiceImpl<OrderCommentMapper, OrderComment> implements IOrderCommentService {

    @Autowired
    private OrderCommentMapper orderCommentMapper;
}