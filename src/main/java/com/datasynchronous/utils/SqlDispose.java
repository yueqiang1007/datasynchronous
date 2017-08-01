package com.datasynchronous.utils;

import com.datasynchronous.entity.SqlVo;
import com.datasynchronous.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.util.*;

/**对获取到的sql日志进行处理
 * 得到不同数据value
 * Created by Administrator on 2017/6/23.
 */

public class SqlDispose {
    @Autowired
    private EntityService entityService;


    @Value("${historyfilename}")
    private String historyfilename;

    @Value("${historicaldata}")
    private String historicaldata;


    @Value("${spring.profiles.active}")
    private String state;


    /**
     *动态的读取文件,并将文件名保存到数组中
     * @param
     */
    public List<String> dynamicReading(String filepath) throws FileNotFoundException, IOException {
        File readFile =null;
        FileOutput fileOutput = new FileOutput();

        List<String> sqllist = new ArrayList<>();
        List<String> sqllist2 = new ArrayList<>();
        String historyfilename = null;
        String historicaldata = null;
        String  historicaldatavalue = null;
        String historyfilename2= null;
        String historicaldata2 = null;
        //获取已读文件坐标（文件名字，数据日期）
        Map<String,String> map = loadContinue();
        System.out.println("map 获取已读文件坐标--->"+map);
        if (map==null){
            historyfilename2 = null;
            historicaldata2 =null;
        }else{
            historyfilename2 = map.get("historyfilename");
            historicaldata2 =map.get("historicaldata");
        }
        String  state = null;
        //读取sql语句执行的状态
        Map<String,String> map2 = stateSql();
        System.out.println("map2 获取已读文件坐标--->"+map2);
        if (map2 ==null){
            state = null;
        }else{
        state =  map2.get("state");
        }
        SqlVo sqlVo = null;
        //动态获取文件
        try {
            File file = new File(filepath);
            //判断是否是文件夹
            if (!file.isDirectory()){//不是文件夹

            }else if (file.isDirectory()){
                String[] fileList = file.list();
                //判断上次读取文件
                int i=0;

                if (historyfilename2 != null){
                    for (int j = 0; j < fileList.length; j++) {
                        if (fileList[j].equals(historyfilename2) ) {
                            i = j;
                            break;
                        }
                    }
                }
                //遍历文件夹中的数据
                for (int j =i ; j<fileList.length;j++){

                    readFile = new File(filepath+"\\"+fileList[j]);
                    historyfilename = readFile.getName();

                    //遍历文件得到文件中的每一条数据
                    sqllist =fileOutput.sqlIo(readFile.getPath());

                    //判断上次读取数据的条数位置
                    int m = 0;

                    if(historicaldata2 != null){
                        for (int n =0;n<sqllist.size();n++){
                            if (sqllist.get(n).indexOf("insert") != -1&&(sqllist.get(n).substring(0,sqllist.get(n).indexOf("insert"))).equals(historicaldata2)){
                                m=n;
                                if (state != null){
                                    if(state.equals("yes")){
                                        m = m+1;
                                        //System.out.println("判断上次读取的位置！sql是否执行成功" + m);
                                    }
                                }
                                break;
                            }else if (sqllist.get(n).indexOf("update") != -1&&(sqllist.get(n).substring(0,sqllist.get(n).indexOf("update"))).equals(historicaldata2)){
                                m=n;
                                //System.out.println("判断上次读取的位置！修改的方法" + m);
                                if (state != null){
                                    if(state.equals("yes")){
                                        m = m+1;
                                        //System.out.println("判断上次读取的位置！sql是否执行成功" + m);
                                    }
                                }
                                break;
                            }else if (sqllist.get(n).indexOf("delete") != -1&&(sqllist.get(n).substring(0,sqllist.get(n).indexOf("delete"))).equals(historicaldata2)){
                                m=n;
                            //System.out.println("判断上次读取的位置！删除的方法" + m);
                            if (state != null){
                                if(state.equals("yes")){
                                    m = m+1;
                                   // System.out.println("判断上次读取的位置！sql是否执行成功" + m);
                                }
                            }
                            break;
                        }else if (sqllist.get(n).indexOf("exec") != -1&&(sqllist.get(n).substring(0,sqllist.get(n).indexOf("exec"))).equals(historicaldata2)){
                                m=n;
                               // System.out.println("判断上次读取的位置！删除的方法" + m);
                                if (state != null){
                                    if(state.equals("yes")){
                                        m = m+1;
                                    }
                                }
                                break;
                            }
                    }
                    }
                    //得到文件中的数据
                    //判断上次sql是否执行成功
                    if (m == sqllist.size()){
                     historicaldata = historicaldata2;
                    }
                  for (int k = m;k < sqllist.size();k++){
                      //System.out.println("readFile--->"+sqllist.get(k));
                      //判断语句的类型
                      if ((sqllist.get(k)).indexOf("insert")!=-1){
                          historicaldata = sqllist.get(k).substring(0,sqllist.get(k).indexOf("insert"));
                          historicaldatavalue = sqllist.get(k).substring(sqllist.get(k).indexOf("insert"));
                      }else if ((sqllist.get(k)).indexOf("update")!=-1){
                          historicaldata = sqllist.get(k).substring(0,sqllist.get(k).indexOf("update"));
                          historicaldatavalue = sqllist.get(k).substring(sqllist.get(k).indexOf("update"));
                      }else if ((sqllist.get(k)).indexOf("delete")!=-1){
                          historicaldata = sqllist.get(k).substring(0,sqllist.get(k).indexOf("delete"));
                          historicaldatavalue = sqllist.get(k).substring(sqllist.get(k).indexOf("delete"));
                      }else if ((sqllist.get(k)).indexOf("call")!=-1){
                          historicaldata = sqllist.get(k).substring(0,sqllist.get(k).indexOf("exec"));
                          historicaldatavalue = sqllist.get(k).substring(sqllist.get(k).indexOf("exec"));
                      }

                      //System.out.println("historicaldatavalue--->"+historicaldatavalue);
                      sqllist2.add(historicaldatavalue);
                  }
                    if (!readFile.isDirectory()){
                    }else if (readFile.isDirectory()){
                        dynamicReading(filepath+"\\"+fileList[j]);
                    }
                }
            }
        } catch (FileNotFoundException  e) {
            e.printStackTrace();
        }
        //将进度保存到文件中
        loadGame(historyfilename,historicaldata);
        return sqllist2;

    }
    /**
     * 读取上次读取的位置
     * @param
     */
    public  Map<String,String>
    loadContinue() throws IOException {

        /*Map<String,String> map = new HashMap<>();

        InputStream input = SqlDispose.class.getClassLoader().getResourceAsStream("loadgame.properties");
        Properties properties = new Properties();
        properties.load(input);
        historyfilename = properties.getProperty("historyfilename");
        historicaldata = properties.getProperty("historicaldata");
        map.put("historyfilename",historyfilename);
        map.put("historicaldata",historicaldata);
        System.out.println("--资源文件----"+map);*/

        Properties properties = new Properties();
        File file = new File("E://proper//loadgame.properties");
        FileInputStream fileInputStream = null;
        Map<String,String> map = new HashMap<>();

        if(!file.exists()){
            return null;
        }
        try {
            fileInputStream = new FileInputStream(file);
            properties.load(fileInputStream);
            Set<Object> setkey = properties.keySet();
            for (Object object:setkey){
                map.put(object.toString(),properties.getProperty(object.toString()));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //System.out.println("ssss------------>"+map);
        return map;
    }
    /**
     * 读取sql语句执行的状态
     * @param
     */
    public  Map<String,String> stateSql()  {

        Properties properties = new Properties();
        File file = new File("E://proper//states.properties");
        FileInputStream fileInputStream = null;
        Map<String,String> map = new HashMap<>();

        if(!file.exists()){
            return null;
        }
        try {
            fileInputStream = new FileInputStream(file);
            properties.load(fileInputStream);
            Set<Object> setkey = properties.keySet();
            for (Object object:setkey){
                map.put(object.toString(),properties.getProperty(object.toString()));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //System.out.println("state"+map);
        return map;
    }

    /**
     * 保存文件读取进度
     * @param
     */
  public static void loadGame(String historyfilename,String historicaldata){
     /* Properties propertirs = new Properties();

      InputStream input= null;
      input = SqlDispose.class.getClassLoader().getResourceAsStream("loadgame.properties");

      if (historyfilename.equals("")||historyfilename==null){
          historyfilename = null;
      }
      //System.out.println("文件进度读取historicaldata--------ss--->"+historicaldata);
      if (historicaldata.equals("")||historicaldata==null){
          historicaldata = null;
      }

      try {
          Properties properties = new Properties();
          properties.load(input);

          propertirs.setProperty("historyfilename",historyfilename);
          propertirs.setProperty("historicaldata",historicaldata);

      } catch (FileNotFoundException e) {
          e.printStackTrace();
      } catch (IOException e) {
          e.printStackTrace();
      }finally {
          try {
              input.close();
          } catch (IOException e) {
              e.printStackTrace();
          }
      }*/

      Properties propertirs = new Properties();
      File file = new File("E://proper//loadgame.properties");
      if (!file.exists()){
          return;
      }
      if (historyfilename.equals("")||historyfilename==null){
          historyfilename = null;
      }
      //System.out.println("文件进度读取historicaldata--------ss--->"+historicaldata);
      if (historicaldata.equals("")||historicaldata==null){
          historicaldata = null;
      }
      try {
          FileInputStream fileInputStream = new FileInputStream(file);
          FileOutputStream fileOutputStream = new FileOutputStream(file);
          propertirs.load(fileInputStream);
          propertirs.setProperty("historyfilename",historyfilename);
          propertirs.setProperty("historicaldata",historicaldata);
          propertirs.store(fileOutputStream,"更新配置文件");
          fileOutputStream.close();
          fileInputStream.close();
      } catch (FileNotFoundException e) {
          e.printStackTrace();
      } catch (IOException e) {
          e.printStackTrace();
      }
  }

  public static void main(String[] args){
      Map<String,String> map = null;

      try {
          map = new SqlDispose().loadContinue();
      } catch (IOException e) {
          e.printStackTrace();
      }

      System.out.println(map);
  }

}
