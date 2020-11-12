package com.learning.commons.bean.api.config;

import lombok.Data;

import java.util.Date;

/**
 * @author Wang Xu
 * @date 2020/11/10
 */
@Data
public class ConfigInfo {
    private Integer id;
    private String configKey;
    private String configValue;
    private Date created;
    private Date modified;
    private Boolean deleted;

}