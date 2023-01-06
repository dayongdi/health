package com.yyds.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.javamenc.constant.MessageConstant;
import com.javamenc.entity.PageResult;
import com.javamenc.entity.QueryPageBean;
import com.javamenc.entity.Result;
import com.javamenc.pojo.Address;
import com.yyds.Service.MapService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/map")
@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_HEALTH_MANAGER')")
public class MapController {
    @Reference
    private MapService mapService;

    // 地图数据的分布查询
    @PostMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return mapService.pageQuery(queryPageBean);
    }

    // 删除一个地址
    @DeleteMapping("/delete")
    public Result delete(Integer id){
        try{
            mapService.delete(id);

            return new Result(true, MessageConstant.DELETE_ADDRESS_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_ADDRESS_FAIL);
        }
    }

    // 添加一个新地址
    @PostMapping("/add")
    public Result add(@RequestBody Address address){
        try{
            mapService.add(address);
            return new Result(true,MessageConstant.ADD_ADDRESS_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_ADDRESS_FAIL);
        }
    }

    // 获取公司地址的详细信息
    @GetMapping("/getAddressDetail")
    public Result getAddressDetail(){
        try{
            List<Address> list = mapService.getAddressDetail();
            return new Result(true, MessageConstant.GET_ADDRESS_SUCCESS, list);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_ADDRESS_FAIL);
        }
    }

}
