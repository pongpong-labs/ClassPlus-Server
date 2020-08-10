package pnu.classplus;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import pnu.classplus.domain.entity.*;
import pnu.classplus.domain.repository.DepartmentRepository;
import pnu.classplus.domain.repository.LectureRepository;
import pnu.classplus.domain.repository.MemberRepository;
import pnu.classplus.domain.repository.UniversityRepository;

import java.util.Collections;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RelationMappingTest {
    @Autowired
    private UniversityRepository univRepo;

    @Autowired
    private DepartmentRepository deptRepo;

    @Autowired
    private LectureRepository lecRepo;

    @Autowired
    private MemberRepository memberRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JPAQueryFactory queryFactory;

    public void testManyToOneInsert() {
        String s[] = {"정보컴퓨터공학부", "기계공학부", "의생명융합공학부", "전기공학과"};
        String d[][] = {
                {"운영체제", "알고리즘", "컴퓨터구조", "C프로그래밍"},
                {"일반물리학", "정역학", "고체역학", "열역학"},
                {"일반물리학(II)", "프로그래밍원리와실습", "일반화학(II)", "공학수학(I)"},
                {"전자기학", "물리전자", "제어공학", "신호및시스템"}
        };
        String s2[] = {"수학교육과", "국어교육과", "체육교욱과"};
        String d2[][] = {
                {"미분적분학(II)", "기하학일반및지도", "응용복소해석학(I)", "위상수학개론"},
                {"국문학원전이해", "현대문학개론", "문법교육론", "현대문학사"},
                {"스포츠사회학", "스포츠마케팅", "농구", "스포츠사회학"}
        };
        String s3[] = {"국어국문학과", "영어영문학과"};
        String d3[][] = {
                {"국어맞춤법", "시조와가사", "문예비평", "국어사"},
                {"영어회화", "영문학입문", "영문학사", "영어음성학"}
        };

        UniversityEntity u1 = new UniversityEntity();
        u1.setName("부산대학교");
        univRepo.save(u1);

        UniversityEntity u2 = new UniversityEntity();
        u2.setName("가톨릭대학교");
        univRepo.save(u2);

        UniversityEntity u3 = new UniversityEntity();
        u3.setName("서울대학교");
        univRepo.save(u3);

        for (int i = 0; i < 4; i++) {
            DepartmentEntity dept = new DepartmentEntity();
            dept.setName(s[i]);
            dept.setUniversity(u1);
            deptRepo.save(dept);
            for (int j = 0; j < 4; j++) {
                LectureEntity lec = new LectureEntity();
                lec.setName(d[i][j]);
                lec.setDepartment(dept);
                lecRepo.save(lec);
            }
        }

        for (int i = 0; i < 3; i++) {
            DepartmentEntity dept = new DepartmentEntity();
            dept.setName(s2[i]);
            dept.setUniversity(u2);
            deptRepo.save(dept);
            for (int j = 0; j < 4; j++) {
                LectureEntity lec = new LectureEntity();
                lec.setName(d2[i][j]);
                lec.setDepartment(dept);
                lecRepo.save(lec);
            }
        }
        for (int i = 0; i < 2; i++) {
            DepartmentEntity dept = new DepartmentEntity();
            dept.setName(s3[i]);
            dept.setUniversity(u3);
            deptRepo.save(dept);
            for (int j = 0; j < 4; j++) {
                LectureEntity lec = new LectureEntity();
                lec.setName(d3[i][j]);
                lec.setDepartment(dept);
                lecRepo.save(lec);
            }
        }
    }

    public void testTwoWayMapping() {
        Set<UniversityEntity> univSet = univRepo.findAll();
        System.out.println("학교 목록");
        for (UniversityEntity univ : univSet) {
            System.out.println(univ);
        }

        UniversityEntity univ = univRepo.findById(1L).get();
        System.out.println(univ.getName() + "의 학과 목록");
        Set<DepartmentEntity> list = univ.getDeptSet();
        for (DepartmentEntity dept : list) {
            System.out.println(dept.toString());
        }

        DepartmentEntity dept = deptRepo.findById(1L).get();
        System.out.println(dept.getName() + "의 강의 목록");
        Set<LectureEntity> lecList = dept.getLecSet();
        for (LectureEntity lec : lecList) {
            System.out.println(lec.toString());
        }

        dept = deptRepo.findById(4L).get();
        System.out.println(dept.getName() + "의 강의 목록");
        lecList = dept.getLecSet();
        for (LectureEntity lec : lecList) {
            System.out.println(lec.toString());
        }

        dept = deptRepo.findById(7L).get();
        System.out.println(dept.getName() + "의 강의 목록");
        lecList = dept.getLecSet();
        for (LectureEntity lec : lecList) {
            System.out.println(lec.toString());
        }
    }
    @Test
    public void insertMember() {
        UniversityEntity univ = univRepo.findById(1L).get();
        DepartmentEntity dept;

        dept = deptRepo.findById(1L).get();
        memberRepo.save(MemberEntity.builder()
            .uid("admin")
            .password(passwordEncoder.encode("admin!@"))
            .role(Role.ROLE_ADMIN)
            .name("관리자")
            .email("admin@eyear.kr")
            .university(univ)
            .department(dept)
            .enabled(true)
            .roles(Collections.singleton(Role.ROLE_ADMIN.toString()))
            .build());

        long stDeptCode[] = {1, 1, 1, 1, 2, 2, 2};
        String stId[] = {"st_depark", "st_msw", "st_hwangmk", "st_kimdb", "st_yeoyh", "st_choisw", "st_jeonhs"};
        String stName[] = {"박대언", "문성욱", "황미경", "김대박", "여윤호", "최시원", "전현성"};
        String stEmail[] = {
            "parkde@pusan.ac.kr", "moonse@pusan.ac.kr", "hwangmk@pusan.ac.kr", "daebak@naver.com",
            "yyh@daum.net", "choisw@navy.mil.kr", "junhs@sajik.hs.kr"
        };

        for (int i = 0; i < stDeptCode.length; ++i) {
            dept = deptRepo.findById(stDeptCode[i]).get();
            memberRepo.save(MemberEntity.builder()
                .uid(stId[i])
                .password(passwordEncoder.encode("pongponglabs!"))
                .role(Role.ROLE_STUDENT)
                .name(stName[i])
                .email(stEmail[i])
                .university(univ)
                .department(dept)
                .enabled(true)
                .roles(Collections.singleton(Role.ROLE_STUDENT.toString()))
                .build());
        }

        long pfDeptCode[] = {1, 1, 1, 1, 2, 2, 2};
        String pfId[] = {"pf_johk", "pf_hschoi", "pf_jwmoon", "pf_kwonhc", "pf_kimsw", "pf_choiym", "pf_jspark"};
        String pfName[] = {"조환규", "채흥석", "문정욱", "권혁철", "김시완", "최용민", "박준석"};
        String pfEmail[] = {
            "johk@pusan.ac.kr", "hschoi@pusan.ac.kr", "jwmoon@pusan.ac.kr", "kwonhc@seoul.ac.kr",
            "kimsw@daum.net", "choiym@pusan.ac.kr", "jspark@naver.com"
        };
        for (int i = 0; i < stDeptCode.length; ++i) {
            dept = deptRepo.findById(pfDeptCode[i]).get();
            memberRepo.save(MemberEntity.builder()
                .uid(pfId[i])
                .password(passwordEncoder.encode("pongponglabs!"))
                .role(Role.ROLE_PROFESSOR)
                .name(pfName[i])
                .email(pfEmail[i])
                .university(univ)
                .department(dept)
                .enabled(true)
                .roles(Collections.singleton(Role.ROLE_PROFESSOR.toString()))
                .build());
        }
    }

    public void testQueryDsl() {

    }
}
