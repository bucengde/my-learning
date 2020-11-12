package com.learning.es.controller;

import com.learning.es.bean.db.GoodInfo;
import com.learning.es.service.ElasticService;
import com.learning.es.utils.HtmlParseUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @author Wang Xu
 * @date 2020/10/25
 */
@RestController
public class DataController {

    @Resource
    private HtmlParseUtil htmlParseUtil;
    @Resource
    private ElasticService elasticService;

    @GetMapping("/data/{keyword}")
    public Boolean addData2Es(@PathVariable("keyword") String keyword) throws IOException {
        return htmlParseUtil.addData2Es(keyword, "jd_goods");
    }

    @GetMapping("/data_search/{keyword}/{pageNo}/{pageSize}")
    public List<GoodInfo> search(@PathVariable("keyword") String keyword, @PathVariable("pageNo") Integer pageNo, @PathVariable("pageSize") Integer pageSize) throws IOException {
        return elasticService.search(GoodInfo.class, keyword, pageNo, pageSize);
    }

}