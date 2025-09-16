package io.github.zbhavyai.quarkustemplate.service.note;

import io.github.zbhavyai.quarkustemplate.dto.note.NoteCreateDTO;
import io.github.zbhavyai.quarkustemplate.dto.note.NoteDTO;
import io.github.zbhavyai.quarkustemplate.dto.note.NoteSummaryDTO;
import io.github.zbhavyai.quarkustemplate.dto.note.NoteUpdateDTO;
import io.smallrye.mutiny.Uni;
import java.util.List;

public interface NoteService {

  Uni<List<NoteSummaryDTO>> listNotes();

  Uni<NoteDTO> getNoteById(String id);

  Uni<NoteDTO> createNote(NoteCreateDTO dto);

  Uni<NoteDTO> updateNote(String id, NoteUpdateDTO dto);

  Uni<Void> deleteNote(String id);
}
