package com.donglu.mapper;

import com.donglu.bean.CardUsage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by panmingzhi on 2016/11/27 0027.
 */
@Mapper
public interface AccessControlMapper {

    @Select("select * from CardUsage")
    List<CardUsage> findCardUsage();

    @Select("select count(id) from CardUsage")
    Long countCardUsage();
}
