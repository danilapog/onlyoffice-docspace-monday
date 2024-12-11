package com.onlyoffice.tenant.persistence.repository;

import com.onlyoffice.tenant.persistence.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {}
