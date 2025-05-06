package com.amtgard.buildertraitscompletions.chainmatcher.matcher;

import com.amtgard.buildertraitscompletions.model.FqnString;
import com.amtgard.buildertraitscompletions.util.Boolish;
import com.amtgard.buildertraitscompletions.util.PsiPatternMatchersUtil;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.psi.elements.impl.VariableImpl;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
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
import static com.amtgard.buildertraitscompletions.chainmatcher.matcher.ToBuilderPattern.TO_BUILDER;
import static com.amtgard.buildertraitscompletions.util.BuilderMode.BUILDER_MODE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ToBuilderPatternTest {
    @Mock
    PsiElement identifier = Mockito.mock(PsiElement.class);

    @Mock
    VariableImpl variableImpl = Mockito.mock(VariableImpl.class);

    @Mock
    Boolish identifierMatches = Mockito.mock(Boolish.class);

    @Mock
    CompletionParameters completionParameters = Mockito.mock(CompletionParameters.class);

    @Mock
    PhpType phpType = Mockito.mock(PhpType.class);

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
    public void toBuilderBuilderMode_isBUILDER_MODE() {
        assertEquals(BUILDER_MODE, new ToBuilderPattern().getBuilderMode());
    }

    @Test
    public void staticPattern_matchesToBuilderPattern() {
        psiPatternMatchersUtilMockedStatic.when(() -> PsiPatternMatchersUtil.identifierMatchesBuilderPattern(identifier, TO_BUILDER))
            .thenReturn(identifierMatches);
        when(identifierMatches.truthy(any())).thenReturn(Optional.of(variableImpl));
        when(variableImpl.getGlobalType()).thenReturn(phpType);
        when(phpType.toString()).thenReturn("Penguin\\Penguin");

        assertEquals("Penguin\\Penguin", ToBuilderPattern.matchToBuilderPattern(identifier).get());

        when(identifierMatches.truthy(any())).thenReturn(null);
        assertEquals(Optional.empty(), ToBuilderPattern.matchToBuilderPattern(identifier));
    }

    @Test
    public void whenMatchNotPresent_thenFalsey() {
        psiPatternMatchersUtilMockedStatic.when(() -> PsiPatternMatchersUtil.identifierMatchesBuilderPattern(identifier, TO_BUILDER))
            .thenReturn(identifierMatches);
        when(identifierMatches.truthy(any())).thenReturn(null);
        when(completionParameters.getPosition()).thenReturn(identifier);
        when(identifier.getPrevSibling()).thenReturn(identifier);

        ToBuilderPattern toBuilderPattern = new ToBuilderPattern();
        assertFalse(toBuilderPattern.match(completionParameters).truthy());
    }

    @Test
    public void testMatch() {
        psiPatternMatchersUtilMockedStatic.when(() -> PsiPatternMatchersUtil.identifierMatchesBuilderPattern(identifier, TO_BUILDER))
            .thenReturn(identifierMatches);
        when(identifierMatches.truthy(any())).thenReturn(Optional.of(variableImpl));
        when(variableImpl.getGlobalType()).thenReturn(phpType);
        when(phpType.toString()).thenReturn("Penguin\\Penguin");
        when(completionParameters.getPosition()).thenReturn(identifier);
        when(identifier.getPrevSibling()).thenReturn(identifier);

        ToBuilderPattern toBuilderPattern = new ToBuilderPattern();
        assertEquals("Penguin\\Penguin", ((FqnString)toBuilderPattern.match(completionParameters).get()).getFqn());
    }}
