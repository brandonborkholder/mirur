# High-priority modernization issue drafts

These files are ready-to-copy issue bodies for the high-priority tasks identified in `docs/modernization-tasks.md`.

## Issue drafts

1. [Replace Indigo-era Eclipse target with a pinned target platform](01-modernize-eclipse-target.md)
2. [Upgrade Tycho in staged migrations](02-upgrade-tycho.md)
3. [Define and modernize the Java runtime/compiler baseline](03-modernize-java-runtime-and-compiler.md)
4. [Rework third-party dependency and native-library packaging](04-rework-third-party-dependencies.md)
5. [Improve p2 repository publication and release flow](05-improve-p2-release-flow.md)
6. [Add automated test structure for core, OSGi, and UI behavior](06-add-automated-test-structure.md)
7. [Establish CI and repository hygiene for modernization work](07-establish-ci-and-repository-hygiene.md)

## Suggested `gh` commands

If the repository is available through the GitHub CLI, create the issues with commands like:

```sh
gh issue create --title "Replace Indigo-era Eclipse target with a pinned target platform" --label modernization,eclipse,tycho,build --body-file docs/issues/high-priority/01-modernize-eclipse-target.md
gh issue create --title "Upgrade Tycho in staged migrations" --label modernization,tycho,maven,build --body-file docs/issues/high-priority/02-upgrade-tycho.md
gh issue create --title "Define and modernize the Java runtime/compiler baseline" --label modernization,java,eclipse,build --body-file docs/issues/high-priority/03-modernize-java-runtime-and-compiler.md
gh issue create --title "Rework third-party dependency and native-library packaging" --label modernization,dependencies,osgi,jogl --body-file docs/issues/high-priority/04-rework-third-party-dependencies.md
gh issue create --title "Improve p2 repository publication and release flow" --label modernization,release,p2,ci --body-file docs/issues/high-priority/05-improve-p2-release-flow.md
gh issue create --title "Add automated test structure for core, OSGi, and UI behavior" --label modernization,tests,quality,eclipse --body-file docs/issues/high-priority/06-add-automated-test-structure.md
gh issue create --title "Establish CI and repository hygiene for modernization work" --label modernization,ci,developer-experience,maintenance --body-file docs/issues/high-priority/07-establish-ci-and-repository-hygiene.md
```
