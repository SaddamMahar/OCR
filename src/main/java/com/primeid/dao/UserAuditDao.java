/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.primeid.dao;

import com.primeid.model.UserAudit;
import java.util.List;

/**
 *
 * @author Saddam Hussain
 */
public interface UserAuditDao {
    List<UserAudit> findByAccountCode (String accountCode);
    public void save (UserAudit userName);
}
