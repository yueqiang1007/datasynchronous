package com.datasynchronous.service;

import com.datasynchronous.dao.EntityMapper;
import com.datasynchronous.entity.SqlVo;
import com.datasynchronous.utils.SqlDispose;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.Properties;

/**
 * Created by Administrator on 2017/6/23.
 */
@Service
public class EntityService {
    @Autowired
    private EntityMapper entityMapper;

    SqlDispose sqlDispose = new SqlDispose();

    public void savetest(String filename){
        SqlVo sqlVo = null;
        try {
           List<String> list =  sqlDispose.dynamicReading(filename);//D://logs//recivelogs

            if (list.size()==0){
                return;
            }
           //System.out.println(list);
           for (String sq:list){
               sqlVo = new SqlVo();
               System.out.println("sql--------->"+sq);
               sqlVo.setSql(sq);
               try{
               entityMapper.savetest(sqlVo);
                   states("yes");
               }catch(Exception e){
                   states("no");
               }
           }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //记录一个状态，用于判断sql语句是否执行成功
    public void states(String str) throws IOException {

      /*  ClassLoader classLoader = getClass().getClassLoader();
        *//**
         getResource()方法会去classpath下找这个文件，获取到url resource, 得到这个资源后，调用url.getFile获取到 文件 的绝对路径
         *//*
        URL url = classLoader.getResource("states.properties");*/


        Properties properties =new Properties();
        File file = new File("E://proper//states.properties");
        if(!file.exists()){
            return;
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            properties.load(fileInputStream);
            properties.setProperty("state",str);
            properties.store(fileOutputStream,"更新配置文件");
            fileOutputStream.close();
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
