package com.primeid.service;

import com.primeid.dao.TokenDao;
import com.primeid.model.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Saddam Hussain
 */
@Service("tokenService")
public class TokenService {

    @Autowired
    private TokenDao tokenDao;

    @Transactional(readOnly = true)
    public Token loadTokenByTokenCode(String tokenCode){
        Token tokenObj = null;
        tokenObj = tokenDao.findByTokenCode(tokenCode);
        return tokenObj;
    }
    
    public Token loadTokenByUserID(String userID){
        Token tokenObj = null;
        tokenObj = tokenDao.findByUserID(userID);
        return tokenObj;
    }
    
    public Token save(Token token){
        Token tokenObj = null;
        tokenObj = tokenDao.save(token);
        return tokenObj;
    }
    
    public Token update(Token token){
        Token tokenObj = null;
        tokenObj = tokenDao.update(token);
        return tokenObj;
    }

}