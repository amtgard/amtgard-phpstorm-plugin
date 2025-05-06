package com.amtgard.buildertraitscompletions;

import com.amtgard.buildertraitscompletions.model.StageContext;
import com.amtgard.buildertraitscompletions.pipeline.CompletionProductionPipeline;
import com.amtgard.buildertraitscompletions.util.LookupElementFactory;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.util.ProcessingContext;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
public class AmtgardBuilderCompletionProviderTest {
    @Mock
    CompletionParameters parameters = mock(CompletionParameters.class);

    @Mock
    ProcessingContext processingContext = mock(ProcessingContext.class);

    @Mock
    CompletionResultSet resultSet = mock(CompletionResultSet.class);

    @Mock
    PsiFile psiFile = mock(PsiFile.class);

    @Mock
    Project project = mock(Project.class);

    MockedStatic<CompletionProductionPipeline> completionProductionPipeline;

    MockedStatic<LookupElementFactory> lookupElementFactory;

    /**
     * Tests the addCompletions method when valid completions are returned by CompletionProductionPipeline.
     * Verifies that the correct number of LookupElements are added to the CompletionResultSet.
     */
    @Test
    public void testAddCompletionsWithValidCompletions() {
        // Arrange
        AmtgardBuilderCompletionProvider provider = new AmtgardBuilderCompletionProvider();

        when(parameters.getOriginalFile()).thenReturn(psiFile);
        when(psiFile.getProject()).thenReturn(project);

        List<StageContext.CompletionStrategy> mockCompletions = new ArrayList<>();
        mockCompletions.add(StageContext.CompletionStrategy.builder().root("Completion1").build());
        mockCompletions.add(StageContext.CompletionStrategy.builder().root("Completion2").build());

        completionProductionPipeline = Mockito.mockStatic(CompletionProductionPipeline.class);
        when(CompletionProductionPipeline.run(any(StageContext.class))).thenReturn(mockCompletions);

        lookupElementFactory = Mockito.mockStatic(LookupElementFactory.class);
        when(LookupElementFactory.build(anyString())).thenReturn(mock(LookupElement.class));

        // Act
        provider.addCompletions(parameters, processingContext, resultSet);

        // Assert
        verify(resultSet, times(2)).addElement(any());

        completionProductionPipeline.close();
        lookupElementFactory.close();
    }

    /**
     * Tests the behavior of addCompletions when CompletionProductionPipeline.run throws an exception.
     * Verifies that the exception is caught and its message is printed.
     */
    @Test
    public void test_addCompletions_handlesException() {
        // Arrange
        AmtgardBuilderCompletionProvider provider = new AmtgardBuilderCompletionProvider();
        CompletionParameters parameters = Mockito.mock(CompletionParameters.class);
        ProcessingContext processingContext = Mockito.mock(ProcessingContext.class);
        CompletionResultSet resultSet = Mockito.mock(CompletionResultSet.class);

        // Mock the behavior to throw an exception
        Exception testException = new RuntimeException("Test exception");
        completionProductionPipeline = Mockito.mockStatic(CompletionProductionPipeline.class);
        when(CompletionProductionPipeline.run(any(StageContext.class))).thenThrow(testException);

        // Act
        provider.addCompletions(parameters, processingContext, resultSet);

        // Assert
        // Verify that no completions were added to the result set
        verify(resultSet, never()).addElement(any());

        completionProductionPipeline.close();
    }
}
