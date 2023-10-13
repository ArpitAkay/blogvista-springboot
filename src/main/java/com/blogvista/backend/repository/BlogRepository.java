package com.blogvista.backend.repository;

import com.blogvista.backend.entity.Blog;
import com.blogvista.backend.entity.UserInfo;
import com.blogvista.backend.enumeration.BlogStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Blog, Integer> {
    Page<Blog> findAllByStatus(BlogStatus blogStatus, PageRequest pageRequest);

    Page<Blog> findByUserInfo(UserInfo userInfo, PageRequest pageRequest);
}
