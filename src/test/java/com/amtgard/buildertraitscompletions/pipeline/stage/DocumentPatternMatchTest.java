package com.amtgard.buildertraitscompletions.pipeline.stage;

import com.amtgard.buildertraitscompletions.chainmatcher.IMatcherChainLink;
import com.amtgard.buildertraitscompletions.model.FqnString;
import com.amtgard.buildertraitscompletions.model.StageContext;
import com.amtgard.buildertraitscompletions.provider.DocumentPatternMatcherProvider;
import com.amtgard.buildertraitscompletions.util.Boolish;
import com.amtgard.buildertraitscompletions.util.BuilderMode;
import com.google.common.collect.ImmutableList;
import com.intellij.codeInsight.completion.CompletionParameters;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DocumentPatternMatchTest {
    MockedStatic<DocumentPatternMatcherProvider> patternMatcherProviderMockedStatic;

    @Mock
    StageContext stageContext = Mockito.mock(StageContext.class);

    @Mock
    FqnString fqnString = Mockito.mock(FqnString.class);

    @Mock
    IMatcherChainLink matcherChainLink = Mockito.mock(IMatcherChainLink.class);

    @Mock
    Boolish<FqnString> match = Mockito.mock(Boolish.class);

    @BeforeEach
    public void setup() {
        patternMatcherProviderMockedStatic = Mockito.mockStatic(DocumentPatternMatcherProvider.class);
    }

    @AfterEach
    public void teardown() {
        patternMatcherProviderMockedStatic.close();
    }

    @Test
    public void happyPath() {
        patternMatcherProviderMockedStatic.when(() -> DocumentPatternMatcherProvider.provideMatcherChain()).thenReturn(ImmutableList.of(matcherChainLink));
        when(match.get()).thenReturn(fqnString);
        when(match.truthy()).thenReturn(true);
        when(matcherChainLink.match(any())).thenReturn(match);
        when(matcherChainLink.getBuilderMode()).thenReturn(BuilderMode.BUILDER_MODE);

        DocumentPatternMatch documentPatternMatch = new DocumentPatternMatch();
        documentPatternMatch.execute(stageContext);
        verify(match).get();
        verify(matcherChainLink).getBuilderMode();
        verify(stageContext).setFqnString(fqnString);
        verify(stageContext).setMode(BuilderMode.BUILDER_MODE);
    }

    @Test
    public void whenMatcherThrows_thenFalsey() {
        patternMatcherProviderMockedStatic.when(() -> DocumentPatternMatcherProvider.provideMatcherChain()).thenReturn(ImmutableList.of(matcherChainLink));
        when(matcherChainLink.match(any())).thenThrow(new RuntimeException("test"));

        DocumentPatternMatch documentPatternMatch = new DocumentPatternMatch();
        assertFalse(documentPatternMatch.execute(stageContext).truthy());
    }
}
