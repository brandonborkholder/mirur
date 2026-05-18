# Release process

This project publishes Mirur as an Eclipse p2 update site. The canonical build
output is `mirur-repository/target/repository`; release automation should upload
that directory instead of relying on a sibling checkout next to the source tree.

## Update site compatibility

Keep the public p2 update-site URL stable for existing installs. Existing users
can continue to point Eclipse at the same update-site link as long as the release
job publishes the contents of `mirur-repository/target/repository` to the same
`update-site/` path that is currently served by the `gh-pages` site.

Changing the URL is only required if the hosting location, branch, or published
path changes. If that ever happens, publish a redirect or a final repository at
the old URL that includes instructions for the new location before removing the
old site.

## Release checklist

1. Confirm the support matrix in `releng/target-platform/mirur.target`, the
   Tycho environments in the parent `pom.xml`, and `mirur-repository/category.xml`.
2. Choose the release version, then update Maven and Tycho/PDE metadata:

   ```sh
   mvn versions:set -DnewVersion=X.Y.Z-SNAPSHOT
   mvn tycho-versions:set-version -DnewVersion=X.Y.Z.qualifier
   ```

   For a final release, use the release version in Maven metadata and the matching
   `.qualifier` value in Eclipse metadata before tagging.
3. Run the full verification build:

   ```sh
   mvn clean verify
   ```

4. Inspect `mirur-repository/target/repository` and archive it as the CI release
   artifact. The repository should contain p2 metadata (`content.jar` or
   `content.xml`, `artifacts.jar` or `artifacts.xml`) plus `features/` and
   `plugins/` entries.
5. Sign jars and repository metadata according to the project's signing policy.
   The historical Pack200/jarprocessor workflow is obsolete and must not be used
   for modern Java and Eclipse releases.
6. Generate checksums for the archived p2 repository artifact, for example:

   ```sh
   (cd mirur-repository/target && zip -r mirur-p2-repository-X.Y.Z.zip repository)
   shasum -a 256 mirur-repository/target/mirur-p2-repository-X.Y.Z.zip
   ```

7. Create and push the release tag after verification succeeds:

   ```sh
   git tag X.Y.Z
   git push origin X.Y.Z
   ```

8. Publish the repository artifact to the existing public update-site path. For
   the current GitHub Pages setup, copy the contents of
   `mirur-repository/target/repository/` into the `update-site/` directory on the
   publishing branch, then deploy that branch from CI.
9. Smoke-test installation from the public update-site URL in a clean Eclipse
   profile.
10. Bump back to the next `-SNAPSHOT`/`.qualifier` development version on the
    development branch.

## Rollback

If a published p2 repository is bad, restore the previous known-good contents of
the public `update-site/` path and redeploy it. Keep the release artifact and its
checksums attached to the release for auditability, but mark the GitHub release or
tag as withdrawn if consumers should not install it.
