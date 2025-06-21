package cn.zwz.music.controller;

import cn.hutool.core.date.DateUtil;
import cn.zwz.basics.utils.PageUtil;
import cn.zwz.basics.utils.ResultUtil;
import cn.zwz.basics.baseVo.PageVo;
import cn.zwz.basics.baseVo.Result;
import cn.zwz.basics.utils.SecurityUtil;
import cn.zwz.data.entity.User;
import cn.zwz.data.utils.ZwzNullUtils;
import cn.zwz.music.entity.OrderComment;
import cn.zwz.music.entity.OrderOnline;
import cn.zwz.music.service.IInstrumentService;
import cn.zwz.music.service.IOrderCommentService;
import cn.zwz.music.service.IOrderOnlineService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@RestController
@Api(tags = "订单评论管理接口")
@RequestMapping("/zwz/orderComment")
@Transactional
public class OrderCommentController {

    @Autowired
    private IOrderCommentService iOrderCommentService;

    @Autowired
    private IOrderOnlineService iOrderOnlineService;

    @Autowired
    private SecurityUtil securityUtil;

    @RequestMapping(value = "/getOne", method = RequestMethod.GET)
    @ApiOperation(value = "查询单条订单评论")
    public Result<OrderComment> get(@RequestParam String id){
        return new ResultUtil<OrderComment>().setData(iOrderCommentService.getById(id));
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    @ApiOperation(value = "查询全部订单评论个数")
    public Result<Long> getCount(){
        return new ResultUtil<Long>().setData(iOrderCommentService.count());
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "查询全部订单评论")
    public Result<List<OrderComment>> getAll(){
        return new ResultUtil<List<OrderComment>>().setData(iOrderCommentService.list());
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "查询订单评论")
    public Result<IPage<OrderComment>> getByPage(@ModelAttribute OrderComment orderComment ,@ModelAttribute PageVo page){
        QueryWrapper<OrderComment> qw = new QueryWrapper<>();
        if(!ZwzNullUtils.isNull(orderComment.getUserName())) {
            qw.like("user_name",orderComment.getUserName());
        }
        IPage<OrderComment> data = iOrderCommentService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<OrderComment>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "增改订单评论")
    public Result<OrderComment> saveOrUpdate(OrderComment orderComment){
        if(iOrderCommentService.saveOrUpdate(orderComment)){
            return new ResultUtil<OrderComment>().setData(orderComment);
        }
        return ResultUtil.error();
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ApiOperation(value = "新增订单评论")
    public Result<OrderComment> insert(OrderComment orderComment){
        OrderOnline order = iOrderOnlineService.getById(orderComment.getTitle());
        if(order == null) {
            return ResultUtil.error("订单不存在");
        }
        User currUser = securityUtil.getCurrUser();
        orderComment.setOrderContent(order.getUserName() + order.getInsName());
        orderComment.setTime(DateUtil.now());
        orderComment.setUserName(currUser.getNickname());
        orderComment.setReplyContent("");
        orderComment.setReplyUser("");
        orderComment.setReplyTime("");
        iOrderCommentService.saveOrUpdate(orderComment);
        return new ResultUtil<OrderComment>().setData(orderComment);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation(value = "编辑订单评论")
    public Result<OrderComment> update(OrderComment orderComment){
        User currUser = securityUtil.getCurrUser();
        orderComment.setReplyTime(DateUtil.now());
        orderComment.setReplyUser(currUser.getNickname());
        iOrderCommentService.saveOrUpdate(orderComment);
        return new ResultUtil<OrderComment>().setData(orderComment);
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "删除订单评论")
    public Result<Object> delByIds(@RequestParam String[] ids){
        for(String id : ids){
            iOrderCommentService.removeById(id);
        }
        return ResultUtil.success();
    }
}
