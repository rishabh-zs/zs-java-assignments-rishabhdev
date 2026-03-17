package com.zs.assignment10;

import com.zs.assignment10.controllers.ProductControllerTest;
import com.zs.assignment10.services.ProductServiceTest;
import com.zs.assignment10.dao.ProductDaoJdbcImplTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * Assignment 10 test suite entry point.
 */
@Suite
@SelectClasses({
        ProductControllerTest.class,
        ProductServiceTest.class,
        ProductDaoJdbcImplTest.class
})
public class MainTest {
}
