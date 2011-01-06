package org.obda.reformulation.tests;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Before;
import org.obda.query.domain.Atom;
import org.obda.query.domain.CQIE;
import org.obda.query.domain.Predicate;
import org.obda.query.domain.PredicateFactory;
import org.obda.query.domain.Term;
import org.obda.query.domain.TermFactory;
import org.obda.query.domain.imp.AtomImpl;
import org.obda.query.domain.imp.BasicPredicateFactoryImpl;
import org.obda.query.domain.imp.CQIEImpl;
import org.obda.query.domain.imp.FunctionalTermImpl;
import org.obda.query.domain.imp.TermFactoryImpl;
import org.obda.reformulation.dllite.CQCUtilities;
import org.obda.reformulation.dllite.PositiveInclusionApplicator;

public class CQCUtilitiesTest extends TestCase {

	CQIE						initialquery1	= null;

	PositiveInclusionApplicator	piapplicator	= new PositiveInclusionApplicator();

	PredicateFactory			pfac			= BasicPredicateFactoryImpl.getInstance();
	TermFactory					tfac			= TermFactoryImpl.getInstance();

	Predicate					r				= pfac.createPredicate(URI.create("R"), 2);
	Predicate					s				= pfac.createPredicate(URI.create("S"), 3);
	Predicate					q				= pfac.createPredicate(URI.create("q"), 5);

	Term						x				= tfac.createVariable("x");
	Term						y				= tfac.createVariable("y");
	Term						c1				= tfac.createURIConstant(URI.create("URI1"));
	Term						c2				= tfac.createValueConstant("m");

	Term						u1				= tfac.createUndistinguishedVariable();
	Term						u2				= tfac.createUndistinguishedVariable();

	@Before
	public void setUp() throws Exception {
		/*
		 * Creating the query:
		 * 
		 * q(x, <URI1>, 'm', y, f(x,y)) :- R(x,y), S('m', f(x), y)
		 * 
		 * Should generate
		 * 
		 * q('CANx1', <URI1>, 'm', 'CANy2', f('CANx1','CANy2')) :-
		 * R('CANx1','CANy2'), S('m', f('CANx1'), 'CANy')
		 */
		List<Term> headTerms = new LinkedList<Term>();
		headTerms.add(x);
		headTerms.add(c1);
		headTerms.add(c2);
		headTerms.add(y);
		List<Term> fterms1 = new LinkedList<Term>();
		fterms1.add(x);
		fterms1.add(y);
		headTerms.add(tfac.createFunctionalTerm(pfac.createPredicate(URI.create("f"), 2), fterms1));

		Atom head = new AtomImpl(q, headTerms);

		List<Atom> body = new LinkedList<Atom>();

		List<Term> atomTerms1 = new LinkedList<Term>();
		atomTerms1.add(x);
		atomTerms1.add(y);
		body.add(new AtomImpl(r, atomTerms1));

		List<Term> atomTerms2 = new LinkedList<Term>();
		atomTerms2.add(c2);
		List<Term> fterms2 = new LinkedList<Term>();
		fterms2.add(x);
		atomTerms2.add(tfac.createFunctionalTerm(pfac.createPredicate(URI.create("f"), 1), fterms2));
		atomTerms2.add(y);
		body.add(new AtomImpl(s, atomTerms2));

		initialquery1 = new CQIEImpl(head, body, false);
	}

	public void testGrounding() {
		CQIE groundedcq = CQCUtilities.getCanonicalQuery(initialquery1);

		List<Term> head = groundedcq.getHead().getTerms();
		assertTrue(head.get(0).equals(tfac.createValueConstant("CANx1")));
		assertTrue(head.get(1).equals(tfac.createURIConstant(URI.create("URI1"))));
		assertTrue(head.get(2).equals(tfac.createValueConstant("m")));
		assertTrue(head.get(3).equals(tfac.createValueConstant("CANy2")));
		FunctionalTermImpl f1 = (FunctionalTermImpl) head.get(4);
		assertTrue(f1.getTerms().get(0).equals(tfac.createValueConstant("CANx1")));
		assertTrue(f1.getTerms().get(1).equals(tfac.createValueConstant("CANy2")));

		head = groundedcq.getBody().get(0).getTerms();
		assertTrue(head.get(0).equals(tfac.createValueConstant("CANx1")));
		assertTrue(head.get(1).equals(tfac.createValueConstant("CANy2")));

		head = groundedcq.getBody().get(1).getTerms();
		assertTrue(head.get(0).equals(tfac.createValueConstant("m")));
		f1 = (FunctionalTermImpl) head.get(1);
		assertTrue(f1.getTerms().get(0).equals(tfac.createValueConstant("CANx1")));
		assertTrue(head.get(2).equals(tfac.createValueConstant("CANy2")));
	}

	public void testContainment1() {

		// Query 1 - q(x,y) :- R(x,y), R(y,z)

		List<Term> headTerms = new LinkedList<Term>();
		headTerms.add(x);
		headTerms.add(y);

		Atom head = new AtomImpl(pfac.createPredicate(URI.create("q"), 2), headTerms);

		List<Atom> body = new LinkedList<Atom>();

		List<Term> terms = new LinkedList<Term>();
		terms.add(tfac.createVariable("x"));
		terms.add(tfac.createVariable("y"));
		body.add(new AtomImpl(pfac.createPredicate(URI.create("R"), 2), terms));

		terms = new LinkedList<Term>();
		terms.add(tfac.createVariable("y"));
		terms.add(tfac.createVariable("z"));
		body.add(new AtomImpl(pfac.createPredicate(URI.create("R"), 2), terms));

		CQIE q1 = new CQIEImpl(head, body, false);

		// Query 2 - q(y,y) :- R(y,y)

		headTerms = new LinkedList<Term>();
		headTerms.add(tfac.createVariable("y"));
		headTerms.add(tfac.createVariable("y"));

		head = new AtomImpl(pfac.createPredicate(URI.create("q"), 2), headTerms);

		body = new LinkedList<Atom>();

		terms = new LinkedList<Term>();
		terms.add(tfac.createVariable("y"));
		terms.add(tfac.createVariable("y"));
		body.add(new AtomImpl(pfac.createPredicate(URI.create("R"), 2), terms));

		CQIE q2 = new CQIEImpl(head, body, false);

		// Query 3 - q(m,n) :- R(m,n)

		headTerms = new LinkedList<Term>();
		headTerms.add(tfac.createVariable("m"));
		headTerms.add(tfac.createVariable("n"));

		head = new AtomImpl(pfac.createPredicate(URI.create("q"), 2), headTerms);

		body = new LinkedList<Atom>();

		terms = new LinkedList<Term>();
		terms.add(tfac.createVariable("m"));
		terms.add(tfac.createVariable("n"));
		body.add(new AtomImpl(pfac.createPredicate(URI.create("R"), 2), terms));

		CQIE q3 = new CQIEImpl(head, body, false);
		
		
		// Query 4 - q(m,n) :- S(m,n) R(m,n)

		headTerms = new LinkedList<Term>();
		headTerms.add(tfac.createVariable("m"));
		headTerms.add(tfac.createVariable("n"));

		head = new AtomImpl(pfac.createPredicate(URI.create("q"), 2), headTerms);

		body = new LinkedList<Atom>();

		terms = new LinkedList<Term>();
		terms.add(tfac.createVariable("m"));
		terms.add(tfac.createVariable("n"));
		body.add(new AtomImpl(pfac.createPredicate(URI.create("S"), 2), terms));
		
		terms = new LinkedList<Term>();
		terms.add(tfac.createVariable("m"));
		terms.add(tfac.createVariable("n"));
		body.add(new AtomImpl(pfac.createPredicate(URI.create("R"), 2), terms));

		CQIE q4 = new CQIEImpl(head, body, false);
		

		// Checking contaiment q2 <= q1

		CQCUtilities cqcu = new CQCUtilities(q2);
		assertTrue(cqcu.isContainedIn(q1));

		// Checking contaiment q1 <= q2

		cqcu = new CQCUtilities(q1);
		assertFalse(cqcu.isContainedIn(q2));

		// Checking contaiment q1 <= q3

		cqcu = new CQCUtilities(q1);
		assertTrue(cqcu.isContainedIn(q3));

		// Checking contaiment q3 <= q1

		cqcu = new CQCUtilities(q3);
		assertFalse(cqcu.isContainedIn(q1));
		
		
		// Checking contaiment q1 <= q4

		cqcu = new CQCUtilities(q1);
		assertFalse(cqcu.isContainedIn(q4));

		// Checking contaiment q4 <= q1

		cqcu = new CQCUtilities(q4);
		assertFalse(cqcu.isContainedIn(q1));

	}

}
