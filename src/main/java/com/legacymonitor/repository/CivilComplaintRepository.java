package com.legacymonitor.repository;

import com.legacymonitor.domain.CivilComplaint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CivilComplaintRepository extends JpaRepository<CivilComplaint, Long> {
}