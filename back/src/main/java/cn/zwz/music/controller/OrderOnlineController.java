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
@Api(tags = "线上订单管理接口")
@RequestMapping("/zwz/orderOnline")
@Transactional
public class OrderOnlineController {

    @Autowired
    private IOrderOnlineService iOrderOnlineService;

    @Autowired
    private IInstrumentService iInstrumentService;

    @Autowired
    private IUserService iUserService;

    @Autowired
    private SecurityUtil securityUtil;

    @RequestMapping(value = "/addOne", method = RequestMethod.GET)
    @ApiOperation(value = "新增")
    public Result<OrderOnline> addOne(@RequestParam String id,@RequestParam String time){
        Instrument i = iInstrumentService.getById(id);
        if(i == null) {
            return ResultUtil.error("乐器不存在");
        }
        User currUser = securityUtil.getCurrUser();
        OrderOnline o = new OrderOnline();
        o.setUserId(currUser.getId());
        o.setUserName(currUser.getNickname());
        o.setInsId(i.getId());
        o.setInsName(i.getTitle());
        o.setStatus("未审核");
        o.setPrice2(BigDecimal.ZERO);
        o.setPrice3(BigDecimal.ZERO);
        o.setPrice4(i.getPrice4());
        o.setPrice5(BigDecimal.ZERO);
        o.setTime(time);
        iOrderOnlineService.saveOrUpdate(o);
        i.setStatus("租赁中");
        iInstrumentService.saveOrUpdate(i);
        return ResultUtil.success();
    }

    @RequestMapping(value = "/re", method = RequestMethod.GET)
    @ApiOperation(value = "查询单条乐器库存")
    public Result<Instrument> re(@RequestParam String id,@RequestParam String status){
        OrderOnline o = iOrderOnlineService.getById(id);
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
        iOrderOnlineService.saveOrUpdate(o);
        i.setStatus("正常");
        iInstrumentService.saveOrUpdate(i);
        return ResultUtil.success();
    }

    @RequestMapping(value = "/getOne", method = RequestMethod.GET)
    @ApiOperation(value = "查询单条线上订单")
    public Result<OrderOnline> get(@RequestParam String id){
        return new ResultUtil<OrderOnline>().setData(iOrderOnlineService.getById(id));
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    @ApiOperation(value = "查询全部线上订单个数")
    public Result<Long> getCount(){
        return new ResultUtil<Long>().setData(iOrderOnlineService.count());
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "查询全部线上订单")
    public Result<List<OrderOnline>> getAll(){
        return new ResultUtil<List<OrderOnline>>().setData(iOrderOnlineService.list());
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "查询线上订单")
    public Result<IPage<OrderOnline>> getByPage(@ModelAttribute OrderOnline orderOnline ,@ModelAttribute PageVo page){
        QueryWrapper<OrderOnline> qw = new QueryWrapper<>();
        User currUser = securityUtil.getCurrUser();
        QueryWrapper<User> userQw = new QueryWrapper<>();
        userQw.eq("id",currUser.getId());
        userQw.inSql("id","SELECT user_id FROM a_user_role WHERE del_flag = 0 AND role_id = '1536606659751841799'");
        long count = iUserService.count(userQw);
        if(count < 1L) {
            qw.eq("user_id",currUser.getId());
        }
        if(!ZwzNullUtils.isNull(orderOnline.getInsName())) {
            qw.like("ins_name",orderOnline.getInsName());
        }
        IPage<OrderOnline> data = iOrderOnlineService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<OrderOnline>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "增改线上订单")
    public Result<OrderOnline> saveOrUpdate(OrderOnline orderOnline){
        if(iOrderOnlineService.saveOrUpdate(orderOnline)){
            return new ResultUtil<OrderOnline>().setData(orderOnline);
        }
        return ResultUtil.error();
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ApiOperation(value = "新增线上订单")
    public Result<OrderOnline> insert(OrderOnline orderOnline){
        iOrderOnlineService.saveOrUpdate(orderOnline);
        return new ResultUtil<OrderOnline>().setData(orderOnline);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation(value = "编辑线上订单")
    public Result<OrderOnline> update(OrderOnline orderOnline){
        iOrderOnlineService.saveOrUpdate(orderOnline);
        return new ResultUtil<OrderOnline>().setData(orderOnline);
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "删除线上订单")
    public Result<Object> delByIds(@RequestParam String[] ids){
        for(String id : ids){
            iOrderOnlineService.removeById(id);
        }
        return ResultUtil.success();
    }

    @RequestMapping(value = "/audit", method = RequestMethod.POST)
    @ApiOperation(value = "审核线上订单")
    public Result<Object> audit(@RequestParam String id){
        OrderOnline o = iOrderOnlineService.getById(id);
        if(o == null) {
            return ResultUtil.error("订单不存在");
        }
        o.setStatus("未归还");
        iOrderOnlineService.saveOrUpdate(o);
        return ResultUtil.success();
    }

    @RequestMapping(value = "/getAntvVoList1", method = RequestMethod.GET)
    @ApiOperation(value = "查询图表数据")
    public Result<List<AntvVo>> getAntvVoList1(){
        List<AntvVo> ansList = new ArrayList<>();
        List<OrderOnline> teacherList = iOrderOnlineService.list();
        for (OrderOnline o : teacherList) {
            String time = o.getTime();
            if(ZwzNullUtils.isNull(time) || time.length() < 10) {
                continue;
            }
            String sub = time.substring(0, 10);

            boolean flag = false;
            for (AntvVo vo : ansList) {
                if(Objects.equals(vo.getTitle(),sub)) {
                    flag = true;
                    vo.setValue(vo.getValue().add(o.getPrice5()));
                    break;
                }
            }
            if(!flag) {
                AntvVo vo = new AntvVo();
                vo.setTitle(sub);
                vo.setType("租赁金额");
                vo.setValue(o.getPrice5());
                ansList.add(vo);
            }
        }
        return new ResultUtil<List<AntvVo>>().setData(ansList);
    }

    @RequestMapping(value = "/getAntvVoList2", method = RequestMethod.GET)
    @ApiOperation(value = "查询图表数据")
    public Result<List<AntvVo>> getAntvVoList2(){
        List<AntvVo> ansList = new ArrayList<>();
        List<OrderOnline> teacherList = iOrderOnlineService.list();
        for (OrderOnline o : teacherList) {
            String time = o.getTime();
            if(ZwzNullUtils.isNull(time) || time.length() < 10) {
                continue;
            }
            String sub = time.substring(0, 10);

            boolean flag = false;
            for (AntvVo vo : ansList) {
                if(Objects.equals(vo.getTitle(),sub)) {
                    flag = true;
                    vo.setValue(vo.getValue().add(o.getPrice2()));
                    break;
                }
            }
            if(!flag) {
                AntvVo vo = new AntvVo();
                vo.setTitle(sub);
                vo.setType("赔付金额");
                vo.setValue(o.getPrice2());
                ansList.add(vo);
            }
        }
        return new ResultUtil<List<AntvVo>>().setData(ansList);
    }

    @RequestMapping(value = "/getAntvVoList3", method = RequestMethod.GET)
    @ApiOperation(value = "查询图表数据")
    public Result<List<AntvVo>> getAntvVoList3(){
        List<AntvVo> ansList = new ArrayList<>();
        List<OrderOnline> teacherList = iOrderOnlineService.list();
        for (OrderOnline o : teacherList) {
            String time = o.getTime();
            if(ZwzNullUtils.isNull(time) || time.length() < 10) {
                continue;
            }
            String sub = time.substring(0, 10);

            boolean flag = false;
            for (AntvVo vo : ansList) {
                if(Objects.equals(vo.getTitle(),sub)) {
                    flag = true;
                    vo.setValue(vo.getValue().add(o.getPrice5().add(o.getPrice2())));
                    break;
                }
            }
            if(!flag) {
                AntvVo vo = new AntvVo();
                vo.setTitle(sub);
                vo.setType("总收入金额");
                vo.setValue(o.getPrice5().add(o.getPrice2()));
                ansList.add(vo);
            }
        }
        return new ResultUtil<List<AntvVo>>().setData(ansList);
    }

    @RequestMapping(value = "/getAntvVoList4", method = RequestMethod.GET)
    @ApiOperation(value = "查询图表数据")
    public Result<List<AntvVo>> getAntvVoList4(){
        List<AntvVo> ansList = new ArrayList<>();
        List<OrderOnline> teacherList = iOrderOnlineService.list();
        for (OrderOnline o : teacherList) {
            String time = o.getTime();
            if(ZwzNullUtils.isNull(time) || time.length() < 10) {
                continue;
            }
            String sub = time.substring(0, 10);

            boolean flag = false;
            for (AntvVo vo : ansList) {
                if(Objects.equals(vo.getTitle(),sub)) {
                    flag = true;
                    vo.setValue(vo.getValue().add(o.getPrice5().add(o.getPrice2().subtract(o.getPrice3()))));
                    break;
                }
            }
            if(!flag) {
                AntvVo vo = new AntvVo();
                vo.setTitle(sub);
                vo.setType("纯收入金额");
                vo.setValue(o.getPrice5().add(o.getPrice2().subtract(o.getPrice3())));
                ansList.add(vo);
            }
        }
        return new ResultUtil<List<AntvVo>>().setData(ansList);
    }
}
