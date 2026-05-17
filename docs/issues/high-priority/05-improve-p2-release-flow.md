# Improve p2 repository publication and release flow

## Priority
High

## Labels
`modernization`, `release`, `p2`, `ci`

## Background
The release flow currently assumes a sibling `mirur-update-site` checkout and includes obsolete Pack200/jarprocessor instructions. Modern releases should produce a repository artifact from CI and publish it without mutating sibling local directories during `mvn install`.

## Scope
- Make `mirur-repository/target/repository` the canonical build artifact.
- Remove or gate the copy-to-sibling-update-site behavior so normal installs do not publish locally.
- Remove obsolete Pack200/jarprocessor release instructions.
- Add release documentation covering version bumps, tags, p2 artifact upload, signing, checksums, and rollback.
- Review `category.xml` filters so advertised architectures match supported fragments.

## Acceptance criteria
- `mvn clean verify` produces a p2 repository artifact without requiring a sibling checkout.
- Release publication is documented and suitable for CI automation.
- Pack200 instructions are removed or clearly marked obsolete.
- `category.xml` architecture/OS filters match the supported bundles/fragments.

## References
- `mirur-repository` currently mirrors output to `../../mirur-update-site/update-site/` during install.
- README release steps reference a sibling checkout and jarprocessor/Pack200 flow.
- See `docs/modernization-tasks.md`, section "Improve p2 repository and release flow".
