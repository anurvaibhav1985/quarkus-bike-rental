package org.acme;

import java.net.URI;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.acme.service.Fruit;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

@Path("data")
// @Blocking
public class PostgresResource {

    @Inject
    io.vertx.mutiny.pgclient.PgPool client;

    @PostConstruct
    void config() {

        initdb();

    }

    @GET
    public Multi<Fruit> get() {
        return Fruit.findAll(client);
    }

    @POST
    public Uni<Response> create(Fruit fruit) {
        return fruit.save(client).onItem().transform(id -> URI.create("/fruits/" + id)).onItem()
                .transform(uri -> Response.created(uri).build());
    }

    private void initdb() {

        client.query("DROP TABLE IF EXISTS fruits").execute()
                .flatMap(r -> client.query("CREATE TABLE fruits (id SERIAL PRIMARY KEY, name TEXT NOT NULL)").execute())
                .flatMap(r -> client.query("INSERT INTO fruits (name) VALUES ('Orange')").execute())
                .flatMap(r -> client.query("INSERT INTO fruits (name) VALUES ('Pear')").execute())
                .flatMap(r -> client.query("INSERT INTO fruits (name) VALUES ('Apple')").execute());
    }
}
