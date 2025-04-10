package com.amtgard.buildertraitscompletions.pipeline.stage;

import com.amtgard.buildertraitscompletions.model.StageContext;
import com.amtgard.buildertraitscompletions.pipeline.AbstractPipelineStage;
import com.amtgard.buildertraitscompletions.util.Boolish;
import com.intellij.psi.PsiElement;

import java.util.List;

import static com.amtgard.buildertraitscompletions.util.PsiPatternMatchersUtil.findProgenitorMethodRefImpl;
import static com.amtgard.buildertraitscompletions.util.PsiPatternMatchersUtil.findChildPsiElementIdentifiers;

public class PruneCompletions extends AbstractPipelineStage {

    @Override
    public Boolish execute(StageContext context) {
        try {
            PsiElement prevSibling = context.getParameters().getPosition().getPrevSibling();
            PsiElement ancestor = findProgenitorMethodRefImpl(prevSibling);
            List<String> identifiers = findChildPsiElementIdentifiers(ancestor);
            context.getCompletions().removeAll(identifiers);
            return Boolish.maybe(context);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Boolish.maybe(context);
        }
    }
}