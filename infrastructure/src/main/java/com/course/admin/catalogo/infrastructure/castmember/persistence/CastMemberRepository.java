package com.course.admin.catalogo.infrastructure.castmember.persistence;

import com.course.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CastMemberRepository extends JpaRepository<CastMemberJpaEntity, String> {

    Page<CastMemberJpaEntity> findAll(Specification<CastMemberJpaEntity> whereClause, Pageable page);

    @Query(value = "select c.id from CastMember c where c.id in :ids")
    List<String> existsByIds(@Param("ids") List<String> ids);
}
