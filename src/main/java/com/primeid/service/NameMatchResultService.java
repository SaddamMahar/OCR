
package com.primeid.service;


import com.primeid.dao.NameMatchResultDao;
import com.primeid.model.NameMatchResult;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("nameMatchResultService")
public class NameMatchResultService {
     @Resource(name="nameMatchResultDao")
    private NameMatchResultDao nameMatchResultDao;
    public void save (NameMatchResult nameMatchResult)
    {
        nameMatchResultDao.save(nameMatchResult);
    }
    
}
