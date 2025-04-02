package com.amtgard.buildertraitscompletions.chainmatcher.matcher;

import com.amtgard.buildertraitscompletions.chainmatcher.IMatcherChainLink;
import com.amtgard.buildertraitscompletions.model.FqnString;
import com.amtgard.buildertraitscompletions.util.Boolish;
import com.amtgard.buildertraitscompletions.util.BuilderMode;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.psi.elements.impl.FieldReferenceImpl;
import com.jetbrains.php.lang.psi.elements.impl.VariableImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class GetterSetterPattern implements IMatcherChainLink {
    @Override
    public Boolish match(CompletionParameters parameters) {
        Boolish match = matchGetterSetter(parameters.getPosition().getPrevSibling());
        if (match.truthy()) {
            return Boolish.maybe(FqnString.builder().fqn((String) match.get()).build());
        }
        return Boolish.falsey();
    }

    @Override
    public BuilderMode getBuilderMode() {
        return BuilderMode.GETTER_SETTER_MODE;
    }

    /**
     * Matches
     *
     * $cat = new Penguin();
     * $cat->
     *
     * @param identifier
     * @return
     */
    public Boolish matchGetterSetter(@NotNull PsiElement identifier) {
        if (identifier.getParent() instanceof FieldReferenceImpl) {
            PsiElement parent = identifier.getParent();
            for (PsiElement child: parent.getChildren()) {
                if (child instanceof VariableImpl) {
                    return Boolish.maybe(((VariableImpl) child).getGlobalType().toString());
                }
            }
        }
        return Boolish.falsey();
    }
}
