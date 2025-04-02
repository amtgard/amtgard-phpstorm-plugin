package com.amtgard.buildertraitscompletions;

import com.amtgard.buildertraitscompletions.model.StageContext;
import com.amtgard.buildertraitscompletions.pipeline.CompletionProductionPipeline;
import com.amtgard.buildertraitscompletions.util.LookupElementFactory;
import com.intellij.codeInsight.completion.*;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.util.ProcessingContext;
import com.jetbrains.php.lang.PhpLanguage;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PhpMethodCompletionContributor extends CompletionContributor {

    public PhpMethodCompletionContributor() {
        this.extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement().withLanguage(PhpLanguage.INSTANCE),
            new CompletionProvider<>() {

                @Override
                protected void addCompletions(
                    @NotNull CompletionParameters parameters,
                    @NotNull ProcessingContext context,
                    @NotNull CompletionResultSet resultSet
                ) {
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
        );
    }
}
