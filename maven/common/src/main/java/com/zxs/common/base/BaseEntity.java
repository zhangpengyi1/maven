package com.zxs.common.base;

import java.util.Date;

import lombok.Data;

@Data
public abstract class BaseEntity {

    // 创建人
    private Long createdBy;
    // 创建人名称
    private String createdByName;
    // 创建日期
    private Date creationDate;
    // 最后更新操作人
    private Long lastUpdatedBy;
    // 最后更新操作人名称
    private String lastUpdatedByName;
    // 最后更新日期
    private Date lastUpdateDate;
    // 数据权限_租户
    private Long authorityTenant;
    // 数据权限_角色，多角色用逗号分隔
    private String authorityRoles;
    // 数据权限_个人
    private Long authorityUserId;

}