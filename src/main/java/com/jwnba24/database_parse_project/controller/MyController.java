package com.jwnba24.database_parse_project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by jiwen on 2019/1/1.
 */
@Controller
@RequestMapping("/")
public class MyController {
        @RequestMapping("/index")
        public String index() {
            return "index";
        }
}
