package io.github.zbhavyai.quarkustemplate.api.rest.note;

import io.github.zbhavyai.quarkustemplate.api.rest.common.ResponseUtils;
import io.github.zbhavyai.quarkustemplate.dto.note.NoteCreateDTO;
import io.github.zbhavyai.quarkustemplate.dto.note.NoteUpdateDTO;
import io.github.zbhavyai.quarkustemplate.service.note.NoteService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

@Path("/v1/note")
public class NoteResource {

  private final NoteService service;
  private final UriInfo uriInfo;

  @Inject
  public NoteResource(NoteService service, UriInfo uriInfo) {
    this.service = service;
    this.uriInfo = uriInfo;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Uni<Response> listNotes() {
    return service.listNotes().onItem().transform(ResponseUtils::handleSuccess);
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Uni<Response> getNoteById(@PathParam("id") String id) {
    return service.getNoteById(id).onItem().transform(ResponseUtils::handleSuccess);
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Uni<Response> createNote(NoteCreateDTO dto) {
    return service
        .createNote(dto)
        .onItem()
        .transform(
            res ->
                ResponseUtils.handleCreated(
                    res, uriInfo.getRequestUriBuilder().path(res.id()).build()));
  }

  @PATCH
  @Path("/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Uni<Response> updateNote(@PathParam("id") String id, NoteUpdateDTO dto) {
    return service.updateNote(id, dto).onItem().transform(ResponseUtils::handleSuccess);
  }

  @DELETE
  @Path("/{id}")
  public Uni<Response> deleteNote(@PathParam("id") String id) {
    return service.deleteNote(id).onItem().transform(ignored -> ResponseUtils.handleDeleted());
  }
}
