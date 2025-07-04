package com.example.AuthMicroservice.AuthMicroservice.Repositories;


import com.example.AuthMicroservice.AuthMicroservice.Domain.Session;
import com.example.AuthMicroservice.AuthMicroservice.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session,Long> {

    Optional<Session> findById(Long id);

    Session findTopByUserOrderByDateTimeStartedDesc(User user);

    @Query("SELECT s FROM Session s WHERE s.user = :userId ORDER BY s.dateTimeStarted DESC")
    Session findMostRecentSessionByUserID(User userId);

    @Query("SELECT s FROM Session s WHERE s.sessionid = :sessionId")
    Session findSessionBySessionID(Long sessionId);


}