package me.dio.bootcamp.project.repository;

import me.dio.bootcamp.project.entity.Text;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TextRepository extends JpaRepository<Text, Long> {
    List<Text> findByTextContainingIgnoreCase(String text);

}
