package mx.dev.blank.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "grade")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Grade implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "grade")
    private int grade;

    @Column(name = "data_register", nullable = false)
    private Date dataRegister ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_teacher_id", nullable = false)
    private CourseTeacher courseTeacher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

}
