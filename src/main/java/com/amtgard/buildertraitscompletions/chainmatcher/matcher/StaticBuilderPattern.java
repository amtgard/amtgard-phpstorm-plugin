package com.amtgard.buildertraitscompletions.chainmatcher.matcher;

import com.amtgard.buildertraitscompletions.chainmatcher.IMatcherChainLink;
import com.amtgard.buildertraitscompletions.model.FqnString;
import com.amtgard.buildertraitscompletions.util.Boolish;
import com.amtgard.buildertraitscompletions.util.BuilderMode;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.psi.elements.impl.ClassReferenceImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static com.amtgard.buildertraitscompletions.util.PsiPatternMatchersUtil.identifierMatchesBuilderPattern;
import static com.amtgard.buildertraitscompletions.util.PsiPatternMatchersUtil.methodRefHasClassRefFirstChild;

public class StaticBuilderPattern implements IMatcherChainLink {
    private static final String BUILDER = "builder";

    @Override
    public Boolish match(CompletionParameters parameters) {
        Optional match = matchStaticBuilderPattern(parameters.getPosition().getPrevSibling());
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
    public static Optional matchStaticBuilderPattern(@NotNull PsiElement identifier) {
        Object match = identifierMatchesBuilderPattern(identifier, BUILDER)
            .truthy(m -> methodRefHasClassRefFirstChild(m, ClassReferenceImpl.class));
        if (match instanceof Optional) {
            return ((Optional) match).map(v -> ((ClassReferenceImpl)v).getFQN());
        }
        return Optional.empty();
    }

}
