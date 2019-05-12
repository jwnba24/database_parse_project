package com.jwnba24.database_parse_project.MySdkTest;

import org.reflections.Reflections;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by jiwen on 2019/3/9.
 */
@Component
public class SDK {

    public void listener(){
        List<Map<String,String>> result = new ArrayList<>();

        try{
            Reflections reflections=new Reflections("com.jwnba24.database_parse_project.testImpl.*");
            Set<Class<? extends CompositionListener>> classes=reflections.getSubTypesOf(CompositionListener.class);
            Class<?> c = null;
            Iterator<Class<? extends CompositionListener>> it = classes.iterator();
            if (it.hasNext()){
                c = it.next();
                Method m =c.getMethod("change",List.class);
                m.invoke(c.newInstance(),result);
            }else{
                System.out.println("没有实现监听器");
            }

        }catch (Exception e){
            System.out.println("init ConfigService fail,exception:{}"+ e);
        }
    }

}
