
package com.primeid.service;

import com.primeid.dao.FacialRecCaseDao;
import com.primeid.model.FacialRecCase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("facialRecCaseService")
public class FacialRecCaseService {
    
      @Autowired
    private FacialRecCaseDao facialRecCaseDao;
       
       public FacialRecCase save(FacialRecCase facialRecCase) {
        return facialRecCaseDao.save(facialRecCase);
    }
    
}
