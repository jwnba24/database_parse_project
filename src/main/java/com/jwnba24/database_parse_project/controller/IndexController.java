package com.jwnba24.database_parse_project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by jiwen on 2019/1/2.
 *
 */
@Controller
@RequestMapping("/")
public class IndexController {
    @RequestMapping("/{page}.html")
    public String index(@PathVariable("page")String page){
        return page;
    }
}
