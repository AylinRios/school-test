package mx.dev.blank.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import mx.dev.blank.entity.*;
import mx.dev.blank.model.CourseNameDTO;
import mx.dev.blank.model.CourseRoomDTO;
import mx.dev.blank.model.CourserTeacherKeycodeDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class StudentJpaDAO implements StudentDAO {

  @Setter(onMethod = @__(@PersistenceContext), value = AccessLevel.PACKAGE)
  private EntityManager em;

  @Override
  public List<Student> getStudentsBySearchCriteria(
      final String nameQuery,
      final String uuidQuery,
      final Date rangeStart,
      final Date rangeEnd) {

    final CriteriaBuilder builder = em.getCriteriaBuilder();
    final CriteriaQuery<Student> query = builder.createQuery(Student.class);
    final Root<Student> root = query.from(Student.class);

    query.select(root);

    final List<Predicate> predicates = new ArrayList<>();
    predicates.add(builder.between(root.get(Student_.birthday), rangeStart, rangeEnd));

    buildSearchCriteria(predicates, builder, root, nameQuery, uuidQuery);

    query.select(root).where(predicates.toArray(new Predicate[0]));

    return em.createQuery(query).getResultList();
  }

  private void buildSearchCriteria(
      final List<Predicate> predicates,
      final CriteriaBuilder builder,
      final Root<Student> root,
      final String nameQuery,
      final String uuidQuery) {

    if (StringUtils.isNotBlank(nameQuery)) {
      final String queryFormat = "%" + nameQuery + "%";

      predicates.add(
              builder.or(
                      builder.like(root.get(Student_.name), queryFormat),
                      builder.like(root.get(Student_.firstSurname), queryFormat),
                      builder.like(root.get(Student_.secondSurname), queryFormat)
              )
      );
    }

    if (uuidQuery != null && uuidQuery.trim().isEmpty()) {
      final String queryFormat = "%" + uuidQuery + "%";

      predicates.add(builder.like(root.get(Student_.uuid), queryFormat));
    }
  }


  /*Busca materias por UUID del estudiante*/
  public List<String> getCourseBySearchUUid(final String uuid) {
    final CriteriaBuilder builder = em.getCriteriaBuilder();
    final CriteriaQuery<String> query = builder.createQuery(String.class);
    final Root<Grade> root = query.from(Grade.class);

    final Join<Grade, Student> gradeJoinStudent=root.join(Grade_.student);
    final Join<Grade, CourseTeacher> gradeJoinCourseTeacher=root.join(Grade_.courseTeacher);
    final Join<CourseTeacher, Course> CourseTeacherJoinCourse = gradeJoinCourseTeacher.join(CourseTeacher_.course);
    query
            .select(CourseTeacherJoinCourse.get(Course_.name))
            .where(builder.equal(gradeJoinStudent.get(Student_.uuid), uuid));

    return em.createQuery(query).getResultList();
  }


}
