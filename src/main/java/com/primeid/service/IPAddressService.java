package com.primeid.service;

import com.primeid.model.IPAddress;
import com.primeid.dao.IPAddressDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Saddam Hussain
 */
@Service
public class IPAddressService {

    @Autowired
    IPAddressDao ipAddressDao;

    @Transactional
    public IPAddress loadIPByAddress(String ip) {

        IPAddress ipAddress = null;
        ipAddress = ipAddressDao.findByIPAddress(ip);
        return ipAddress;

    }
}
