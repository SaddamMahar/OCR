package com.primeid.dao;

import com.primeid.model.Token;

/**
 *
 * @author Saddam Hussain
 */
public interface TokenDao {
    Token findByTokenCode (String tokenCode);
    Token findByUserID (String userID);
    Token save (Token token);
    Token update (Token token);
}
