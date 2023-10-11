package com.blogvista.backend.repository;

import com.blogvista.backend.entity.Blog;
import com.blogvista.backend.enumeration.BlogStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Integer> {
    Page<Blog> findAllByStatus(BlogStatus blogStatus, PageRequest pageRequest);

    @Query(
            value = "SELECT * FROM blogvista.blog WHERE email = ?1",
            nativeQuery = true
    )
    List<Blog> findByEmail(String email);
}
