package io.github.zbhavyai.quarkustemplate.dao.note;

import io.github.zbhavyai.quarkustemplate.entity.note.Note;
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class NoteRepository implements PanacheRepositoryBase<Note, String> {}
