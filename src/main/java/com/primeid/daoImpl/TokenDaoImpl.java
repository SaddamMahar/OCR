package com.primeid.daoImpl;

import com.primeid.dao.TokenDao;
import com.primeid.model.Token;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Saddam Hussain
 */

@Repository("tokenDao")
public class TokenDaoImpl implements TokenDao{

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    public Token findByTokenCode(String tokenCode) {

        List<Token> tokens;

        tokens = sessionFactory.getCurrentSession()
                .createQuery("from Token where tokenCode=?")
                .setParameter(0, tokenCode)
                .list();

        if (tokens.size() > 0) {
            return tokens.get(0);
        } else {
            return null;
        }

    }
    
    @Transactional
    public Token findByUserID(String userID) {
        List list = sessionFactory.getCurrentSession()
                .createQuery("from Token where userID=?")
                .setParameter(0, userID)
                .list();
        if(list != null && list.size() > 0){
            return (Token) list.get(0);
        }
		return null;
    }
    
    @Transactional(readOnly = false)
    public Token save(Token token) {
        sessionFactory.getCurrentSession().save(token);
        return findByTokenCode(token.getTokenCode());
    }
    
    @Transactional(readOnly = false)
    public Token update(Token token) {
        sessionFactory.getCurrentSession().update(token);
        return findByTokenCode(token.getTokenCode());
    }

}