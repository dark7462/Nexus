package com.dark.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dark.model.Chat;
import com.dark.model.User;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {

	public List<Chat> findByUsersId(Integer userId);

	@Query("select c from Chat c where :sender Member of c.users and :reciver member of c.users")
	public Chat findChatByUsersId(@Param("sender") User sender, @Param("reciver") User reciver);
}
