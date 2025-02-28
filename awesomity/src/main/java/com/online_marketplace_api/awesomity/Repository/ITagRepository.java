package com.online_marketplace_api.awesomity.Repository;


import com.online_marketplace_api.awesomity.Entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;


@Repository
public interface ITagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String name);

    Set<Tag> findByNameIn(List<String> names);

    boolean existsByNameIgnoreCase(String name);
}