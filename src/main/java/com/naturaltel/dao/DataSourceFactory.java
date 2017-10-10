package com.naturaltel.dao;

import javax.sql.DataSource;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import com.naturaltel.cache.Config;
import com.naturaltel.config.entity.DBInfo;

public class DataSourceFactory {
    
    private static DataSource ds;

    public static DataSource getDataSource() throws Exception {
        
        if(ds == null) {
            MysqlDataSource mysqlDs = new MysqlDataSource();
            DBInfo dbInfo = Config.getDbInfo();
            if(dbInfo != null) {
                mysqlDs.setURL(dbInfo.getConnectionUrl());
                mysqlDs.setUser(dbInfo.getUserId());
                mysqlDs.setPassword(dbInfo.getUserPassword());
                ds = mysqlDs;
            }
        }
        
        return ds;
    }
    
}
