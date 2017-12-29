package com.primeid.dao;

import com.primeid.model.IPAddress;

/**
 *
 * @author Saddam Hussain
 */
public interface IPAddressDao {
    IPAddress findByIPAddress(String ip);
}
