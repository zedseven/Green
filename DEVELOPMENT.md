# Development
Whenever working on a new feature, bump the version locally in [`build.properties`](/resources/build.properties) so
previous builds aren't overwritten.


To build the project, run the Ant build target `clean` from inside the [`resources`](/resources) directory.

It's ill-named, but it cleans the project, builds the new version, generates the documentation, packages it up, and puts
everything in the `distribution` folder.


This project now uses [`nix-direnv`](https://github.com/nix-community/nix-direnv) to manage its development environment.

When Processing and the `flake.lock` are updated, it may be necessary to update the hash in the name of `core.jar` in [`build.properties`](/resources/build.properties)' `classpath.local.include` value.


# Releasing a New Version
1. Make sure the version has been updated.
2. Push the new commit(s) to GitHub.
3. Copy the contents of the new version's `reference` folder to overwrite the contents in the
   [`gh-pages`](https://github.com/zedseven/Green/tree/gh-pages) branch, and commit those changes.
4. Create a new release on GitHub with the new version, and upload the new version's `.zip` and `.txt` files to it.
5. Push those same two files to the server hosting the latest version for users to download.
