package com.learning.vps.controller.shorten;

import org.springframework.web.bind.annotation.RestController;

/**
 * 长链转短链服务
 *
 *  特点：MurmurHash算法，是一种非加密型hash算法，适用于一般的hash检索操作。
 *  优势：MurmurHash算法在对于规律性较强的key进行随机分配的性能效果上要更好。
 *  应用：redis字典的实现，两种算法之一就是MurmurHash，另一种是djb。
 *
 *  长链转短链（hash）：hash冲突，可以使用多次加入自定义字符串再hash解决。
 *  l->MurmurHash => l_hash->转为62进制（使长度更短） => l_hash_62->bool_filter => 落库
 *
 *  对于长链转短链 hash方式的替换，还可以使用 uuid、redis、snowflake、mysql维护id段的表等等方式。
 *  uuid：无序（如果插入mysql，有页分裂的问题）；而且很长。
 *  redis：性能好；需要考虑持久化问题；需要做容灾备份，高可用的成本较高。
 *  snowflake：基于系统，可能发生时钟回拨问题，导致生成重复短链。
 *  mysql维护id段的表：使用简单，扩展方便。
 *
 * @author Wang Xu
 * @date 2020/10/26
 */
@RestController
public class ShortUrlController {

}