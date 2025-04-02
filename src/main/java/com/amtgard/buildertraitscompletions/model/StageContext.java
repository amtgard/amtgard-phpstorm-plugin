package com.amtgard.buildertraitscompletions.model;

import com.amtgard.buildertraitscompletions.util.AmtgardBuilderUtil;
import com.amtgard.buildertraitscompletions.util.BuilderMode;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.openapi.project.Project;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Function;

@Data
@Builder
public class StageContext {
    @Builder
    @Getter
    public static class CompletionStrategy {
        String root;
        String display;
        Callable postDisplay;

        public static CompletionStrategy build(String root, String suffix) {
            return CompletionStrategy.builder().root(root).display(root + suffix).build();
        }
    }

    CompletionParameters parameters;
    FqnString fqnString;
    List<CompletionStrategy> completions;
    Project project;
    BuilderMode mode;
}
