package com.donglu.mapper;

import com.donglu.bean.SystemAccount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LoginUserMapper {

    @Select("select * from SystemAccout lu where lu.accountName = #{accountName}")
    SystemAccount findOne(String accountName);
}
