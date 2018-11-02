package cn.thoughtworks.school.programCenter.repositories;


import cn.thoughtworks.school.programCenter.entities.SectionVideoAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SectionVideoAddressRepository extends JpaRepository<SectionVideoAddress,Long>{
    SectionVideoAddress findByTaskIdAndSectionId(Long taskId, Long sectionId);
}
