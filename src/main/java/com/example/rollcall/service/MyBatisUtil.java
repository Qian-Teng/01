package com.example.rollcall.service;

import com.example.rollcall.mapper.StudentMapper;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

public final class MyBatisUtil {
    private static final SqlSessionFactory SQL_SESSION_FACTORY = buildFactory();

    private MyBatisUtil() {}

    public static SqlSessionFactory getSqlSessionFactory() {
        return SQL_SESSION_FACTORY;
    }

    private static SqlSessionFactory buildFactory() {
        PooledDataSource dataSource = new PooledDataSource(
                "org.h2.Driver",
                "jdbc:h2:./data/class_roll_call;MODE=MySQL;AUTO_SERVER=TRUE;DATABASE_TO_UPPER=false",
                "sa",
                ""
        );
        Environment environment = new Environment("development", new JdbcTransactionFactory(), dataSource);
        Configuration configuration = new Configuration(environment);
        configuration.addMapper(StudentMapper.class);
        return new SqlSessionFactoryBuilder().build(configuration);
    }
}
