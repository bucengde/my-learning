package com.learning.es.utils;

import com.alibaba.fastjson.JSON;
import com.learning.commons.exception.RuntimeServerException;
import com.learning.es.bean.db.GoodInfo;
import org.assertj.core.util.Lists;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Wang Xu
 * @date 2020/10/25
 */
@Component
public class HtmlParseUtil {
    @Resource
    private RestHighLevelClient restHighLevelClient;

    public Boolean addData2Es(String keyword, String esIndex) throws IOException {
        List<GoodInfo> goods = parseData(keyword);
        Optional.ofNullable(goods).orElseThrow(RuntimeServerException::new);

        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout(TimeValue.timeValueSeconds(60));
        goods.stream().map(item -> new IndexRequest(esIndex).source(JSON.toJSONString(item), XContentType.JSON)).forEach(bulkRequest::add);

        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        return !bulk.hasFailures();
    }

    private List<GoodInfo> parseData(String keyword) throws IOException {
        // https://search.jd.com/Search?keyword=
        Document document = Jsoup.parse(new URL("https://search.jd.com/Search?keyword=" + URLEncoder.encode(keyword, "UTF-8")), 5000);
        Element jdGoodsEle = document.getElementById("J_goodsList");
        Elements lis = jdGoodsEle.getElementsByTag("li");
        ArrayList<GoodInfo> objects = Lists.newArrayList();
        lis.forEach(item -> {
            String img = item.getElementsByTag("img").eq(0).attr("data-lazy-img");
            String price = item.getElementsByClass("p-price").eq(0).text();
            String title = item.getElementsByClass("p-name").eq(0).text();
            String shop = item.getElementsByClass("p-shop").eq(0).text();
            objects.add(new GoodInfo(img, title, price, shop));
        });
        return CollectionUtils.isEmpty(objects) ? null : objects;
    }
}