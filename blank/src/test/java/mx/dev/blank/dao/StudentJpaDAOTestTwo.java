package mx.dev.blank.dao;

import com.beust.jcommander.internal.Lists;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import mx.dev.blank.DAOTestConfig;
import mx.dev.blank.DBTestConfig;
import mx.dev.blank.dao.StudentDAO;
import mx.dev.blank.dao.StudentJpaDAO;
import mx.dev.blank.entity.*;
import mx.dev.blank.model.CourseNameDTO;
import mx.dev.blank.model.CourseRoomDTO;
import mx.dev.blank.model.CourserTeacherKeycodeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.util.Collections;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;


@ContextConfiguration(classes = {DAOTestConfig.class, DBTestConfig.class})
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class
})

public class StudentJpaDAOTestTwo extends AbstractTransactionalTestNGSpringContextTests {
    private static final String DBUNIT_XML = "classpath:dbunit/dao/data.xml";
    @Autowired
    private StudentDAO studentDAO;

    @DataProvider
    public Object[][] findByID_dataProvider() {
        List<String> namesCourses = Lists.newArrayList("Historia", "Matematicas");

        return new Object[][] {
               //{"U-1", "Oscar", "H1", "Historia"}
                {"U-1", namesCourses}/*,
                {null}*/
        };
    }
    @Test(dataProvider = "findByID_dataProvider")
    @DatabaseSetup(DBUNIT_XML)
    public void getCourseByStudent(final String uuid, final List<String> nameCourse) {

        final List<String> course = studentDAO.getCourseBySearchUUid(uuid);

        assertThat(course).isNotNull();
      assertThat(course).isEqualTo(nameCourse);

    }


}

