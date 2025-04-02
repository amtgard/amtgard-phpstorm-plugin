package com.amtgard.buildertraitscompletions.chainmatcher;

import com.amtgard.buildertraitscompletions.util.Boolish;
import com.amtgard.buildertraitscompletions.util.BuilderMode;
import com.intellij.codeInsight.completion.CompletionParameters;

public interface IMatcherChainLink {
    Boolish match(CompletionParameters parameters);
    BuilderMode getBuilderMode();
}
