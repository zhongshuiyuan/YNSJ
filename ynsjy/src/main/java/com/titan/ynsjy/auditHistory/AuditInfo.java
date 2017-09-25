package com.titan.ynsjy.auditHistory;

/**
 * Created by hanyw on 2017/9/22/022.
 * 审计信息修改
 */

public interface AuditInfo {
    void showEditDialog(String alias,String value);
    void closeDialog();
    void sure(String alias,String value);
}
