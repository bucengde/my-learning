package com.learning.es.mapper;

import com.learning.es.bean.db.GoodInfo;
import com.learning.es.service.ElasticService;
import com.learning.es.utils.HtmlParseUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @author Wang Xu
 * @date 2020/10/25
 */
@Slf4j
public class HtmlParseUtilTest extends BaseTestBean {
    @Resource
    private HtmlParseUtil htmlParseUtil;
    @Resource
    private ElasticService elasticService;

    private static final String INDEX_NAME = "jd_goods";

    @Test
    public void addData() throws IOException {
        String kw = "动漫";
        Boolean result = htmlParseUtil.addData2Es(kw, INDEX_NAME);
        log.info("htmlParseUtil - addData result: {}", result);
    }

    @Test
    public void search() throws IOException {
        List<GoodInfo> list = elasticService.search(GoodInfo.class, "java", 1, 10);
        log.error(list.get(0).toString());

    }
}