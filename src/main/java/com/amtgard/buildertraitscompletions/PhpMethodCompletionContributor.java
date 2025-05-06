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
            new AmtgardBuilderCompletionProvider()
        );
    }
}
