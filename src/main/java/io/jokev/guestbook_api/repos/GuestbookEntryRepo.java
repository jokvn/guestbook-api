package io.jokev.guestbook_api.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import io.jokev.guestbook_api.models.GuestbookEntry;

@Repository
public interface GuestbookEntryRepo extends JpaRepository<GuestbookEntry, Long> {

}
