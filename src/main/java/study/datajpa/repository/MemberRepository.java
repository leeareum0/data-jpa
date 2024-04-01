package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.Entity.Member;
import study.datajpa.dto.MemberDto;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    //쿼리가 간단할 때 사용
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    //@Query(name = "Member.findByUsername") //생략 가능
    List<Member> findByUsername(@Param("username") String username);

    //메서드에 JPQL 쿼리 작성 - 복잡할 때 사용
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    //Dto 조회
    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    //컬렉션 파라미터 바인딩 - in절로 여러 값을 조회하고 싶을 때 사용
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

    //반환 타입
    List<Member> findListByUsername(String username); //컬렉션

    Member findMemberByUsername(String username); //단건

    Optional<Member> findOptionalByUsername(String username); //단건 Optional

    @Query(value = "select m from Member m left join m.team t",
                countQuery = "select count(m) from Member m") //countQuery 분리
//    Slice<Member> findByAge(int age, Pageable pageable);
    Page<Member> findByAge(int age, Pageable pageable);

    //벌크성 수정, 삭제 쿼리일 때 필수 사용, 옵션 - clear 자동 실행
    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age +1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);
}
