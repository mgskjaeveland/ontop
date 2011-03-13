package org.obda.owlrefplatform.core.abox;

import org.semanticweb.owl.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

/**
 * Store ABox assertions in the DB
 *
 * @author Sergejs Pugac
 */
public class ABoxSerializer {
    private final static Logger log = LoggerFactory
            .getLogger(ABoxSerializer.class);

    public static final String class_table = "class";
    public static final String role_table = "role";
    public static final String literal_table = "literal";

    public static final String class_table_create =
            "CREATE TABLE " + class_table + " ( "
                    + "URI VARCHAR(100),"
                    + "IDX INTEGER" + ");";

    public static final String role_table_create =
            "CREATE TABLE " + role_table + " ( "
                    + "URI1 VARCHAR(100), "
                    + "URI2 VARCHAR(100), "
                    + "IDX INTEGER" + ");";

    public static final String literal_table_create =
            "CREATE TABLE " + literal_table + " ( "
                    + "URI VARCHAR(100),"
                    + "LITERAL VARCHAR(100),"
                    + "IDX INTEGER" + ");";

    public static final String class_table_drop = "DROP TABLE " + class_table
            + " IF EXISTS;";

    public static final String role_table_drop = "DROP TABLE " + role_table
            + " IF EXISTS;";

    public static final String literal_table_drop = "DROP TABLE "
            + literal_table + " IF EXISTS;";

    public static final String class_insert = "INSERT INTO " + class_table
            + "(URI, IDX) VALUES (?, ?)";

    public static final String role_insert = "INSERT INTO " + role_table
            + "(URI1, URI2, IDX) VALUES (?, ?, ?)";

    public static final String literal_insert = "INSERT INTO " + literal_table
            + "(URI, LITERAL, IDX) VALUES (?, ?, ?)";

    public static void recreate_tables(Connection conn) {
        try {
            log.debug("Recreating ABox tables");
            conn.createStatement().execute(class_table_drop);
            conn.createStatement().execute(role_table_drop);
            conn.createStatement().execute(literal_table_drop);

            conn.createStatement().execute(class_table_create);
            conn.createStatement().execute(role_table_create);
            conn.createStatement().execute(literal_table_create);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    public static void ABOX2DB(Set<OWLOntology> ontologies, DAG dag, Connection conn) throws SQLException {
        PreparedStatement cls_stm = conn.prepareStatement(class_insert);
        PreparedStatement role_stm = conn.prepareStatement(role_insert);
        PreparedStatement lit_stm = conn.prepareStatement(literal_insert);

        Map<String, DAGNode> index = dag.getIndex();


        for (OWLOntology onto : ontologies) {
            onto.getIndividualAxioms();

            for (OWLIndividualAxiom ax : onto.getIndividualAxioms()) {
                if (ax instanceof OWLDataPropertyAssertionAxiom) {
                    OWLDataPropertyAssertionAxiom triple = (OWLDataPropertyAssertionAxiom) ax;
                    String prop = triple.getProperty().asOWLDataProperty().getURI().toString();
                    String uri = triple.getSubject().asOWLIndividual().getURI().toString();
                    String lit = triple.getObject().getLiteral();

                    int idx = index.get(prop).getIndex();

                    lit_stm.setString(1, uri);
                    lit_stm.setString(2, lit);
                    lit_stm.setInt(3, idx);
                    lit_stm.addBatch();

                } else if (ax instanceof OWLObjectPropertyAssertionAxiom) {
                    OWLObjectPropertyAssertionAxiom triple = (OWLObjectPropertyAssertionAxiom) ax;
                    String prop = triple.getProperty().asOWLObjectProperty().getURI().toString();
                    String uri1 = triple.getSubject().asOWLIndividual().getURI().toString();
                    String uri2 = triple.getObject().asOWLIndividual().getURI().toString();


                    int idx = index.get(prop).getIndex();
                    //log.debug(uri1+" "+uri2+" " + Integer.toString(idx));

                    role_stm.setString(1, uri1);
                    role_stm.setString(2, uri2);
                    role_stm.setInt(3, idx);
                    role_stm.addBatch();

                } else if (ax instanceof OWLClassAssertionAxiom) {
                    OWLClassAssertionAxiom triple = (OWLClassAssertionAxiom) ax;
                    String cls = triple.getDescription().asOWLClass().getURI().toString();
                    // XXX: strange behaviour - owlapi generates an extra assertion of the form ClassAssertion(Thing, i)
                    if (!cls.equals(DAG.owl_thing)) {
                        String uri = triple.getIndividual().getURI().toString();
                        int idx = index.get(cls).getIndex();

                        cls_stm.setString(1, uri);
                        cls_stm.setInt(2, idx);
                        cls_stm.addBatch();
                    }
                }
            }
        }
        cls_stm.executeBatch();
        role_stm.executeBatch();
        lit_stm.executeBatch();
    }
}
