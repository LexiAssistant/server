package dev.changuii.project.repository;

import dev.changuii.project.entity.SampleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<SampleEntity, Long>, CustomSampleRepository {
}
