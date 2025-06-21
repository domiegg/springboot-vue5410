package cn.zwz.music.controller;

import cn.zwz.basics.utils.PageUtil;
import cn.zwz.basics.utils.ResultUtil;
import cn.zwz.basics.baseVo.PageVo;
import cn.zwz.basics.baseVo.Result;
import cn.zwz.data.utils.ZwzNullUtils;
import cn.zwz.music.entity.InstrumentType;
import cn.zwz.music.service.IInstrumentTypeService;
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
@Api(tags = "乐器信息管理接口")
@RequestMapping("/zwz/instrumentType")
@Transactional
public class InstrumentTypeController {

    @Autowired
    private IInstrumentTypeService iInstrumentTypeService;

    @RequestMapping(value = "/getOne", method = RequestMethod.GET)
    @ApiOperation(value = "查询单条乐器信息")
    public Result<InstrumentType> get(@RequestParam String id){
        return new ResultUtil<InstrumentType>().setData(iInstrumentTypeService.getById(id));
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    @ApiOperation(value = "查询全部乐器信息个数")
    public Result<Long> getCount(){
        return new ResultUtil<Long>().setData(iInstrumentTypeService.count());
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "查询全部乐器信息")
    public Result<List<InstrumentType>> getAll(){
        return new ResultUtil<List<InstrumentType>>().setData(iInstrumentTypeService.list());
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "查询乐器信息")
    public Result<IPage<InstrumentType>> getByPage(@ModelAttribute InstrumentType instrumentType ,@ModelAttribute PageVo page){
        QueryWrapper<InstrumentType> qw = new QueryWrapper<>();
        if(!ZwzNullUtils.isNull(instrumentType.getTitle())) {
            qw.like("title",instrumentType.getTitle());
        }
        IPage<InstrumentType> data = iInstrumentTypeService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<InstrumentType>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "增改乐器信息")
    public Result<InstrumentType> saveOrUpdate(InstrumentType instrumentType){
        if(iInstrumentTypeService.saveOrUpdate(instrumentType)){
            return new ResultUtil<InstrumentType>().setData(instrumentType);
        }
        return ResultUtil.error();
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ApiOperation(value = "新增乐器信息")
    public Result<InstrumentType> insert(InstrumentType instrumentType){
        iInstrumentTypeService.saveOrUpdate(instrumentType);
        return new ResultUtil<InstrumentType>().setData(instrumentType);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation(value = "编辑乐器信息")
    public Result<InstrumentType> update(InstrumentType instrumentType){
        iInstrumentTypeService.saveOrUpdate(instrumentType);
        return new ResultUtil<InstrumentType>().setData(instrumentType);
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "删除乐器信息")
    public Result<Object> delByIds(@RequestParam String[] ids){
        for(String id : ids){
            iInstrumentTypeService.removeById(id);
        }
        return ResultUtil.success();
    }
}
