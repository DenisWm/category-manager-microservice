package com.course.admin.catalogo.infrastructure.video.persistence;

import com.course.admin.catalogo.domain.castmember.CastMemberID;
import com.course.admin.catalogo.domain.category.CategoryID;
import com.course.admin.catalogo.domain.video.VideoPreview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;
import java.util.UUID;

public interface VideoRepository extends JpaRepository<VideoJpaEntity, String> {

    Page<VideoJpaEntity> findAll(Specification<VideoJpaEntity> whereClause, Pageable page);

    @Query("""
            SELECT new com.course.admin.catalogo.domain.video.VideoPreview(
                v.id as id,
                v.title as title,
                v.description as description,
                v.createdAt as createdAt,
                v.updatedAt as updatedAt
            ) 
            from Video v
            join v.castMembers members 
            join v.categories categories 
            join v.genres genres
            where 
                ( :terms is null or UPPER(v.title) like :terms  )
            and
                ( :castMembers is null or members.id.castMemberId in :castMembers )
            and
                ( :categories is null or categories.id.categoryId in :categories )
            and
                ( :genres is null or genres.id.genreId in :genres )
""")
    Page<VideoPreview> findAll(
            @Param("terms") String terms,
            @Param("castMembers") Set<String> castMembers,
            @Param("categories") Set<String> categories,
            @Param("genres") Set<String> genres,
            Pageable page
    );
}