package com.learning.es.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Wang Xu
 * @date 2020/10/25
 */
@Controller
public class IndexController {

    @GetMapping({"/","/index"})
    public String index() {
        return "index";
    }
}