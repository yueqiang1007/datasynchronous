package com.datasynchronous.dao;


import com.datasynchronous.entity.SqlVo;
import org.apache.ibatis.annotations.*;


/**
 * Created by Administrator on 2017/6/22.
 */
@Mapper
public interface EntityMapper {

    @Select("${sql}")
    void savetest(SqlVo sql);

}
