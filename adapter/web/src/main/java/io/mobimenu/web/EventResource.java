package io.mobimenu.web;

import io.smallrye.mutiny.Multi;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.RestStreamElementType;

@Path("/event-stream")
public class EventResource {

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @RestStreamElementType(MediaType.APPLICATION_JSON)
    public Multi<String> eventStream() {
        return Multi.createFrom().item("Testing testing");
    }

}
