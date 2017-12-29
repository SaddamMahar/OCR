package com.primeid.daoImpl;

import com.primeid.model.IPAddress;
import com.primeid.dao.IPAddressDao;
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

@Repository("ipAddressDao")
public class IPAddressDaoImpl implements IPAddressDao{
    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    public IPAddress findByIPAddress(String ip) {

        List<IPAddress> ipAddresses;

        ipAddresses = sessionFactory.getCurrentSession()
                .createQuery("from IPAddress where ip=?")
                .setParameter(0, ip)
                .list();

        if (ipAddresses.size() > 0) {
            return ipAddresses.get(0);
        } else {
            return null;
        }

    }
}
