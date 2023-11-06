package com.blogvista.backend.repository;

import com.blogvista.backend.entity.Blog;
import com.blogvista.backend.entity.UserInfo;
import com.blogvista.backend.enumeration.BlogStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BlogRepository extends JpaRepository<Blog, Integer>, JpaSpecificationExecutor<Blog> {
    Page<Blog> findByUserInfo(Sort createdDate, UserInfo userInfo, PageRequest pageRequest);
    Page<Blog> findAllByStatus(Sort by, BlogStatus blogStatus, PageRequest pageRequest);
}
