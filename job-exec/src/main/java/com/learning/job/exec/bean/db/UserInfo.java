package com.learning.job.exec.bean.db;

import lombok.Data;

/**
 * @author Wang Xu
 * @date 2021/2/4
 */
@Data
public class UserInfo {
    private Long id;
    private String name;

    public UserInfo(String name) {
        this.name = name;
    }
}