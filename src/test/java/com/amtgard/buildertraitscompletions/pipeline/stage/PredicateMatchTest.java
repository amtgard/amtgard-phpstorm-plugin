package com.amtgard.buildertraitscompletions.pipeline.stage;

import com.amtgard.buildertraitscompletions.model.StageContext;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.psi.PsiElement;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PredicateMatchTest {
    @Mock
    CompletionParameters completionParameters = Mockito.mock(CompletionParameters.class);

    @Mock
    PsiElement psiElement = Mockito.mock(PsiElement.class);

    @Mock
    PsiElement prevSibling = Mockito.mock(PsiElement.class);

    @Test
    public void whenArrow_thenMatch() {
        when(completionParameters.getPosition()).thenReturn(psiElement);
        when(psiElement.getPrevSibling()).thenReturn(prevSibling);
        when(prevSibling.textMatches("->")).thenReturn(true);
        PredicateMatch predicateMatch = new PredicateMatch();
        assertTrue(predicateMatch.execute(StageContext.builder().parameters(completionParameters).build()).truthy());
    }

}
