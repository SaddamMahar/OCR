package com.primeid.service;


import com.primeid.model.TamperResult;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.primeid.dao.TamperResultDao;

/**
 *
 * @author hashim
 */
@Service("tamperResultService")
public class TamperResultService {
        @Resource(name="tamperResultDao")
        private TamperResultDao tamperResultDao;
        
        public TamperResult findTamperResultByArtifactID(long artifactID){
            return tamperResultDao.findByArtifactID(artifactID);
        }
        
        public void save (TamperResult tamperResults)
        {
            tamperResultDao.save(tamperResults);
        }
            
    
}
