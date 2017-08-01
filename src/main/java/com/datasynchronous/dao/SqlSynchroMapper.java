package com.datasynchronous.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**同步sql语句
 * Created by Administrator on 2017/7/3.
 */
@Mapper
public interface SqlSynchroMapper {
    @Insert("#{sql}")
    public void saveSql(String sql);



}
