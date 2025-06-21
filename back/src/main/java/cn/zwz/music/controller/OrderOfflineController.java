package cn.zwz.music.controller;

import cn.zwz.basics.utils.PageUtil;
import cn.zwz.basics.utils.ResultUtil;
import cn.zwz.basics.baseVo.PageVo;
import cn.zwz.basics.baseVo.Result;
import cn.zwz.basics.utils.SecurityUtil;
import cn.zwz.data.entity.User;
import cn.zwz.data.service.IUserService;
import cn.zwz.data.utils.ZwzNullUtils;
import cn.zwz.data.vo.AntvVo;
import cn.zwz.music.entity.Instrument;
import cn.zwz.music.entity.OrderOffline;
import cn.zwz.music.entity.OrderOnline;
import cn.zwz.music.service.IInstrumentService;
import cn.zwz.music.service.IOrderOfflineService;
import cn.zwz.music.service.IOrderOnlineService;
import cn.zwz.test.entity.Teacher;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Slf4j
@RestController
@Api(tags = "线下订单管理接口")
@RequestMapping("/zwz/orderOffline")
@Transactional
public class OrderOfflineController {

    @Autowired
    private IOrderOfflineService iOrderOfflineService;

    @Autowired
    private IInstrumentService iInstrumentService;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IOrderOnlineService iOrderOnlineService;

    @RequestMapping(value = "/addOne", method = RequestMethod.GET)
    @ApiOperation(value = "新增")
    public Result<OrderOnline> addOne(@RequestParam String id){
        Instrument i = iInstrumentService.getById(id);
        if(i == null) {
            return ResultUtil.error("乐器不存在");
        }
        User currUser = securityUtil.getCurrUser();
        OrderOffline o = new OrderOffline();
        o.setUserId(currUser.getId());
        o.setUserName(currUser.getNickname());
        o.setInsId(i.getId());
        o.setInsName(i.getTitle());
        o.setStatus("未归还");
        o.setPrice2(BigDecimal.ZERO);
        o.setPrice3(BigDecimal.ZERO);
        o.setPrice4(i.getPrice4());
        o.setPrice5(BigDecimal.ZERO);
        iOrderOfflineService.saveOrUpdate(o);
        i.setStatus("租赁中");
        iInstrumentService.saveOrUpdate(i);
        return ResultUtil.success();
    }

    @RequestMapping(value = "/re", method = RequestMethod.GET)
    @ApiOperation(value = "查询单条乐器库存")
    public Result<Instrument> re(@RequestParam String id,@RequestParam String status){
        OrderOffline o = iOrderOfflineService.getById(id);
        if(o == null) {
            return ResultUtil.error("订单不存在");
        }
        Instrument i = iInstrumentService.getById(o.getInsId());
        if(i == null) {
            return ResultUtil.error("乐器不存在");
        }
        if(Objects.equals("破损归还",status)) {
            o.setPrice2(i.getPrice2());
        } else if(Objects.equals("维修归还",status)) {
            o.setPrice3(i.getPrice3());
        }
        o.setStatus("已归还");
        iOrderOfflineService.saveOrUpdate(o);
        i.setStatus("正常");
        iInstrumentService.saveOrUpdate(i);
        return ResultUtil.success();
    }

    @RequestMapping(value = "/getOne", method = RequestMethod.GET)
    @ApiOperation(value = "查询单条线下订单")
    public Result<OrderOffline> get(@RequestParam String id){
        return new ResultUtil<OrderOffline>().setData(iOrderOfflineService.getById(id));
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    @ApiOperation(value = "查询全部线下订单个数")
    public Result<Long> getCount(){
        return new ResultUtil<Long>().setData(iOrderOfflineService.count());
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "查询全部线下订单")
    public Result<List<OrderOffline>> getAll(){
        return new ResultUtil<List<OrderOffline>>().setData(iOrderOfflineService.list());
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "查询线下订单")
    public Result<IPage<OrderOffline>> getByPage(@ModelAttribute OrderOffline orderOffline ,@ModelAttribute PageVo page){
        QueryWrapper<OrderOffline> qw = new QueryWrapper<>();
        User currUser = securityUtil.getCurrUser();
        QueryWrapper<User> userQw = new QueryWrapper<>();
        userQw.eq("id",currUser.getId());
        userQw.inSql("id","SELECT user_id FROM a_user_role WHERE del_flag = 0 AND role_id = '1536606659751841799'");
        long count = iUserService.count(userQw);
        if(count < 1L) {
            qw.eq("user_id",currUser.getId());
        }
        if(!ZwzNullUtils.isNull(orderOffline.getInsName())) {
            qw.like("ins_name",orderOffline.getInsName());
        }
        IPage<OrderOffline> data = iOrderOfflineService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<OrderOffline>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "增改线下订单")
    public Result<OrderOffline> saveOrUpdate(OrderOffline orderOffline){
        if(iOrderOfflineService.saveOrUpdate(orderOffline)){
            return new ResultUtil<OrderOffline>().setData(orderOffline);
        }
        return ResultUtil.error();
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ApiOperation(value = "新增线下订单")
    public Result<OrderOffline> insert(OrderOffline orderOffline){
        iOrderOfflineService.saveOrUpdate(orderOffline);
        return new ResultUtil<OrderOffline>().setData(orderOffline);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation(value = "编辑线下订单")
    public Result<OrderOffline> update(OrderOffline orderOffline){
        iOrderOfflineService.saveOrUpdate(orderOffline);
        return new ResultUtil<OrderOffline>().setData(orderOffline);
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "删除线下订单")
    public Result<Object> delByIds(@RequestParam String[] ids){
        for(String id : ids){
            iOrderOfflineService.removeById(id);
        }
        return ResultUtil.success();
    }


    @RequestMapping(value = "/getAntvVoList", method = RequestMethod.GET)
    @ApiOperation(value = "查询图表数据")
    public Result<List<AntvVo>> getAntvVoList(){
        List<AntvVo> ansList = new ArrayList<>();

        AntvVo vo = new AntvVo();
        vo.setTitle("线上订单");
        vo.setType("线上订单");
        vo.setValue(BigDecimal.valueOf(iOrderOnlineService.count()));
        ansList.add(vo);

        AntvVo v2o = new AntvVo();
        v2o.setTitle("工资金额");
        v2o.setType("工资金额");
        v2o.setValue(BigDecimal.valueOf(iOrderOfflineService.count()));
        ansList.add(v2o);
        return new ResultUtil<List<AntvVo>>().setData(ansList);
    }
}
