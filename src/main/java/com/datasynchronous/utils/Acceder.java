package com.datasynchronous.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/1.
 */
public class Acceder {

    String fileName =  "D://logs//recivelogs";

    public List<List<String>> textSql(){

        List<List<String>> lists =new ArrayList<>();
        List<String> list =new ArrayList<>();
        File red = null;
        FileOutput fileOutput = new FileOutput();
        File file = new File(fileName);
        if (!file.isDirectory()){

        }else if(file.isDirectory()) {
            String[] filelist = file.list();

          for(int i = 0;i<filelist.length;i++){
              red = new File( fileName+"//"+filelist[i] );
              list =fileOutput.sqlIo( red.getPath() );
              lists.add( list );
          }

        }

        return lists;
    }

}
