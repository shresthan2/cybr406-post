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
public class PostEventHandler {

    @HandleBeforeCreate
    public void beforeCreate(Post post) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        post.setUsername(username);
    }

    @HandleBeforeSave
    @PreAuthorize("hasRole('ROLE_ADMIN') or #post.username == authentication.principal")
    public void handleBeforeSave(Post post) {

    }

    @HandleBeforeDelete
    @PreAuthorize("hasRole('ROLE_ADMIN') or #post.username == authentication.principal")
    public void handleBeforeDelete(Post post) {
    }

}
