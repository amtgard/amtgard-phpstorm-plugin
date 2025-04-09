package com.amtgard.buildertraitscompletions.provider;

import com.amtgard.buildertraitscompletions.chainmatcher.IMatcherChainLink;
import com.amtgard.buildertraitscompletions.chainmatcher.matcher.GetterSetterPattern;
import com.amtgard.buildertraitscompletions.chainmatcher.matcher.StaticBuilderPattern;
import com.amtgard.buildertraitscompletions.chainmatcher.matcher.ToBuilderPattern;
import com.google.common.collect.ImmutableList;

import java.util.List;

public class DocumentPatternMatcherProvider {
    final static List<IMatcherChainLink> matcherChain = ImmutableList.of(
        new StaticBuilderPattern(),
        new ToBuilderPattern(),
        new GetterSetterPattern()
    );

    public static List<IMatcherChainLink> provideMatcherChain() {
        return matcherChain;
    }
}
