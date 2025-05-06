package com.amtgard.buildertraitscompletions.chainmatcher.matcher;

import com.amtgard.buildertraitscompletions.model.FqnString;
import com.amtgard.buildertraitscompletions.util.Boolish;
import com.amtgard.buildertraitscompletions.util.PsiPatternMatchersUtil;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.impl.ClassReferenceImpl;
import com.jetbrains.php.lang.psi.elements.impl.MethodReferenceImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.amtgard.buildertraitscompletions.chainmatcher.matcher.StaticBuilderPattern.BUILDER;
import static com.amtgard.buildertraitscompletions.util.BuilderMode.BUILDER_MODE;
import static com.amtgard.buildertraitscompletions.util.BuilderMode.GETTER_SETTER_MODE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StaticBuilderPatternTest {
    @Mock
    PsiElement identifier = Mockito.mock(PsiElement.class);

    @Mock
    ClassReferenceImpl classReference = Mockito.mock(ClassReferenceImpl.class);

    @Mock
    Boolish identifierMatches = Mockito.mock(Boolish.class);

    @Mock
    CompletionParameters completionParameters = Mockito.mock(CompletionParameters.class);

    MockedStatic<PsiPatternMatchersUtil> psiPatternMatchersUtilMockedStatic;

    @BeforeEach
    void setUp() {
        psiPatternMatchersUtilMockedStatic = Mockito.mockStatic(PsiPatternMatchersUtil.class);
    }

    @AfterEach
    void tearDown() {
        psiPatternMatchersUtilMockedStatic.close();
    }

    @Test
    public void staticBuilderMode_isBUILDER_MODE() {
        assertEquals(BUILDER_MODE, new StaticBuilderPattern().getBuilderMode());
    }

    @Test
    public void staticPattern_matchesStaticBuilderPattern() {
        psiPatternMatchersUtilMockedStatic.when(() -> PsiPatternMatchersUtil.identifierMatchesBuilderPattern(identifier, BUILDER))
            .thenReturn(identifierMatches);
        when(identifierMatches.truthy(any())).thenReturn(Optional.of(classReference));
        when(classReference.getFQN()).thenReturn("Penguin\\Penguin");

        assertEquals("Penguin\\Penguin", StaticBuilderPattern.matchStaticBuilderPattern(identifier).get());

        when(identifierMatches.truthy(any())).thenReturn(null);
        assertEquals(Optional.empty(), StaticBuilderPattern.matchStaticBuilderPattern(identifier));
    }

    @Test
    public void whenMatchNotPresent_thenFalsey() {
        psiPatternMatchersUtilMockedStatic.when(() -> PsiPatternMatchersUtil.identifierMatchesBuilderPattern(identifier, BUILDER))
            .thenReturn(identifierMatches);
        when(identifierMatches.truthy(any())).thenReturn(null);

        when(completionParameters.getPosition()).thenReturn(identifier);
        when(identifier.getPrevSibling()).thenReturn(identifier);

        StaticBuilderPattern staticBuilderPattern = new StaticBuilderPattern();
        assertFalse(staticBuilderPattern.match(completionParameters).truthy());
    }

    @Test
    public void testMatch() {
        psiPatternMatchersUtilMockedStatic.when(() -> PsiPatternMatchersUtil.identifierMatchesBuilderPattern(identifier, BUILDER))
            .thenReturn(identifierMatches);
        when(identifierMatches.truthy(any())).thenReturn(Optional.of(classReference));
        when(classReference.getFQN()).thenReturn("Penguin\\Penguin");

        when(completionParameters.getPosition()).thenReturn(identifier);
        when(identifier.getPrevSibling()).thenReturn(identifier);

        StaticBuilderPattern staticBuilderPattern = new StaticBuilderPattern();
        assertEquals("Penguin\\Penguin", ((FqnString)staticBuilderPattern.match(completionParameters).get()).getFqn());
    }
}
