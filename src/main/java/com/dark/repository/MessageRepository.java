package com.dark.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dark.model.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
	public List<Message> findByChatId(Integer chatId);
}
