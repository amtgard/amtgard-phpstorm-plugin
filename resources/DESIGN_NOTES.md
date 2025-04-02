## Tasks
#### Determine if current element matches an AmtgardBuilderTrait
    1. Quick prematch (is an arrow)
    2. Match on a given rule & return FQN
#### Get a list of completions
    3. Convert FQN to class
    4. Match to AmtgardBuilder
    5. Get list of completions
#### Excise already used completionsTasks
    6. Look up other calls
    7. Take difference of all completions - used completions
#### Return list

## Classes
```
class CompletionProductionPipeline
    class PipelineStage
        Truthy execute()
```

```
FqnString
    String Fqn;
```

#### Stages
```
Truthy(Boolean) PredicateMatches(CompletionParameters)
Truthy(FqnString) Matchers(CompletionParameters)
Truthy(Boolean) IsAmtgardBuilder(FqnString)
Truthy(List<String>) GetCompletionList(FqnString)
Truthy(List<String>) PruneExistingCompletions(CompletionParameters, List<String>)
```
```
Matchers extends PipelineStage
    Set<LinkInterface> documentPatternMatchers;
```
```
LinkInterface
    Truthy(FqnString) match(CompletionParameters)
```