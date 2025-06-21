package cn.zwz.music.controller;

import cn.zwz.basics.utils.PageUtil;
import cn.zwz.basics.utils.ResultUtil;
import cn.zwz.basics.baseVo.PageVo;
import cn.zwz.basics.baseVo.Result;
import cn.zwz.data.utils.ZwzNullUtils;
import cn.zwz.music.entity.Instrument;
import cn.zwz.music.service.IInstrumentService;
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
@Api(tags = "乐器库存管理接口")
@RequestMapping("/zwz/instrument")
@Transactional
public class InstrumentController {

    @Autowired
    private IInstrumentService iInstrumentService;

    @RequestMapping(value = "/getOne", method = RequestMethod.GET)
    @ApiOperation(value = "查询单条乐器库存")
    public Result<Instrument> get(@RequestParam String id){
        return new ResultUtil<Instrument>().setData(iInstrumentService.getById(id));
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    @ApiOperation(value = "查询全部乐器库存个数")
    public Result<Long> getCount(){
        return new ResultUtil<Long>().setData(iInstrumentService.count());
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "查询全部乐器库存")
    public Result<List<Instrument>> getAll(){
        return new ResultUtil<List<Instrument>>().setData(iInstrumentService.list());
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "查询乐器库存")
    public Result<IPage<Instrument>> getByPage(@ModelAttribute Instrument instrument ,@ModelAttribute PageVo page){
        QueryWrapper<Instrument> qw = new QueryWrapper<>();
        if(!ZwzNullUtils.isNull(instrument.getTitle())) {
            qw.like("title",instrument.getTitle());
        }
        IPage<Instrument> data = iInstrumentService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<Instrument>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "增改乐器库存")
    public Result<Instrument> saveOrUpdate(Instrument instrument){
        if(iInstrumentService.saveOrUpdate(instrument)){
            return new ResultUtil<Instrument>().setData(instrument);
        }
        return ResultUtil.error();
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ApiOperation(value = "新增乐器库存")
    public Result<Instrument> insert(Instrument instrument){
        iInstrumentService.saveOrUpdate(instrument);
        return new ResultUtil<Instrument>().setData(instrument);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation(value = "编辑乐器库存")
    public Result<Instrument> update(Instrument instrument){
        iInstrumentService.saveOrUpdate(instrument);
        return new ResultUtil<Instrument>().setData(instrument);
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "删除乐器库存")
    public Result<Object> delByIds(@RequestParam String[] ids){
        for(String id : ids){
            iInstrumentService.removeById(id);
        }
        return ResultUtil.success();
    }
}
