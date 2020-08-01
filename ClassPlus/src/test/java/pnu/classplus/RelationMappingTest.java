package pnu.classplus;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pnu.classplus.domain.entity.DepartmentEntity;
import pnu.classplus.domain.entity.LectureEntity;
import pnu.classplus.domain.entity.UniversityEntity;
import pnu.classplus.domain.repository.DepartmentRepository;
import pnu.classplus.domain.repository.LectureRepository;
import pnu.classplus.domain.repository.UniversityRepository;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RelationMappingTest {
    @Autowired
    private UniversityRepository univRepo;

    @Autowired
    private DepartmentRepository deptRepo;

    @Autowired
    private LectureRepository lecRepo;


    public void testManyToOneInsert() {
        String s[] = {"정보컴푸터공학부", "기계공학부", "의생명융합공학부", "전기공학과"};
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

    @Test
    public void testTwoWayMapping() {
        UniversityEntity univ = univRepo.findById(1L).get();

        System.out.println(univ.getName() + "의 학과 목록");
        List<DepartmentEntity> list = univ.getDeptList();
        for (DepartmentEntity dept : list) {
            System.out.println(dept.toString());
        }

        DepartmentEntity dept = deptRepo.findById(1L).get();
        System.out.println(dept.getName() + "의 강의 목록");
        List<LectureEntity> lecList = dept.getLecList();
        for (LectureEntity lec : lecList) {
            System.out.println(lec.toString());
        }

        dept = deptRepo.findById(4L).get();
        System.out.println(dept.getName() + "의 강의 목록");
        lecList = dept.getLecList();
        for (LectureEntity lec : lecList) {
            System.out.println(lec.toString());
        }

        dept = deptRepo.findById(7L).get();
        System.out.println(dept.getName() + "의 강의 목록");
        lecList = dept.getLecList();
        for (LectureEntity lec : lecList) {
            System.out.println(lec.toString());
        }
    }
}
