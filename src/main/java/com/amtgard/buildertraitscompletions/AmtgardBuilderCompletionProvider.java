package com.amtgard.buildertraitscompletions;

import com.amtgard.buildertraitscompletions.model.StageContext;
import com.amtgard.buildertraitscompletions.pipeline.CompletionProductionPipeline;
import com.amtgard.buildertraitscompletions.util.LookupElementFactory;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.VisibleForTesting;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AmtgardBuilderCompletionProvider extends CompletionProvider<CompletionParameters> {
    @Override
    @VisibleForTesting
    public void addCompletions(@NotNull CompletionParameters parameters,
                                  @NotNull ProcessingContext processingContext,
                                  @NotNull CompletionResultSet resultSet) {
        try {
            StageContext stageContext = StageContext.builder()
                .parameters(parameters)
                .completions(new ArrayList<>())
                .project(parameters.getOriginalFile().getProject())
                .build();
            List<StageContext.CompletionStrategy> completions = CompletionProductionPipeline.run(stageContext);

            Optional.ofNullable(completions)
                .ifPresent(c -> {
                    for (StageContext.CompletionStrategy completion: c) {
                        resultSet.addElement(LookupElementFactory.build(completion.getDisplay()));
                    }
                });
        } catch (Exception e) {
            String estr = e.getMessage();
            System.out.println(estr);
        }
    }
}
