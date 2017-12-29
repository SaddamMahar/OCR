package com.primeid.service;

import com.primeid.dao.FacialRecResultDao;
import com.primeid.model.FacialRecResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Saddam Hussain
 */
@Service("facialRecResultService")
public class FacialRecResultService {

    @Autowired
    private FacialRecResultDao facialRecResultDao;

    public FacialRecResult save(FacialRecResult facialRecResult) {
        return facialRecResultDao.save(facialRecResult);
    }
}
