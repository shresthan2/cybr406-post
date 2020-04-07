package com.cybr406.post;

import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler
public class CommentEventHandler {

    @HandleBeforeCreate
    public void handleBeforeCreate(Comment comment) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        comment.setUsername(username);
    }

    @HandleBeforeSave
    @PreAuthorize("hasRole('ROLE_ADMIN') or #comment.username == authentication.principal")
    public void handleBeforeSave(Comment comment) {

    }

    @HandleBeforeDelete
    @PreAuthorize("hasRole('ROLE_ADMIN') or #comment.username == authentication.principal")
    public void handleBeforeDelete(Comment comment) {

    }

}
