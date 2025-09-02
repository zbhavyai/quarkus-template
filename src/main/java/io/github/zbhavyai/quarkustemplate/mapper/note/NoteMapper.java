package io.github.zbhavyai.quarkustemplate.mapper.note;

import io.github.zbhavyai.quarkustemplate.dto.note.NoteCreateDTO;
import io.github.zbhavyai.quarkustemplate.dto.note.NoteDTO;
import io.github.zbhavyai.quarkustemplate.dto.note.NoteSummaryDTO;
import io.github.zbhavyai.quarkustemplate.dto.note.NoteUpdateDTO;
import io.github.zbhavyai.quarkustemplate.entity.note.Note;

public class NoteMapper {

  public static NoteDTO toDTO(Note note) {
    return new NoteDTO(note.id, note.title, note.content, note.createdAt, note.updatedAt);
  }

  public static NoteSummaryDTO toSummaryDTO(Note note) {
    return new NoteSummaryDTO(note.id, note.title, note.updatedAt);
  }

  public static Note fromCreateDTO(NoteCreateDTO dto) {
    Note note = new Note();
    note.title = dto.title();
    note.content = dto.content();
    return note;
  }

  public static Note updateNote(Note note, NoteUpdateDTO dto) {
    if (dto.title() != null) {
      note.title = dto.title();
    }

    if (dto.content() != null) {
      note.content = dto.content();
    }

    return note;
  }
}
