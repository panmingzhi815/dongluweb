package com.donglu.mapper;

import com.donglu.bean.Visitor;
import com.donglu.bean.VisitorBooking;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by panmingzhi on 2016/12/23 0023.
 */
@Mapper
@Component
public interface VisitorControlMapper {

    List<VisitorBooking> findVisitorBookList(Map<String, Object> conditionMap);

    Long countVisitorBookList(Map<String, Object> conditionMap);

    void insert(VisitorBooking visitorBooking);

    void update(VisitorBooking visitorBooking);

    Long deleteVisitorBooking(@Param(value = "id") Long id);

    VisitorBooking findVisitorBooking(@Param(value = "id") Long id);

    Visitor findVisitor(@Param(value = "id") Long id);
}
