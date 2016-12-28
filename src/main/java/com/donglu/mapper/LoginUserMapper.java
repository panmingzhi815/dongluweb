package com.donglu.mapper;

import com.donglu.bean.CardUser;
import com.donglu.bean.SystemAccount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface LoginUserMapper {

    SystemAccount findOne(@Param(value = "accountName") String accountName);

    CardUser findCardUser(@Param(value = "idCard") String idCard);

    Long updateCardUserPassword(@Param(value = "loginName") String loginName, @Param(value = "newPassword") String newPassword);
}
