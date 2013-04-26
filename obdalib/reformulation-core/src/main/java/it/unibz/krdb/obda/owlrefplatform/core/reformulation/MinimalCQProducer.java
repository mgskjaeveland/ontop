package it.unibz.krdb.obda.owlrefplatform.core.reformulation;

import it.unibz.krdb.obda.model.Function;
import it.unibz.krdb.obda.model.NewLiteral;
import it.unibz.krdb.obda.model.impl.AnonymousVariable;
import it.unibz.krdb.obda.ontology.BasicClassDescription;
import it.unibz.krdb.obda.ontology.OntologyFactory;
import it.unibz.krdb.obda.ontology.PropertySomeRestriction;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinimalCQProducer {
	private final TreeWitnessReasonerLite reasoner;
	private List<Function> atoms = new LinkedList<Function>();
	private List<Function> noCheckAtoms = new LinkedList<Function>();
	
	private final OntologyFactory ontFactory;
	private static final Logger log = LoggerFactory.getLogger(MinimalCQProducer.class);	
	
	public MinimalCQProducer(TreeWitnessReasonerLite reasoner) {
		this.reasoner = reasoner;
		this.ontFactory = reasoner.getOntologyFactory();
	}

	public MinimalCQProducer(TreeWitnessReasonerLite reasoner, MinimalCQProducer cqp) {
		this.reasoner = reasoner;
		this.ontFactory = reasoner.getOntologyFactory();
		this.atoms.addAll(cqp.atoms);
		this.noCheckAtoms.addAll(cqp.noCheckAtoms);
	}

	public boolean isMoreSpecific(Function a1, Function a2) {
		if (a1 == a2 || a1.equals(a2))
			return true;

		if ((a2.getArity() == 1) && (a1.getArity() == 1)) {
			if (a1.getTerm(0).equals(a2.getTerm(0))) {
					// add a2.NewLiteral anonymous 
				Set<BasicClassDescription> subconcepts = reasoner.getSubConcepts(a2.getPredicate());
				if (subconcepts.contains(ontFactory.createClass(a1.getPredicate()))) {
					log.debug("{} IS MORE SPECIFIC (1-1) THAN {}", a1, a2);
					return true;
				}
			}
		}
		else if ((a1.getArity() == 1) && (a2.getArity() == 2)) {
			NewLiteral a1NewLiteral = a1.getTerm(0);
			if ((a2.getTerm(1) instanceof AnonymousVariable) && a2.getTerm(0).equals(a1NewLiteral)) {
				PropertySomeRestriction prop = ontFactory.getPropertySomeRestriction(a2.getPredicate(), false);
				Set<BasicClassDescription> subconcepts = reasoner.getSubConcepts(prop);
				if (subconcepts.contains(ontFactory.createClass(a1.getPredicate()))) {
					log.debug("{} IS MORE SPECIFIC (1-2) THAN {}",  a1, a2);
					return true;
				}
			}
			else if ((a2.getTerm(0) instanceof AnonymousVariable) && a2.getTerm(1).equals(a1NewLiteral)) {
				PropertySomeRestriction prop = ontFactory.getPropertySomeRestriction(a2.getPredicate(), true);
				Set<BasicClassDescription> subconcepts = reasoner.getSubConcepts(prop);
				if (subconcepts.contains(ontFactory.createClass(a1.getPredicate()))) {
					log.debug("{} IS MORE SPECIFIC (1-2) THAN {}",  a1, a2);
					return true;
				}
			}
		}
		else if ((a1.getArity() == 2) && (a2.getArity() == 1)) { // MOST USEFUL
			NewLiteral a2term = a2.getTerm(0);
			if (a1.getTerm(0).equals(a2term)) {
				PropertySomeRestriction prop = ontFactory.getPropertySomeRestriction(a1.getPredicate(), false);
				if (reasoner.getSubConcepts(a2.getPredicate()).contains(prop)) {
					log.debug("{} IS MORE SPECIFIC (2-1) THAN ", a1, a2);
					return true;
				}
			}
			if (a1.getTerm(1).equals(a2term)) {
				PropertySomeRestriction prop = ontFactory.getPropertySomeRestriction(a1.getPredicate(), true);
				if (reasoner.getSubConcepts(a2.getPredicate()).contains(prop)) {
					log.debug("{} IS MORE SPECIFIC (2-1) THAN {}", a1, a2);
					return true;
				}
			}
		}
		return false;
	}

	public boolean subsumes(Function atom) {
		for (Function a : atoms) 
			if (isMoreSpecific(a, atom))
				return true;
		return false;
	}
	
	public void add(Function atom) {
		if (subsumes(atom))
			return;
			
		Iterator<Function> i = atoms.iterator();
		while (i.hasNext()) {
			Function a = i.next();
			if (isMoreSpecific(atom, a))
				i.remove();
		}
		atoms.add(atom);
	}
	
	public void addAll(Collection<Function> aa) {
		for (Function a : aa) 
			add(a);
	}
	
	public void addNoCheck(Function atom) {
		noCheckAtoms.add(atom);
	}
	
	public void addAllNoCheck(Collection<Function> aa) {
		noCheckAtoms.addAll(aa);
	}

	public List<Function> getAtoms() {
		return atoms;
	}
	
	public List<Function> getNoCheckAtoms() {
		return noCheckAtoms;
	}
}
