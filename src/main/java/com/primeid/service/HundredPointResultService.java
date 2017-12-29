
package com.primeid.service;

import com.primeid.dao.HundredPointResultDao;
import com.primeid.model.HundredPointResult;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;


@Service("hundredPointResultService")
public class HundredPointResultService {
      @Resource(name="hundredPointResultDao")
    private HundredPointResultDao hundredPointResultDao;
    public void save (HundredPointResult hundredPointResult)
    {
        hundredPointResultDao.save(hundredPointResult);
    }
}
