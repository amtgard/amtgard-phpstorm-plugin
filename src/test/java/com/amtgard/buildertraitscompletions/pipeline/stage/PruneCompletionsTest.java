package com.amtgard.buildertraitscompletions.pipeline.stage;

import com.amtgard.buildertraitscompletions.model.StageContext;
import com.amtgard.buildertraitscompletions.util.AmtgardBuilderUtil;
import com.amtgard.buildertraitscompletions.util.Boolish;
import com.amtgard.buildertraitscompletions.util.PsiPatternMatchersUtil;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.psi.PsiElement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class PruneCompletionsTest {
    @Mock
    CompletionParameters completionParameters = Mockito.mock(CompletionParameters.class);

    @Mock
    PsiElement psiElement = Mockito.mock(PsiElement.class);

    @Mock
    PsiElement prevSibling = Mockito.mock(PsiElement.class);

    @Mock
    PsiElement ancestor = Mockito.mock(PsiElement.class);

    MockedStatic<PsiPatternMatchersUtil> psiPatternMatchersUtilMockedStatic;


    @BeforeEach
    public void setup() {
        psiPatternMatchersUtilMockedStatic = Mockito.mockStatic(PsiPatternMatchersUtil.class);
    }

    @AfterEach
    public void teardown() {
        psiPatternMatchersUtilMockedStatic.close();
    }

    @Test
    public void whenPriorMatchesFound_thenPruned() {
        when(completionParameters.getPosition()).thenReturn(psiElement);
        when(psiElement.getPrevSibling()).thenReturn(prevSibling);
        psiPatternMatchersUtilMockedStatic.when(() -> PsiPatternMatchersUtil.findProgenitorMethodRefImpl(prevSibling)).thenReturn(ancestor);
        psiPatternMatchersUtilMockedStatic.when(() -> PsiPatternMatchersUtil.findChildPsiElementIdentifiers(ancestor)).thenReturn(List.of("repeatField"));

        StageContext.CompletionStrategy unique = StageContext.CompletionStrategy.builder().root("uniqueField").build();
        StageContext context = StageContext.builder()
            .parameters(completionParameters)
            .completions(List.of(
                StageContext.CompletionStrategy.builder().root("repeatField").build(),
                unique))
            .build();
        PruneCompletions pruneCompletions = new PruneCompletions();
        Boolish<StageContext> result = pruneCompletions.execute(context);
        assertEquals(1, result.get().getCompletions().size());
        assertTrue(result.get().getCompletions().contains(unique));
    }
}
