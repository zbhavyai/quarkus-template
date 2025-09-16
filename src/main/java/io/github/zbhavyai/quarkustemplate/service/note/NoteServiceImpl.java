package io.github.zbhavyai.quarkustemplate.service.note;

import io.github.zbhavyai.quarkustemplate.dao.note.NoteRepository;
import io.github.zbhavyai.quarkustemplate.dto.note.NoteCreateDTO;
import io.github.zbhavyai.quarkustemplate.dto.note.NoteDTO;
import io.github.zbhavyai.quarkustemplate.dto.note.NoteSummaryDTO;
import io.github.zbhavyai.quarkustemplate.dto.note.NoteUpdateDTO;
import io.github.zbhavyai.quarkustemplate.mapper.note.NoteMapper;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import java.util.List;
import org.jboss.logging.Logger;

@ApplicationScoped
public class NoteServiceImpl implements NoteService {

  private static final Logger LOG = Logger.getLogger(NoteServiceImpl.class);

  private final NoteRepository noteRepository;

  @Inject
  public NoteServiceImpl(NoteRepository noteRepository) {
    this.noteRepository = noteRepository;
  }

  @WithSession
  @Override
  public Uni<List<NoteSummaryDTO>> listNotes() {
    LOG.debugf("listNotes");

    return noteRepository
        .listAll()
        .map(list -> list.stream().map(NoteMapper::toSummaryDTO).toList());
  }

  @WithSession
  @Override
  public Uni<NoteDTO> getNoteById(String id) {
    LOG.debugf("getNoteById: id=\"%s\"", id);

    return noteRepository
        .findById(id)
        .onItem()
        .ifNull()
        .failWith(() -> new NotFoundException("Note not found"))
        .map(NoteMapper::toDTO);
  }

  @WithTransaction
  @Override
  public Uni<NoteDTO> createNote(NoteCreateDTO dto) {
    LOG.debugf("createNote: dto=\"%s\"", dto);

    return noteRepository.persist(NoteMapper.fromCreateDTO(dto)).map(NoteMapper::toDTO);
  }

  @WithTransaction
  @Override
  public Uni<NoteDTO> updateNote(String id, NoteUpdateDTO dto) {
    LOG.debugf("updateNote: dto=\"%s\"", dto);

    return noteRepository
        .findById(id)
        .onItem()
        .ifNull()
        .failWith(() -> new NotFoundException("Note not found"))
        .map(note -> NoteMapper.updateNote(note, dto))
        .map(NoteMapper::toDTO);
  }

  @WithTransaction
  @Override
  public Uni<Void> deleteNote(String id) {
    return noteRepository
        .deleteById(id)
        .onItem()
        .transformToUni(
            deleted ->
                deleted
                    ? Uni.createFrom().voidItem()
                    : Uni.createFrom().failure(new NotFoundException("Note not found")));
  }
}
