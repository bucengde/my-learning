package com.learning.controller;

import com.google.common.collect.Maps;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Wang Xu
 * @date 2020/10/11
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @GetMapping("/map")
    public Map<String, Object> query(@RequestParam("key1") String v1, @RequestParam("key2") String v2, @RequestParam("param") String param) {
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);
        map.put("k1", v1);
        map.put("k2", v2);
        map.put("param", param);
        return map;
    }
}