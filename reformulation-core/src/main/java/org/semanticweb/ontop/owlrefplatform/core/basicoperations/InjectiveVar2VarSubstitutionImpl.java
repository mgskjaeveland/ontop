package org.semanticweb.ontop.owlrefplatform.core.basicoperations;


import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.semanticweb.ontop.model.ImmutableSubstitution;
import org.semanticweb.ontop.model.ImmutableTerm;
import org.semanticweb.ontop.model.impl.VariableImpl;

import java.util.Map;

public class InjectiveVar2VarSubstitutionImpl extends Var2VarSubstitutionImpl implements InjectiveVar2VarSubstitution {
    /**
     * Regular constructor
     */
    public InjectiveVar2VarSubstitutionImpl(Map<VariableImpl, VariableImpl> substitutionMap) {
        super(substitutionMap);

        /**
         * Injectivity constraint
         */
        ImmutableSet<VariableImpl> valueSet = ImmutableSet.copyOf(substitutionMap.values());
        if (valueSet.size() < substitutionMap.keySet().size()) {
            throw new IllegalArgumentException("Non-injective map given: " + substitutionMap);
        }

    }

    @Override
    public ImmutableSubstitution applyRenaming(ImmutableSubstitution substitutionToRename) {

        ImmutableMap.Builder<VariableImpl, ImmutableTerm> substitutionMapBuilder = ImmutableMap.builder();

        /**
         * Substitutes the keys and values of the substitution to rename.
         */
        for (Map.Entry<VariableImpl, ImmutableTerm> originalEntry : substitutionToRename.getImmutableMap().entrySet()) {

            VariableImpl convertedVariable = applyToVariable(originalEntry.getKey());
            ImmutableTerm convertedTargetTerm = apply(originalEntry.getValue());

            // Safe because the local substitution is injective
            substitutionMapBuilder.put(convertedVariable, convertedTargetTerm);
        }

        return new ImmutableSubstitutionImpl(substitutionMapBuilder.build());
    }

}