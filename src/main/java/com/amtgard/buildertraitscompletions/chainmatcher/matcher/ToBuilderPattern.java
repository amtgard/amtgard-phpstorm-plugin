package com.amtgard.buildertraitscompletions.chainmatcher.matcher;

import com.amtgard.buildertraitscompletions.chainmatcher.IMatcherChainLink;
import com.amtgard.buildertraitscompletions.model.FqnString;
import com.amtgard.buildertraitscompletions.util.Boolish;
import com.amtgard.buildertraitscompletions.util.BuilderMode;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.psi.elements.impl.VariableImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static com.amtgard.buildertraitscompletions.util.PsiPatternMatchersUtil.identifierMatchesBuilderPattern;
import static com.amtgard.buildertraitscompletions.util.PsiPatternMatchersUtil.methodRefHasClassRefFirstChild;

public class ToBuilderPattern implements IMatcherChainLink {
    static final String TO_BUILDER = "toBuilder";

    @Override
    public Boolish match(CompletionParameters parameters) {
        Optional match = matchToBuilderPattern(parameters.getPosition().getPrevSibling());
        if (match.isPresent()) {
            return Boolish.maybe(FqnString.builder().fqn((String) match.get()).build());
        }
        return Boolish.falsey();
    }

    @Override
    public BuilderMode getBuilderMode() {
        return BuilderMode.BUILDER_MODE;
    }

    /**
     * Matches some part of a builder chain where the root class is an Amtgard Builder.
     * <p>
     * $cat = Cat::builder-> // Match
     *
     * @param identifier
     * @return
     */
    public static Optional matchToBuilderPattern(@NotNull PsiElement identifier) {
        Object match = identifierMatchesBuilderPattern(identifier, TO_BUILDER)
            .truthy(m -> methodRefHasClassRefFirstChild(m, VariableImpl.class));
        if (match instanceof Optional) {
            return ((Optional) match).map(v -> ((VariableImpl)v).getGlobalType().toString());
        }
        return Optional.empty();
    }
}
